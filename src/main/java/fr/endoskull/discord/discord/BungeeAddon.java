package fr.endoskull.discord.discord;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.discord.Main;
import fr.endoskull.discord.listeners.DiscordJoinListener;
import fr.endoskull.discord.listeners.ReactListener;
import fr.endoskull.discord.listeners.SlashCommandListener;
import fr.endoskull.discord.utils.Candidature;
import fr.endoskull.discord.utils.WaitingLink;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.spicord.api.addon.SimpleAddon;
import org.spicord.bot.DiscordBot;
import org.spicord.bot.command.DiscordBotCommand;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class BungeeAddon extends SimpleAddon {
    
    private Main plugin;
    private static BungeeAddon instance;
    private DiscordBot bot;
    private String prefix;
    private HashMap<String, WaitingLink> waitingLinks = new HashMap();
    private HashMap<String, String> waitingReact = new HashMap<>();
    
    public BungeeAddon(Main plugin) {
        super("EndoSkullDiscord", "endoskull_discord", "BebeDlaStreat");
        instance = this;
        this.plugin = plugin;
    }

    public static BungeeAddon getInstance() {
        return instance;
    }

    @Override
    public void onLoad(DiscordBot bot) {
        this.bot = bot;
        prefix = bot.getCommandPrefix();
        enableCommands();
        bot.getJda().addEventListener(new ReactListener());
        bot.getJda().addEventListener(new DiscordJoinListener());
        bot.getJda().addEventListener(new SlashCommandListener());
    }

    private void enableCommands() {
        bot.onCommand("link", this::linkCommand);
        bot.onCommand("minecraft", this::minecraftCommand);
        bot.onCommand("discord", this::discordCommand);
        bot.onCommand("candidature", this::candidatureCommand);
        //bot.getJda().upsertCommand("ping", "Obtenir la latence du bot").queue();
    }

    private void candidatureCommand(DiscordBotCommand command) {
        User user = command.getMessage().getAuthor();
        Category category = command.getChannel().getParentCategory();
        if (category == null || category.getIdLong() != 823551934669651968L) {
            command.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Pour faire une candidature, vous devez être dans le channel d'un ticket").setColor(Color.RED).build()).queue();
            return;
        }
        if (Candidature.getHelperCandidating().containsKey(user.getIdLong()) || Candidature.getBuilderCandidating().containsKey(user.getIdLong()) || Candidature.getDevCandidating().containsKey(user.getIdLong()) || Candidature.getGraphistCandidating().containsKey(user.getIdLong())) {
            command.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Vous avez déjà une candidature en cours").setColor(Color.RED).build()).queue();
            return;
        }
        command.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Recrutements").setDescription("Bonjour " + user.getName() + ", pour quelle catégorie souhaitez-vous postuler ?\n\n🏹 Modération\n⚡ Build\n⚙ Développement\n🎨 Graphisme").setColor(Color.green).setThumbnail("https://www.univ-tours.fr/medias/photo/resume-freepik_1540214642628-png?ID_FICHE=291341").build()).queue(message -> {
            message.addReaction("🏹").queue();
            message.addReaction("⚡").queue();
            message.addReaction("⚙").queue();
            message.addReaction("🎨").queue();
            waitingReact.put(message.getId(), user.getId());
        });
    }

    private void minecraftCommand(DiscordBotCommand command) {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            User user = command.getMessage().getAuthor();
            Guild guild = command.getGuild();
            Member member = guild.retrieveMember(user).complete();
            if (!member.hasPermission(Permission.ADMINISTRATOR)) return;
            String msg = command.getMessage().getContentRaw();
            if(msg.split(" ").length == 1) {
                return;
            } else {
                command.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Cette fonctionnalité est désativé temporairement").setColor(Color.GREEN).build()).queue();
                /*String targeId = msg.split(" ")[1];
                List<String> accounts = SQLManager.getMinecraftAccountsByDiscordId(targeId);
                if (accounts.isEmpty()) {
                    command.getChannel().sendMessage(new EmbedBuilder().setDescription("Aucun compte minecraft n'a été trouvé pour cette identifiant").setColor(Color.RED).build()).queue();
                } else {
                    command.getChannel().sendMessage(new EmbedBuilder().setDescription("Résultat: `" + accounts + "`").setColor(Color.GREEN).build()).queue();
                }*/
            }
        });
    }

    private void discordCommand(DiscordBotCommand command) {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            User user = command.getMessage().getAuthor();
            Guild guild = command.getGuild();
            Member member = guild.retrieveMember(user).complete();
            if (!member.hasPermission(Permission.ADMINISTRATOR)) return;
            String msg = command.getMessage().getContentRaw();
            if(msg.split(" ").length == 1) {
                return;
            } else {
                command.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Cette fonctionnalité est désactivé temporairement").setColor(Color.RED).build()).queue();
                /*String account = msg.split(" ")[1];
                String discordId = SQLManager.getDiscordIdByMinecraftAccount(account);
                if (discordId == null || discordId.length() < 10) {
                    command.getChannel().sendMessage(new EmbedBuilder().setDescription("Aucun compte discord n'a été trouvé pour ce pseudo").setColor(Color.RED).build()).queue();
                } else {
                    command.getChannel().sendMessage(new EmbedBuilder().setDescription("Résultat: `" + discordId + "`").setColor(Color.GREEN).build()).queue();
                }*/
            }
        });
    }

    private void linkCommand(DiscordBotCommand command) {
        User user = command.getMessage().getAuthor();
        String msg = command.getMessage().getContentRaw();
        if(msg.split(" ").length == 1) {
            command.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Utilisation").setDescription("-link <Pseudo>").setColor(Color.RED).build()).queue();
        } else {
            ProxiedPlayer player = plugin.getProxy().getPlayer(msg.split(" ")[1]);
            if (player == null) {
                command.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Erreur").setDescription("Ce joueur n'est pas connecté").setColor(Color.RED).build()).queue();
            } else {
                Account account = AccountProvider.getAccount(player.getUniqueId());
                if (!account.getProperty("discord", "none").equalsIgnoreCase("none")) {
                    command.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Erreur").setDescription("Ce compte minecraft est déjà relié avec un compte discord").setColor(Color.RED).build()).queue();
                    return;
                }
                String random = getRandomNumber();
                command.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("Validation").setDescription("Pour confirmer la liaison entre votre compte discord et minecraft faîtes\n`/link " + random + "`\nAttention une fois ceci fait vous ne pourrez plus changer le compte discord lié à votre compte minecraft sur EndoSkull").setColor(Color.GREEN).build()).queue();
                waitingLinks.put(user.getId(), new WaitingLink(user.getId(), System.currentTimeMillis() + 1000*60*5, player.getUniqueId(), random));
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

    public HashMap<String, WaitingLink> getWaitingLinks() {
        return waitingLinks;
    }

    public DiscordBot getBot() {
        return bot;
    }

    public HashMap<String, String> getWaitingReact() {
        return waitingReact;
    }
}
