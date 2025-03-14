package dev.ravirai.ultracrystalkits.menus;

import dev.ravirai.ultracrystalkits.extra.KitRoomMainHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class mainmenu {
    public Inventory inv;
    public mainmenu(Player player) {
        inv = Bukkit.createInventory(new KitRoomMainHolder(), 45, ChatColor.LIGHT_PURPLE + player.getDisplayName() + "'s kits");
        initializeItems();
        if(player.hasPermission("kitroom.admin")) {
            ItemStack admin = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
            ItemMeta adminmeta = admin.getItemMeta();
            adminmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Admin Tools");
            admin.setItemMeta(adminmeta);
            inv.setItem(40, admin);
        }
        player.openInventory(inv);
    }

    public void initializeItems() {
        ItemStack purpleGlass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta meta = purpleGlass.getItemMeta();
        meta.setDisplayName(" ");
        purpleGlass.setItemMeta(meta);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, purpleGlass);
        }
        for (int i = 27; i < 45; i++) {
            inv.setItem(i, purpleGlass);
        }

        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta metachest = chest.getItemMeta();
        for (int i = 9; i < 18; i++) {
            metachest.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "KIT " + Integer.toString(i-8));
            List<String> chestlore = new ArrayList<>();
            chestlore.add(ChatColor.GRAY + "- Left click to load kit");
            chestlore.add(ChatColor.GRAY + "- Right click to edit kit");
            metachest.setLore(chestlore);
            chest.setItemMeta(metachest);
            inv.setItem(i, chest);
        }

        ItemStack echest = new ItemStack(Material.ENDER_CHEST);
        ItemMeta metaechest = echest.getItemMeta();
        for (int i = 18; i < 27; i++) {
            metaechest.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "ENDERCHEST " + Integer.toString(i-17));
            List<String> echestlore = new ArrayList<>();
            echestlore.add(ChatColor.GRAY + "- Left click to load ender chest");
            echestlore.add(ChatColor.GRAY + "- Right click to edit ender chest");
            metaechest.setLore(echestlore);
            echest.setItemMeta(metaechest);
            inv.setItem(i, echest);
        }

        ItemStack kitroom = new ItemStack(Material.NETHER_STAR);
        ItemMeta kitroommeta = kitroom.getItemMeta();
        kitroommeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "KIT ROOM");
        kitroom.setItemMeta(kitroommeta);
        inv.setItem(30, kitroom);

        ItemStack info = new ItemStack(Material.OAK_SIGN);
        ItemMeta infometa = info.getItemMeta();
        infometa.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "INFO");
        List<String> infolore = new ArrayList<>();
        infolore.add(ChatColor.GRAY + "- Click a kit slot to load your kit");
        infolore.add(ChatColor.GRAY + "- Right click a kit slot to edit your kit");
        infometa.setLore(infolore);
        info.setItemMeta(infometa);
        inv.setItem(31, info);

        ItemStack clear = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta clearmeta = clear.getItemMeta();
        clearmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CLEAR INVENTORY");
        List<String> clearlore = new ArrayList<>();
        clearlore.add(ChatColor.GRAY + "- Shift Click");
        clearmeta.setLore(clearlore);
        clear.setItemMeta(clearmeta);
        inv.setItem(32, clear);

        ItemStack presetKit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) presetKit.getItemMeta();
        skullMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Preset Kit");
        List<String> presetKitLore = new ArrayList<>();
        presetKitLore.add(ChatColor.GRAY + "Click to claim the preset kit");
        skullMeta.setLore(presetKitLore);
        presetKit.setItemMeta(skullMeta);
        inv.setItem(33, presetKit);
    }

}
