package com.crystolnetwork.offices.security;

import com.crystolnetwork.offices.manager.configs.CredentialsConfig;

import java.io.File;

public final class SecurityService {

    private final CredentialsConfig internalBuffer;

    public SecurityService(File dir) {
        File configFile = new File(dir, "credentials.yml");
        this.internalBuffer = new CredentialsConfig(configFile.toPath());
    }

    public SecurityObject getCredential(String key) {
        return internalBuffer.getCredentials().get(key);
    }

    public String getServerName() {
        return internalBuffer.getServerName();
    }

    public boolean useMongoDB() {
        return internalBuffer.useMongoDB();
    }

}
