package com.walkgs.crystolnetwork.offices;

import com.walkgs.crystolnetwork.offices.api.PlayerPermission;
import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.job.RedisJob;
import com.walkgs.crystolnetwork.offices.listeners.InjectListener;
import com.walkgs.crystolnetwork.offices.listeners.RedisListener;
import com.walkgs.crystolnetwork.offices.services.GroupService;
import com.walkgs.crystolnetwork.offices.services.NetworkService;
import com.walkgs.crystolnetwork.offices.services.TabService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class OfficesPlugin extends JavaPlugin {

    private ServerOffices serverOffices;
    private NetworkService networkService;

    @Override
    public void onEnable() {

        serverOffices = ServerOffices.getInstance();
        networkService = serverOffices.getNetworkService();

        if (serverOffices.getGroupLoader().loadGroups()) {

            getServer().getPluginManager().registerEvents(new InjectListener(), this);
            getServer().getPluginManager().registerEvents(new RedisListener(), this);

            final PlayerPermission playerPermission = serverOffices.getPlayerPermission();

            final RedisJob redisJob = serverOffices.getRedisJob();
            redisJob.start();

            for (Player player : Bukkit.getOnlinePlayers()) {
                final PlayerPermission.UserData user = playerPermission.getUser(player);
                user.load();
                user.inject();
            }

            final TabService tabService = serverOffices.getTabService();
            tabService.start(this, getServer());
            tabService.execute(new TabService.TabUpdate() {

                @Override
                public void onUpdate(TabService.TabFactory tabFactory) {
                    final Player player = tabFactory.getPlayer();
                    final PlayerPermission.UserData user = playerPermission.getUser(player);
                    final GroupService groupService = user.getLargestGroup();
                    tabFactory.appendPrefix(groupService.getPrefix());
                    tabFactory.appendSuffix(groupService.getSuffix());
                }

            });

        }

    }

    @Override
    public void onDisable() {
        networkService.closePool();
    }
}
