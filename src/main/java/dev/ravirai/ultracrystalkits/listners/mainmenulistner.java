package dev.ravirai.ultracrystalkits.listners;

import dev.ravirai.ultracrystalkits.UltraCrystalKits;
import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.KitRoomMainHolder;
import dev.ravirai.ultracrystalkits.extra.Messages;
import dev.ravirai.ultracrystalkits.menus.editechest;
import dev.ravirai.ultracrystalkits.menus.editkit;
import dev.ravirai.ultracrystalkits.menus.kitroomadmin;
import dev.ravirai.ultracrystalkits.menus.kitroomitems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class mainmenulistner implements Listener {
    private final InventorySerializations inventorySerializations;
    private final PointsDatabase pointsDatabase;
    private final Messages messages;

    public mainmenulistner(UltraCrystalKits plugin, InventorySerializations inventorySerializations, PointsDatabase pointsDatabase) {
        this.inventorySerializations = inventorySerializations;
        this.pointsDatabase = pointsDatabase;
        this.messages = plugin.getMessages();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof KitRoomMainHolder){
            Player player = (Player) e.getWhoClicked();

            if (e.getCurrentItem() == null) {
                e.setCancelled(true);
                return;
            }

            if(e.isShiftClick() && e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                player.getInventory().clear();
            } else if(e.getCurrentItem().getType() == Material.NETHER_STAR) {
                Inventory deserializedInventory = inventorySerializations.deserializeInventory("kitroom", "vanillapvp");
                new kitroomitems(player, deserializedInventory, "vanillapvp");
            } else if (e.getRawSlot() >= 9 && e.getRawSlot() <= 17){
                if (e.isRightClick()){
                    handleKitClickKit(player, e.getRawSlot(), e.isRightClick());
                } else if (e.isLeftClick()) {
                    player.closeInventory();
                    handleKitClickKit(player, e.getRawSlot(), e.isRightClick());
                }
            } else if (e.getRawSlot() >= 18 && e.getRawSlot() <= 26) {
                if (e.isRightClick()){
                    handleKitClickEchest(player, e.getRawSlot(), e.isRightClick());
                } else if (e.isLeftClick()) {
                    player.closeInventory();
                    handleKitClickEchest(player, e.getRawSlot(), e.isRightClick());
                }
            } else if (e.getRawSlot() == 40 && player.hasPermission("kitroom.admin")) {
                Inventory deserializedInventory = inventorySerializations.deserializeInventory("kitroom", "vanillapvp");
                new kitroomadmin(player, "vanillapvp", deserializedInventory);
            } else if (e.getRawSlot() == 33 && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                handlePresetKitClick(player);
            }
            e.setCancelled(true);
        }
    }

    private void handleKitClickKit(Player player, int rawSlot, boolean isRightClick) {
        String playerUUID = player.getUniqueId().toString();
        String kitNumber = String.valueOf(rawSlot - 8);
        String kitName = "Kit " + kitNumber;

        Inventory deserializedInventory = inventorySerializations.deserializeInventory(playerUUID, kitName);

        if (isRightClick) {
            new editkit(player, rawSlot - 8, null, deserializedInventory, null);
        } else {
            player.getInventory().clear();
            if (deserializedInventory != null) {
                ItemStack[] contents = deserializedInventory.getContents();
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i] != null && contents[i].getType() != Material.AIR) {
                        player.getInventory().setItem(i, contents[i]);
                    }
                }

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("player", player.getName());
                placeholders.put("kit", kitNumber);
                placeholders.put("distance", "");
                messages.broadcast("player_equipped_kit", placeholders);
            } else {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("kit", kitNumber);
                messages.sendTo(player, "kit_not_found", placeholders);
            }
        }
    }

    private void handleKitClickEchest(Player player, int rawSlot, boolean isRightClick) {
        String playerUUID = player.getUniqueId().toString();
        String echestNumber = String.valueOf(rawSlot - 17);
        String kitName = "EC " + echestNumber;

        Inventory deserializedInventory = inventorySerializations.deserializeInventory(playerUUID, kitName);

        if (isRightClick) {
            new editechest(player, rawSlot - 17, null, deserializedInventory);
        } else {
            player.getEnderChest().clear();
            if (deserializedInventory != null) {
                ItemStack[] contents = deserializedInventory.getContents();
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i] != null && contents[i].getType() != Material.AIR) {
                        player.getEnderChest().setItem(i, contents[i]);
                    }
                }

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("player", player.getName());
                placeholders.put("number", echestNumber);
                placeholders.put("distance", "");
                messages.broadcast("enderchest_loaded", placeholders);
            } else {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("number", echestNumber);
                messages.sendTo(player, "enderchest_not_found", placeholders);
            }
        }
    }

    private void handlePresetKitClick(Player player) {
        Inventory presetKit = inventorySerializations.deserializeInventory("presetkit", "default");
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());

        if (presetKit != null) {
            player.getInventory().clear();
            ItemStack[] contents = presetKit.getContents();
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] != null && contents[i].getType() != Material.AIR) {
                    player.getInventory().setItem(i, contents[i]);
                }
            }
            messages.sendTo(player, "preset_kit_claimed", placeholders);
        } else {
            messages.sendTo(player, "preset_kit_not_found", placeholders);
        }
        player.closeInventory();
    }
}
