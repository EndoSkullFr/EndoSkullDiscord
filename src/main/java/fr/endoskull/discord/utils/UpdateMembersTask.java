package fr.endoskull.discord.utils;

import fr.endoskull.api.data.sql.MySQL;
import fr.endoskull.discord.Main;
import fr.endoskull.discord.discord.BungeeAddon;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

public class UpdateMembersTask implements Runnable {
    @Override
    public void run() {
        BungeeAddon addon = BungeeAddon.getInstance();
        Guild guild = addon.getBot().getJda().getGuildById("819973375903531009");

        guild.retrieveMetaData()
                .map(Guild.MetaData::getApproximateMembers)
                .queue(integer -> MySQL.getInstance().update("INSERT INTO `discord_count`(`members`) VALUES ('" + integer + "')"));

        //final int memberCount = guild.getMemberCount();
        //MySQL.getInstance().update("INSERT INTO `discord_count`(`members`) VALUES ('" + memberCount + "')");
    }
}
