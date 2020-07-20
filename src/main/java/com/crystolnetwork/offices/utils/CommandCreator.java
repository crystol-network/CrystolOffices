package com.crystolnetwork.offices.utils;

import com.crystolnetwork.offices.interfaces.CommandFunction;
import com.crystolnetwork.offices.services.CommandService;
import com.crystolnetwork.offices.services.SingletonService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandCreator {

    private CommandService commandService = SingletonService.getOrFill(CommandService.class);
    private List<String> aliases = new LinkedList<>();

    public FunctionRegister addCommand(String... aliases){
        this.aliases = Arrays.asList(aliases);
        return new FunctionRegister(this, this.aliases, null);
    }

    public FunctionRegister addSubCommand(String... aliases){
        return new FunctionRegister(this, this.aliases, Arrays.asList(aliases));
    }

    public class FunctionRegister {

        private final CommandCreator commandFactory;
        private final List<String> aliases;
        private final List<String> subAliases;

        public FunctionRegister(CommandCreator commandFactory, List<String> aliases, List<String> subAliases){
            this.commandFactory = commandFactory;
            this.aliases = aliases;
            this.subAliases = subAliases;
        }

        public CommandCreator on(CommandFunction function){
            commandService.register(aliases, subAliases, function);
            return commandFactory;
        }

    }

}
