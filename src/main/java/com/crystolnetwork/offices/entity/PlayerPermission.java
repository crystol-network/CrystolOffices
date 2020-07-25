package com.crystolnetwork.offices.entity;

import com.crystolnetwork.offices.events.PlayerInjectPermissibleEvent;
import com.crystolnetwork.offices.events.PlayerUnInjectPermissibleEvent;
import com.crystolnetwork.offices.manager.Group;
import com.crystolnetwork.offices.manager.UserGroup;
import com.crystolnetwork.offices.manager.job.jedis.RedisSender;
import com.crystolnetwork.offices.services.NetworkService;
import com.crystolnetwork.offices.services.OfficesServices;
import com.crystolnetwork.offices.services.loaders.GroupLoader;
import com.crystolnetwork.offices.services.loaders.UserLoader;
import com.crystolnetwork.offices.utils.exceptions.CrystolException;
import com.crystolnetwork.offices.utils.inject.CrystolPermissible;
import com.crystolnetwork.offices.utils.inject.PermissibleInjector;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.plugin.Plugin;

import java.util.*;

public final class PlayerPermission {

    private final UUID uuid;
    private final Plugin plugin;
    private final UserLoader userLoader;
    private final GroupLoader groupLoader;
    private final NetworkService networkService;
    private final OfficesServices officesServices;

    private final Gson gson = new Gson();

    private UserGroup userData;

    public PlayerPermission(final UUID uuid, final OfficesServices officesServices) {

        this.uuid = uuid;
        this.officesServices = officesServices;

        this.plugin = officesServices.getPlugin();
        this.userLoader = officesServices.getUserLoader();
        this.groupLoader = officesServices.getGroupLoader();
        this.networkService = officesServices.getNetworkService();

        if (userLoader.hasLoaded(uuid))
            this.userData = userLoader.get(uuid);

    }

    //REDIS FUNCTIONS

    //Add group

    public void addGroup(final Group groupService) {
        addGroups(officesServices.getServerName(), false, groupService);
    }

    public void addGroup(final Group groupService, final String serverName) {
        addGroups(serverName, false, groupService);
    }

    public void addGroup(final Group groupService, final String serverName, final boolean force) {
        addGroups(serverName, force, groupService);
    }

    public void addSilentGroup(final Group groupService) {
        addSilentGroups(groupService);
    }

    //Add groups

    public void addGroups(final Group... groupServices) {
        addGroups(officesServices.getServerName(), false, groupServices);
    }

    public void addGroups(final List<Group> groupServices) {
        addGroups(groupServices, officesServices.getServerName(), false);
    }

    public void addGroups(final String serverName, final Group... groupServices) {
        addGroups(serverName, false, groupServices);
    }

    public void addGroups(final List<Group> groupServices, final String serverName) {
        addGroups(groupServices, serverName, false);
    }

    public void addGroups(final String serverName, final boolean force, final Group... groupServices) {
        addGroups(Arrays.asList(groupServices), serverName, force);
    }

    public void addGroups(final List<Group> groupServices, final String serverName, final boolean force) {
        final RedisSender redisSender = new RedisSender(networkService);
        final List<Integer> groupsRanks = new LinkedList<>();
        for (Group groupService : groupServices)
            groupsRanks.add(groupService.getRank());
        redisSender.add("updateOffice");
        redisSender.add("" + uuid);
        redisSender.add("addGroups");
        redisSender.add((force ? "forced" : "normal"));
        redisSender.add(gson.toJson(groupsRanks));
        redisSender.send(serverName);
    }

    public void addSilentGroups(final Group... groupServices) {
        addSilentGroups(Arrays.asList(groupServices));
    }

    public void addSilentGroups(final List<Group> groupServices) {
        userData.addGroups(groupServices);
    }

    //Remove group

    public void removeGroup(final Group groupService) {
        removeGroups(groupService);
    }

    public void removeGroup(final Group groupService, final String serverName) {
        removeGroups(serverName, groupService);
    }

    public void removeSilentGroup(final Group groupService) {
        removeSilentGroups(groupService);
    }

    //Remove groups

    public void removeGroups(final Group... groupServices) {
        removeGroups(officesServices.getServerName(), groupServices);
    }

