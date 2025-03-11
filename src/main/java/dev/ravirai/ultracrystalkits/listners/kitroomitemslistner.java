package dev.ravirai.ultracrystalkits.listners;

import dev.ravirai.ultracrystalkits.UltraCrystalKits;
import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.KitRoomItemsMainHolder;
import dev.ravirai.ultracrystalkits.extra.Messages;
import dev.ravirai.ultracrystalkits.menus.kitroomadmin;
import dev.ravirai.ultracrystalkits.menus.kitroomitems;
import dev.ravirai.ultracrystalkits.menus.mainmenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class kitroomitemslistner implements Listener {
    private final PointsDatabase pointsDatabase;
    private final InventorySerializations inventorySerializer;
    private final Messages messages;

    public kitroomitemslistner(UltraCrystalKits plugin, InventorySerializations inventorySerializer, PointsDatabase pointsDatabase) {
        this.inventorySerializer = inventorySerializer;
        this.pointsDatabase = pointsDatabase;
        this.messages = plugin.getMessages();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof KitRoomItemsMainHolder) {
            if (e.getRawSlot() >= 45 && e.getRawSlot() <= 53) {
                Player player = (Player) e.getWhoClicked();
                String title = e.getView().getTitle();
                if (e.getRawSlot() == 53) {
                    new mainmenu(player);
                } else if (e.getRawSlot() == 47) {
                    handleKitClickKit(player, "vanillapvp");
                } else if (e.getRawSlot() == 48) {
                    handleKitClickKit(player, "diamoncrystal");
                } else if (e.getRawSlot() == 49) {
                    handleKitClickKit(player, "potions");
                } else if (e.getRawSlot() == 50) {
                    handleKitClickKit(player, "armory");
                } else if (e.getRawSlot() == 51) {
                    handleKitClickKit(player, "axe");
                } else if (e.getRawSlot() == 45) {
                    handleKitClickKit(player, title);

                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", player.getName());
                    placeholders.put("category", title);
                    messages.sendTo(player, "refill_success", placeholders);
                } else if (e.getRawSlot() == 52) {
                    handleKitClickKit(player, "consumables");
                }
                e.setCancelled(true);
            }
        }
    }

    private void handleKitClickKit(Player player, String category) {
        Inventory deserializedInventory = inventorySerializer.deserializeInventory("kitroom", category);
        new kitroomitems(player, deserializedInventory, category);
    }
}