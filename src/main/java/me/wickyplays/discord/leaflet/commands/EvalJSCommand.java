package me.wickyplays.discord.leaflet.commands;

import me.wickyplays.discord.leaflet.enums.CommandCategory;
import me.wickyplays.discord.leaflet.interfaces.SlashCommand;
import me.wickyplays.discord.leaflet.utils.CoreUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.graalvm.polyglot.Context;

import javax.script.ScriptException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class EvalJSCommand extends SlashCommand {

    private final Context context;

    public EvalJSCommand() {
        super("evaljs", "Evaluate JavaScript code", true);
        this.category = CommandCategory.OWNER;

        this.context = Context.newBuilder("js")
                .allowAllAccess(false)
                .build();

        if (this.context == null) {
            throw new IllegalStateException(
                    "GraalJS ScriptEngine not found. Ensure org.graalvm.js:js is on the classpath."
            );
        }
    }

    @Override
    public SlashCommandData build() {
        return Commands.slash(name, description)
                .addOption(STRING, "code", "JavaScript code to evaluate", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping option = event.getOption("code");

        if (option == null) {
            MessageEmbed error = CoreUtil.sendError(
                    "Missing code",
                    "You must provide JavaScript code to evaluate"
            );
            event.replyEmbeds(error).setEphemeral(true).queue();
            return;
        }

        Object result = context.eval("js", option.getAsString());
        String output = result == null ? "undefined" : result.toString();
        event.reply("Result:\n```js\n" + output + "\n```").queue();
    }
}