package dev.ravirai.ultracrystalkits.commands;

import dev.ravirai.ultracrystalkits.UltraCrystalKits;
import dev.ravirai.ultracrystalkits.extra.Messages;
import dev.ravirai.ultracrystalkits.menus.mainmenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandKit implements CommandExecutor {
    private final Messages messages;

    public CommandKit(UltraCrystalKits plugin) {
        this.messages = plugin.getMessages();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Map<String, String> placeholders = new HashMap<>();
            messages.sendTo((Player) sender, "player_only", placeholders);
            return true;
        }

        Player player = (Player) sender;
        new mainmenu(player);

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        messages.broadcast("kit_menu_opened", placeholders);

        return true;
    }
}