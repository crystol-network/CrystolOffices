package com.crystolnetwork.offices.utils;

import com.crystolnetwork.offices.utils.reflect.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class TitleBar {

    private final Player player;
    private String title;
    private String subtitle;
    private int fadeInTime;
    private int showTime;
    private int fadeOutTime;
    private Object packet;
    private Object timingPacket;

    public TitleBar(final Player player) {
        this.player = player;
    }

    public TitleBar setTitle(final String title) {
        this.title = title;
        return this;
    }

    public TitleBar setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public TitleBar setOptions(final int fadeInTime, final int showTime, final int fadeOutTime) {
        this.fadeInTime = fadeInTime;
        this.showTime = showTime;
        this.fadeOutTime = fadeOutTime;
        return this;
    }

    public TitleBar build() {
        try {
            final Object chatTitle = SpigotUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + this.title + "\"}");
            final Constructor<?> titleConstructor = SpigotUtils.getNMSClass("PacketPlayOutTitle").getConstructor(SpigotUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], SpigotUtils.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            this.packet = titleConstructor.newInstance(SpigotUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, this.fadeInTime, this.showTime, this.fadeOutTime);
            final Object chatsTitle = SpigotUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + this.subtitle + "\"}");
            final Constructor<?> timingTitleConstructor = SpigotUtils.getNMSClass("PacketPlayOutTitle").getConstructor(SpigotUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], SpigotUtils.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            this.timingPacket = timingTitleConstructor.newInstance(SpigotUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle, this.fadeInTime, this.showTime, this.fadeOutTime);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void sendForAll() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            this.send(player);
        }
    }

    public void send() {
        this.send(this.player);
    }

    public void send(final Player player) {
        SpigotUtils.sendPacket(player, this.packet);
        SpigotUtils.sendPacket(player, this.timingPacket);
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public int getFadeInTime() {
        return this.fadeInTime;
    }

    public int getShowTime() {
        return this.showTime;
    }

    public int getFadeOutTime() {
        return this.fadeOutTime;
    }

    public Object getPacket() {
        return this.packet;
    }

    public Object getTimingPacket() {
        return this.timingPacket;
    }

}
