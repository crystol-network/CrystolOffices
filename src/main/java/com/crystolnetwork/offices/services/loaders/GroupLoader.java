package com.crystolnetwork.offices.services.loaders;


import com.crystolnetwork.offices.manager.Group;
import com.crystolnetwork.offices.manager.configs.OfficesConfig;
import com.crystolnetwork.offices.manager.configs.data.OfficesData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class GroupLoader {

    private final Plugin plugin;

    private File file;
    private OfficesConfig config;

    public GroupLoader(final Plugin plugin) {
        this.plugin = plugin;
    }

    private final Map<String, Group> groups = new LinkedHashMap();

    public List<Group> getDefaultGroups() {
        final List<Group> groups = new ArrayList<>();
        for (Group group : this.groups.values()) {
            if (group.isDefault())
                groups.add(group);
        }
        return groups;
    }

    public Group getGroup(String name) {
        return groups.get(name);
    }

    public Group getGroup(Integer rank) {
        for (Group groupService : groups.values()) {
            if (groupService.getRank() == rank)
                return groupService;
        }
        return null;
    }

    public List<Group> getGroups() {
        return new LinkedList<>(groups.values());
    }

    public boolean existsGroup(String name) {
        return groups.containsKey(name);
    }

    public boolean loadGroups() {

        file = new File(plugin.getDataFolder(), "config.yml");
        config = new OfficesConfig(file.toPath());

        groups.clear();

        Map<String, OfficesData> registredGroups = config.getRegistredGroups();
        for (Map.Entry<String, OfficesData> groups : registredGroups.entrySet()) {
            OfficesData officesData = groups.getValue();

            Group groupService = new Group(
                    groups.getKey(),
                    officesData.getPrefix(),
                    officesData.getSuffix(),
                    officesData.getRank(),
                    officesData.isDefault()
            );

            for (String permission : officesData.getPermissions())
                groupService.addPermission(permission.replaceFirst("e:", ""));

            for (Map.Entry<String, OfficesData> childrens : registredGroups.entrySet()) {
                OfficesData childrenData = childrens.getValue();
                if (childrenData.getRank() > groupService.getRank())
                    for (String permission : childrenData.getPermissions()) {
                        if (!permission.startsWith("e:"))
                            groupService.addPermission(permission);
                    }
            }

            this.groups.put(groupService.getName(), groupService);

        }

        Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] §aLoaded in total §2" + registredGroups.size() + " §aregistered groups.");

        return true;

    }

    public Plugin getPlugin() {
        return plugin;
    }

}
