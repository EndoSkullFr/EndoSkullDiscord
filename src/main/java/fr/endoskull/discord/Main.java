package fr.endoskull.discord;

import fr.endoskull.discord.commands.LinkDiscordCommand;
import fr.endoskull.discord.discord.BungeeAddon;
import fr.endoskull.discord.listeners.JoinListener;
import fr.endoskull.discord.utils.UpdateMembersTask;
import net.md_5.bungee.api.plugin.Plugin;
import org.spicord.SpicordLoader;

import java.util.concurrent.TimeUnit;

public class Main extends Plugin {
    private static Main instance;
    public BungeeAddon addon;

    public void onEnable() {
        instance = this;
        addon = new BungeeAddon(this);
        SpicordLoader.addStartupListener(spicord -> {
            spicord.getAddonManager().registerAddon(addon);
            getProxy().getPluginManager().registerCommand(this, new LinkDiscordCommand(this));
            getProxy().getPluginManager().registerListener(this, new JoinListener(this));
            getProxy().getScheduler().schedule(this, () -> {
                getProxy().getScheduler().runAsync(this, new UpdateMembersTask());
            }, 5, 30, TimeUnit.SECONDS);
        });
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Main getInstance() {
        return instance;
    }
}
