package fr.endoskull.discord.utils;

import fr.endoskull.api.data.sql.MySQL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SQLManager {

    public static List<String> getMinecraftAccountsByDiscordId(String id) {
        List<String> result = new ArrayList<>();
        MySQL.getInstance().query("SELECT `name` FROM `accounts` WHERE `uuid`=(SELECT `uuid` FROM `properties` WHERE `key`='discord' AND `value`='" + id + "' LIMIT 1)", rs -> {
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
        MySQL.getInstance().query("SELECT `value` FROM `properties` WHERE `key`='discord' AND`uuid`=(SELECT `uuid` FROM `accounts` WHERE `name`='" + id + "' LIMIT 1);", rs -> {
            try {
                while (rs.next()) {
                    result.set(rs.getString("value"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        return result.get();
    }
}