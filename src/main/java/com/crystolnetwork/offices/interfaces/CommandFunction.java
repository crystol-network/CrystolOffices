package com.crystolnetwork.offices.interfaces;

import org.bukkit.command.CommandSender;

public abstract class CommandFunction {

    public boolean allowConsole = false;

    public abstract void onExecute(CommandSender executor, String command, String subCommand, String[] arguments);

}
