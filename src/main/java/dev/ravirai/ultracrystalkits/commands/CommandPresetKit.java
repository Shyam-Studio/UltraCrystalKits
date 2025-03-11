package dev.ravirai.ultracrystalkits.commands;

import dev.ravirai.ultracrystalkits.UltraCrystalKits;
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

import java.util.HashMap;
import java.util.Map;

public class CommandPresetKit implements CommandExecutor {
    private final InventorySerializations inventorySerializer;
    private final Messages messages;

    public CommandPresetKit(UltraCrystalKits plugin, InventorySerializations inventorySerializer) {
        this.inventorySerializer = inventorySerializer;
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
        Inventory presetKit = inventorySerializer.deserializeInventory("presetkit", "default");

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());

        if (presetKit != null) {
            player.getInventory().clear();

            ItemStack[] contents = presetKit.getContents();
            for (int i = 0; i < Math.min(41, contents.length); i++) {
                if (contents[i] != null && contents[i].getType() != Material.AIR) {
                    player.getInventory().setItem(i, contents[i]);
                }
            }

            messages.broadcast("preset_kit_claimed", placeholders);
            messages.sendTo(player, "preset_kit_success", placeholders);
        } else {
            messages.sendTo(player, "preset_kit_not_found", placeholders);
        }

        return true;
    }
}