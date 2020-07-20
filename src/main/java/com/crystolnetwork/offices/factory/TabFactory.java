package com.crystolnetwork.offices.factory;

import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public final class TabFactory {

    private final Player player;

    private int rank = -1;

    private final Map<Integer, String> prefix = new LinkedHashMap<>();
    private final Map<Integer, String> suffix = new LinkedHashMap<>();

    private int lastPrefixIndex = -1;
    private int lastSuffixIndex = -1;

    public TabFactory(final Player player) {
        this.player = player;
    }

    public void appendPrefix(String prefix) {
        appendPrefix(getLastPrefixIndex() + 1, prefix);
    }

    public void appendPrefix(int index, String prefix) {
        this.prefix.put(index, prefix);
        lastPrefixIndex++;
    }

    public void appendSuffix(String suffix) {
        appendSuffix(getLastPrefixIndex() + 1, suffix);
    }

    public void appendSuffix(int index, String suffix) {
        this.prefix.put(index, suffix);
        lastPrefixIndex++;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public Map<Integer, String> getPrefix() {
        return prefix;
    }

    public Map<Integer, String> getSuffix() {
        return suffix;
    }

    public int getLastPrefixIndex() {
        return lastPrefixIndex;
    }

    public int getLastSuffixIndex() {
        return lastSuffixIndex;
    }

    public Player getPlayer() {
        return player;
    }

    public void reset() {
        prefix.clear();
        suffix.clear();
        lastPrefixIndex = -1;
        lastSuffixIndex = -1;
    }

}
