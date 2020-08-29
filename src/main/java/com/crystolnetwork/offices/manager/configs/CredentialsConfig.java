package com.crystolnetwork.offices.manager.configs;

import com.crystolnetwork.offices.security.SecurityObject;
import com.crystolnetwork.offices.utils.MultiValue;
import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ElementType;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Comment(
        {
                "credential file for security hosts",
                "[WARN] No TYPE is required in REDIS...",
                "These are the types of connections available: MONGODB, MYSQL, SQLITE",
                "Examples:",
                "- to use MONGODB leave the 'type: MONGODB' and leave only the 'URI: you_uri' field.",
                "- to use SQLITE leave the 'type: SQLITE' and leave only the 'host: localdb.db' field.",
                "- to use MYSQL leave the 'type: MYSQL' and leave only the fields:",
                "* 'host: address', 'port: address port', 'user: your user', 'password: your password', 'database: database name'"
        }
)
public final class CredentialsConfig extends BukkitYamlConfiguration {

    private String serverName = "ExampleServer";
    private boolean useMongoDB = false;

    @ElementType(SecurityObject.class)
    private Map<String, SecurityObject> credentials = new LinkedHashMap<>();

    public CredentialsConfig(Path path) {
        super(path);

        credentials.put("redis", new SecurityObject());
        credentials.put("database", new SecurityObject());

        loadAndSave();
    }

    public String getServerName() {
        return serverName;
    }

    public boolean useMongoDB() {
        return useMongoDB;
    }

    public Map<String, SecurityObject> getCredentials() {
        return credentials;
    }

}
