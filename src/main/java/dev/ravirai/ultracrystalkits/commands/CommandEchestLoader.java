package dev.ravirai.ultracrystalkits.commands;

import dev.ravirai.ultracrystalkits.UltraCrystalKits;
import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandEchestLoader implements CommandExecutor {
    private final PointsDatabase pointsDatabase;
    private final InventorySerializations inventorySerializer;
    private final Messages messages;
    private final UltraCrystalKits plugin;

    public CommandEchestLoader(UltraCrystalKits plugin, InventorySerializations inventorySerializer, PointsDatabase pointsDatabase) {
        this.plugin = plugin;
        this.inventorySerializer = inventorySerializer;
        this.pointsDatabase = pointsDatabase;
        this.messages = plugin.getMessages();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Map<String, String> placeholders = new HashMap<>();
            messages.sendTo(sender, "player_only", placeholders);
            return true;
        }

        Player player = (Player) sender;
        String commandName = command.getName().toLowerCase();

        if (commandName.startsWith("ec")) {
            int echestNumber = Integer.parseInt(commandName.substring(2));
            String echestName = "EC " + echestNumber;
            Inventory deserializedInventory = inventorySerializer.deserializeInventory(player.getUniqueId().toString(), echestName);
            player.getEnderChest().clear();

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", player.getName());
            placeholders.put("number", String.valueOf(echestNumber));
            placeholders.put("distance", "");

            if (deserializedInventory != null) {
                ItemStack[] contents = deserializedInventory.getContents();
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i] != null && contents[i].getType() != Material.AIR) {
                        player.getEnderChest().setItem(i, contents[i]);
                    }
                }
                messages.broadcast("enderchest_loaded", placeholders);
            } else {
                messages.sendTo(player, "enderchest_not_found", placeholders);
            }
            return true;
        }
        return false;
    }
}