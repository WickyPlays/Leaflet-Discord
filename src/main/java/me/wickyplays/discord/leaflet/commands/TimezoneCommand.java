package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.enums.CommandCategory;
import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.utils.CoreUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class TimezoneCommand extends SlashCommand {

    public TimezoneCommand() {
        super("timezone", "Check time based on timezone");
        this.category = CommandCategory.UTILITY;
        this.usageExamples = java.util.List.of("time UTC", "time Asia/Tokyo");
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "timezone", "Timezone (e.g. UTC, Asia/Tokyo)", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping option = event.getOption("timezone");
        User user = event.getUser();

        if (option == null || option.getAsString().isEmpty()) {
            event.replyEmbeds(CoreUtil.sendError(
                    "Invalid argument",
                    "Please provide a timezone to check!"
            )).setEphemeral(true).queue();
            return;
        }

        String zoneInput = option.getAsString();
        TimeZone zone = TimeZone.getTimeZone(zoneInput);

        if (zone.getID().equals("GMT") && !zoneInput.equalsIgnoreCase("GMT")) {
            event.replyEmbeds(CoreUtil.sendError(
                    "Invalid timezone",
                    "Please re-check your timezone and try again"
            )).setEphemeral(true).queue();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy zz");
        sdf.setTimeZone(zone);

        event.replyEmbeds(new EmbedBuilder()
                .setColor(Color.CYAN)
                .setAuthor("Timezone: " + zone.getDisplayName())
                .setDescription("**Time:** " + sdf.format(new Date()))
                .setFooter("Requested by: " + user.getName() + "#" + user.getDiscriminator(),
                        user.getAvatarUrl())
                .build()
        ).queue();
    }
}