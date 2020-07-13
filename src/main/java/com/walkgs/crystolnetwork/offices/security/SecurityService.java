package com.walkgs.crystolnetwork.offices.security;

import com.walkgs.crystolnetwork.offices.utils.CredentialsConfig;

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

}
