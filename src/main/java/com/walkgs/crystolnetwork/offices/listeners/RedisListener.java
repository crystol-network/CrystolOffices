package com.walkgs.crystolnetwork.offices.listeners;

import com.google.gson.Gson;
import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.events.RedisReceiveMessageEvent;
import com.walkgs.crystolnetwork.offices.manager.UserManager;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.GroupService;
import com.walkgs.crystolnetwork.offices.utils.JsonBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class RedisListener implements Listener {

    private final Gson gson = new Gson();
    private final ServerOffices serverOffices = ServerOffices.getInstance();

    @EventHandler
    public void onMessage(final RedisReceiveMessageEvent event) {

        final JsonBuilder jsonBuilder = new JsonBuilder();

        final List<String> messages = new LinkedList<>(event.getReceivedData().values());
        if (messages.size() > 0) {
            final String functionType = messages.get(0);
            if (functionType.equals("updateOffice")) {

                final UUID uuid = UUID.fromString(messages.get(1));
                final String eventType = messages.get(2);
                final UserManager userManager = serverOffices.getUserLoader().get(uuid);

                //addGroups function
                if (eventType.equals("addGroups")) {
                    final String updateType = messages.get(3);
                    final List<Double> groupsRanks = gson.fromJson(messages.get(4), List.class);
                    if (groupsRanks.size() > 0) {

                        final boolean normal = updateType.equals("normal");

                        OfflinePlayer offlinePlayer = serverOffices.getPlugin().getServer().getPlayer(uuid);

                        final String keySet = "settedOffice";
                        if (!userManager.hasData(keySet))
                            userManager.setData(keySet, new LinkedList<GroupService>());

                        final GroupLoader groupLoader = serverOffices.getGroupLoader();
                        for (Double rank : groupsRanks) {
                            final int groupRank = rank.intValue();

                            final GroupService group = groupLoader.getGroup(groupRank);
                            if (group != null && !userManager.hasGroup(group.getRank())) {
                                userManager.addGroup(group);
                            }

                            if (normal) {
                                if (offlinePlayer != null) {
                                    if (offlinePlayer.isOnline()) {

                                        final String groupName = group.getName();
                                        final Player player = offlinePlayer.getPlayer();
                                        final String charAt = "" + groupName.charAt(0);

                                        jsonBuilder.append("§6 * §eFoi adicionado o grupo §6" + groupName.replaceFirst(charAt, charAt.toUpperCase()) + "§e em sua conta.");
                                        jsonBuilder.append("\n§6 * §eClique ");
                                        jsonBuilder.append("§6[AQUI]",
                                                "§eComemorar :)" +
                                                        "\n§7- Irá ser enviado um título para todos que estiver online."
                                                , HoverEvent.Action.SHOW_TEXT);
                                        jsonBuilder.append(" §epara comemorar.");

                                        player.sendMessage("");
                                        player.spigot().sendMessage(jsonBuilder.build());
                                        player.sendMessage("");

                                    }

                                    //((List<GroupService>) userManager.getData(keySet)).add(group);

                                }
                            }

                        }

                    }
                }
                //removeGroups function
                else if (eventType.equals("removeGroups")) {
                    List<String> groups = gson.fromJson(messages.get(3), List.class);
                    if (groups.size() > 0) {

                        final GroupLoader groupLoader = serverOffices.getGroupLoader();
                        for (String groupName : groups) {
                            final GroupService group = groupLoader.getGroup(groupName);
                            userManager.removeGroup(group);
                        }

                    }
                }
            }
        }
    }

    public void a() {


    }


}
