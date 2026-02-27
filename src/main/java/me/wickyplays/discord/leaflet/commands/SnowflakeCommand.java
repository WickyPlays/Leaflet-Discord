package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.enums.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.utils.TimeUtil;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SnowflakeCommand extends SlashCommand {

    public SnowflakeCommand() {
        super("snowflake", "Convert a Discord Snowflake ID into a date");
        this.category = CommandCategory.UTILITY;
        this.usageExamples = List.of("snowflake 123456789012345678 UTC");
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "id", "Discord snowflake ID", true)
                .addOption(STRING, "timezone", "Timezone (default: UTC)", false);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping idOption = event.getOption("id");
        OptionMapping zoneOption = event.getOption("timezone");

        if (idOption == null) {
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle("Error: Missing ID")
                    .setDescription("You must provide a snowflake ID")
                    .build()
            ).setEphemeral(true).queue();
            return;
        }

        long snowflake;
        try {
            snowflake = Long.parseLong(idOption.getAsString());
        } catch (NumberFormatException e) {
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle("Error: Invalid ID")
                    .setDescription("The provided ID is not a valid snowflake")
                    .build()
            ).setEphemeral(true).queue();
            return;
        }

        String zoneStr = zoneOption != null ? zoneOption.getAsString() : "UTC";
        TimeZone timeZone = TimeZone.getTimeZone(zoneStr);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy z");
        sdf.setTimeZone(timeZone);

        OffsetDateTime odt = TimeUtil.getTimeCreated(snowflake);
        Date date = new Date(odt.toEpochSecond() * 1000);
        String formatted = sdf.format(date);

        event.replyEmbeds(new EmbedBuilder()
                .setColor(Color.CYAN)
                .setAuthor("Snowflake Converter")
                .setDescription("**Date:** " + formatted)
                .setFooter("Zone: " + timeZone.getDisplayName())
                .build()
        ).queue();
    }
}