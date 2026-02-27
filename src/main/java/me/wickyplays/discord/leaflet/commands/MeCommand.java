package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.enums.CommandCategory;
import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

public class MeCommand extends SlashCommand {

    private static final DateTimeFormatter DTF =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String GITHUB_URL =
            "https://github.com/WickyPlays/Leaflet-Discord";

    private static final long OWNER_ID =
            123456789012345678L;

    public MeCommand() {
        super("me", "Information about this bot");
        this.category = CommandCategory.GENERAL;
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) return;

        User bot = event.getJDA().getSelfUser();
        User owner = event.getJDA().getUserById(OWNER_ID);

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setAuthor(bot.getName(), null, bot.getAvatarUrl())
                .setThumbnail(bot.getAvatarUrl())
                .addField("Bot ID", bot.getId(), false)
                .addField(
                        "Owner",
                        owner != null
                                ? owner.getName() + "#" + owner.getDiscriminator()
                                : "Unknown",
                        false
                )
                .addField(
                        "Created",
                        bot.getTimeCreated().format(DTF),
                        true
                )
                .addField(
                        "Joined this server",
                        guild.getSelfMember().getTimeJoined().format(DTF),
                        true
                )
                .addField(
                        "Source Code",
                        "[GitHub](" + GITHUB_URL + ")",
                        false
                )
                .setFooter("Requested by " + event.getUser().getName(),
                        event.getUser().getAvatarUrl());

        event.replyEmbeds(eb.build()).queue();
    }
}