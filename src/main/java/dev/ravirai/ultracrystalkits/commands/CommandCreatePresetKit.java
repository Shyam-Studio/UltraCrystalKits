package dev.ravirai.ultracrystalkits.commands;

import dev.ravirai.ultracrystalkits.UltraCrystalKits;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandCreatePresetKit implements CommandExecutor {
    private final InventorySerializations inventorySerializer;
    private final Messages messages;

    public CommandCreatePresetKit(UltraCrystalKits plugin, InventorySerializations inventorySerializer) {
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
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());

        if (!player.hasPermission("kitroom.admin")) {
            messages.sendTo(player, "no_permission", placeholders);
            return true;
        }

        inventorySerializer.serializeInventory(player.getInventory(), "presetkit", "default", 41);
        messages.broadcast("preset_kit_created", placeholders);
        return true;
    }
}