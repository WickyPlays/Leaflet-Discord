package me.wickyplays.discord.leaflet;

import io.github.cdimascio.dotenv.Dotenv;
import me.wickyplays.discord.leaflet.commands.*;
import me.wickyplays.discord.leaflet.listeners.SlashCommandListener;
import me.wickyplays.discord.leaflet.managers.CommandRegistryManager;
import me.wickyplays.discord.leaflet.runnables.PresenceRunnable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        String systemEnv = System.getenv("DISCORD_TOKEN");
        String token = "";

        //Attempt to find .env instead
        if (systemEnv == null) {
            Dotenv dotenv = Dotenv.load();
            token = dotenv.get("DISCORD_TOKEN");
        } else {
            token = systemEnv;
        }

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
        CommandRegistryManager.register(new EvalJSCommand());
        CommandRegistryManager.register(new EvalJavaCommand());
        CommandRegistryManager.register(new DadJokeCommand());
        CommandRegistryManager.register(new SnowflakeCommand());
        CommandRegistryManager.register(new TimezoneCommand());
        CommandRegistryManager.register(new TimeConverterCommand());
        CommandRegistryManager.register(new ColorCommand());
        CommandRegistryManager.register(new UserInfoCommand());
        CommandRegistryManager.register(new MeCommand());
        CommandRegistryManager.register(new LanguageDetectCommand());

        CommandRegistryManager.upsert(jda);

        new PresenceRunnable().run(jda);
    }
}