package me.wickyplays.discord.leaflet.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.SplittableRandom;

public class CoreUtil {

    public static MessageEmbed sendSuccess(String title, String description) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(title)
                .setDescription(description)
                .build();
    }

    public static MessageEmbed sendError(String title, String description) {
        return sendError(title, description, null);
    }

    public static MessageEmbed sendError(String title, String description, String footer) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(title)
                .setDescription(description)
                .setFooter(footer)
                .build();
    }

    public static int getRandomInt(int min, int max) {
        SplittableRandom rnd = new SplittableRandom();
        return rnd.nextInt(min, max);
    }

    public static double getRandomDouble(double min, double max) {
        SplittableRandom rnd = new SplittableRandom();
        return rnd.nextDouble(min, max);
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            return new JSONObject(jsonText);
        }
    }

    public static String readStringFromUrl(String url) throws IOException, JSONException {
        return readStringFromUrl(url, false);
    }

    public static String readStringFromUrl(String url, boolean useProperty) throws IOException, JSONException {

        URL u = new URL(url);

        if (useProperty) {
            URLConnection conn = u.openConnection();
            conn.addRequestProperty("User-Agent", "Chrome");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
        }

        try (InputStream is = u.openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }

    public static Color getRandomColorRGB() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }

    public static String capFirstChar(String s) {
        String lower = s.toLowerCase();
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }

}