    public void removeGroups(final List<Group> groupServices) {
        removeGroups(groupServices, officesServices.getServerName());
    }

    public void removeGroups(final String serverName, final Group... groupServices) {
        removeGroups(Arrays.asList(groupServices), serverName);
    }

    public void removeGroups(final List<Group> groupServices, final String serverName) {
        final RedisSender redisSender = new RedisSender(networkService);
        final List<Integer> groupsRanks = new LinkedList<>();
        for (Group groupService : groupServices)
            groupsRanks.add(groupService.getRank());
        redisSender.add("updateOffice");
        redisSender.add("" + uuid);
        redisSender.add("removeGroups");
        redisSender.add(gson.toJson(groupsRanks));
        redisSender.send(serverName);
    }

    public void removeSilentGroups(final Group... groupServices) {
        removeSilentGroups(Arrays.asList(groupServices));
    }

    public void removeSilentGroups(final List<Group> groupServices) {
        userData.removeGroups(groupServices);
    }

    //LOCAL FUNCTIONS

    public boolean hasGroup(final int groupPermissionRank) {
        return userData.hasGroup(groupPermissionRank);
    }

    public boolean hasGroup(final Group groupService) {
        return userData.hasGroup(groupService);
    }

    public boolean hasGroupOrHigher(final int groupRank) {
        return userData.hasGroupOrHigher(groupRank);
    }

    public boolean hasGroupOrHigher(final Group group) {
        return userData.hasGroupOrHigher(group);
    }

    public Group getGroup(final int groupRank) {
        return userData.getGroup(groupRank);
    }

    public Group getGroup(final String groupName) {
        return userData.getGroup(groupName);
    }

    public Map<Integer, Group> getGroupAndHighers(final int groupRank) {
        return userData.getGroupAndHighers(groupRank);
    }

    public Map<Integer, Group> getGroupAndHighers(final Group group) {
        return userData.getGroupAndHighers(group);
    }

    public Group getLargestGroup() {
        return userData.getLargestGroup();
    }

    public List<Group> getGroups() {
        return userData.getGroups();
    }

    //User data manager
    public void setData(final String key, final Object object) {
        userData.setData(key, object);
    }

    public boolean removeData(final String key) {
        return userData.removeData(key);
    }

    public <T> T getData(final String key) {
        final Object data = userData.getData(key);
        return (data != null ? (T) userData.getData(key) : null);
    }

    public boolean hasData(final String key) {
        return userData.hasData(key);
    }

    //Others

    public void load() {
        if (userLoader.loadIfNotLoaded(uuid, new UserGroup(officesServices, uuid))) {
            userData = userLoader.get(uuid);
            addSilentGroups(groupLoader.getDefaultGroups());
            try {
                addSilentGroups(pullGroups());
            } catch (CrystolException e) {
                e.printStackTrace();
            }
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

    public void pushGroups() throws CrystolException {
        String groups = "";
        final Iterator<Group> groupsIterator = new LinkedList<>(getGroups()).iterator();
        while (groupsIterator.hasNext()){
            groups += groupsIterator.next().getName() + (groupsIterator.hasNext() ? "," : "");
        }
        try {
            final MongoCollection<Document> colletion = officesServices.getMongoJob().getColletion();
            final BasicDBObject document = new BasicDBObject("$set",
                            new BasicDBObject("uuid", uuid.toString())
                                .append("offices", groups)
                    );
            colletion.updateMany(Filters.eq("uuid", uuid.toString()), document);
        } catch (Exception e){
            throw new CrystolException("Failed to save groups.", this.getClass());
        }
    }

    public List<Group> pullGroups() throws CrystolException {
        final LinkedList<Group> groups = new LinkedList<>();
        try {
            final MongoCollection<Document> colletion = officesServices.getMongoJob().getColletion();
            final Document document = colletion.find(Filters.eq("uuid", uuid.toString())).first();
            if (document != null){
                final String[] groupsName = document.getString("offices").split(",");
                for (String groupName : groupsName){
                    Group group = groupLoader.getGroup(groupName);
                    if (group != null)
                        groups.add(group);
                }
            }
        } catch (Exception e){
            throw new CrystolException("Failed to load groups.", this.getClass());
        }
        return groups;
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
