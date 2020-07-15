package com.walkgs.crystolnetwork.offices.manager;

import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.util.*;

public class UserManager implements Serializable, Cloneable {

    private final ServerOffices serverOffices;
    private final GroupLoader groupLoader;
    private final Plugin plugin;
    private final UUID uuid;

    private int largestRank = -1;

    //Caches

    private final Map<Integer, GroupPermission> groups = new LinkedHashMap<>();
    private final Map<String, Object> data = new LinkedHashMap<>();

    public UserManager(final ServerOffices serverOffices, final UUID uuid) {
        this.uuid = uuid;
        this.serverOffices = serverOffices;
        this.plugin = serverOffices.getPlugin();
        this.groupLoader = serverOffices.getGroupLoader();
    }

    public GroupPermission getLargestGroup() {
        if (largestRank == -1) return null;
        return groups.get(largestRank);
    }

    public boolean addGroup(final GroupPermission groupPermission) {
        final int rank = groupPermission.getRank();
        if (hasGroup(rank)) return false;
        groups.put(rank, groupPermission);
        if (largestRank >= rank)
            largestRank = rank;
        return true;
    }

    public List<Boolean> addGroups(final List<GroupPermission> groupPermissions) {
        final List<Boolean> result = new ArrayList<>();
        for (GroupPermission groupPermission : groupPermissions) {
            result.add(addGroup(groupPermission));
        }
        return result;
    }

    public boolean removeGroup(final GroupPermission groupPermission) {
        return removeGroup(groupPermission, true);
    }

    private boolean removeGroup(final GroupPermission groupPermission, boolean recalculate) {
        final int rank = groupPermission.getRank();
        if (!hasGroup(rank)) return false;
        groups.remove(rank);
        if (recalculate) {
            if (!groups.isEmpty()) {
                if (largestRank > rank) {
                    for (GroupPermission group : groups.values()) {
                        final int groupRank = group.getRank();
                        if (largestRank > groupRank)
                            largestRank = groupRank;
                    }
                }
            } else
                largestRank = -1;
        }
        return true;
    }

    public List<Boolean> removeGroups(final List<GroupPermission> groupPermissions) {
        final List<Boolean> sucess = new ArrayList<>();
        for (GroupPermission groupPermission : groupPermissions)
            sucess.add(removeGroup(groupPermission, false));
        if (!groups.isEmpty()) {
            for (GroupPermission group : groups.values()) {
                final int groupRank = group.getRank();
                if (largestRank > groupRank)
                    largestRank = groupRank;
            }
        } else
            largestRank = -1;
        return sucess;
    }

    public GroupPermission getGroup(final String name) {
        final GroupPermission groupPermission = groupLoader.getGroup(name.toLowerCase());
        if (groupPermission == null || !hasGroup(groupPermission.getRank()))
            return null;
        return groupPermission;
    }


    public GroupPermission getGroup(final int rank) {
        return groups.get(rank);
    }

    public Map<Integer, GroupPermission> getGroupAndHighers(final int rank) {
        final Map<Integer, GroupPermission> result = new LinkedHashMap<>();

        final List<GroupPermission> groups = getGroups();
        Collections.sort(groups);

        for (GroupPermission groupPermission : groups) {
            final int groupRank = groupPermission.getRank();
            if (rank >= groupRank)
                result.put(groupRank, groupPermission);
        }

        return result;
    }

    public Map<Integer, GroupPermission> getGroupAndHighers(final GroupPermission groupPermission) {
        return getGroupAndHighers(groupPermission.getRank());
    }

    public boolean hasGroup(final GroupPermission groupPermission) {
        return hasGroup(groupPermission.getRank());
    }

    public boolean hasGroup(final int rank) {
        return groups.containsKey(rank);
    }

    public boolean hasGroupOrHigher(final GroupPermission groupPermission) {
        return !getGroupAndHighers(groupPermission.getRank()).isEmpty();
    }

    public boolean hasGroupOrHigher(final int rank) {
        return !getGroupAndHighers(rank).isEmpty();
    }

    //Data setter

    public void setData(final String key, final Object object) {
        removeData(key);
        data.put(key, object);
    }

    public boolean removeData(final String key) {
        if (!hasData(key)) return false;
        data.remove(key);
        return true;
    }

    public Object getData(final String key) {
        return data.get(key);
    }

    public boolean hasData(final String key) {
        return data.containsKey(key);
    }

    //Others

    public List<GroupPermission> getGroups() {
        return new LinkedList<>(groups.values());
    }

    public UUID getUUID() {
        return uuid;
    }

    public OfflinePlayer getPlayer() {
        return plugin.getServer().getOfflinePlayer(uuid);
    }

    public ServerOffices getServerOffices() {
        return serverOffices;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public GroupLoader getGroupLoader() {
        return groupLoader;
    }
}
