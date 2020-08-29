package com.crystolnetwork.offices.utils;

import com.crystolnetwork.offices.utils.reflect.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class TitleBar {

    //Variables
    private String title;
    private String subtitle;
    private int fadeInTime;
    private int showTime;
    private int fadeOutTime;
    private Object packet;
    private Object timingPacket;

    /**
     * To define a title in the title bar.
     *
     * @param title is the title that will appear on the player's screen.
     */
    public TitleBar setTitle(final String title) {
        this.title = title;
        return this;
    }

    /**
     * To define a subtitle in the title bar.
     *
     * @param subtitle is the subtitle that will appear on the player's screen.
     */
    public TitleBar setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * To define the title bar properties.
     *
     * @param fadeInTime  it's time to appear on the player's screen.
     * @param showTime    it's time that will stay on the player's screen.
     * @param fadeOutTime it's time to disappear on the player's screen.
     */
    public TitleBar setOptions(final int fadeInTime, final int showTime, final int fadeOutTime) {
        this.fadeInTime = fadeInTime;
        this.showTime = showTime;
        this.fadeOutTime = fadeOutTime;
        return this;
    }

    /**
     * To create the title bar, doing so will be ready to be sent to the player.
     */
    public TitleBar build() {
        try {
            final Object chatTitle = Objects.requireNonNull(SpigotUtils.getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + this.title + "\"}");
            final Constructor<?> titleConstructor = Objects.requireNonNull(SpigotUtils.getNMSClass("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(SpigotUtils.getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], SpigotUtils.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            this.packet = titleConstructor.newInstance(Objects.requireNonNull(SpigotUtils.getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, this.fadeInTime, this.showTime, this.fadeOutTime);
            final Object chatsTitle = Objects.requireNonNull(SpigotUtils.getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + this.subtitle + "\"}");
            final Constructor<?> timingTitleConstructor = Objects.requireNonNull(SpigotUtils.getNMSClass("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(SpigotUtils.getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], SpigotUtils.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            this.timingPacket = timingTitleConstructor.newInstance(Objects.requireNonNull(SpigotUtils.getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle, this.fadeInTime, this.showTime, this.fadeOutTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * To send the title bar to all players.
     */
    public void sendForAll() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            this.send(player);
        }
    }

    /**
     * To send the title bar to a player.
     *
     * @param player is the player who will receive the title bar on your screen.
     */
    public void send(final Player player) {
        SpigotUtils.sendPacket(player, this.packet);
        SpigotUtils.sendPacket(player, this.timingPacket);
    }

    //Getters
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
