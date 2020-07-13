package com.walkgs.crystolnetwork.offices;

import com.walkgs.crystolnetwork.offices.api.ServerOffices;
import com.walkgs.crystolnetwork.offices.job.RedisJob;
import com.walkgs.crystolnetwork.offices.job.RedisSender;
import com.walkgs.crystolnetwork.offices.listeners.InjectListener;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
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

            for (Player player : Bukkit.getOnlinePlayers()) {
                serverOffices.loadUser(player);
                serverOffices.injectInUser(player);
            }

            final RedisJob redisJob = serverOffices.getRedisJob();
            redisJob.start();

            final TabService tabService = serverOffices.getTabService();
            tabService.start(this, getServer());
            tabService.execute(new TabService.TabUpdate() {

                @Override
                public void onUpdate(TabService.TabFactory tabFactory) {
                    final Player player = tabFactory.getPlayer();
                    final GroupPermission groupPermission = serverOffices.getUser(player).getLargestGroup();
                    tabFactory.appendPrefix(groupPermission.getPrefix());
                    tabFactory.appendSuffix(groupPermission.getSuffix());

                    RedisSender redisSender = new RedisSender(networkService);
                    redisSender.add("FILHO DA PUTA ARROMBADO");
                    redisSender.add("TESTE DOIS OTARIO");
                    redisSender.send(serverOffices.getServerName());

                }

            });

        }

    }

    @Override
    public void onDisable() {
        networkService.closePool();
    }
}
