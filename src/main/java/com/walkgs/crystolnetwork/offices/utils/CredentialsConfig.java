package com.walkgs.crystolnetwork.offices.utils;

import com.walkgs.crystolnetwork.offices.security.SecurityObject;
import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ElementType;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Comment(
        "credential file for security hosts"
)
public class CredentialsConfig extends BukkitYamlConfiguration {

    public CredentialsConfig(Path path) {
        super(path);

        loadAndSave();
    }

    @ElementType(SecurityObject.class)
    private Map<String, SecurityObject> credentials = getExistsCredentials();

    private Map<String, SecurityObject> getExistsCredentials() {
        Map<String, SecurityObject> credentials = new HashMap<>();
        credentials.put("redis", new SecurityObject());

        return credentials;
    }

    public Map<String, SecurityObject> getCredentials() {
        return credentials;
    }
}
