package com.crystolnetwork.offices.security;

import de.exlll.configlib.annotation.ConfigurationElement;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationElement
public final class SecurityObject {

    private Map<String, Object> acess = new LinkedHashMap<>();

    public SecurityObject() {
        this(false);
    }

    public SecurityObject(final boolean isUri) {
        if (isUri) {
            acess.put("uri", "mongo://localhost:27017");
        }
        else {
            acess.put("host", "localhost");
            acess.put("port", 5000);
            acess.put("user", "root");
            acess.put("password", "password");
            acess.put("database", "youdatabase");
        }
    }

    public SecurityObject(final Map<String, Object> acess) {
        this.acess = acess;
    }

    public String getHost() {
        return (String) acess.get("host");
    }

    public int getPort() {
        return (Integer) acess.get("port");
    }

    public String getDatabase() {
        return (String) acess.get("database");
    }

    public String getPassword() {
        return (String) acess.get("password");
    }

    public String getUser() {
        return (String) acess.get("user");
    }

    public String getUri() {
        return (String) acess.get("uri");
    }

}
