package com.walkgs.crystolnetwork.offices.manager;

import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.GroupService;
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

    private final Map<Integer, GroupService> groups = new LinkedHashMap<>();
    private final Map<String, Object> data = new LinkedHashMap<>();

    public UserManager(final ServerOffices serverOffices, final UUID uuid) {
        this.uuid = uuid;
        this.serverOffices = serverOffices;
        this.plugin = serverOffices.getPlugin();
        this.groupLoader = serverOffices.getGroupLoader();
    }

    public GroupService getLargestGroup() {
        if (largestRank == -1) return null;
        serverOffices.getPlugin().getServer().getConsoleSender().sendMessage("Largest: " + largestRank);
        return groups.get(largestRank);
    }

    public boolean addGroup(final GroupService groupService) {
        final int rank = groupService.getRank();
        if (hasGroup(rank)) return false;
        groups.put(rank, groupService);
        if (largestRank >= rank)
            largestRank = rank;
        return true;
    }

    public List<Boolean> addGroups(final List<GroupService> groupServices) {
        final List<Boolean> result = new ArrayList<>();
        for (GroupService groupService : groupServices) {
            result.add(addGroup(groupService));
        }
        return result;
    }

    public boolean removeGroup(final GroupService groupService) {
        return removeGroup(groupService, true);
    }

    private boolean removeGroup(final GroupService groupService, boolean recalculate) {
        final int rank = groupService.getRank();
        if (!hasGroup(rank)) return false;
        groups.remove(rank);
        if (recalculate) {
            if (!groups.isEmpty()) {
                if (largestRank > rank) {
                    for (GroupService group : groups.values()) {
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

    public List<Boolean> removeGroups(final List<GroupService> groupServices) {
        final List<Boolean> sucess = new ArrayList<>();
        for (GroupService groupService : groupServices)
            sucess.add(removeGroup(groupService, false));
        if (!groups.isEmpty()) {
            for (GroupService group : groups.values()) {
                final int groupRank = group.getRank();
                if (largestRank > groupRank)
                    largestRank = groupRank;
            }
        } else
            largestRank = -1;
        return sucess;
    }

    public GroupService getGroup(final String name) {
        final GroupService groupService = groupLoader.getGroup(name.toLowerCase());
        if (groupService == null || !hasGroup(groupService.getRank()))
            return null;
        return groupService;
    }


    public GroupService getGroup(final int rank) {
        return groups.get(rank);
    }

    public Map<Integer, GroupService> getGroupAndHighers(final int rank) {
        final Map<Integer, GroupService> result = new LinkedHashMap<>();

        final List<GroupService> groups = getGroups();
        Collections.sort(groups);

        for (GroupService groupService : groups) {
            final int groupRank = groupService.getRank();
            if (rank >= groupRank)
                result.put(groupRank, groupService);
        }

        return result;
    }

    public Map<Integer, GroupService> getGroupAndHighers(final GroupService groupService) {
        return getGroupAndHighers(groupService.getRank());
    }

    public boolean hasGroup(final GroupService groupService) {
        return hasGroup(groupService.getRank());
    }

    public boolean hasGroup(final int rank) {
        return groups.containsKey(rank);
    }

    public boolean hasGroupOrHigher(final GroupService groupService) {
        return !getGroupAndHighers(groupService.getRank()).isEmpty();
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

    public List<GroupService> getGroups() {
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
