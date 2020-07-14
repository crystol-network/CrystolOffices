package com.walkgs.crystolnetwork.offices.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class JsonBuilder {

    private final ComponentBuilder componentBuilder;

    {
        componentBuilder = new ComponentBuilder("");
    }

    public JsonBuilder append(String message, String hover, HoverEvent.Action hoverType, String click, ClickEvent.Action clickType) {
        componentBuilder.append(message);
        if (click != null && clickType != null)
            componentBuilder.event(new ClickEvent(clickType, click));
        if (hover != null && hoverType != null)
            componentBuilder.event(new HoverEvent(hoverType, new ComponentBuilder(hover).create()));
        return this;
    }

    public JsonBuilder append(String message, String click, ClickEvent.Action clickType) {
        return append(message, null, null, click, clickType);
    }

    public JsonBuilder append(String message, String hover, HoverEvent.Action hoverType) {
        return append(message, hover, hoverType, null, null);
    }

    public JsonBuilder append(String message) {
        return append(message, null, null, null, null);
    }

    public BaseComponent[] build() {
        BaseComponent[] rcb = componentBuilder.create();
        return rcb;
    }

}
