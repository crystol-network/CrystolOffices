package com.walkgs.crystolnetwork.offices.listeners;

import com.google.gson.Gson;
import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.events.RedisReceiveMessageEvent;
import com.walkgs.crystolnetwork.offices.manager.UserManager;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
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

                        final GroupLoader groupLoader = serverOffices.getGroupLoader();
                        for (String groupName : groups) {
                            final GroupPermission group = groupLoader.getGroup(groupName);
                            if (!userManager.hasGroup(groupName))
                                userManager.addGroup(group);
                        }

                        if (updateType.equals("normal")) {
                            Player player = serverOffices.getPlugin().getServer().getPlayer(uuid);
                            if (player != null) {
                                player.sendMessage("FOI ADICIONADO UM GRUPO");
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
