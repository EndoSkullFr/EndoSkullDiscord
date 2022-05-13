package fr.endoskull.discord.utils;

import fr.endoskull.api.BungeeMain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SQLManager {

    public static List<String> getMinecraftAccountsByDiscordId(String id) {
        List<String> result = new ArrayList<>();
        BungeeMain.getInstance().getMySQL().query("SELECT name FROM accounts WHERE discord='" + id + "'", rs -> {
            try {
                while (rs.next()) {
                    result.add(rs.getString("name"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        return result;
    }

    public static String getDiscordIdByMinecraftAccount(String id) {
        AtomicReference<String> result = new AtomicReference<>("");
        BungeeMain.getInstance().getMySQL().query("SELECT discord FROM accounts WHERE name='" + id + "'", rs -> {
            try {
                while (rs.next()) {
                    result.set(rs.getString("discord"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        return result.get();
    }
}
