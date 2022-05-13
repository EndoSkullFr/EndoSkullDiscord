package fr.endoskull.discord.listeners;

import fr.endoskull.discord.discord.BungeeAddon;
import fr.endoskull.discord.utils.Candidature;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import java.awt.*;

public class ReactListener implements EventListener {

    @Override
    public void onEvent(GenericEvent genericEvent) {
        if (genericEvent instanceof MessageReactionAddEvent) {
            MessageReactionAddEvent e = (MessageReactionAddEvent) genericEvent;
            Member member = e.getMember();
            String message = e.getMessageId();
            if (BungeeAddon.getInstance().getWaitingReact().containsKey(message)) {
                if (BungeeAddon.getInstance().getWaitingReact().get(message).equalsIgnoreCase(member.getId())) {
                    String reaction = e.getReactionEmote().getAsReactionCode();
                    if (reaction.equalsIgnoreCase("🏹")) {
                        BungeeAddon.getInstance().getWaitingReact().remove(message);
                        Candidature.getHelperCandidating().put(member.getIdLong(), 0);
                        Candidature.getHelperChannel().put(member.getIdLong(), e.getChannel().getIdLong());
                        e.getChannel().sendMessageEmbeds(Candidature.getHelperEmbed(member.getIdLong())).queue();
                    } else if (reaction.equalsIgnoreCase("⚡")) {
                        BungeeAddon.getInstance().getWaitingReact().remove(message);
                        Candidature.getBuilderCandidating().put(member.getIdLong(), 0);
                        Candidature.getBuilderChannel().put(member.getIdLong(), e.getChannel().getIdLong());
                        e.getChannel().sendMessageEmbeds(Candidature.getBuilderEmbed(member.getIdLong())).queue();
                    } else if (reaction.equalsIgnoreCase("⚙")) {
                        BungeeAddon.getInstance().getWaitingReact().remove(message);
                        Candidature.getDevCandidating().put(member.getIdLong(), 0);
                        Candidature.getDevChannel().put(member.getIdLong(), e.getChannel().getIdLong());
                        e.getChannel().sendMessageEmbeds(Candidature.getDevEmbed(member.getIdLong())).queue();
                    } else if (reaction.equalsIgnoreCase("🎨")) {
                        BungeeAddon.getInstance().getWaitingReact().remove(message);
                        Candidature.getGraphistCandidating().put(member.getIdLong(), 0);
                        Candidature.getGraphistChannel().put(member.getIdLong(), e.getChannel().getIdLong());
                        e.getChannel().sendMessageEmbeds(Candidature.getGraphistEmbed(member.getIdLong())).queue();
                    }
                }
            }
        }
        if (genericEvent instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) genericEvent;
            if (e.getMember() == null) return;
            long memberId = e.getMember().getIdLong();
            if (Candidature.getHelperCandidating().containsKey(memberId) && e.getChannel().getIdLong() == Candidature.getHelperChannel().get(memberId)) {
                Candidature.getHelperCandidating().put(memberId, Candidature.getHelperCandidating().get(memberId) + 1);
                e.getChannel().sendMessageEmbeds(Candidature.getHelperEmbed(memberId)).queue();
            }
            if (Candidature.getBuilderCandidating().containsKey(memberId) && e.getChannel().getIdLong() == Candidature.getBuilderChannel().get(memberId)) {
                Candidature.getBuilderCandidating().put(memberId, Candidature.getBuilderCandidating().get(memberId) + 1);
                e.getChannel().sendMessageEmbeds(Candidature.getBuilderEmbed(memberId)).queue();
            }
            if (Candidature.getDevCandidating().containsKey(memberId) && e.getChannel().getIdLong() == Candidature.getDevChannel().get(memberId)) {
                Candidature.getDevCandidating().put(memberId, Candidature.getDevCandidating().get(memberId) + 1);
                e.getChannel().sendMessageEmbeds(Candidature.getDevEmbed(memberId)).queue();
            }
            if (Candidature.getGraphistCandidating().containsKey(memberId) && e.getChannel().getIdLong() == Candidature.getGraphistChannel().get(memberId)) {
                Candidature.getGraphistCandidating().put(memberId, Candidature.getGraphistCandidating().get(memberId) + 1);
                e.getChannel().sendMessageEmbeds(Candidature.getGraphistEmbed(memberId)).queue();
            }
        }
    }
}
