package com.walkgs.crystolnetwork.offices.manager;

import com.walkgs.crystolnetwork.offices.services.GroupPermission;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.util.*;

public class UserManager implements Serializable, Cloneable {

    private static final List<GroupPermission> EMPTY_BUFFER = new LinkedList<>();

    private final Plugin plugin;
    private final UUID uuid;

    private final List<GroupPermission> groups = new LinkedList<>();

    public UserManager(final Plugin plugin, final UUID uuid) {
        this.uuid = uuid;
        this.plugin = plugin;
    }

    public GroupPermission getLargestGroup() {
        GroupPermission largestGroup = null;

        for (GroupPermission group : groups) {
            if (largestGroup != null && largestGroup.getRank() > group.getRank()) continue;
            largestGroup = group;
        }

        return largestGroup;
    }

    public boolean addGroup(final GroupPermission groupPermission) {
        if (groups.contains(groupPermission)) return false;

        groups.add(groupPermission);
        return true;
    }

    public List<Boolean> addGroups(final GroupPermission... groupPermissions) {
        return addGroups(Arrays.asList(groupPermissions));
    }

    public List<Boolean> addGroups(final List<GroupPermission> groupPermissions) {
        final List<Boolean> sucess = new ArrayList<>();
        for (GroupPermission groupPermission : groupPermissions) {
            sucess.add(addGroup(groupPermission));
        }
        return sucess;
    }

    public boolean removeGroup(final GroupPermission groupPermission) {
        if (!groups.contains(groupPermission)) return false;

        groups.remove(groupPermission);
        return true;
    }

    public List<Boolean> removeGroups(final GroupPermission... groupPermissions) {
        return removeGroups(Arrays.asList(groupPermissions));
    }

    public List<Boolean> removeGroups(final List<GroupPermission> groupPermissions) {
        final List<Boolean> sucess = new ArrayList<>();
        for (GroupPermission groupPermission : groupPermissions)
            sucess.add(removeGroup(groupPermission));
        return sucess;
    }

    public GroupPermission getGroup(final String name) {
        GroupPermission groupPermission = null;

        for (GroupPermission group : groups) {
            if (group.getName().toLowerCase().equals(name.toLowerCase()))
                groupPermission = group;
        }

        return groupPermission;
    }


    public GroupPermission getGroup(final int rank) {
        GroupPermission groupPermission = null;

        for (GroupPermission group : groups) {
            if (group.getRank() == rank)
                groupPermission = group;
        }

        return groupPermission;
    }

    public GroupPermission getGroup(final GroupPermission groupPermission) {
        return getGroup(groupPermission.getRank());
    }

    public List<GroupPermission> getGroupAndHighers(final String name) {
        List<GroupPermission> groups = new ArrayList<>();

        GroupPermission groupPermission = getGroup(name);
        if (groupPermission != null) {
            groups.addAll(getGroupHighers(groupPermission));
            groups.add(groupPermission);
        }

        return groups;
    }

    public List<GroupPermission> getGroupAndHighers(final int rank) {
        List<GroupPermission> groups = new ArrayList<>();

        GroupPermission groupPermission = getGroup(rank);
        if (groupPermission != null) {
            groups.addAll(getGroupHighers(groupPermission));
            groups.add(groupPermission);
        }

        return groups;
    }

    public List<GroupPermission> getGroupAndHighers(final GroupPermission groupPermission) {
        return getGroupAndHighers(groupPermission.getRank());
    }

    public boolean hasGroup(final String name) {
        return getGroup(name) != null;
    }

    public boolean hasGroup(final GroupPermission groupPermission) {
        return getGroup(groupPermission.getRank()) != null;
    }

    public boolean hasGroup(final int rank) {
        return getGroup(rank) != null;
    }

    public boolean hasGroupOrHigher(final String name) {
        return !getGroupAndHighers(name).isEmpty();
    }

    public boolean hasGroupOrHigher(final GroupPermission groupPermission) {
        return !getGroupAndHighers(groupPermission.getRank()).isEmpty();
    }

    public boolean hasGroupOrHigher(final int rank) {
        return !getGroupAndHighers(rank).isEmpty();
    }

    public List<GroupPermission> getGroupHighers(final GroupPermission groupPermission) {
        if (groupPermission == null) return EMPTY_BUFFER;

        List<GroupPermission> groups = new LinkedList<>();

        for (GroupPermission group : groups) {
            if (group.getRank() < groupPermission.getRank())
                groups.add(group);
        }

        return groups;
    }

    public List<GroupPermission> getGroups() {
        return groups;
    }

    public UUID getUUID() {
        return uuid;
    }

    public OfflinePlayer getPlayer() {
        return plugin.getServer().getOfflinePlayer(uuid);
    }

    public Plugin getPlugin() {
        return plugin;
    }

}
