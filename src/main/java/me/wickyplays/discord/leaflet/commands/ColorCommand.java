package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.enums.CommandCategory;
import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Formatter;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class ColorCommand extends SlashCommand {

    private final int w = 512;
    private final int h = 90;
    private final int MIN = 0;
    private final int MAX = 220;

    public ColorCommand() {
        super("color", "Color converter", true);
        this.category = CommandCategory.UTILITY;
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "mode", "rgb | hex | gradient", true)
                .addOption(STRING, "value1", "RGB (r,g,b) or Hex (#RRGGBB)", false)
                .addOption(STRING, "value2", "Secondary RGB for gradient", false)
                .addOption(STRING, "style", "light | dark | dim (gradient only)", false);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String mode = event.getOption("mode").getAsString();
        String v1 = get(event, "value1");
        String v2 = get(event, "value2");
        String style = get(event, "style");

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            switch (mode.toLowerCase()) {
                case "rgb" -> handleRgb(img, os, v1, event);
                case "hex" -> handleHex(img, os, v1, event);
                case "gradient" -> handleGradient(img, os, v1, v2, style, event);
                default -> event.reply("Unknown mode: `" + mode + "`").setEphemeral(true).queue();
            }
        } catch (Exception e) {
            event.reply("Something went wrong! Error: `" + e.getMessage() + "`")
                    .setEphemeral(true).queue();
        }
    }

    private void handleRgb(BufferedImage img, ByteArrayOutputStream os, String input, SlashCommandInteractionEvent event) throws Exception {
        boolean randomized = false;
        Color c;

        if (input == null) {
            c = getRandomColorRGB();
            randomized = true;
        } else {
            String[] rgb = input.split(",");
            c = getColor(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
        }

        fill(img, c);

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(c)
                .setAuthor("RGB Color Generated")
                .setFooter(new Formatter().format(
                        "R:%d, G:%d, B:%d / Hex: #%s %s",
                        c.getRed(), c.getGreen(), c.getBlue(),
                        Integer.toHexString(c.getRGB()).substring(2),
                        randomized ? "(Randomized)" : ""
                ).toString())
                .setImage("attachment://color.png");

        send(img, os, eb, event);
    }

    private void handleHex(BufferedImage img, ByteArrayOutputStream os, String input, SlashCommandInteractionEvent event) throws Exception {
        boolean randomized = false;
        Color c;

        if (input == null) {
            c = getRandomColorHex();
            randomized = true;
        } else {
            c = getColor(input);
        }

        fill(img, c);

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(c)
                .setAuthor("Hex Color Generated")
                .setFooter(new Formatter().format(
                        "Hex: #%s / R:%d, G:%d, B:%d %s",
                        Integer.toHexString(c.getRGB()).substring(2),
                        c.getRed(), c.getGreen(), c.getBlue(),
                        randomized ? "(Randomized)" : ""
                ).toString())
                .setImage("attachment://color.png");

        send(img, os, eb, event);
    }

    private void handleGradient(BufferedImage img, ByteArrayOutputStream os, String v1, String v2, String style, SlashCommandInteractionEvent event) throws Exception {
        Color c = v1 == null ? getRandomColorRGB() : parseRgb(v1);
        Color c2 = v2 != null ? parseRgb(v2) : getRandomColorRGB();

        int mode = "dark".equalsIgnoreCase(style) ? 1 : "dim".equalsIgnoreCase(style) ? 2 : 0;

        Graphics2D g = img.createGraphics();
        int delta = 0;

        for (int i = 0; i < w; i++) {
            g.setColor(c);
            g.fillRect(i, 0, 1, h);
            delta++;
            if (delta >= 5) {
                c = mode == 2 ? getGradientFrom(c, c2) : getLighter(c, mode);
                delta = 0;
            }
        }

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(c)
                .setAuthor("Gradient Color Generated")
                .setImage("attachment://color.png");

        send(img, os, eb, event);
    }

    private void fill(BufferedImage img, Color c) {
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                img.setRGB(x, y, c.getRGB());
    }

    private void send(BufferedImage img, ByteArrayOutputStream os, EmbedBuilder eb, SlashCommandInteractionEvent event) throws Exception {
        ImageIO.write(img, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        event.replyFiles(net.dv8tion.jda.api.utils.FileUpload.fromData(is, "color.png"))
                .addEmbeds(eb.build())
                .queue();
    }

    private String get(SlashCommandInteractionEvent e, String key) {
        OptionMapping o = e.getOption(key);
        return o == null ? null : o.getAsString();
    }

    private Color parseRgb(String s) {
        String[] rgb = s.split(",");
        return getColor(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }

    private Color getGradientFrom(Color c1, Color c2) {
        int r = step(c1.getRed(), c2.getRed());
        int g = step(c1.getGreen(), c2.getGreen());
        int b = step(c1.getBlue(), c2.getBlue());
        return new Color(r, g, b);
    }

    private int step(int a, int b) {
        if (a < b && a < MAX) return a + 1;
        if (a > b && a > MIN) return a - 1;
        return a;
    }

    private Color getLighter(Color c, int mode) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        if (mode == 0) {
            if (r < MAX) r++;
            if (g < MAX) g++;
            if (b < MAX) b++;
        } else {
            if (r > MIN) r--;
            if (g > MIN) g--;
            if (b > MIN) b--;
        }
        return new Color(r, g, b);
    }

    private Color getColor(int r, int g, int b) {
        return new Color(r, g, b);
    }

    private Color getColor(String hex) {
        return new Color(
                Integer.valueOf(hex.substring(1, 3), 16),
                Integer.valueOf(hex.substring(3, 5), 16),
                Integer.valueOf(hex.substring(5, 7), 16)
        );
    }

    private Color getRandomColorRGB() {
        return new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );
    }

    private Color getRandomColorHex() {
        char[] c = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("#");
        for (int i = 0; i < 6; i++)
            sb.append(c[(int) (Math.random() * c.length)]);
        return getColor(sb.toString());
    }
}