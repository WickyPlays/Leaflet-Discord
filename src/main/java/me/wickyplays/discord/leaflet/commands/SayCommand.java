package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.utils.CoreUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SayCommand extends SlashCommand {

    public SayCommand() {
        super("say", "Make the bot say something");
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "content", "Text to send", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping content = event.getOption("content");
        if (content == null) {
            MessageEmbed errorEmbed = CoreUtil.sendError("Empty content", "Content cannot be empty!");
            event.replyEmbeds(errorEmbed).queue();
            return;
        }
        event.reply(content.getAsString()).queue();
    }
}