package fr.endoskull.discord.listeners;

import fr.endoskull.discord.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import java.awt.*;
import java.util.Date;

public class SlashCommandListener implements EventListener {

    @Override
    public void onEvent(GenericEvent genericEvent) {
        if (genericEvent instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent e = (SlashCommandInteractionEvent) genericEvent;
            if (e.getName().equalsIgnoreCase("ping")) {
                long time = System.currentTimeMillis();
                e.reply("Pong!").setEphemeral(true)
                        .flatMap(v ->
                                e.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)
                        ).queue();
            }

        }
    }
}
