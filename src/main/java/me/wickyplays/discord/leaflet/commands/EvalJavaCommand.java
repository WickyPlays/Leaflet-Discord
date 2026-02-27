package me.wickyplays.discord.leaflet.commands;

import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import me.wickyplays.discord.leaflet.enums.CommandCategory;
import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.utils.CoreUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class EvalJavaCommand extends SlashCommand {

    public EvalJavaCommand() {
        super("evaljava", "Evaluate Java code", true);
        this.category = CommandCategory.OWNER;
        System.out.println("Eval java engine registered");
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "code", "Java code to evaluate", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping option = event.getOption("code");
        if (option == null) {
            MessageEmbed error = CoreUtil.sendError("Missing code", "You must provide Java code");
            event.replyEmbeds(error).setEphemeral(true).queue();
            return;
        }

        try (JShell jshell = JShell.create()) {
            StringBuilder out = new StringBuilder();
            for (SnippetEvent e : jshell.eval(option.getAsString())) {
                if (e.value() != null) {
                    out.append(e.value()).append("\n");
                }
            }
            event.reply("Result:\n```java\n" + (out.isEmpty() ? "void" : out) + "\n```").queue();
        }
    }
}