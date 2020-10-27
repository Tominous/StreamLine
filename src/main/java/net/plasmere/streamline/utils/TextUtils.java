package net.plasmere.streamline.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.hover.content.Text;

public class TextUtils {
    public static TextComponent codedText(String text){
        text = ChatColor.translateAlternateColorCodes('&', newLined(text));

        if (text.contains("https://")){
            return makeLinked(text, text.substring(text.indexOf("https://"), text.substring(text.indexOf(text.indexOf("https://"))).indexOf(" ") + text.indexOf("https://")));
        }
        return new TextComponent(text);
    }

    public static TextComponent clhText(String text, String hoverPrefix){
        text = ChatColor.translateAlternateColorCodes('&', newLined(text));

        if (text.contains("https://")){
            String substring = text.substring(text.indexOf("https://"), text.substring(text.indexOf(text.indexOf("https://"))).indexOf(" ") + text.indexOf("https://"));
            TextComponent tc = makeLinked(text, substring);
            tc = makeHoverable(tc, hoverPrefix + substring);
            return tc;
        }
        return new TextComponent(text);
    }

    public static String codedString(String text){
        return ChatColor.translateAlternateColorCodes('&', newLined(text));
    }

    public static TextComponent makeLinked(String text, String url){
        TextComponent tc = new TextComponent(text);
        ClickEvent ce = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        tc.setClickEvent(ce);
        return tc;
    }

    public static TextComponent makeHoverable(String text, String hoverText){
        TextComponent tc = new TextComponent(text);
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(codedString(hoverText)));
        tc.setHoverEvent(he);
        return tc;
    }

    public static TextComponent makeHoverable(TextComponent textComponent, String hoverText){
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(codedString(hoverText)));
        textComponent.setHoverEvent(he);
        return textComponent;
    }

    public static String newLined(String text){
        return text.replace("%newline%", "\n");
    }

    public static boolean isCommand(String msg){
        return msg.startsWith("/");
    }

    public static String concat(String[] splitMsg){
        int i = 0;
        StringBuilder text = new StringBuilder();

        for (String split : splitMsg){
            i++;
            if (i < splitMsg.length)
                text.append(split).append(" ");
            else
                text.append(split);
        }

        return text.toString();
    }
}
