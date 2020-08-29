package com.crystolnetwork.offices.utils;

import com.crystolnetwork.offices.interfaces.ClickAction;
import com.crystolnetwork.offices.services.JsonService;
import com.crystolnetwork.offices.services.SingletonService;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JsonMessage {
    private final Player player;
    private final JsonService jsonService;
    private final ComponentBuilder componentBuilder;

    public JsonMessage(final Player player) {
        this.jsonService = SingletonService.getOrFill(JsonService.class);
        this.componentBuilder = new ComponentBuilder("");
        this.player = player;
    }

    public JsonMessage append(final String message) {
        this.componentBuilder.append(message);
        return this;
    }

    public JsonMessage newLine() {
        return this.append("\n");
    }

    public JsonMessage event(final String hover, final HoverEvent.Action hoverType) {
        this.componentBuilder.event(new HoverEvent(hoverType, new ComponentBuilder(hover).create()));
        return this;
    }

    public JsonMessage event(final String click, final ClickEvent.Action clickType) {
        this.componentBuilder.event(new ClickEvent(clickType, click));
        return this;
    }

    public JsonMessage event(final ClickAction clickAction) {
        final String hoverName = UUID.randomUUID().toString();
        this.jsonService.register(this.player.getUniqueId(), hoverName, clickAction);
        this.componentBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + hoverName));
        return this;
    }

    public BaseComponent[] build() {
        return this.componentBuilder.create();
    }

    public void send() {
        this.player.spigot().sendMessage(this.build());
    }
}