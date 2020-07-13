package com.walkgs.crystolnetwork.offices.utils;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class CachedCycle {

    private final Plugin plugin;

    private static final Map<Plugin, Map<String, MCycle>> instances = new HashMap<>();

    public CachedCycle(Plugin plugin) {
        this.plugin = plugin;
    }

    public <T> ICycle<T> getOrCreate(String name) {
        return new MCycle().getOrCreate(plugin, name);
    }

    class MCycle {

        //TODO: GET OR CREATE PLUGIN REGISTRATION
        private <T> ICycle<T> getOrCreate(Plugin plugin, String name) {
            if (!containsPlugin(plugin))
                instances.put(plugin, new LinkedHashMap<>());
            Map<String, MCycle> cache = instances.get(plugin);
            if (!cache.containsKey(name))
                cache.put(name, new ICycle<T>());
            return (ICycle<T>) cache.get(name);
        }

        //TODO: CONTAINS PLUGIN
        private boolean containsPlugin(Plugin plugin) {
            return instances.containsKey(plugin);
        }

        //TODO: CLEAR INSTANCES
        private void clear() {
            instances.clear();
        }
        private void clear(Plugin plugin) {
            instances.get(plugin).clear();
        }

    }

    public class ICycle<T> extends MCycle {

        private T value;

        //TODO: GETTER OR DEFINE
        public T getOrComputer(Supplier<T> supplier) {
            final T _result = value;
            return (_result == null) ? maybeCompute(supplier) : _result;
        }

        //TODO: DEFINE IF NOT DEFINED
        private T maybeCompute(Supplier<T> supplier) {
            if (value == null)
                value = supplier.get();
            return value;
        }

    }

}
