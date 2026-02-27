package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.utils.CoreUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class DadJokeCommand extends SlashCommand {

    private static final String API_URL = "https://icanhazdadjoke.com/slack";

    public DadJokeCommand() {
        super("dadjoke", "Generate a random dad joke");
        this.category = me.wickyplays.discord.leaflet.enums.CommandCategory.FUN;
        this.cooldownSeconds = 5;
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        sendJoke(event.getUser(), event);
    }

    private void sendJoke(User user, SlashCommandInteractionEvent event) {
        try {
            String jsonString = CoreUtil.readStringFromUrl(API_URL, true);
            JSONObject json = new JSONObject(jsonString);
            JSONArray attachments = json.getJSONArray("attachments");
            JSONObject attachment = attachments.getJSONObject(0);
            String joke = attachment.getString("text");

            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(CoreUtil.getRandomColorRGB())
                    .setAuthor("Random Dad Joke")
                    .setDescription(joke)
                    .setFooter(
                            "Requested by " + user.getName() + "#" + user.getDiscriminator(),
                            user.getAvatarUrl()
                    );

            event.getHook().editOriginalEmbeds(eb.build()).queue();

        } catch (IOException e) {
            EmbedBuilder error = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setAuthor("Uh oh")
                    .setDescription("Failed to fetch a dad joke. Please try again.");

            event.getHook().editOriginalEmbeds(error.build()).queue();
        }
    }
}