package me.wickyplays.discord.leaflet.managers;

import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandRegistryManager {
    private static final Map<String, SlashCommand> commands = new LinkedHashMap<>();

    public static void register(SlashCommand command) {
        commands.put(command.getName(), command);
    }

    public static Collection<SlashCommand> getCommands() {
        return commands.values();
    }

    public static SlashCommand get(String name) {
        return commands.get(name);
    }

    public static void upsert(JDA jda) {
        CommandListUpdateAction action = jda.updateCommands();
        for (SlashCommand command : commands.values()) {
            action.addCommands(command.build());
        }

        System.out.println("Total commands registered: " + commands.size());
        action.queue();
    }
}
