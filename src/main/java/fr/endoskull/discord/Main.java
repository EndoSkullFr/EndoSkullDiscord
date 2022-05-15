package fr.endoskull.discord;


import fr.endoskull.api.data.sql.MySQL;
import fr.endoskull.discord.commands.LinkDiscordCommand;
import fr.endoskull.discord.listeners.DiscordJoinListener;
import fr.endoskull.discord.listeners.JoinListener;
import fr.endoskull.discord.listeners.ReactListener;
import fr.endoskull.discord.listeners.SlashCommandListener;
import fr.endoskull.discord.utils.WaitingLink;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class Main extends Plugin {
    private static Main instance;
    private JDA jda;
    private int memberCount;

    private HashMap<String, String> waitingReact = new HashMap<>();
    private HashMap<String, WaitingLink> waitingLinks = new HashMap();

    public void onEnable() {
        instance = this;

        getProxy().getScheduler().schedule(this,() -> {
            getProxy().getScheduler().runAsync(this, () -> {
                System.out.println("[EndoSkullDiscord] Démarrage du bot");
                try {
                    jda = JDABuilder.createDefault("ODg5MDc5ODU2MDQ5MDI5MTIw.YUcCIw.cGWZ9ZbFa7kGfRS-alFuv5EIKVk")
                            .enableIntents(GatewayIntent.GUILD_MEMBERS)
                            .setActivity(Activity.watching("Le développement du serveur"))
                            .setAutoReconnect(true)
                            .addEventListeners(new SlashCommandListener(), new ReactListener(), new DiscordJoinListener())
                            .build();
                    jda.upsertCommand("ping", "Obtenir la latence bot").queue(command -> System.out.println("Ajout de la commande " + command.getName()));
                    jda.upsertCommand("candidature", "Créer une candidature").queue(command -> System.out.println("Ajout de la commande " + command.getName()));
                    jda.upsertCommand("link", "Lier son compte sur le serveur minecraft").addOption(OptionType.STRING, "pseudo", "Le pseudo du compte minecraft que vous voulez lier", true).queue(command -> System.out.println("Ajout de la commande " + command.getName()));
                    jda.upsertCommand("minecraft", "Obtenir le compte minecraft lié à un compte discord").addOption(OptionType.USER, "pseudo", "Le pseudo du compte minecraft", true).queue(command -> System.out.println("Ajout de la commande " + command.getName()));
                    jda.upsertCommand("discord", "Obtenir le compte discord lié à un compte minecraft").addOption(OptionType.STRING, "pseudo", "Le compte discord", true).queue(command -> System.out.println("Ajout de la commande " + command.getName()));
                    jda.awaitReady();
                    setMemberCount(jda.getGuildById("819973375903531009").getMemberCount());
                    updateMemberCount();
                    getProxy().getPluginManager().registerCommand(this, new LinkDiscordCommand(this));
                    getProxy().getPluginManager().registerListener(this, new JoinListener(this));
                } catch (LoginException e) {
                    System.out.println("[EndoSkullDiscord] Echec");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }, 5, TimeUnit.SECONDS);


        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Main getInstance() {
        return instance;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public JDA getJda() {
        return jda;
    }

    public HashMap<String, String> getWaitingReact() {
        return waitingReact;
    }

    public void updateMemberCount() {
        MySQL.getInstance().update("INSERT INTO `discord_count`(`members`) VALUES ('" + memberCount + "')");
    }

    public HashMap<String, WaitingLink> getWaitingLinks() {
        return waitingLinks;
    }
}
