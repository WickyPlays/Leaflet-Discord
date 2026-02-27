package me.wickyplays.discord.leaflet.interfaces;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class SlashCommand {

    protected final String name;
    protected final String description;

    protected SlashCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract SlashCommandData build();
    public abstract void execute(SlashCommandInteractionEvent event);
}