package dev.ravirai.ultracrystalkits.listners;

import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.KitEditHolder;
import dev.ravirai.ultracrystalkits.extra.KitEditOthersHolder;
import dev.ravirai.ultracrystalkits.menus.editkit;
import dev.ravirai.ultracrystalkits.menus.mainmenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.SQLException;

public class editkitlistnet implements Listener {
    private final PointsDatabase pointsDatabase;
    private final InventorySerializations inventorySerializer;

    public editkitlistnet(InventorySerializations inventorySerializer, PointsDatabase pointsDatabase) {
        this.inventorySerializer = inventorySerializer;
        this.pointsDatabase = pointsDatabase;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws SQLException {
        if (e.getInventory().getHolder() instanceof KitEditHolder) {
            if(e.getRawSlot() <= 53 && e.getRawSlot() >= 41) {
                String title = e.getView().getTitle();
                String lastLetter = title.substring(title.length() - 1);
                Player player = (Player) e.getWhoClicked();
                 if(e.getRawSlot() == 53) {
                     new mainmenu(player);
                 } else if (e.getRawSlot() == 51) {
                     new editkit(player,Integer.valueOf(lastLetter), player.getInventory(), null, null);
                 } else if (e.getRawSlot() == 50) {
                     inventorySerializer.serializeInventory(e.getClickedInventory(), e.getWhoClicked().getUniqueId().toString(), "Kit " + lastLetter, 41);
                 } else if (e.getRawSlot() == 52) {
                     if (e.isShiftClick()) {
                         String uuid = player.getUniqueId().toString();
                         String kitName = "Kit " + lastLetter;
                         pointsDatabase.deleteKit(uuid, kitName);
                         new editkit(player, Integer.valueOf(lastLetter), null, null, null);
                     }

                 }
                e.setCancelled(true);
            }
        } else if (e.getInventory().getHolder() instanceof KitEditOthersHolder) {
            if(e.getRawSlot() <= 53 && e.getRawSlot() >= 41) {
                String title = e.getView().getTitle();
                String lastLetter = String.valueOf(title.charAt(0));
                Player player = Bukkit.getPlayer(title.substring(2));
                if(e.getRawSlot() == 53) {
                    new mainmenu((Player) e.getWhoClicked());
                } else if (e.getRawSlot() == 51) {
                    new editkit((Player) e.getWhoClicked(),Integer.valueOf(lastLetter), e.getWhoClicked().getInventory(), null, player);
                } else if (e.getRawSlot() == 50) {
                    inventorySerializer.serializeInventory(e.getClickedInventory(), String.valueOf(player.getUniqueId()), "Kit " + lastLetter, 41);
                } else if (e.getRawSlot() == 52) {
                    if (e.isShiftClick()) {
                        String uuid = player.getUniqueId().toString();
                        String kitName = "Kit " + lastLetter;
                        pointsDatabase.deleteKit(uuid, kitName);
                        new editkit(player, Integer.valueOf(lastLetter), null, null, null);
                    }

                }
                e.setCancelled(true);
            }
        }
    }
}
