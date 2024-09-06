package kr.choyunjin.chlorine.models;

import java.util.UUID;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerMap {
    private HashMap<UUID, ChlorinePlayer> cache;

    public PlayerMap() {
        this.cache = new HashMap<>();
    }

    public ChlorinePlayer getPlayer(String name) {
        UUID uuid = Bukkit.getPlayerUniqueId(name);
        return this.getPlayer(uuid);
    }

    public ChlorinePlayer getPlayer(Player player) {
        return this.getPlayer(player.getUniqueId());
    }

    public ChlorinePlayer getPlayer(UUID uuid) {
        if (uuid == null) return null;

        ChlorinePlayer player = this.cache.get(uuid);
        if (player == null) {
            Player bukkitPlayer = Bukkit.getPlayer(uuid);
            if (bukkitPlayer == null) {
                return null;
            }
            player = new ChlorinePlayer(bukkitPlayer);
            this.cache.put(uuid, player);
        }
        return player;
    }
}