package com.crystolnetwork.offices.security;

import com.crystolnetwork.offices.utils.MultiValue;
import de.exlll.configlib.annotation.ConfigurationElement;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationElement
public final class SecurityObject {

    private Map<String, Object> acess = new LinkedHashMap<>();

    public SecurityObject(){
        this(
                new MultiValue<>("type", "UNDEFINED"),
                new MultiValue<>("host", "HOSTNAME"),
                new MultiValue<>("port", 2000),
                new MultiValue<>("user", "USERNAME"),
                new MultiValue<>("password", "PASSWORD"),
                new MultiValue<>("database", "DATABASE")
        );
    }

    public SecurityObject(final MultiValue<String, Object>... values) {
        if (values == null) return;
        for (final MultiValue<String, Object> value : values) {
            acess.put(value.getOne(), value.getTwo());
        }
    }

    public SecurityObject(final Map<String, Object> acess) {
        this.acess = acess;
    }

    public String getUri() {
        return (String) acess.get("uri");
    }

    public String getType() {
        return (String) acess.get("type");
    }

    public String getHost() {
        return (String) acess.get("host");
    }

    public int getPort() {
        return (Integer) acess.get("port");
    }

    public String getUser() {
        return (String) acess.get("user");
    }

    public String getPassword() {
        return (String) acess.get("password");
    }

    public String getDatabase() {
        return (String) acess.get("database");
    }

}
