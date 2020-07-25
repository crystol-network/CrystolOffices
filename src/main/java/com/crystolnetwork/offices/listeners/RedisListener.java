package com.crystolnetwork.offices.listeners;

import com.crystolnetwork.offices.events.RedisReceiveMessageEvent;
import com.crystolnetwork.offices.interfaces.ClickAction;
import com.crystolnetwork.offices.manager.Group;
import com.crystolnetwork.offices.manager.UserGroup;
import com.crystolnetwork.offices.services.OfficesServices;
import com.crystolnetwork.offices.services.SingletonService;
import com.crystolnetwork.offices.services.loaders.GroupLoader;
import com.crystolnetwork.offices.utils.JsonMessage;
import com.crystolnetwork.offices.utils.TitleBar;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class RedisListener implements Listener {

    private final Gson gson = new Gson();
    private final OfficesServices officesServices = SingletonService.getOrFill(OfficesServices.class);
    private final ConsoleCommandSender consoleSender = officesServices.getPlugin().getServer().getConsoleSender();

    @EventHandler
    public void onMessage(final RedisReceiveMessageEvent event) {

        final List<String> messages = new LinkedList<>(event.getReceivedData().values());
        if (messages.size() > 0) {
            final String functionType = messages.get(0);
            if (functionType.equals("updateOffice")) {

                final UUID uuid = UUID.fromString(messages.get(1));
                final String eventType = messages.get(2);
                officesServices.getPlayerBase().getUser(uuid).load();
                final UserGroup userData = officesServices.getUserLoader().get(uuid);

                //addGroups function
                boolean updated = false;
                if (eventType.equals("addGroups")) {
                    final String updateType = messages.get(3);
                    final List<Double> groupsRanks = gson.fromJson(messages.get(4), List.class);
                    if (groupsRanks.size() > 0) {

                        final boolean normal = updateType.equals("normal");

                        OfflinePlayer offlinePlayer = officesServices.getPlugin().getServer().getPlayer(uuid);

                        final String keySet = "settedOffice";
                        if (!userData.hasData(keySet))
                            userData.setData(keySet, new LinkedList<Group>());

                        final GroupLoader groupLoader = officesServices.getGroupLoader();
                        for (Double rank : groupsRanks) {
                            final int groupRank = rank.intValue();
                            final Group group = groupLoader.getGroup(groupRank);
                            if (group != null) {
                                if (!userData.hasGroup(group.getRank())) {
                                    updated = true;
                                    userData.addGroup(group);
                                    if (normal) {
                                        if (offlinePlayer != null) {
                                            ((List<Group>) userData.getData(keySet)).add(group);
                                            if (offlinePlayer.isOnline()) {

                                                final String groupName = group.getName();
                                                final Player player = offlinePlayer.getPlayer();
                                                final String charAt = "" + groupName.charAt(0);

                                                final JsonMessage jsonMessage = new JsonMessage(player)
                                                        .append("§6 * §eFoi adicionado o grupo §6" + groupName.replaceFirst(charAt, charAt.toUpperCase()) + "§e em sua conta.").newLine()
                                                        .append("§6 * §eClique ").append("§6[AQUI]")
                                                        .event("§eComemorar ☺\n" +
                                                                "§7‣ Irá ser enviado um título para todos que estiver online.\n" +
                                                                "§c( Obs: Você só poderá utilizar esta função uma vez. )", HoverEvent.Action.SHOW_TEXT)
                                                        .event(new ClickAction() {

                                                            @Override
                                                            public void run() {
                                                                player.sendMessage(
                                                                        Strings.repeat("\n ", 100) +
                                                                        "\n§aVocê comemorou com todos os jogadores."
                                                                );
                                                                ((List<Group>) userData.getData(keySet)).remove(group);
                                                                new TitleBar()
                                                                        .setTitle("§eUhuuu!! ☺")
                                                                        .setSubtitle("§6‣ §f" + player.getName() + "§e adquiriu o grupo §f[" + groupName.replaceFirst(charAt, charAt.toUpperCase()) + "]")
                                                                        .setOptions(3, 5, 10)
                                                                        .build()
                                                                .sendForAll();
                                                                expire = true;
                                                            }
                                                        })
                                                        .append(" §epara comemorar.");
                                                player.sendMessage("");
                                                jsonMessage.send();
                                                player.sendMessage("");

                                            }
                                        }
                                    }
                                } else {
                                    consoleSender.sendMessage("§cPlayer " + offlinePlayer.getName() + " already has the " + group.getName() + " group.");
                                }
                            } else {
                                consoleSender.sendMessage("§cGroup rank '" + groupRank + "' sended of " + event.getServerName() + " not found.");
                            }

                        }

                    }
                }
                //removeGroups function
                else if (eventType.equals("removeGroups")) {
                    List<String> groups = gson.fromJson(messages.get(3), List.class);
                    if (groups.size() > 0) {

                        final GroupLoader groupLoader = officesServices.getGroupLoader();
                        for (String groupName : groups) {
                            final Group group = groupLoader.getGroup(groupName);
                            userData.removeGroup(group);
                        }

                    }
                }
                if (updated){
                    try {
                        officesServices.getPlayerBase().getUser(uuid).pushGroups();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
