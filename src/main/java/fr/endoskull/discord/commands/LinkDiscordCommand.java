package fr.endoskull.discord.commands;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.discord.Main;
import fr.endoskull.discord.utils.WaitingLink;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class LinkDiscordCommand extends Command {
    public LinkDiscordCommand(Main main) {
        super("link");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length < 1) {
            player.sendMessage("§cUtilisation: /link <code>");
            return;
        }
        Account account = AccountProvider.getAccount(player.getUniqueId());
        if (!account.getProperty("discord", "none").equalsIgnoreCase("none")) {
            player.sendMessage("§cVotre compte minecraft est déjà relié avec un compte discord");
            return;
        }
        String code = args[0];
        for (String s : new ArrayList<>(Main.getInstance().getWaitingLinks().keySet())) {
            WaitingLink waitingLink = Main.getInstance().getWaitingLinks().get(s);
            if (waitingLink.getExpiry() < System.currentTimeMillis()) {
                Main.getInstance().getWaitingLinks().remove(s);
                continue;
            }
            if (waitingLink.getUuid().equals(player.getUniqueId())) {
                if (code.equalsIgnoreCase(waitingLink.getCode())) {
                    account.setProperty("discord", waitingLink.getDiscordId());
                    player.sendMessage("§aVotre compte minecraft a été relié avec succès à votre compte discord !");
                    Main.getInstance().getWaitingLinks().remove(s);
                } else {
                    player.sendMessage("§cCode invalide !");
                }
                return;
            }
        }
        player.sendMessage("§cVous n'avez pas essayer de lier votre compte minecraft à votre discord dans les 5 dernières minutes");
    }
}