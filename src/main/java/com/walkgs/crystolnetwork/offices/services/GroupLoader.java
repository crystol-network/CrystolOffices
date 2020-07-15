package com.walkgs.crystolnetwork.offices.services;


import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.utils.OfficesConfig;
import com.walkgs.crystolnetwork.offices.utils.OfficesData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupLoader {

    private final Plugin plugin;

    private File file;
    private OfficesConfig config;
    private ServerOffices serverOffices;


    public GroupLoader(final Plugin plugin) {
        this.plugin = plugin;
    }

    private final Map<String, GroupPermission> groups = new LinkedHashMap();

    public List<GroupPermission> getDefaultGroups() {
        final List<GroupPermission> groups = new ArrayList<>();
        for (GroupPermission group : this.groups.values()) {
            if (group.isDefault())
                groups.add(group);
        }
        return groups;
    }

    public GroupPermission getGroup(String name) {
        return groups.get(name);
    }

    public GroupPermission getGroup(Integer rank) {
        for (GroupPermission groupPermission : groups.values()) {
            if (groupPermission.getRank() == rank)
                return groupPermission;
        }
        return null;
    }

    public boolean existsGroup(String name) {
        return groups.containsKey(name);
    }

    public boolean loadGroups() {

        file = new File(plugin.getDataFolder(), "config.yml");
        config = new OfficesConfig(file.toPath());
        config.loadAndSave();
        serverOffices = ServerOffices.getInstance();

        groups.clear();

        serverOffices.setServerName(config.getServerName());

        Map<String, OfficesData> registredGroups = config.getRegistredGroups();
        for (Map.Entry<String, OfficesData> groups : registredGroups.entrySet()) {
            OfficesData officesData = groups.getValue();

            GroupPermission groupPermission = new GroupPermission(
                    groups.getKey(),
                    officesData.getPrefix(),
                    officesData.getSuffix(),
                    officesData.getRank(),
                    officesData.isDefault()
            );

            for (String permission : officesData.getPermissions())
                groupPermission.addPermission(permission);

            for (Map.Entry<String, OfficesData> childrens : registredGroups.entrySet()) {
                OfficesData childrenData = childrens.getValue();
                if (childrenData.getRank() > groupPermission.getRank())
                    for (String permission : childrenData.getPermissions()) {
                        if (permission.startsWith("c:"))
                            groupPermission.addPermission(permission.replaceFirst("c:", ""));
                    }
            }

            this.groups.put(groupPermission.getName(), groupPermission);

        }

        Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] §aLoaded in total §2" + registredGroups.size() + " §aregistered groups.");

        return true;

    }

    public Plugin getPlugin() {
        return plugin;
    }

}
