package fr.endoskull.discord.listeners;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.discord.Main;
import fr.endoskull.discord.discord.BungeeAddon;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Set;
import java.util.stream.Collectors;

public class JoinListener implements Listener {

    private Main main;
    private final LuckPerms luckPerms = LuckPermsProvider.get();
    private final UserManager userManager = luckPerms.getUserManager();

    public JoinListener(Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        Account account = AccountProvider.getAccount(player.getUniqueId());
        if (!account.getProperty("discord", "none").equalsIgnoreCase("none")) {
            main.getProxy().getScheduler().runAsync(main, () -> {
                BungeeAddon addon = BungeeAddon.getInstance();
                Guild guild = addon.getBot().getJda().getGuildById("819973375903531009");
                Member member = guild.retrieveMemberById(account.getProperty("discord")).complete();
                Role vip = guild.getRoleById("819975366570541147");
                Role hero = guild.getRoleById("889479964137750589");

                User user = userManager.getUser(player.getUniqueId());

                Set<String> groups = user.getNodes().stream()
                        .filter(NodeType.INHERITANCE::matches)
                        .map(NodeType.INHERITANCE::cast)
                        .map(InheritanceNode::getGroupName)
                        .collect(Collectors.toSet());
                if (groups.contains("hero")) {
                    if (!member.getRoles().contains(hero)) {
                        guild.addRoleToMember(member, hero).queue();
                    }
                } else {
                    if (member.getRoles().contains(hero)) {
                        guild.removeRoleFromMember(member, hero).queue();
                    }
                }
                if (groups.contains("vip")) {
                    if (!member.getRoles().contains(vip)) {
                        guild.addRoleToMember(member, vip).queue();
                    }
                } else {
                    if (member.getRoles().contains(vip)) {
                        guild.removeRoleFromMember(member, vip).queue();
                    }
                }
            });
        }
    }
}
