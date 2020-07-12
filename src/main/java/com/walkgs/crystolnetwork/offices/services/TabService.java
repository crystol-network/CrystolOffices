package com.walkgs.crystolnetwork.offices.services;

import com.walkgs.crystolnetwork.offices.OfficesPlugin;
import com.walkgs.crystolnetwork.offices.api.ServerOffices;
import com.walkgs.crystolnetwork.offices.utils.CachedCycle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TabService {

    //TODO: CYCLE OF USER PERMISSIONS
    private static final CachedCycle.ICycle<TabService> cycle = new CachedCycle(OfficesPlugin.getPlugin(OfficesPlugin.class)).getOrCreate("TabService");

    public static TabService getInstance() {
        return cycle.getOrComputer(TabService::new);
    }

    public static CachedCycle.ICycle<TabService> getCycle() {
        return cycle;
    }

    //TODO: FUNCTIONS CLASS

    private final ServerOffices serverOffices = ServerOffices.getInstance();

    private final char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private final List<TabUpdate> updateList = new LinkedList<>();

    public abstract static class TabUpdate {
        public void onUpdate(TabFactory tabFactory) {
        }
    }

    public class TabFactory {

        private final Player player;

        private int rank = -1;

        private final Map<Integer, String> prefix = new LinkedHashMap<>();
        private final Map<Integer, String> suffix = new LinkedHashMap<>();

        private int lastPrefixIndex = -1;
        private int lastSuffixIndex = -1;

        public TabFactory(final Player player) {
            this.player = player;
        }

        public void appendPrefix(String prefix) {
            appendPrefix(getLastPrefixIndex() + 1, prefix);
        }

        public void appendPrefix(int index, String prefix) {
            this.prefix.put(index, prefix);
            lastPrefixIndex++;
        }

        public void appendSuffix(String suffix) {
            appendSuffix(getLastPrefixIndex() + 1, suffix);
        }

        public void appendSuffix(int index, String suffix) {
            this.prefix.put(index, suffix);
            lastPrefixIndex++;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getRank() {
            return rank;
        }

        public Map<Integer, String> getPrefix() {
            return prefix;
        }

        public Map<Integer, String> getSuffix() {
            return suffix;
        }

        public int getLastPrefixIndex() {
            return lastPrefixIndex;
        }

        public int getLastSuffixIndex() {
            return lastSuffixIndex;
        }

        public Player getPlayer() {
            return player;
        }

        public void reset() {
            prefix.clear();
            suffix.clear();
            lastPrefixIndex = -1;
            lastSuffixIndex = -1;
        }

    }

    public void execute(TabUpdate updater) {
        updateList.add(updater);
    }

    public void start(Plugin plugin, Server server) {

        server.getScheduler().runTaskTimer(plugin, () -> {
            for (final Player player : Bukkit.getOnlinePlayers()) {

                Scoreboard scoreboard = player.getScoreboard();
                if (scoreboard == null)
                    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

                for (final Player tabPlayer : Bukkit.getOnlinePlayers()) {

                    //TODO: MAKE TAB FACTORY
                    final TabFactory tabFactory = new TabFactory(player);
                    for (TabUpdate tabUpdate : updateList)
                        tabUpdate.onUpdate(tabFactory);
                    //TODO: GET TAB DATA
                    final String uuidString = tabPlayer.getUniqueId().toString().replace("-", "").substring(0, 15);
                    final GroupPermission groupPermission = serverOffices.getUser(tabPlayer).getLargestGroup();
                    final String teamName = chars[groupPermission.getRank()] + uuidString;
                    Team team = scoreboard.getTeam(teamName);
                    if (team == null)
                        team = scoreboard.registerNewTeam(teamName);
                    //TODO: BUILD THE PREFIX AND SUFFIX
                    String prefix = String.join("", tabFactory.prefix.values());
                    String suffix = String.join("", tabFactory.suffix.values());
                    if (prefix.length() > 16)
                        prefix = prefix.substring(0, 15);
                    if (suffix.length() > 16)
                        suffix = suffix.substring(0, 15);
                    //TODO: SET PREFIX AND SUFFIX
                    team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
                    team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));

                    team.addPlayer(tabPlayer);

                }

            }
        }, 0, 20L);

    }

}
