package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.enums.CommandCategory;
import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.utils.CoreUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class TimeConverterCommand extends SlashCommand {

    public TimeConverterCommand() {
        super("tconvert", "Convert time between timezones");
        this.category = CommandCategory.UTILITY;
        this.usageExamples = java.util.List.of("tconvert 13:00 GMT+1 GMT+7");
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "time", "Time in hh:mm or hh:mm:ss", true)
                .addOption(STRING, "from", "Source timezone", true)
                .addOption(STRING, "to", "Target timezone", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String timeArg = event.getOption("time").getAsString();
        String fromArg = event.getOption("from").getAsString();
        String toArg = event.getOption("to").getAsString();

        String[] timeParts = timeArg.split(":");
        if (timeParts.length < 2) {
            event.replyEmbeds(CoreUtil.sendError(
                    "Invalid time format!",
                    "Time must be in **hh:mm** or **hh:mm:ss** format"
            )).setEphemeral(true).queue();
            return;
        }

        int hh;
        int mm;
        int ss;

        try {
            hh = Integer.parseInt(timeParts[0]);
            mm = Integer.parseInt(timeParts[1]);
            ss = timeParts.length >= 3 ? Integer.parseInt(timeParts[2]) : 0;
            if (hh >= 24 || mm >= 60 || ss >= 60) {
                event.replyEmbeds(CoreUtil.sendError(
                        "Invalid time format",
                        "Hours must be < 24, minutes < 60, seconds < 60"
                )).setEphemeral(true).queue();
                return;
            }
        } catch (NumberFormatException e) {
            event.replyEmbeds(CoreUtil.sendError(
                    "Invalid time format!",
                    "Time must only contain numbers"
            )).setEphemeral(true).queue();
            return;
        }

        TimeZone zoneFrom = resolveZone(fromArg);
        TimeZone zoneTo = resolveZone(toArg);

        if (zoneFrom == null || zoneTo == null) {
            String which =
                    zoneFrom == null && zoneTo == null ? "both **from** and **to**"
                            : zoneFrom == null ? "**from**"
                            : "**to**";

            event.replyEmbeds(CoreUtil.sendError(
                    "Invalid time zone",
                    "Please re-check your " + which + " timezone"
            )).setEphemeral(true).queue();
            return;
        }

        Calendar cal = Calendar.getInstance(zoneFrom);
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, hh);
        cal.set(Calendar.MINUTE, mm);
        cal.set(Calendar.SECOND, ss);

        Date sourceDate = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy zz");
        sdf.setTimeZone(zoneTo);
        String formatted = sdf.format(sourceDate);

        Calendar cal2 = Calendar.getInstance();
        Date parsed;
        try {
            parsed = sdf.parse(formatted);
            cal2.setTime(parsed);
        } catch (ParseException e) {
            event.replyEmbeds(CoreUtil.sendError(
                    "Conversion error",
                    "Failed to convert the provided time"
            )).setEphemeral(true).queue();
            return;
        }

        String addition1 = "";
        if (cal2.get(Calendar.HOUR_OF_DAY) > 12) {
            addition1 = "(**" + cal2.get(Calendar.HOUR) + ":" + cal2.get(Calendar.MINUTE) + "**pm)";
        }

        String addition2 = "";
        if (zoneTo.inDaylightTime(parsed)) {
            addition2 = "**NOTE**: Resulted timezone is in **DST** (Daylight Saving Time)";
        }

        event.replyEmbeds(new EmbedBuilder()
                .setColor(Color.CYAN)
                .setAuthor("Time converter")
                .setDescription("Your converted time: **" + formatted + "** " + addition1 +
                        (!addition2.isEmpty() ? "\n\n" + addition2 : ""))
                .setFooter("Conversion: " + zoneFrom.getDisplayName() + " -> " + zoneTo.getID())
                .build()
        ).queue();
    }

    private TimeZone resolveZone(String input) {
        if (input.startsWith("UTC") || input.startsWith("GMT") ||
                (input.equals(input.toUpperCase()) && input.length() <= 4)) {
            return TimeZone.getTimeZone(input);
        }

        String match = Arrays.stream(TimeZone.getAvailableIDs())
                .filter(id -> id.endsWith(input))
                .findFirst()
                .orElse(null);

        return match == null ? null : TimeZone.getTimeZone(match);
    }
}