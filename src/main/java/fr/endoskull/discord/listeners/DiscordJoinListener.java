package fr.endoskull.discord.listeners;

import fr.endoskull.discord.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import java.awt.*;
import java.util.Date;

public class DiscordJoinListener implements EventListener {

    @Override
    public void onEvent(GenericEvent genericEvent) {
        if (genericEvent instanceof GuildMemberJoinEvent) {
            GuildMemberJoinEvent e = (GuildMemberJoinEvent) genericEvent;
            Main.getInstance().setMemberCount(Main.getInstance().getMemberCount() + 1);
            Main.getInstance().updateMemberCount();
            Member member = e.getMember();
            TextChannel channel = e.getJDA().getTextChannelById("820585969086496788");
            channel.sendMessageEmbeds(new EmbedBuilder().setTitle("Nouvelle arrivant !").setColor(Color.decode("#2ECC71")).setThumbnail(member.getUser().getAvatarUrl()).setDescription("Bienvenue" + member.getAsMention() + " sur EndoSkull, avant de commencer à parler nous t'invitons à jeter un coup d'oeil au <#819973376340525116>").setTimestamp(new Date().toInstant()).build()).queue();
            if (member.getUser().hasPrivateChannel()) {
                member.getUser().openPrivateChannel()
                        .flatMap(c -> c.sendMessage("Bonjour `" + member.getUser().getName() + "`, merci d'avoir rejoint le discord de EndoSkull\nVoici quelques informations sur le serveur Minecraft:\n\n> Version: À partir de la **1.8**\n> Ip: **MC.ENDOSKULL.FR**"))
                        .queue();
            }
        }
        if (genericEvent instanceof GuildMemberRemoveEvent) {
            GuildMemberRemoveEvent e = (GuildMemberRemoveEvent) genericEvent;
            Main.getInstance().setMemberCount(Main.getInstance().getMemberCount() - 1);
            Main.getInstance().updateMemberCount();
            User user = e.getUser();
            TextChannel channel = e.getJDA().getTextChannelById("820585969086496788");
            channel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.RED).setDescription("C'est en ce triste jour qu'est survenu le départ du combattant " + user.getAsMention() + " :'(").build()).queue();
        }
    }
}