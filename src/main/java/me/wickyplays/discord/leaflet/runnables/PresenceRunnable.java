package me.wickyplays.discord.leaflet.runnables;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PresenceRunnable {

    private static final List<String> PRESENCES = List.of(
            "Watching the server",
            "Listening to music",
            "Managing commands",
            "Having fun",
            "Watching Wicky",
            "Enjoying nature"
    );

    private ScheduledExecutorService scheduler = null;

    public void run(JDA jda) {
        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();

        Random random = new Random();
        scheduler.scheduleAtFixedRate(() -> {
            String text = PRESENCES.get(random.nextInt(PRESENCES.size()));
            jda.getPresence().setActivity(Activity.playing(text));
        }, 0, 20, TimeUnit.SECONDS);
    }

}
