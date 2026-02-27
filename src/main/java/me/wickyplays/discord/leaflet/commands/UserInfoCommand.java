package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.enums.CommandCategory;
import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static net.dv8tion.jda.api.interactions.commands.OptionType.USER;

public class UserInfoCommand extends SlashCommand {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public UserInfoCommand() {
        super("userinfo", "Check member information", true);
        this.category = CommandCategory.UTILITY;
        this.hidden = true;
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "tag", "a = avatar, c = client, p = position", false)
                .addOption(USER, "user", "Target user (defaults to yourself)", false);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping tagOpt = event.getOption("tag");
        OptionMapping userOpt = event.getOption("user");

        Guild guild = event.getGuild();
        Member requester = event.getMember();
        if (guild == null || requester == null) return;

        Member target = userOpt != null
                ? guild.getMember(userOpt.getAsUser())
                : requester;

        if (target == null) {
            event.reply("This user is not found in this guild!")
                    .setEphemeral(true).queue();
            return;
        }

        User author = event.getUser();
        String tag = tagOpt != null ? tagOpt.getAsString().toLowerCase() : "";

        switch (tag) {
            case "a" -> event.replyEmbeds(getUserAvatar(target.getUser(), author).build()).queue();
            case "c" -> event.replyEmbeds(getUserClient(target, author).build()).queue();
            case "p" -> event.replyEmbeds(getUserPosition(guild, target, author).build()).queue();
            default -> event.replyEmbeds(getUserGeneral(target, author).build()).queue();
        }
    }

    private EmbedBuilder getUserGeneral(Member member, User request) {
        User user = member.getUser();

        String status = switch (member.getOnlineStatus()) {
            case DO_NOT_DISTURB -> "Do not disturb";
            case IDLE -> "Idle";
            case ONLINE -> "Online";
            case OFFLINE -> "Offline";
            case INVISIBLE -> "Invisible";
            default -> "Unknown";
        };

        String alias = member.getNickname() != null
                ? member.getNickname()
                : user.getName() + " (No nickname)";

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl())
                .setTitle("Member info")
                .addField("Alias:", alias, false)
                .addField("Creation date:", user.getTimeCreated().format(dtf), false)
                .addField("Join date:", member.getTimeJoined().format(dtf), true)
                .setDescription("**Status:** " + status);
    }

    private EmbedBuilder getUserAvatar(User user, User request) {
        String url = user.getAvatarUrl();
        if (url == null) url = user.getDefaultAvatarUrl();

        String base = url.substring(0, url.lastIndexOf('.'));
        String jpeg = base + ".jpeg?size=1024";
        String webp = base + ".webp?size=1024";

        return new EmbedBuilder()
                .setColor(Color.CYAN)
                .setAuthor("Avatar for " + user.getName() + "#" + user.getDiscriminator())
                .setDescription(
                        "Obtain via: [PNG](" + url + "?size=1024) | " +
                                "[JPEG](" + jpeg + ") | " +
                                "[WEBP](" + webp + ")"
                )
                .setImage(url + "?size=1024")
                .setFooter(
                        "Requested by " + request.getName() + "#" + request.getDiscriminator(),
                        request.getAvatarUrl()
                );
    }

    private EmbedBuilder getUserPosition(Guild guild, Member member, User request) {
        User user = member.getUser();

        List<Member> members = guild.getMembers().stream()
                .filter(m -> !m.getUser().isBot())
                .sorted(Comparator.comparingLong(m -> m.getTimeJoined().toEpochSecond()))
                .collect(Collectors.toList());

        int position = 1;
        for (Member m : members) {
            if (m.getIdLong() == member.getIdLong()) break;
            position++;
        }

        String suffix;
        if (position % 100 >= 11 && position % 100 <= 13) suffix = "th";
        else suffix = switch (position % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };

        return new EmbedBuilder()
                .setColor(Color.CYAN)
                .setAuthor("Position of " + user.getName() + "#" + user.getDiscriminator())
                .setDescription("**Position:** " + position + suffix)
                .setFooter(
                        "Requested by " + request.getName() + "#" + request.getDiscriminator(),
                        request.getAvatarUrl()
                );
    }

    private EmbedBuilder getUserClient(Member member, User request) {
        User user = member.getUser();

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setAuthor("Client Checker", null, user.getAvatarUrl())
                .setDescription("User **" + user.getName() + "#" + user.getDiscriminator() + "** is using Discord on:");

        if (member.getActiveClients().isEmpty()) {
            eb.appendDescription("\n*None*");
        } else {
            for (ClientType type : member.getActiveClients()) {
                eb.appendDescription("\n• " + type.name());
            }
        }

        eb.setFooter(
                "Requested by " + request.getName() + "#" + request.getDiscriminator(),
                request.getAvatarUrl()
        );

        return eb;
    }
}