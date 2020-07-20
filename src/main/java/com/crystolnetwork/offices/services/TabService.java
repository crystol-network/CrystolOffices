package com.crystolnetwork.offices.services;

import com.crystolnetwork.offices.annotations.Singleton;
import com.crystolnetwork.offices.entity.PlayerBase;
import com.crystolnetwork.offices.entity.PlayerPermission;
import com.crystolnetwork.offices.factory.TabFactory;
import com.crystolnetwork.offices.interfaces.TabUpdate;
import com.crystolnetwork.offices.manager.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.LinkedList;
import java.util.List;

@Singleton
public class TabService {

    private final OfficesServices officesServices = SingletonService.getOrFill(OfficesServices.class);

    private final char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private final List<TabUpdate> updateList = new LinkedList<>();

    public void execute(TabUpdate updater) {
        updateList.add(updater);
    }

    public void start(Plugin plugin, Server server) {
        final PlayerBase playerBase = officesServices.getPlayerBase();
        server.getScheduler().runTaskTimer(plugin, () -> {
            for (final Player player : Bukkit.getOnlinePlayers()) {

                Scoreboard scoreboard = player.getScoreboard();
                if (scoreboard == null)
                    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

                for (final Player tabPlayer : Bukkit.getOnlinePlayers()) {

                    final PlayerPermission user = playerBase.getUser(tabPlayer);

                    //MAKE TAB FACTORY

                    final TabFactory tabFactory = new TabFactory(player);
                    for (TabUpdate tabUpdate : updateList)
                        tabUpdate.onUpdate(tabFactory);
                    //GET TAB DATA
                    final String uuidString = tabPlayer.getUniqueId().toString().replace("-", "").substring(0, 15);
                    final Group groupService = user.getLargestGroup();
                    final String teamName = chars[groupService.getRank()] + uuidString;
                    Team team = scoreboard.getTeam(teamName);
                    if (team == null)
                        team = scoreboard.registerNewTeam(teamName);
                    //BUILD THE PREFIX AND SUFFIX
                    String prefix = String.join("", tabFactory.getPrefix().values());
                    String suffix = String.join("", tabFactory.getSuffix().values());
                    if (prefix.length() > 16)
                        prefix = prefix.substring(0, 15);
                    if (suffix.length() > 16)
                        suffix = suffix.substring(0, 15);
                    //SET PREFIX AND SUFFIX
                    team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
                    team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));

                    team.addPlayer(tabPlayer);
                }

            }
        }, 0, 20L);

    }

}
