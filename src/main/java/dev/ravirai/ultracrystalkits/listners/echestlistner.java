package dev.ravirai.ultracrystalkits.listners;

import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.KitEChestHolder;
import dev.ravirai.ultracrystalkits.extra.KitEditHolder;
import dev.ravirai.ultracrystalkits.menus.editechest;
import dev.ravirai.ultracrystalkits.menus.mainmenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.SQLException;

public class echestlistner implements Listener {
    private final PointsDatabase pointsDatabase;
    private final InventorySerializations inventorySerializer;

    public echestlistner(InventorySerializations inventorySerializer, PointsDatabase pointsDatabase) {
        this.inventorySerializer = inventorySerializer;
        this.pointsDatabase = pointsDatabase;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws SQLException {
        if(e.getInventory().getHolder() instanceof KitEChestHolder) {
            if(e.getRawSlot() >= 27 && e.getRawSlot() <= 35) {
                String title = e.getView().getTitle();
                String lastLetter = title.substring(title.length() - 1);
                Player player = (Player) e.getWhoClicked();
                String uuid = player.getUniqueId().toString();
                if (e.getRawSlot() == 35) {
                    new mainmenu(player);
                } else if(e.getRawSlot() == 33) {
                    new editechest(player, Integer.valueOf(lastLetter), player.getEnderChest(), null);
                } else if (e.getRawSlot() == 32) {
                    inventorySerializer.serializeInventory(e.getClickedInventory(), e.getWhoClicked().getUniqueId().toString(), "EC " + lastLetter, 27);
                } else if (e.getRawSlot() == 34 && e.isShiftClick()) {
                    pointsDatabase.deleteKit(uuid, "EC " + lastLetter);
                    new editechest(player, Integer.valueOf(lastLetter), player.getEnderChest(), null);
                }
                e.setCancelled(true);
            }
        }
    }
}
