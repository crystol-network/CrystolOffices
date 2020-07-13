package com.walkgs.crystolnetwork.offices.security;

import de.exlll.configlib.annotation.ConfigurationElement;

@ConfigurationElement
public final class SecurityObject {

    private String host, user, password, database;
    private int port;

    public SecurityObject() {
        this(
                "localhost",
                5000,
                "root",
                "senha",
                "store"
        );
    }

    public SecurityObject(
            String host,
            int port,
            String user,
            String password,
            String database
    ) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }
}
