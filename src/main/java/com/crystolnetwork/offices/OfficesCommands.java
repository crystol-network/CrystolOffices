package com.crystolnetwork.offices;

import com.crystolnetwork.offices.entity.PlayerBase;
import com.crystolnetwork.offices.entity.PlayerPermission;
import com.crystolnetwork.offices.interfaces.CommandFunction;
import com.crystolnetwork.offices.manager.Group;
import com.crystolnetwork.offices.services.OfficesServices;
import com.crystolnetwork.offices.services.SingletonService;
import com.crystolnetwork.offices.services.loaders.GroupLoader;
import com.crystolnetwork.offices.utils.CommandCreator;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Iterator;
import java.util.LinkedList;

public class OfficesCommands {

    private final OfficesServices officesServices = SingletonService.getOrFill(OfficesServices.class);
    private final PlayerBase playerBase = officesServices.getPlayerBase();
    private final GroupLoader groupLoader = officesServices.getGroupLoader();
    private final CommandCreator commandCreator = new CommandCreator();

    public void load() {

        final String helpMessage =
                " \n" +
                        "§e           Comandos disponíveis:\n" +
                        " \n" +
                        "§6‣ §egroups ver §b- §epara ver os grupos de uma jogador.\n" +
                        "§6‣ §egroups add §b- §epara adicionar um grupo para um jogador.\n" +
                        "§6‣ §egroups remover §b- §epara remover um grupo de um jogador.\n" +
                        "§6‣ §egroups listar §b- §epara listar os jogadores de um certo grupo.\n" +
                        "§6‣ §egroups tipos §b- §epara listar os grupos existentes.\n" +
                        "§6‣ §egroups ajuda §b- §epara exibir esta mensagem.\n ";

        commandCreator
                .addCommand("office", "offices", "group", "groups", "grupo", "grupos", "cargo", "cargos")
                .on(new CommandFunction() {

                    {
                        allowConsole = true;
                    }

                    @Override
                    public void onExecute(CommandSender executor, String command, String subCommand, String[] arguments) {
                        if (arguments.length < 1) {
                            if (executor.hasPermission("crystolnetwork.admin"))
                                executor.sendMessage(helpMessage);
                            else
                                executor.sendMessage("§cVocê não tem permissão para executar este comando.");
                        }
                    }

                })
                .addSubCommand("show", "ver")
                .on(new CommandFunction() {

                    {
                        allowConsole = true;
                    }

                    @Override
                    public void onExecute(CommandSender executor, String command, String subCommand, String[] arguments) {
                        if (arguments.length > 0) {
                            if (executor.hasPermission("crystolnetwork.admin")) {
                                final OfflinePlayer player = Bukkit.getOfflinePlayer(arguments[0]);
                                if (player != null) {
                                    final PlayerPermission playerPermission = playerBase.getUser(player);
                                    playerPermission.load();
                                    String groupsString = "";
                                    final Iterator<Group> groups = new LinkedList<>(playerPermission.getGroups()).iterator();
                                    while (groups.hasNext())
                                        groupsString += groups.next().getName() + (groups.hasNext() ? ", " : "");
                                    executor.sendMessage("§eGrupos de §f" + player.getName() + "§e: §f" + groupsString);
                                }
                            } else
                                executor.sendMessage("§cVocê não tem permissão para utilizar esta função.");
                        } else
                            executor.sendMessage("§cUso: /" + command + " ver <player>");
                    }

                })
                .addSubCommand("add", "adicionar", "set", "setar")
                .on(new CommandFunction() {

                    {
                        allowConsole = true;
                    }

                    @Override
                    public void onExecute(CommandSender executor, String command, String subCommand, String[] arguments) {
                        if (arguments.length > 1) {
                            if (executor.hasPermission("crystolnetwork.gerente")) {
                                final OfflinePlayer player = Bukkit.getOfflinePlayer(arguments[0]);
                                if (player != null) {
                                    Group group = groupLoader.getGroup(arguments[1]);
                                    if (group != null) {
                                        final PlayerPermission playerPermission = playerBase.getUser(player);
                                        playerPermission.load();

                                        if (arguments.length > 2) {
                                            playerPermission.addGroup(group, arguments[2]);
                                            executor.sendMessage("§eGrupo §f" + group.getName() + " §eadicionado para §f" + player.getName() + "§e no servidor §f" + arguments[2] + "§e.");
                                        } else {
                                            if (!playerPermission.hasGroup(group)) {
                                                playerPermission.addGroup(group);
                                                executor.sendMessage("§eGrupo §f" + group.getName() + " §eadicionado para §f" + player.getName() + "§e.");
                                            } else
                                                executor.sendMessage("§cEste jogador já tem este grupo.");
                                        }
                                    } else
                                        executor.sendMessage("§cO grupo inserido é invalido.");
                                }
                            } else
                                executor.sendMessage("§cVocê não tem permissão para utilizar esta função.");
                        } else
                            executor.sendMessage("§cUso: /" + command + " add <player> <grupo> [server]");
                    }

                })
                .addSubCommand("remove", "remover", "unset", "retirar")
                .on(new CommandFunction() {

                    {
                        allowConsole = true;
                    }

                    @Override
                    public void onExecute(CommandSender executor, String command, String subCommand, String[] arguments) {
                        if (arguments.length > 1) {
                            if (executor.hasPermission("crystolnetwork.gerente")) {
                                final OfflinePlayer player = Bukkit.getOfflinePlayer(arguments[0]);
                                if (player != null) {
                                    Group group = groupLoader.getGroup(arguments[1]);
                                    if (group != null) {
                                        final PlayerPermission playerPermission = playerBase.getUser(player);
                                        playerPermission.load();

                                        if (arguments.length > 2) {
                                            playerPermission.removeGroup(group, arguments[2]);
                                            executor.sendMessage("§eGrupo §f" + group.getName() + " §eremovido de §f" + player.getName() + "§e no servidor §f" + arguments[2] + "§e.");
                                        } else {
                                            if (playerPermission.hasGroup(group)) {
                                                playerPermission.removeGroup(group);
                                                executor.sendMessage("§eGrupo §f" + group.getName() + " §eremovido de §f" + player.getName() + "§e.");
                                            } else
                                                executor.sendMessage("§cEste jogador não tem este grupo.");
                                        }
                                    } else
                                        executor.sendMessage("§cO grupo inserido é invalido.");
                                }
                            } else
                                executor.sendMessage("§cVocê não tem permissão para utilizar esta função.");
                        } else
                            executor.sendMessage("§cUso: /" + command + " remover <player> <grupo> [server]");
                    }

                })
                .addSubCommand("list", "listar")
                .on(new CommandFunction() {

                    {
                        allowConsole = true;
                    }

                    @Override
                    public void onExecute(CommandSender executor, String command, String subCommand, String[] arguments) {
                        if (arguments.length > 1) {
                            if (executor.hasPermission("crystolnetwork.gerente")) {

                            } else
                                executor.sendMessage("§cVocê não tem permissão para utilizar esta função.");
                        } else
                            executor.sendMessage("§cUso: /" + command + " listar <grupo>");
                    }

                })
                .addSubCommand("types", "tipos", "groups", "grupos")
                .on(new CommandFunction() {

                    {
                        allowConsole = true;
                    }

                    @Override
                    public void onExecute(CommandSender executor, String command, String subCommand, String[] arguments) {
                        if (executor.hasPermission("crystolnetwork.admin")) {
                            String helpMessage =
                                    " \n" +
                                            "§e           Grupos disponíveis:\n" +
                                            " \n";
                            for (Group group : groupLoader.getGroups()) {
                                helpMessage += "§6‣ §e" + group.getName() + "\n";
                            }
                            executor.sendMessage(helpMessage);
                        } else
                            executor.sendMessage("§cVocê não tem permissão para utilizar esta função.");
                    }

                })
                .addSubCommand("help", "ajuda", "?")
                .on(new CommandFunction() {

                    {
                        allowConsole = true;
                    }

                    @Override
                    public void onExecute(CommandSender executor, String command, String subCommand, String[] arguments) {
                        if (executor.hasPermission("crystolnetwork.admin")) {
                            executor.sendMessage(helpMessage);
                        } else
                            executor.sendMessage("§cVocê não tem permissão para utilizar esta função.");
                    }

                })

        ;
    }

}
