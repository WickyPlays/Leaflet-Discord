package me.wickyplays.discord.leaflet.interfaces;

import me.wickyplays.discord.leaflet.enums.CommandCategory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class SlashCommand {

    protected final String name;
    protected final String description;

    protected CommandCategory category = CommandCategory.GENERAL;
    protected boolean ownerOnly = false;
    protected boolean guildOnly = false;
    protected boolean nsfw = false;
    protected boolean hidden = false;

    protected long cooldownSeconds = 0;

    protected Set<Permission> requiredPermissions = Collections.emptySet();
    protected List<String> aliases = Collections.emptyList();
    protected List<String> usageExamples = Collections.emptyList();

    protected SlashCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected SlashCommand(String name, String description, boolean ownerOnly) {
        this.name = name;
        this.description = description;
        this.ownerOnly = ownerOnly;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CommandCategory getCategory() {
        return category;
    }

    public boolean isOwnerOnly() {
        return ownerOnly;
    }

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public boolean isHidden() {
        return hidden;
    }

    public long getCooldownSeconds() {
        return cooldownSeconds;
    }

    public Set<Permission> getRequiredPermissions() {
        return requiredPermissions;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getUsageExamples() {
        return usageExamples;
    }

    public abstract SlashCommandData build();
    public abstract void execute(SlashCommandInteractionEvent event);
}