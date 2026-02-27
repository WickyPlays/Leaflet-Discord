package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.managers.CommandRegistryManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.ActionComponent;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

import java.awt.*;
import java.util.List;

public class HelpCommand extends SlashCommand {

    private static final int PER_PAGE = 5;

    public HelpCommand() {
        super("help", "Show all available commands");
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<SlashCommand> commands = List.copyOf(CommandRegistryManager.getCommands());

        event.replyEmbeds(buildEmbed(commands, 0))
                .addComponents(
                        ActionRow.of(Button.primary("help_prev:0", "◀"), Button.primary("help_next:0", "▶"))
                ).queue();
    }

    public static MessageEmbed buildEmbed(List<SlashCommand> commands, int page) {
        int maxPage = (commands.size() + PER_PAGE - 1) / PER_PAGE;
        int start = page * PER_PAGE;
        int end = Math.min(start + PER_PAGE, commands.size());

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Leaflet Command Helper")
                .setColor(Color.GREEN);

        for (int i = start; i < end; i++) {
            SlashCommand cmd = commands.get(i);
            eb.addField("/" + cmd.getName(), cmd.getDescription(), false);
        }

        eb.setFooter("Page " + (page + 1) + " / " + maxPage);
        return eb.build();
    }
}