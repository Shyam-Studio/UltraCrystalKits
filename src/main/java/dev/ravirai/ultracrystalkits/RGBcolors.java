package dev.ravirai.ultracrystalkits;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class RGBcolors {
    public static String translate(String message) {
        try {
            String version = Bukkit.getServer().getVersion();
            Pattern versionPattern = Pattern.compile("\\(MC: (\\d+\\.\\d+)");
            Matcher versionMatcher = versionPattern.matcher(version);

            boolean supportsRGB = false;
            if (versionMatcher.find()) {
                String mcVersion = versionMatcher.group(1);
                double versionNumber = Double.parseDouble(mcVersion);
                supportsRGB = versionNumber >= 1.16;
            }

            if (supportsRGB) {
                Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
                Matcher matcher = pattern.matcher(message);

                while (matcher.find()) {
                    String color = message.substring(matcher.start(), matcher.end());
                    message = message.replace(color, ChatColor.of(color.substring(1)).toString());
                }
            }

            return ChatColor.translateAlternateColorCodes('&', message);

        } catch (Exception e) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
    }
}