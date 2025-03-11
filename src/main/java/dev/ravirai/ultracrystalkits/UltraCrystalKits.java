package dev.ravirai.ultracrystalkits;

import dev.ravirai.ultracrystalkits.commands.*;
import dev.ravirai.ultracrystalkits.database.PointsDatabase;
import dev.ravirai.ultracrystalkits.extra.DBstartup;
import dev.ravirai.ultracrystalkits.extra.InventorySerializations;
import dev.ravirai.ultracrystalkits.extra.Messages;
import dev.ravirai.ultracrystalkits.listners.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

public final class UltraCrystalKits extends JavaPlugin implements Listener {
    private PointsDatabase pointsDatabase;
    private InventorySerializations inventorySerializer;
    private List<String> blacklistedWorlds;
    private boolean worldBlacklistEnabled;
    private Messages messages;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        loadMainConfig();
        this.messages = new Messages(this);

        initializeDatabase();
        registerCommands();
        registerListeners();
    }

    private void loadMainConfig() {
        worldBlacklistEnabled = getConfig().getBoolean("world-blacklist.enabled", false);
        blacklistedWorlds = getConfig().getStringList("world-blacklist.worlds");
    }

    private void initializeDatabase() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            pointsDatabase = new PointsDatabase(getDataFolder().getAbsolutePath() + "/kits.db");
            inventorySerializer = new InventorySerializations(this, pointsDatabase);

            List<String> filePaths = Arrays.asList("vanillapvp", "diamoncrystal", "potions", "armory", "axe", "consumables");
            DBstartup dBstartup = new DBstartup(getLogger(), getDataFolder(), pointsDatabase);
            dBstartup.defaultDB(filePaths);
        } catch (SQLException ex) {
            ex.printStackTrace();
            getLogger().severe("Failed to connect to database: " + ex.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerCommands() {
        this.getCommand("kitadmin").setExecutor(wrapCommand(new CommandKitAdmin(inventorySerializer, pointsDatabase)));
        this.getCommand("kit").setExecutor(wrapCommand(new CommandKit(this)));
        this.getCommand("kitroom").setExecutor(wrapCommand(new CommandKitroom(this, inventorySerializer, pointsDatabase)));
        this.getCommand("presetkit").setExecutor(wrapCommand(new CommandPresetKit(this, inventorySerializer)));
        this.getCommand("createpresetkit").setExecutor(wrapCommand(new CommandCreatePresetKit(this, inventorySerializer)));
        this.getCommand("ultracrystalkits").setExecutor(this::onReloadCommand);

        for (int i = 1; i <= 9; i++) {
            this.getCommand("k" + i).setExecutor(wrapCommand(new CommandKitLoader(this, inventorySerializer, pointsDatabase)));
            this.getCommand("ec" + i).setExecutor(wrapCommand(new CommandEchestLoader(this, inventorySerializer, pointsDatabase)));
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new kitadminlistner(inventorySerializer, pointsDatabase), this);
        getServer().getPluginManager().registerEvents(new mainmenulistner(this, inventorySerializer, pointsDatabase), this);
        getServer().getPluginManager().registerEvents(new editkitlistnet(inventorySerializer, pointsDatabase), this);
        getServer().getPluginManager().registerEvents(new kitroomitemslistner(this, inventorySerializer, pointsDatabase), this);
        getServer().getPluginManager().registerEvents(new echestlistner(inventorySerializer, pointsDatabase), this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    private CommandExecutor wrapCommand(CommandExecutor executor) {
        return (sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (isWorldBlacklisted(player.getWorld())) {
                    player.sendMessage("This command is not available in this world.");
                    return true;
                }
            }
            return executor.onCommand(sender, command, label, args);
        };
    }

    private boolean isWorldBlacklisted(World world) {
        return worldBlacklistEnabled && blacklistedWorlds.contains(world.getName());
    }

    @Override
    public void onDisable() {
        try {
            if (pointsDatabase != null) {
                pointsDatabase.closeConnection();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean onReloadCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ultracrystalkits.reload")) {
            sender.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("Usage: /ultracrystalkits reload");
            return true;
        }

        reloadConfig();
        loadMainConfig();
        messages.reload();

        sender.sendMessage("Configuration reloaded successfully.");
        return true;
    }

    public Messages getMessages() {
        return messages;
    }
}