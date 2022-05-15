package fr.endoskull.discord.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Candidature {
    private static HashMap<Long, Integer> helperCandidating = new HashMap<>();
    private static HashMap<Long, Long> helperChannel = new HashMap<>();
    private static HashMap<Long, Integer> builderCandidating = new HashMap<>();
    private static HashMap<Long, Long> builderChannel = new HashMap<>();
    private static HashMap<Long, Integer> devCandidating = new HashMap<>();
    private static HashMap<Long, Long> devChannel = new HashMap<>();
    private static HashMap<Long, Integer> graphistCandidating = new HashMap<>();
    private static HashMap<Long, Long> graphistChannel = new HashMap<>();

    private static List<String> helperQuestions = new ArrayList<String>() {{
        add("Tout d'abord, si ce n'est pas le cas veuillez relier votre commpte minecraft à votre compte discord sur le serveur pour confirmer votre identité et assurer un meilleur traitement de la candidature\n\n**Question:** Pouvez vous faire une présentation de vous irl ? (prénom, âge, etc...)");
        add("Quel est votre pseudo IG ?");
        add("Depuis quand jouez-vous à minecraft ?");
        add("À quand date votre première connection au serveur ?");
        add("Quelles sont les points à améliorer sur le serveur ?");
        add("En quoi contriburiez-vous au serveur ?");
        add("Individuellement, donnez-nous le rôle de chacun des administrateurs de ce serveur (GoldenBlue_, BebeDlaStreat et Samu_Fan_De_KFC)\nps: Vous avez le droit de ne pas savoir");
        add("Quelle est votre niveau d'anglais ?");
        add("Disposez-vous d'un micro correct ?");
        add("Quelles sont vos disponibilités ?");
        add("Êtes-vous la seule personne à utiliser ce compte discord et/ou minecraft ?");
        add("Habitez-vous en France ? Si non, précisez votre fuseau horaire");
        add("Quelle est l'id de ce channel ?");

    }};

    private static List<String> builderQuestions = new ArrayList<String>() {{
        add("Tout d'abord, si ce n'est pas le cas veuillez relier votre commpte minecraft à votre compte discord sur le serveur pour confirmer votre identité et assurer un meilleur traitement de la candidature\n\n**Question:** Pouvez vous faire une présentation de vous irl ? (prénom, âge, etc...)");
        add("Quel est votre pseudo IG ?");
        add("Depuis quand jouez-vous à minecraft ?");
        add("En quoi contriburiez-vous au serveur ?");
        add("Disposez-vous d'un micro correct ?");
        add("Quelles sont vos disponibilités ?");
        add("Êtes-vous la seule personne à utiliser ce compte discord et/ou minecraft ?");
        add("Habitez-vous en France ? Si non, précisez votre fuseau horaire");
        add("Avez-vous des screens de builds/de projets déjà déjà réalisés à nous montrer ? (si oui les mettre)");
    }};

    private static List<String> devQuestions = new ArrayList<String>() {{
        add("Tout d'abord, si ce n'est pas le cas veuillez relier votre commpte minecraft à votre compte discord sur le serveur pour confirmer votre identité et assurer un meilleur traitement de la candidature\n\n**Question:** Pouvez vous faire une présentation de vous irl ? (prénom, âge, etc...)");
        add("Quel est votre pseudo IG ?");
        add("Depuis quand jouez-vous à minecraft ?");
        add("En quoi contriburiez-vous au serveur ?");
        add("Disposez-vous d'un micro correct ?");
        add("Quelles sont vos disponibilités ?");
        add("Êtes-vous la seule personne à utiliser ce compte discord et/ou minecraft ?");
        add("Habitez-vous en France ? Si non, précisez votre fuseau horaire");
        add("Quel(s) langage(s) de programmation maîtrisez-vous ?");
        add("Savez-vous utiliser MySQL ?");
        add("Savez-vous utiliser Redis ?");
        add("Savez-vous utiliser Git ?");
        add("Avez-vous un environnement de travail avec MySQL et Redis d'installer sur votre ordinateur ?");
        add("Avez-vous un github (ou gitlab) ? Et des projets déjà réalisés à présenter ?");
        add("Quel est votre IDE ?");
    }};

    private static List<String> graphistQuestions = new ArrayList<String>() {{
        add("Tout d'abord, si ce n'est pas le cas veuillez relier votre commpte minecraft à votre compte discord sur le serveur pour confirmer votre identité et assurer un meilleur traitement de la candidature\n\n**Question:** Pouvez vous faire une présentation de vous irl ? (prénom, âge, etc...)");
        add("En quoi contriburiez-vous au serveur ?");
        add("Disposez-vous d'un micro correct ?");
        add("Quelles sont vos disponibilités ?");
        add("Êtes-vous la seule personne à utiliser ce compte discord et/ou minecraft ?");
        add("Habitez-vous en France ? Si non, précisez votre fuseau horaire");
        add("Avez-vous des designs/des projets déjà déjà réalisés à nous montrer ? (si oui les mettre)");
    }};

    public static HashMap<Long, Integer> getHelperCandidating() {
        return helperCandidating;
    }

    public static List<String> getHelperQuestions() {
        return helperQuestions;
    }

    public static MessageEmbed getHelperEmbed(long member) {
        if (helperCandidating.get(member) >= helperQuestions.size()) {
            helperChannel.remove(member);
            helperCandidating.remove(member);
            return new EmbedBuilder().setTitle("Fin des questions").setDescription("Merci d'y avoir répondu, vous allez recevoir une réponse d'ici maximum une semaine dans ce channel").setColor(Color.GREEN).build();
        }
        return new EmbedBuilder().setAuthor("Candidature Helper").setTitle("Question n°" + (helperCandidating.get(member) + 1) + "/" + Candidature.getHelperQuestions().size()).setDescription(Candidature.getHelperQuestions().get(helperCandidating.get(member))).setColor(Color.GREEN).build();
    }

    public static MessageEmbed getBuilderEmbed(long member) {
        if (builderCandidating.get(member) >= builderQuestions.size()) {
            builderChannel.remove(member);
            builderCandidating.remove(member);
            return new EmbedBuilder().setTitle("Fin des questions").setDescription("Merci d'y avoir répondu, vous allez recevoir une réponse d'ici maximum une semaine dans ce channel").setColor(Color.GREEN).build();
        }
        return new EmbedBuilder().setAuthor("Candidature Builder").setTitle("Question n°" + (builderCandidating.get(member) + 1) + "/" + Candidature.getBuilderQuestions().size()).setDescription(Candidature.getBuilderQuestions().get(builderCandidating.get(member))).setColor(Color.GREEN).build();
    }

    public static MessageEmbed getDevEmbed(long member) {
        if (devCandidating.get(member) >= devQuestions.size()) {
            devChannel.remove(member);
            devCandidating.remove(member);
            return new EmbedBuilder().setTitle("Fin des questions").setDescription("Merci d'y avoir répondu, vous allez recevoir une réponse d'ici maximum une semaine dans ce channel").setColor(Color.GREEN).build();
        }
        return new EmbedBuilder().setAuthor("Candidature Développeur").setTitle("Question n°" + (devCandidating.get(member) + 1) + "/" + Candidature.getDevQuestions().size()).setDescription(Candidature.getDevQuestions().get(devCandidating.get(member))).setColor(Color.GREEN).build();
    }

    public static MessageEmbed getGraphistEmbed(long member) {
        if (graphistCandidating.get(member) >= graphistQuestions.size()) {
            graphistChannel.remove(member);
            graphistCandidating.remove(member);
            return new EmbedBuilder().setTitle("Fin des questions").setDescription("Merci d'y avoir répondu, vous allez recevoir une réponse d'ici maximum une semaine dans ce channel").setColor(Color.GREEN).build();
        }
        return new EmbedBuilder().setAuthor("Candidature Graphiste").setTitle("Question n°" + (graphistCandidating.get(member) + 1) + "/" + Candidature.getGraphistQuestions().size()).setDescription(Candidature.getGraphistQuestions().get(graphistCandidating.get(member))).setColor(Color.GREEN).build();
    }

    public static HashMap<Long, Long> getHelperChannel() {
        return helperChannel;
    }

    public static HashMap<Long, Integer> getBuilderCandidating() {
        return builderCandidating;
    }

    public static HashMap<Long, Long> getBuilderChannel() {
        return builderChannel;
    }

    public static HashMap<Long, Integer> getDevCandidating() {
        return devCandidating;
    }

    public static HashMap<Long, Long> getDevChannel() {
        return devChannel;
    }

    public static HashMap<Long, Integer> getGraphistCandidating() {
        return graphistCandidating;
    }

    public static HashMap<Long, Long> getGraphistChannel() {
        return graphistChannel;
    }

    public static List<String> getBuilderQuestions() {
        return builderQuestions;
    }

    public static List<String> getDevQuestions() {
        return devQuestions;
    }

    public static List<String> getGraphistQuestions() {
        return graphistQuestions;
    }
}