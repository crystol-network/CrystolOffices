package com.crystolnetwork.offices;

import com.crystolnetwork.offices.entity.PlayerBase;
import com.crystolnetwork.offices.entity.PlayerPermission;
import com.crystolnetwork.offices.factory.TabFactory;
import com.crystolnetwork.offices.interfaces.TabUpdate;
import com.crystolnetwork.offices.listeners.InjectListener;
import com.crystolnetwork.offices.listeners.RedisListener;
import com.crystolnetwork.offices.manager.Group;
import com.crystolnetwork.offices.manager.job.jedis.RedisJob;
import com.crystolnetwork.offices.services.NetworkService;
import com.crystolnetwork.offices.services.OfficesServices;
import com.crystolnetwork.offices.services.SingletonService;
import com.crystolnetwork.offices.services.TabService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OfficesPlugin extends JavaPlugin {

    private OfficesServices officesServices;
    private NetworkService networkService;

    @Override
    public void onEnable() {

        officesServices = SingletonService.getOrFill(OfficesServices.class);
        networkService = officesServices.getNetworkService();

        if (officesServices.getGroupLoader().loadGroups()) {

            final PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvents(new InjectListener(), this);
            pluginManager.registerEvents(new RedisListener(), this);

            final PlayerBase playerBase = officesServices.getPlayerBase();

            final RedisJob redisJob = officesServices.getRedisJob();
            redisJob.start();

            for (Player player : Bukkit.getOnlinePlayers()) {
                final PlayerPermission user = playerBase.getUser(player);
                user.load();
                user.inject();
            }

            final TabService tabService = officesServices.getTabService();
            tabService.start(this, getServer());
            tabService.execute(new TabUpdate() {

                @Override
                public void onUpdate(TabFactory tabFactory) {
                    final Player player = tabFactory.getPlayer();
                    final PlayerPermission user = playerBase.getUser(player);
                    final Group groupService = user.getLargestGroup();
                    tabFactory.appendPrefix(groupService.getPrefix());
                    tabFactory.appendSuffix(groupService.getSuffix());
                }

            });

        }

        new OfficesCommands().load();

    }

    @Override
    public void onDisable() {
        networkService.closeJedisPool();
    }
}
