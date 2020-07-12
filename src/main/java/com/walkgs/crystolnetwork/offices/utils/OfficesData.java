package com.walkgs.crystolnetwork.offices.utils;

import de.exlll.configlib.annotation.ConfigurationElement;

import java.util.Arrays;
import java.util.List;

@ConfigurationElement
public final class OfficesData {

    private int rank;
    private String prefix;
    private String suffix;
    private boolean odefault;
    private List<String> permissions;

    public OfficesData() {
        this(0, "&a", "", true, Arrays.asList("permission.*"));
    }

    public OfficesData(int rank, String prefix, String suffix, boolean odefault, List<String> permissions) {
        this.rank = rank;
        this.prefix = prefix;
        this.suffix = suffix;
        this.odefault = odefault;
        this.permissions = permissions;
    }

    public int getRank() {
        return rank;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean isDefault() {
        return odefault;
    }

    public List<String> getPermissions() {
        return permissions;
    }

}