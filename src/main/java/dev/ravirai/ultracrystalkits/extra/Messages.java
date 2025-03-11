package dev.ravirai.ultracrystalkits.extra;

import dev.ravirai.ultracrystalkits.RGBcolors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Messages {
    private final JavaPlugin plugin;
    private FileConfiguration messagesConfig;
    private final Map<String, MessageData> messages = new HashMap<>();

    public Messages(JavaPlugin plugin) {
        this.plugin = plugin;
        loadMessages();
    }

    private void loadMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        loadMessageData();
    }

    private void loadMessageData() {
        messages.put("player_equipped_kit", new MessageData(
                messagesConfig.getString("messages.player_equipped_kit.text", "&7🗡 <player> loaded Kit <kit> [⚓ <distance>m away]"),
                MessageType.valueOf(messagesConfig.getString("messages.player_equipped_kit.type", "CHAT").toUpperCase())
        ));

        messages.put("kit_room_opened", new MessageData(
                messagesConfig.getString("messages.kit_room_opened.text", "&7<player> opened the Virtual Kitroom"),
                MessageType.valueOf(messagesConfig.getString("messages.kit_room_opened.type", "CHAT").toUpperCase())
        ));

        messages.put("kit_saved", new MessageData(
                messagesConfig.getString("messages.kit_saved.text", "&aKit <kit> has been saved"),
                MessageType.valueOf(messagesConfig.getString("messages.kit_saved.type", "ACTION_BAR").toUpperCase())
        ));

        messages.put("kit_deleted", new MessageData(
                messagesConfig.getString("messages.kit_deleted.text", "&cKit <kit> has been deleted"),
                MessageType.valueOf(messagesConfig.getString("messages.kit_deleted.type", "CHAT").toUpperCase())
        ));

        messages.put("kit_not_found", new MessageData(
                messagesConfig.getString("messages.kit_not_found.text", "&cKit <kit> does not exist"),
                MessageType.valueOf(messagesConfig.getString("messages.kit_not_found.type", "CHAT").toUpperCase())
        ));

        messages.put("preset_kit_claimed", new MessageData(
                messagesConfig.getString("messages.preset_kit_claimed.text", "&a<player> claimed the preset kit!"),
                MessageType.valueOf(messagesConfig.getString("messages.preset_kit_claimed.type", "CHAT").toUpperCase())
        ));

        messages.put("enderchest_loaded", new MessageData(
                messagesConfig.getString("messages.enderchest_loaded.text", "&7🗡 <player> loaded Ender Chest <number> [⚓ <distance>m away]"),
                MessageType.valueOf(messagesConfig.getString("messages.enderchest_loaded.type", "CHAT").toUpperCase())
        ));

        messages.put("kit_menu_opened", new MessageData(
                messagesConfig.getString("messages.kit_menu_opened.text", "&7<player> opened the kit menu"),
                MessageType.valueOf(messagesConfig.getString("messages.kit_menu_opened.type", "CHAT").toUpperCase())
        ));

    }

    public void broadcast(String messageKey, Map<String, String> placeholders) {
        MessageData messageData = messages.get(messageKey);
        if (messageData == null) return;

        Player actor = Bukkit.getPlayer(placeholders.get("player"));
        if (actor == null) return;

        for (Player recipient : Bukkit.getOnlinePlayers()) {
            Map<String, String> recipientPlaceholders = new HashMap<>(placeholders);

            if (recipientPlaceholders.containsKey("distance")) {
                if (recipient.getWorld() == actor.getWorld() && !recipient.equals(actor)) {
                    double distance = recipient.getLocation().distance(actor.getLocation());
                    recipientPlaceholders.put("distance", String.format("%.1f", distance));
                } else {
                    recipientPlaceholders.put("distance", "0.0");
                }
            }

            String message = messageData.text;
            for (Map.Entry<String, String> placeholder : recipientPlaceholders.entrySet()) {
                message = message.replace("<" + placeholder.getKey() + ">", placeholder.getValue());
            }

            String finalMessage = RGBcolors.translate(message);

            switch (messageData.type) {
                case CHAT:
                    recipient.sendMessage(finalMessage);
                    break;
                case ACTION_BAR:
                    recipient.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(finalMessage));
                    break;
            }
        }
    }

    public void sendTo(CommandSender sender, String messageKey, Map<String, String> placeholders) {
        MessageData messageData = messages.get(messageKey);
        if (messageData == null) return;

        String message = messageData.text;
        for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            message = message.replace("<" + placeholder.getKey() + ">", placeholder.getValue());
        }

        String finalMessage = RGBcolors.translate(message);

        if (sender instanceof Player player) {
            switch (messageData.type) {
                case CHAT:
                    player.sendMessage(finalMessage);
                    break;
                case ACTION_BAR:
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(finalMessage));
                    break;
            }
        } else {
            sender.sendMessage(finalMessage);
        }
    }

    private static class MessageData {
        final String text;
        final MessageType type;

        MessageData(String text, MessageType type) {
            this.text = text;
            this.type = type;
        }
    }

    private enum MessageType {
        CHAT,
        ACTION_BAR
    }
    public void reload() {
        loadMessages();
    }
}