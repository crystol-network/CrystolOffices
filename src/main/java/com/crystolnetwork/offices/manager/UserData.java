package com.crystolnetwork.offices.manager;

import com.crystolnetwork.offices.services.OfficesServices;
import com.crystolnetwork.offices.services.loaders.GroupLoader;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.util.*;

public class UserData implements Serializable, Cloneable {

    private final OfficesServices officesServices;
    private final GroupLoader groupLoader;
    private final Plugin plugin;
    private final UUID uuid;

    private int largestRank = -1;

    //Caches

    private final Map<Integer, Group> groups = new LinkedHashMap<>();
    private final Map<String, Object> data = new LinkedHashMap<>();

    public UserData(final OfficesServices officesServices, final UUID uuid) {
        this.uuid = uuid;
        this.officesServices = officesServices;
        this.plugin = officesServices.getPlugin();
        this.groupLoader = officesServices.getGroupLoader();
    }

    public Group getLargestGroup() {
        if (largestRank == -1) return null;
        return groups.get(largestRank);
    }

    public boolean addGroup(final Group groupService) {
        final int rank = groupService.getRank();
        if (hasGroup(rank)) return false;
        groups.put(rank, groupService);
        if (largestRank == -1 || largestRank >= rank)
            largestRank = rank;
        return true;
    }

    public List<Boolean> addGroups(final List<Group> groupServices) {
        final List<Boolean> result = new ArrayList<>();
        for (Group groupService : groupServices) {
            result.add(addGroup(groupService));
        }
        return result;
    }

    public boolean removeGroup(final Group groupService) {
        return removeGroup(groupService, true);
    }

    private boolean removeGroup(final Group groupService, boolean recalculate) {
        final int rank = groupService.getRank();
        if (!hasGroup(rank)) return false;
        groups.remove(rank);
        if (recalculate) {
            if (!groups.isEmpty()) {
                if (largestRank > rank) {
                    for (Group group : groups.values()) {
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

    public List<Boolean> removeGroups(final List<Group> groupServices) {
        final List<Boolean> sucess = new ArrayList<>();
        for (Group groupService : groupServices)
            sucess.add(removeGroup(groupService, false));
        if (!groups.isEmpty()) {
            for (Group group : groups.values()) {
                final int groupRank = group.getRank();
                if (largestRank > groupRank)
                    largestRank = groupRank;
            }
        } else
            largestRank = -1;
        return sucess;
    }

    public Group getGroup(final String name) {
        final Group groupService = groupLoader.getGroup(name.toLowerCase());
        if (groupService == null || !hasGroup(groupService.getRank()))
            return null;
        return groupService;
    }


    public Group getGroup(final int rank) {
        return groups.get(rank);
    }

    public Map<Integer, Group> getGroupAndHighers(final int rank) {
        final Map<Integer, Group> result = new LinkedHashMap<>();

        final List<Group> groups = getGroups();
        Collections.sort(groups);

        for (Group groupService : groups) {
            final int groupRank = groupService.getRank();
            if (rank >= groupRank)
                result.put(groupRank, groupService);
        }

        return result;
    }

    public Map<Integer, Group> getGroupAndHighers(final Group groupService) {
        return getGroupAndHighers(groupService.getRank());
    }

    public boolean hasGroup(final Group groupService) {
        return hasGroup(groupService.getRank());
    }

    public boolean hasGroup(final int rank) {
        return groups.containsKey(rank);
    }

    public boolean hasGroupOrHigher(final Group groupService) {
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

    public List<Group> getGroups() {
        return new LinkedList<>(groups.values());
    }

    public UUID getUUID() {
        return uuid;
    }

    public OfflinePlayer getPlayer() {
        return plugin.getServer().getOfflinePlayer(uuid);
    }

    public OfficesServices getOfficesServices() {
        return officesServices;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public GroupLoader getGroupLoader() {
        return groupLoader;
    }
}
