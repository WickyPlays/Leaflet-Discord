package me.wickyplays.discord.leaflet;

import io.github.cdimascio.dotenv.Dotenv;
import me.wickyplays.discord.leaflet.listeners.MessageReceiveListener;
import me.wickyplays.discord.leaflet.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.EnumSet;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Main {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");
        JDA jda = JDABuilder.createLight(token,
                        EnumSet.of(GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.MESSAGE_CONTENT))
                .addEventListeners(new MessageReceiveListener())
                .addEventListeners(new SlashCommandListener())
                .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("say", "Makes the bot say what you tell it to")
                        .addOption(STRING, "content", "What the bot should say", true),
                Commands.slash("leave", "Makes the bot leave the server")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );

        commands.queue();
    }
}