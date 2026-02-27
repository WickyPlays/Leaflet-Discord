package me.wickyplays.discord.leaflet;

import io.github.cdimascio.dotenv.Dotenv;
import me.wickyplays.discord.leaflet.commands.HelpCommand;
import me.wickyplays.discord.leaflet.commands.SayCommand;
import me.wickyplays.discord.leaflet.listeners.SlashCommandListener;
import me.wickyplays.discord.leaflet.managers.CommandRegistryManager;
import me.wickyplays.discord.leaflet.runnables.PresenceRunnable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");

        JDA jda = JDABuilder.createLight(
                        token,
                        EnumSet.of(
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.MESSAGE_CONTENT
                        )
                )
                .addEventListeners(new SlashCommandListener())
                .build();

        jda.awaitReady();

        CommandRegistryManager.register(new SayCommand());
        CommandRegistryManager.register(new HelpCommand());

        CommandRegistryManager.upsert(jda);

        new PresenceRunnable().run(jda);
    }
}