package com.crystolnetwork.offices.manager.configs;

import com.crystolnetwork.offices.security.SecurityObject;
import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ElementType;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Comment(
        "credential file for security hosts"
)
public final class CredentialsConfig extends BukkitYamlConfiguration {

    private String serverName = "ExampleServer";

    @ElementType(SecurityObject.class)
    private Map<String, SecurityObject> credentials = new LinkedHashMap<>();

    public CredentialsConfig(Path path) {
        super(path);

        credentials.put("redis", new SecurityObject());
        credentials.put("mongo", new SecurityObject(true));

        loadAndSave();
    }

    public String getServerName() {
        return serverName;
    }

    public Map<String, SecurityObject> getCredentials() {
        return credentials;
    }

}
