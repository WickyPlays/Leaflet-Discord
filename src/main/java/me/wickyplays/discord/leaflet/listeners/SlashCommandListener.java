package me.wickyplays.discord.leaflet.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "say" -> {
                String content = event.getOption("content", OptionMapping::getAsString);
                assert content != null;
                event.reply(content).queue();
            }
            case "leave" -> {
                event.reply("I'm leaving the server now!")
                        .setEphemeral(true)
                        .flatMap(m -> event.getGuild().leave())
                        .queue();
            }
        }
    }
}
