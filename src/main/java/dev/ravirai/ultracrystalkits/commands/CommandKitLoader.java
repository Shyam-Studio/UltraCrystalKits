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

public class CommandKitLoader implements CommandExecutor {
    private final PointsDatabase pointsDatabase;
    private final InventorySerializations inventorySerializer;
    private final Messages messages;
    private final UltraCrystalKits plugin;

    public CommandKitLoader(UltraCrystalKits plugin, InventorySerializations inventorySerializer, PointsDatabase pointsDatabase) {
        this.plugin = plugin;
        this.inventorySerializer = inventorySerializer;
        this.pointsDatabase = pointsDatabase;
        this.messages = plugin.getMessages();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Map<String, String> placeholders = new HashMap<>();
            messages.sendTo(sender, "player_only", placeholders);
            return true;
        }

        Player player = (Player) sender;
        String commandName = command.getName().toLowerCase();

        if (commandName.startsWith("k")) {
            int kitNumber = Integer.parseInt(commandName.substring(1));
            String kitname = "Kit " + kitNumber;
            Inventory deserializedInventory = inventorySerializer.deserializeInventory(player.getUniqueId().toString(), kitname);
            player.getInventory().clear();

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", player.getName());
            placeholders.put("kit", String.valueOf(kitNumber));
            placeholders.put("distance", "");

            if (deserializedInventory != null) {
                ItemStack[] contents = deserializedInventory.getContents();
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i] != null && contents[i].getType() != Material.AIR) {
                        player.getInventory().setItem(i, contents[i]);
                    }
                }
                messages.broadcast("player_equipped_kit", placeholders);
            } else {
                messages.sendTo(player, "kit_not_found", placeholders);
            }
            return true;
        }
        return false;
    }
}