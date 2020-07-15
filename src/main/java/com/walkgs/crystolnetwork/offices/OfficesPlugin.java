package com.walkgs.crystolnetwork.offices;

import com.walkgs.crystolnetwork.offices.api.PlayerPermission;
import com.walkgs.crystolnetwork.offices.api.services.OfficesServices;
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

    private OfficesServices officesServices;
    private NetworkService networkService;

    @Override
    public void onEnable() {

        officesServices = OfficesServices.getInstance();
        networkService = officesServices.getNetworkService();

        if (officesServices.getGroupLoader().loadGroups()) {

            getServer().getPluginManager().registerEvents(new InjectListener(), this);
            getServer().getPluginManager().registerEvents(new RedisListener(), this);

            final PlayerPermission playerPermission = officesServices.getPlayerPermission();

            final RedisJob redisJob = officesServices.getRedisJob();
            redisJob.start();

            for (Player player : Bukkit.getOnlinePlayers()) {
                final PlayerPermission.UserData user = playerPermission.getUser(player);
                user.load();
                user.inject();
            }

            final TabService tabService = officesServices.getTabService();
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
