package me.wickyplays.discord.leaflet.listeners;

import me.wickyplays.discord.leaflet.enums.CommandCategory;
import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.managers.CommandRegistryManager;
import me.wickyplays.discord.leaflet.utils.CoreUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        SlashCommand command = CommandRegistryManager.get(event.getName());
        if (command != null) {
            if (command.isOwnerOnly() || command.getCategory() == CommandCategory.OWNER) {
                Guild guild = event.getGuild();
                if (guild == null) {
                    MessageEmbed errEmbed = CoreUtil.sendError("Unknown guild", "I cannot find a guild to execute this command.");
                    event.getChannel().sendMessageEmbeds(errEmbed).queue();
                    return;
                }
                String serverOwnerId =  guild.getOwnerId();
                if (event.getUser().getId().equals(serverOwnerId)) {
                    command.execute(event);
                } else {
                    MessageEmbed errEmbed = CoreUtil.sendError("Insufficient permission", "Only superior user can execute this command!");
                    event.getChannel().sendMessageEmbeds(errEmbed).queue();
                    return;
                }
                return;
            }
            command.execute(event);
        }
    }
}