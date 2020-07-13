package com.walkgs.crystolnetwork.offices.api;

import com.google.gson.Gson;
import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.events.PlayerInjectPermissibleEvent;
import com.walkgs.crystolnetwork.offices.events.PlayerUnInjectPermissibleEvent;
import com.walkgs.crystolnetwork.offices.inject.CrystolPermissible;
import com.walkgs.crystolnetwork.offices.inject.PermissibleInjector;
import com.walkgs.crystolnetwork.offices.job.RedisSender;
import com.walkgs.crystolnetwork.offices.manager.UserManager;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
import com.walkgs.crystolnetwork.offices.services.UserLoader;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerPermission {

    private final Plugin plugin;
    private final Gson gson = new Gson();

    private final UserLoader userLoader;
    private final GroupLoader groupLoader;

    private final ServerOffices serverOffices;

    public PlayerPermission(final ServerOffices serverOffices) {
        this.serverOffices = serverOffices;
        this.plugin = serverOffices.getPlugin();
        this.userLoader = serverOffices.getUserLoader();
        this.groupLoader = serverOffices.getGroupLoader();
    }

    public UserData getUser(OfflinePlayer player) {
        return new UserData(player.getUniqueId());
    }

    public UserData getUser(UUID uuid) {
        return new UserData(uuid);
    }

    public class UserData {

        private final UUID uuid;

        private final RedisSender redisSender;

        private UserManager userManager;

        public UserData(final UUID uuid) {

            this.uuid = uuid;

            this.redisSender = new RedisSender(serverOffices.getNetworkService());

            if (userLoader.hasLoaded(uuid))
                this.userManager = userLoader.get(uuid);

        }

        //REDIS FUNCTIONS

        //Add group

        public void addGroup(final GroupPermission groupPermission) {
            addGroups(serverOffices.getServerName(), false, groupPermission);
        }

        public void addGroup(final GroupPermission groupPermission, final String serverName) {
            addGroups(serverName, false, groupPermission);
        }

        public void addGroup(final GroupPermission groupPermission, final String serverName, final boolean force) {
            addGroups(serverName, force, groupPermission);
        }

        public void addGroup(final String groupPermission) {
            addGroups(serverOffices.getServerName(), false, groupPermission);
        }

        public void addGroup(final String groupPermission, final String serverName) {
            addGroups(serverName, false, groupPermission);
        }

        public void addGroup(final String groupPermission, final String serverName, final boolean force) {
            addGroups(serverName, force, groupPermission);
        }

        //Add groups

        public void addGroups(final GroupPermission... groupPermissions) {
            addGroups(Arrays.asList(groupPermissions), serverOffices.getServerName(), false);
        }

        public void addGroupsString(final String... groupPermissions) {
            addGroupsString(Arrays.asList(groupPermissions), serverOffices.getServerName(), false);
        }

        public void addGroups(final List<GroupPermission> groupPermissions) {
            addGroups(groupPermissions, serverOffices.getServerName(), false);
        }

        public void addGroupsString(final List<String> groupPermissions) {
            addGroupsString(groupPermissions, serverOffices.getServerName(), false);
        }

        public void addGroups(final String serverName, final GroupPermission... groupPermissions) {
            addGroups(Arrays.asList(groupPermissions), serverName, false);
        }

        public void addGroups(final String serverName, final String... groupPermissions) {
            addGroupsString(Arrays.asList(groupPermissions), serverName, false);
        }

        public void addGroups(final List<GroupPermission> groupPermissions, final String serverName) {
            addGroups(groupPermissions, serverName, false);
        }

        public void addGroupsString(final List<String> groupPermissions, final String serverName) {
            addGroupsString(groupPermissions, serverName, false);
        }

        public void addGroups(final String serverName, final boolean force, final GroupPermission... groupPermissions) {
            addGroups(Arrays.asList(groupPermissions), serverName, force);
        }

        public void addGroups(final String serverName, final boolean force, final String... groupPermissions) {
            addGroupsString(Arrays.asList(groupPermissions), serverName, force);
        }

        public void addGroups(final List<GroupPermission> groupPermissions, final String serverName, final boolean force) {
            final List<String> groups = new LinkedList<>();
            for (GroupPermission groupPermission : groupPermissions) {
                groups.add(groupPermission.getName());
            }
            addGroupsString(groups, serverName, force);
        }

        public void addGroupsString(final List<String> groupPermissions, final String serverName, final boolean force) {
            redisSender.clear();
            redisSender.add("updateOffice");
            redisSender.add("" + uuid);
            redisSender.add("addGroups");
            redisSender.add((force ? "forced" : "normal"));
            redisSender.add(gson.toJson(groupPermissions));
            redisSender.send(serverName);
        }

        //Remove group

        public void removeGroup(final GroupPermission groupPermission) {
            removeGroups(groupPermission);
        }

        public void removeGroup(final GroupPermission groupPermission, final String serverName) {
            removeGroups(serverName, groupPermission);
        }

        public void removeGroup(final String groupPermission) {
            removeGroups(groupPermission);
        }

        public void removeGroup(final String groupPermission, final String serverName) {
            removeGroupsString(serverName, groupPermission);
        }

        //Remove groups

        public void removeGroups(final GroupPermission... groupPermissions) {
            removeGroups(Arrays.asList(groupPermissions), serverOffices.getServerName());
        }

        public void removeGroups(final List<GroupPermission> groupPermissions) {
            removeGroups(groupPermissions, serverOffices.getServerName());
        }

        public void removeGroupsString(final List<String> groupPermissions) {
            removeGroupsString(groupPermissions, serverOffices.getServerName());
        }

        public void removeGroups(final String serverName, final GroupPermission... groupPermissions) {
            removeGroups(Arrays.asList(groupPermissions), serverName);
        }

        public void removeGroupsString(final String serverName, final String... groupPermissions) {
            removeGroupsString(Arrays.asList(groupPermissions), serverName);
        }

        public void removeGroups(final List<GroupPermission> groupPermissions, final String serverName) {
            final List<String> groups = new LinkedList<>();
            for (GroupPermission groupPermission : groupPermissions) {
                groups.add(groupPermission.getName());
            }
            removeGroupsString(groups, serverName);
        }

        public void removeGroupsString(final List<String> groupPermissions, final String serverName) {
            redisSender.clear();
            redisSender.add("updateOffice");
            redisSender.add("" + uuid);
            redisSender.add("removeGroups");
            redisSender.add(gson.toJson(groupPermissions));
            redisSender.send(serverName);
        }

        //LOCAL FUNCTIONS

        public boolean hasGroup(final int groupPermissionRank) {
            return userManager.hasGroup(groupPermissionRank);
        }

        public boolean hasGroup(final String groupPermission) {
            return userManager.hasGroup(groupPermission);
        }

        public boolean hasGroup(final GroupPermission groupPermission) {
            return userManager.hasGroup(groupPermission);
        }

        public boolean hasGroupOrHigher(final int groupPermissionRank) {
            return userManager.hasGroupOrHigher(groupPermissionRank);
        }

        public boolean hasGroupOrHigher(final String groupPermission) {
            return userManager.hasGroupOrHigher(groupPermission);
        }

        public boolean hasGroupOrHigher(final GroupPermission groupPermission) {
            return userManager.hasGroupOrHigher(groupPermission);
        }

        public GroupPermission getGroup(final int groupPermissionRank) {
            return userManager.getGroup(groupPermissionRank);
        }

        public GroupPermission getGroup(final String groupPermission) {
            return userManager.getGroup(groupPermission);
        }

        public GroupPermission getGroup(final GroupPermission groupPermission) {
            return userManager.getGroup(groupPermission);
        }

        public List<GroupPermission> getGroupAndHighers(final int groupPermissionRank) {
            return userManager.getGroupAndHighers(groupPermissionRank);
        }

        public List<GroupPermission> getGroupAndHighers(final String groupPermission) {
            return userManager.getGroupAndHighers(groupPermission);
        }

        public List<GroupPermission> getGroupAndHighers(final GroupPermission groupPermission) {
            return userManager.getGroupAndHighers(groupPermission);
        }

        public GroupPermission getLargestGroup() {
            return userManager.getLargestGroup();
        }

        public List<GroupPermission> getGroups() {
            return userManager.getGroups();
        }

        //Others

        public void load() {
            if (userLoader.loadIfNotLoaded(uuid, new UserManager(plugin, uuid))) {
                userManager = userLoader.get(uuid);
                userManager.addGroups(groupLoader.getDefaultGroups());
            }
        }

        public void inject() {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null) {
                final PermissibleBase permissibleBase = PermissibleInjector.getPermissible(player);
                if (permissibleBase != null) {

                    final CrystolPermissible newPermissibleBase = new CrystolPermissible(player);
                    newPermissibleBase.setOldPermissibleBase(permissibleBase);

                    final PlayerInjectPermissibleEvent permissibleEvent = new PlayerInjectPermissibleEvent(player, newPermissibleBase, permissibleBase).call();
                    if (!permissibleEvent.isCancelled()) {
                        PermissibleInjector.inject(player, permissibleEvent.getNewPermissible());
                    }
                }
            }
        }

        public void uninject() {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null) {
                final PermissibleBase injectedPermissibleBase = PermissibleInjector.getPermissible(player);
                if (injectedPermissibleBase != null) {

                    CrystolPermissible permissibleBase = (CrystolPermissible) injectedPermissibleBase;

                    final PlayerUnInjectPermissibleEvent permissibleEvent = new PlayerUnInjectPermissibleEvent(player, permissibleBase.getOldPermissibleBase(), permissibleBase).call();
                    if (!permissibleEvent.isCancelled()) {
                        PermissibleInjector.uninject(player);
                    }
                }
            }
        }

        public boolean alreadyLoaded() {
            return userLoader.hasLoaded(uuid);
        }

        public UUID getUUID() {
            return uuid;
        }

        public OfflinePlayer getPlayer() {
            return plugin.getServer().getOfflinePlayer(uuid);
        }

    }

}
