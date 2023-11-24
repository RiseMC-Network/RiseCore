package xyz.lotho.risecore.network.util;


import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CC {
    public static String translate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        for (Matcher matcher = pattern.matcher(message); matcher.find(); matcher = pattern.matcher(message)) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, String.valueOf(ChatColor.of(color))); // You're missing this replacing
        }
        message = ChatColor.translateAlternateColorCodes('&', message); // Translates any & codes too
        return message;
    }

    public static String NEWLINE = "\n";

    public static String GREEN = ChatColor.GREEN.toString();
    public static String RED = ChatColor.RED.toString();
    public static String WHITE = ChatColor.WHITE.toString();
    public static String GRAY = ChatColor.GRAY.toString();
    public static String GOLD = ChatColor.GOLD.toString();
    public static String YELLOW = ChatColor.YELLOW.toString();
    public static String AQUA = ChatColor.AQUA.toString();
    public static String DARK_AQUA = ChatColor.DARK_AQUA.toString();
    public static String DARK_BLUE = ChatColor.DARK_BLUE.toString();
    public static String DARK_GRAY = ChatColor.DARK_GRAY.toString();
    public static String DARK_GREEN = ChatColor.DARK_GREEN.toString();
    public static String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
    public static String DARK_RED = ChatColor.DARK_RED.toString();
    public static String BLACK = ChatColor.BLACK.toString();
    public static String BLUE = ChatColor.BLUE.toString();
    public static String LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString();

    public static String BOLD = ChatColor.BOLD.toString();
    public static String ITALIC = ChatColor.ITALIC.toString();
    public static String MAGIC = ChatColor.MAGIC.toString();
    public static String RESET = ChatColor.RESET.toString();
    public static String STRIKETHROUGH = ChatColor.STRIKETHROUGH.toString();
    public static String UNDERLINE = ChatColor.UNDERLINE.toString();

}
