package com.walkgs.crystolnetwork.offices.utils;

import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ElementType;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Comment({
        "This plugin developed by WalkGS for CrystolNetwork",
        "Use 'e:' to leave the permission exclusive to a group.",
        "Use '*' to have all permissions",
        "Use 'permission. *' To have all permissions for a certain permission"
})
public final class OfficesConfig extends BukkitYamlConfiguration {


    //TODO: THIS CONFIG LOADER

    private String serverName = "ExampleServer";

    @ElementType(OfficesData.class)
    private Map<String, OfficesData> groups = new LinkedHashMap();

    {
        groups.put("example", new OfficesData());
        groups.put("example2", new OfficesData(1));
    }

    public OfficesConfig(Path path) {
        super(path);
    }

    public String getServerName() {
        return serverName;
    }

    public Map<String, OfficesData> getRegistredGroups() {
        return groups;
    }

}
