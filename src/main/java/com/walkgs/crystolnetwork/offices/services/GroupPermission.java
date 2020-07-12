package com.walkgs.crystolnetwork.offices.services;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GroupPermission {

    private final String name;
    private final String prefix;
    private final String suffix;

    private final int rank;

    private final boolean _default;

    //TODO: Function of Map<Permission, IsNode>
    private final List<String> permissions = new LinkedList<>();

    public GroupPermission(final String name, final String prefix, final String suffix, final int rank, final boolean _default) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.rank = rank;
        this._default = _default;
    }

    public boolean addPermission(final String permission) {
        boolean sucess = false;
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
            sucess = true;
        }
        return sucess;
    }

    public List<Boolean> addPermissions(final String... permissions) {
        final List<Boolean> sucessList = Arrays.asList(false);
        for (String permission : permissions)
            sucessList.add(addPermission(permission));
        return sucessList;
    }

    public boolean removePermission(final String permission) {
        boolean sucess = false;
        if (this.permissions.contains(permission)) {
            this.permissions.remove(permission);
            sucess = true;
        }
        return sucess;
    }

    public List<Boolean> removePermissions(final String... permissions) {
        final List<Boolean> sucessList = Arrays.asList(false);
        for (String permission : permissions)
            sucessList.add(removePermission(permission));
        return sucessList;
    }

    public boolean hasPermission(final String permission) {
        boolean has = permissions.contains(permission);

        if (permissions.contains("*"))
            has = true;

        if (!has) {

            String[] nodes = permission.split("\\.");
            if (nodes.length > 0) {
                String makeNode = "";
                for (String node : nodes) {
                    makeNode += node;
                    if (permissions.contains(makeNode + ".*")) {
                        has = true;
                        break;
                    }
                }
            }

        }

        return has;
    }

    public List<Boolean> hasPermissions(final String... permissions) {
        final List<Boolean> sucessList = Arrays.asList(false);
        for (String permission : permissions)
            sucessList.add(hasPermission(permission));
        return sucessList;
    }

    public void setPermissions(final List<String> permissions) {
        this.permissions.clear();
        this.permissions.addAll(permissions);
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getRank() {
        return rank;
    }

    public boolean isDefault() {
        return _default;
    }


}
