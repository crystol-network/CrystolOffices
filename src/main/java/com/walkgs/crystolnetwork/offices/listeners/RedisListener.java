package com.walkgs.crystolnetwork.offices.listeners;

import com.google.gson.Gson;
import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.events.RedisReceiveMessageEvent;
import com.walkgs.crystolnetwork.offices.manager.UserManager;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
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
                    final List<String> groups = gson.fromJson(messages.get(4), List.class);
                    if (groups.size() > 0) {

                        final boolean normal = updateType.equals("normal");

                        OfflinePlayer offlinePlayer = serverOffices.getPlugin().getServer().getPlayer(uuid);

                        final String keySet = "settedOffice";
                        if (!userManager.hasData(keySet))
                            userManager.setData(keySet, new LinkedList<GroupPermission>());

                        final GroupLoader groupLoader = serverOffices.getGroupLoader();
                        for (String groupName : groups) {

                            final GroupPermission group = groupLoader.getGroup(groupName);
                            if (!userManager.hasGroup(groupName)) {
                                userManager.addGroup(group);
                            }
                            if (normal) {
                                if (offlinePlayer != null) {
                                    if (offlinePlayer.isOnline()) {

                                        final Player player = offlinePlayer.getPlayer();
                                        final String charAt = "" + groupName.charAt(0);

                                        jsonBuilder.append("§e * §3Foi adicionado o grupo §7" + groupName.replaceFirst(charAt, charAt.toUpperCase()) + "§3.");
                                        jsonBuilder.append("\n§e * §3Clique ");
                                        jsonBuilder.append("§6[AQUI]",
                                                "§eComemorar :)" +
                                                        "\n§7- Irá ser enviado um título para todos que estiver online."
                                                , HoverEvent.Action.SHOW_TEXT);
                                        jsonBuilder.append(" §3para comemorar.");

                                        player.sendMessage("");
                                        player.spigot().sendMessage(jsonBuilder.build());
                                        player.sendMessage("");

                                    }
                                    
                                    ((List<GroupPermission>) userManager.getData(keySet)).add(group);

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
                            final GroupPermission group = groupLoader.getGroup(groupName);
                            userManager.removeGroup(group);
                        }

                    }
                }
            }
        }
    }

}
