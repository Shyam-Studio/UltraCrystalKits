package dev.ravirai.ultracrystalkits.listners;

import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.KitAdminHolder;
import dev.ravirai.ultracrystalkits.menus.editkit;
import dev.ravirai.ultracrystalkits.menus.kitroomadmin;
import dev.ravirai.ultracrystalkits.menus.mainmenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class kitadminlistner implements Listener {
    private final PointsDatabase pointsDatabase;
    private final InventorySerializations inventorySerializer;

    public kitadminlistner(InventorySerializations inventorySerializer, PointsDatabase pointsDatabase) {
        this.inventorySerializer = inventorySerializer;
        this.pointsDatabase = pointsDatabase;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof KitAdminHolder) {
            if (e.getRawSlot() >= 45 && e.getRawSlot() <= 53) {
                Player player = (Player) e.getWhoClicked();
                if (e.getRawSlot() == 47) {
                    handleKitClickKit(player, "vanillapvp");
                } else if (e.getRawSlot() == 48) {
                    handleKitClickKit(player, "diamoncrystal");
                } else if (e.getRawSlot() == 49) {
                    handleKitClickKit(player, "potions");
                } else if (e.getRawSlot() == 50) {
                    handleKitClickKit(player, "armory");
                } else if (e.getRawSlot() == 51) {
                    handleKitClickKit(player, "axe");
                } else if (e.getRawSlot() == 52) {
                    handleKitClickKit(player, "consumables");
                } else if (e.getRawSlot() == 45) {
                    inventorySerializer.serializeInventory(e.getClickedInventory(), "kitroom", e.getView().getTitle(), 45);
                } else if (e.getRawSlot() == 53) {
                    new mainmenu(player);
                }
                e.setCancelled(true);
            }
        }
    }
    private void handleKitClickKit(Player player, String name) {
        Inventory deserializedInventory = inventorySerializer.deserializeInventory("kitroom", name);

        new kitroomadmin(player, name, deserializedInventory);
    }
}
