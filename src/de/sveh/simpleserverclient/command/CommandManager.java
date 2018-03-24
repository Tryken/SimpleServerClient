package de.sveh.simpleserverclient.command;

import java.util.HashMap;

import de.sveh.simpleserverclient.sender.Sender;
import de.sveh.simpleserverclient.sender.SenderType;
import de.sveh.simpleserverclient.server.logger.LogType;
import de.sveh.simpleserverclient.server.logger.ILogger;

public class CommandManager {

    private String prefix = "/";

    private HashMap<String, ICommandHandler> commandHandlers;

    public CommandManager() {
        commandHandlers = new HashMap<>();
    }

    public void registerCommandHandler(ICommandHandler commandHandler, String... labels) {
        commandHandler.onInit();
        for (String label : labels)
            this.commandHandlers.put(label, commandHandler);
    }

    public boolean callCommand(Sender sender, String message) {
        if (!message.startsWith(getPrefix()))
            return false;

        String[] split = message.split(" ");
        String label = split[0].replace(prefix, "");
        String[] args = new String[split.length - 1];

        System.arraycopy(split, 1, args, 0, split.length - 1);

        ICommandHandler commandHandler = commandHandlers.get(label);
        if (commandHandler == null) {

            if (sender.getType() == SenderType.SERVER)
                ILogger.log(LogType.INFO, "Unknown command!");
            else
                sender.sendMessage("Unknown command!");
            return true;
        }

        if (sender.getType() == SenderType.CLIENT)
            ILogger.log(LogType.INFO, sender.getUserName() + " use " + message);

        new Thread(() -> {
            CommandResponse response = commandHandler.onCommand(sender, label, args);

            switch (response) {
                case NO_PERMISSIONS:
                    if (sender.getType() == SenderType.SERVER)
                        ILogger.log(LogType.INFO, "This command is not available for the console!");
                    else
                        sender.sendMessage("You have no authority to use this command!");
                    break;
                case SYNTAX_ERROR:
                    if (sender.getType() == SenderType.SERVER)
                        ILogger.log(LogType.INFO, "Syntaxerror use " + getPrefix() + "help " + label);
                    else
                        sender.sendMessage("Syntaxerror use " + getPrefix() + "help " + label);
                    break;
            }
        }).start();

        return true;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
