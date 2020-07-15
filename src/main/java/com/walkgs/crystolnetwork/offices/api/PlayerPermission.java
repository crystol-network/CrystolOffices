package com.walkgs.crystolnetwork.offices.api;

import com.google.gson.Gson;
import com.walkgs.crystolnetwork.offices.api.services.OfficesServices;
import com.walkgs.crystolnetwork.offices.events.PlayerInjectPermissibleEvent;
import com.walkgs.crystolnetwork.offices.events.PlayerUnInjectPermissibleEvent;
import com.walkgs.crystolnetwork.offices.inject.CrystolPermissible;
import com.walkgs.crystolnetwork.offices.inject.PermissibleInjector;
import com.walkgs.crystolnetwork.offices.job.RedisSender;
import com.walkgs.crystolnetwork.offices.manager.UserManager;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.GroupService;
import com.walkgs.crystolnetwork.offices.services.NetworkService;
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
    private final NetworkService networkService;

    private final OfficesServices officesServices;

    public PlayerPermission(final OfficesServices officesServices) {
        this.officesServices = officesServices;
        this.plugin = officesServices.getPlugin();
        this.userLoader = officesServices.getUserLoader();
        this.groupLoader = officesServices.getGroupLoader();
        this.networkService = officesServices.getNetworkService();
    }

    public UserData getUser(OfflinePlayer player) {
        return new UserData(player.getUniqueId());
    }

    public UserData getUser(UUID uuid) {
        return new UserData(uuid);
    }

    public class UserData {

        private final UUID uuid;

        private UserManager userManager;

        public UserData(final UUID uuid) {

            this.uuid = uuid;

            if (userLoader.hasLoaded(uuid))
                this.userManager = userLoader.get(uuid);

        }

        //REDIS FUNCTIONS

        //Add group

        public void addGroup(final GroupService groupService) {
            addGroups(officesServices.getServerName(), false, groupService);
        }

        public void addGroup(final GroupService groupService, final String serverName) {
            addGroups(serverName, false, groupService);
        }

        public void addGroup(final GroupService groupService, final String serverName, final boolean force) {
            addGroups(serverName, force, groupService);
        }

        //Add groups

        public void addGroups(final GroupService... groupServices) {
            addGroups(officesServices.getServerName(), false, groupServices);
        }

        public void addGroups(final List<GroupService> groupServices) {
            addGroups(groupServices, officesServices.getServerName(), false);
        }

        public void addGroups(final String serverName, final GroupService... groupServices) {
            addGroups(serverName, false, groupServices);
        }

        public void addGroups(final List<GroupService> groupServices, final String serverName) {
            addGroups(groupServices, serverName, false);
        }

        public void addGroups(final String serverName, final boolean force, final GroupService... groupServices) {
            addGroups(Arrays.asList(groupServices), serverName, force);
        }

        public void addGroups(final List<GroupService> groupServices, final String serverName, final boolean force) {
            final RedisSender redisSender = new RedisSender(networkService);
            final List<Integer> groupsRanks = new LinkedList<>();
            for (GroupService groupService : groupServices)
                groupsRanks.add(groupService.getRank());
            redisSender.add("updateOffice");
            redisSender.add("" + uuid);
            redisSender.add("addGroups");
            redisSender.add((force ? "forced" : "normal"));
            redisSender.add(gson.toJson(groupsRanks));
            redisSender.send(serverName);
        }

        //Remove group

        public void removeGroup(final GroupService groupService) {
            removeGroups(groupService);
        }

        public void removeGroup(final GroupService groupService, final String serverName) {
            removeGroups(serverName, groupService);
        }

        //Remove groups

        public void removeGroups(final GroupService... groupServices) {
            removeGroups(officesServices.getServerName(), groupServices);
        }

        public void removeGroups(final List<GroupService> groupServices) {
            removeGroups(groupServices, officesServices.getServerName());
        }

        public void removeGroups(final String serverName, final GroupService... groupServices) {
            removeGroups(Arrays.asList(groupServices), serverName);
        }

        public void removeGroups(final List<GroupService> groupServices, final String serverName) {
            final RedisSender redisSender = new RedisSender(networkService);
            final List<Integer> groupsRanks = new LinkedList<>();
            for (GroupService groupService : groupServices)
                groupsRanks.add(groupService.getRank());
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

        public boolean hasGroup(final GroupService groupService) {
            return userManager.hasGroup(groupService);
        }

        public boolean hasGroupOrHigher(final int groupRank) {
            return userManager.hasGroupOrHigher(groupRank);
        }

        public boolean hasGroupOrHigher(final GroupService group) {
            return userManager.hasGroupOrHigher(group);
        }

        public GroupService getGroup(final int groupRank) {
            return userManager.getGroup(groupRank);
        }

        public GroupService getGroup(final String groupName) {
            return userManager.getGroup(groupName);
        }

        public Map<Integer, GroupService> getGroupAndHighers(final int groupRank) {
            return userManager.getGroupAndHighers(groupRank);
        }

        public Map<Integer, GroupService> getGroupAndHighers(final GroupService group) {
            return userManager.getGroupAndHighers(group);
        }

        public GroupService getLargestGroup() {
            return userManager.getLargestGroup();
        }

        public List<GroupService> getGroups() {
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
            if (userLoader.loadIfNotLoaded(uuid, new UserManager(officesServices, uuid))) {
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
