package dev.ravirai.ultracrystalkits.commands;

import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.menus.kitroomadmin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CommandKitAdmin implements CommandExecutor {
    private final PointsDatabase pointsDatabase;
    private final InventorySerializations inventorySerializer;

    public CommandKitAdmin(InventorySerializations inventorySerializer, PointsDatabase pointsDatabase) {
        this.inventorySerializer = inventorySerializer;
        this.pointsDatabase = pointsDatabase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("kitroom.admin")) {
            return true;
        } else {
            Inventory deserializedInventory = inventorySerializer.deserializeInventory("kitroom", "vanillapvp");
            new kitroomadmin(player, "vanillapvp", deserializedInventory);
        }
        return false;
    }
}
