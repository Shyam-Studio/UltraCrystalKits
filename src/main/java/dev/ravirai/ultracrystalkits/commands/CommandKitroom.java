package dev.ravirai.ultracrystalkits.commands;

import dev.ravirai.ultracrystalkits.UltraCrystalKits;
import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.Messages;
import dev.ravirai.ultracrystalkits.menus.kitroomitems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandKitroom implements CommandExecutor {
    private final PointsDatabase pointsDatabase;
    private final InventorySerializations inventorySerializer;
    private final Messages messages;
    private final List<String> validCategories = Arrays.asList("vanillapvp", "diamoncrystal", "potions", "armory", "axe", "consumables");

    public CommandKitroom(UltraCrystalKits plugin, InventorySerializations inventorySerializer, PointsDatabase pointsDatabase) {
        this.inventorySerializer = inventorySerializer;
        this.pointsDatabase = pointsDatabase;
        this.messages = plugin.getMessages();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            Map<String, String> placeholders = new HashMap<>();
            messages.sendTo(sender, "player_only", placeholders);
            return true;
        }

        Player player = (Player) sender;
        String category = "vanillapvp";

        if (args.length > 0) {
            String requestedCategory = args[0].toLowerCase().replace("diamond", "diamon");
            if (validCategories.contains(requestedCategory)) {
                category = requestedCategory;
            } else {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("category", requestedCategory);
                messages.sendTo(player, "invalid_category", placeholders);
            }
        }

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        messages.broadcast("kit_room_opened", placeholders);

        Inventory deserializedInventory = inventorySerializer.deserializeInventory("kitroom", category);
        new kitroomitems(player, deserializedInventory, category);

        return true;
    }
}