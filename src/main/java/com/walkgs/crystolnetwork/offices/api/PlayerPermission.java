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

import java.util.*;

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

        //Add groups

        public void addGroups(final GroupPermission... groupPermissions) {
            addGroups(serverOffices.getServerName(), false, groupPermissions);
        }

        public void addGroups(final List<GroupPermission> groupPermissions) {
            addGroups(groupPermissions, serverOffices.getServerName(), false);
        }

        public void addGroups(final String serverName, final GroupPermission... groupPermissions) {
            addGroups(serverName, false, groupPermissions);
        }

        public void addGroups(final List<GroupPermission> groupPermissions, final String serverName) {
            addGroups(groupPermissions, serverName, false);
        }

        public void addGroups(final String serverName, final boolean force, final GroupPermission... groupPermissions) {
            addGroups(Arrays.asList(groupPermissions), serverName, force);
        }

        public void addGroups(final List<GroupPermission> groupPermissions, final String serverName, final boolean force) {
            final List<Integer> groupsRanks = new LinkedList<>();
            for (GroupPermission groupPermission : groupPermissions)
                groupsRanks.add(groupPermission.getRank());
            redisSender.add("updateOffice");
            redisSender.add("" + uuid);
            redisSender.add("addGroups");
            redisSender.add((force ? "forced" : "normal"));
            redisSender.add(gson.toJson(groupsRanks));
            redisSender.send(serverName);
        }

        //Remove group

        public void removeGroup(final GroupPermission groupPermission) {
            removeGroups(groupPermission);
        }

        public void removeGroup(final GroupPermission groupPermission, final String serverName) {
            removeGroups(serverName, groupPermission);
        }

        //Remove groups

        public void removeGroups(final GroupPermission... groupPermissions) {
            removeGroups(serverOffices.getServerName(), groupPermissions);
        }

        public void removeGroups(final List<GroupPermission> groupPermissions) {
            removeGroups(groupPermissions, serverOffices.getServerName());
        }

        public void removeGroups(final String serverName, final GroupPermission... groupPermissions) {
            removeGroups(Arrays.asList(groupPermissions), serverName);
        }

        public void removeGroups(final List<GroupPermission> groupPermissions, final String serverName) {
            final List<Integer> groupsRanks = new LinkedList<>();
            for (GroupPermission groupPermission : groupPermissions)
                groupsRanks.add(groupPermission.getRank());
            redisSender.add("updateOffice");
            redisSender.add("" + uuid);
            redisSender.add("removeGroups");
            redisSender.add(gson.toJson(groupsRanks));
            redisSender.send(serverName);
        }

        //LOCAL FUNCTIONS

        public boolean hasGroup(final int groupPermissionRank) {
            return userManager.hasGroup(groupPermissionRank);
        }

        public boolean hasGroup(final GroupPermission groupPermission) {
            return userManager.hasGroup(groupPermission);
        }

        public boolean hasGroupOrHigher(final int groupRank) {
            return userManager.hasGroupOrHigher(groupRank);
        }

        public boolean hasGroupOrHigher(final GroupPermission group) {
            return userManager.hasGroupOrHigher(group);
        }

        public GroupPermission getGroup(final int groupRank) {
            return userManager.getGroup(groupRank);
        }

        public GroupPermission getGroup(final String groupName) {
            return userManager.getGroup(groupName);
        }

        public Map<Integer, GroupPermission> getGroupAndHighers(final int groupRank) {
            return userManager.getGroupAndHighers(groupRank);
        }

        public Map<Integer, GroupPermission> getGroupAndHighers(final GroupPermission group) {
            return userManager.getGroupAndHighers(group);
        }

        public GroupPermission getLargestGroup() {
            return userManager.getLargestGroup();
        }

        public List<GroupPermission> getGroups() {
            return userManager.getGroups();
        }

        //User data manager
        public void setData(final String key, final Object object) {
            userManager.setData(key, object);
        }

        public boolean removeData(final String key) {
            return userManager.removeData(key);
        }

        public <T> T getData(final String key) {
            final Object data = userManager.getData(key);
            return (data != null ? (T) userManager.getData(key) : null);
        }

        public boolean hasData(final String key) {
            return userManager.hasData(key);
        }

        //Others

        public void load() {
            if (userLoader.loadIfNotLoaded(uuid, new UserManager(serverOffices, uuid))) {
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
