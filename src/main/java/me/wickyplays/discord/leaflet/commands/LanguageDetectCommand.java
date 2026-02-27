package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.utils.CoreUtil;
import me.wickyplays.discord.leaflet.utils.LanguageUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class LanguageDetectCommand extends SlashCommand {

    public LanguageDetectCommand() {
        super("language", "Detect what language a sentence is");
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "sentence", "Text to send", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping content = event.getOption("sentence");
        if (content == null) {
            MessageEmbed errorEmbed = CoreUtil.sendError("Empty content", "Content cannot be empty!");
            event.replyEmbeds(errorEmbed).queue();
            return;
        }
        String language = LanguageUtil.getLanguage(content.getAsString());
        event.reply("Language: " + language).queue();
    }
}