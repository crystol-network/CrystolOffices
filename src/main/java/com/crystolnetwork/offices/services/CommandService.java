package com.crystolnetwork.offices.services;

import com.crystolnetwork.offices.annotations.Singleton;
import com.crystolnetwork.offices.interfaces.CommandFunction;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class CommandService implements Listener {

    private final OfficesServices officesServices = SingletonService.getOrFill(OfficesServices.class);
    private final Map<String, Map<String, CommandFunction>> functions = new LinkedHashMap<>();

    public CommandService() {
        final Plugin plugin = officesServices.getPlugin();
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(this, plugin);
    }

    public void register(List<String> aliases, List<String> subAliases, CommandFunction function) {
        for (String aliase : aliases) {
            String toLowerCase = aliase.toLowerCase();
            if (!functions.containsKey(toLowerCase))
                functions.put(toLowerCase, new LinkedHashMap<>());
            final Map<String, CommandFunction> functionMap = functions.get(toLowerCase);
            if (subAliases == null) {
                if (!functionMap.containsKey(""))
                    functionMap.put("", function);
            } else {
                for (String subAliase : subAliases) {
                    if (!functionMap.containsKey(subAliase)) {
                        functionMap.put(subAliase, function);
                    }
                }
            }
        }
    }

    private boolean tryRun(CommandSender sender, boolean isConsole, String command, String arguments) {
        final String[] messages = arguments.split(" ");
        if (messages.length > 0) {
            if (functions.containsKey(command)) {
                final Map<String, CommandFunction> functionMap = functions.get(command);
                final CommandFunction commandFunction = functionMap.get("");
                boolean sucess = false;
                if (commandFunction != null) {
                    if (isConsole && !commandFunction.allowConsole)
                        return false;
                    commandFunction.onExecute(sender, command, "", Arrays.copyOfRange(messages, 1, messages.length));
                    sucess = true;
                }
                if (messages.length >= 2) {
                    String subCommand = messages[1].toLowerCase();
                    final CommandFunction subCommandFunction = functionMap.get(subCommand);
                    if (subCommandFunction != null) {
                        if (isConsole && !subCommandFunction.allowConsole)
                            return false;
                        subCommandFunction.onExecute(sender, command, subCommand, Arrays.copyOfRange(messages, 2, messages.length));
                        sucess = true;
                    }
                }
                return sucess;
            }
        }
        return false;
    }

    @EventHandler
    public void onServerSenderCommand(ServerCommandEvent event) {
        String message = event.getCommand().split(" ")[0];
        if (tryRun(event.getSender(), true, message.toLowerCase(), event.getCommand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSenderCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().split(" ")[0].substring(1);
        if (tryRun(event.getPlayer(), false, message.toLowerCase(), event.getMessage())) {
            event.setCancelled(true);
        }
    }

}
