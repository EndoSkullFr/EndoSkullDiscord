package fr.endoskull.discord.listeners;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.discord.Main;
import fr.endoskull.discord.utils.Candidature;
import fr.endoskull.discord.utils.SQLManager;
import fr.endoskull.discord.utils.WaitingLink;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.getName().equalsIgnoreCase("ping")) {
            long time = System.currentTimeMillis();
            e.reply("Pong!").setEphemeral(false)
                    .flatMap(v ->
                            e.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                    ).queue(); // Queue both reply and edit
        }
        if (e.getName().equalsIgnoreCase("candidature")) {
            User user = e.getMember().getUser();
            Category category = e.getTextChannel().getParentCategory();
            if (category == null || category.getIdLong() != 823551934669651968L) {
                e.replyEmbeds(new EmbedBuilder().setDescription("Pour faire une candidature, vous devez être dans le channel d'un ticket").setColor(Color.RED).build()).setEphemeral(true).queue();
                return;
            }
            if (Candidature.getHelperCandidating().containsKey(user.getIdLong()) || Candidature.getBuilderCandidating().containsKey(user.getIdLong()) || Candidature.getDevCandidating().containsKey(user.getIdLong()) || Candidature.getGraphistCandidating().containsKey(user.getIdLong())) {
                e.replyEmbeds(new EmbedBuilder().setDescription("Vous avez déjà une candidature en cours").setColor(Color.RED).build()).setEphemeral(true).queue();
                return;
            }
            e.replyEmbeds(new EmbedBuilder().setTitle("Recrutements").setDescription("Bonjour " + user.getName() + ", pour quelle catégorie souhaitez-vous postuler ?\n\n🏹 Modération\n⚡ Build\n⚙ Développement\n🎨 Graphisme").setColor(Color.green).setThumbnail("https://www.univ-tours.fr/medias/photo/resume-freepik_1540214642628-png?ID_FICHE=291341").build()).queue(interaction -> {
                interaction.retrieveOriginal().queue(message -> {
                    message.addReaction("🏹").queue();
                    message.addReaction("⚡").queue();
                    message.addReaction("⚙").queue();
                    message.addReaction("🎨").queue();
                    Main.getInstance().getWaitingReact().put(message.getId(), user.getId());
                });
            });
        }
        if (e.getName().equalsIgnoreCase("minecraft")) {
            OptionMapping option = e.getOption("pseudo");
            String targeId = option.getAsMember().getId();
            List<String> accounts = SQLManager.getMinecraftAccountsByDiscordId(targeId);
            if (accounts.isEmpty()) {
                e.replyEmbeds(new EmbedBuilder().setDescription("Aucun compte minecraft n'a été trouvé pour cet identifiant").setColor(Color.RED).build()).queue();
            } else {
                e.replyEmbeds(new EmbedBuilder().setDescription("Résultat: `" + accounts + "`").setColor(Color.GREEN).build()).queue();
            }
        }
        if (e.getName().equalsIgnoreCase("discord")) {
            OptionMapping option = e.getOption("pseudo");
            String name = option.getAsString();
            String discordId = SQLManager.getDiscordIdByMinecraftAccount(name);
            if (discordId == null || discordId.length() < 10) {
                e.replyEmbeds(new EmbedBuilder().setDescription("Aucun compte discord n'a été trouvé pour ce pseudo").setColor(Color.RED).build()).queue();
            } else {
                e.replyEmbeds(new EmbedBuilder().setDescription("Résultat: `" + discordId + "` <@" + discordId + ">").setColor(Color.GREEN).build()).queue();
            }
        }
        if (e.getName().equalsIgnoreCase("link")) {
            OptionMapping option = e.getOption("pseudo");
            User user = e.getUser();
            ProxiedPlayer player = Main.getInstance().getProxy().getPlayer(option.getAsString());
            if (player == null) {
                e.replyEmbeds(new EmbedBuilder().setTitle("Erreur").setDescription("Ce joueur n'est pas connecté").setColor(Color.RED).build()).setEphemeral(true).queue();
            } else {
                Account account = AccountProvider.getAccount(player.getUniqueId());
                if (!account.getProperty("discord", "none").equalsIgnoreCase("none")) {
                    e.replyEmbeds(new EmbedBuilder().setTitle("Erreur").setDescription("Ce compte minecraft est déjà relié avec un compte discord").setColor(Color.RED).build()).setEphemeral(true).queue();
                    return;
                }
                String random = getRandomNumber();
                e.replyEmbeds(new EmbedBuilder().setTitle("Validation").setDescription("Pour confirmer la liaison entre votre compte discord et minecraft faîtes\n`/link " + random + "`\nAttention une fois ceci fait vous ne pourrez plus changer le compte discord lié à votre compte minecraft sur EndoSkull").setColor(Color.GREEN).build()).setEphemeral(true).queue();
                Main.getInstance().getWaitingLinks().put(user.getId(), new WaitingLink(user.getId(), System.currentTimeMillis() + 1000 * 60 * 5, player.getUniqueId(), random));
            }
        }
    }

    private String getRandomNumber() {
        String s = "";
        for (int i = 0; i < 6; i++) {
            s += String.valueOf(new Random().nextInt(10));
        }
        return s;
    }
}