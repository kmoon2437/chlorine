package kr.choyunjin.chlorine.models;

import java.util.UUID;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

public class ChlorinePlayer {
    // 원본 player 데이터
    private Player player;
    
    // 이 플레이어가 받은 모든 tpa/tpahere 요청 데이터
    // 요청을 처리하고 나면 데이터는 여기서 지워짐
    private LinkedHashMap<UUID, TPARequest> tpaStack;

    public ChlorinePlayer(Player player) {
        this.player = player;
        this.tpaStack = new LinkedHashMap<>();
    }

    public Component displayName() {
        return this.player.displayName();
    }

    public void displayName(Component displayName) {
        this.player.displayName(displayName);
    }

    public void sendMessage(String message) {
        this.player.sendMessage(message);
    }

    public void sendMessage(Component message) {
        this.player.sendMessage(message);
    }

    public void sendMessage(ComponentLike message) {
        this.player.sendMessage(message);
    }

    public void addTPARequest(UUID senderUUID) {
        this.addTPARequest(senderUUID, false);
    }

    public void addTPARequest(UUID senderUUID, boolean isTpahere) {
        this.tpaStack.put(senderUUID, new TPARequest(senderUUID, this.player.getUniqueId(), isTpahere));
    }

    // 가장 최근의 tpa 요청을 map에서 지우고 그걸 반환
    public TPARequest getNextTPARequest() {
        Set<UUID> uuidSet = this.tpaStack.keySet();
        Iterator<UUID> uuidSetIter = uuidSet.iterator();
        if (!uuidSetIter.hasNext()) {
            return null;
        }
        UUID uuid = null;
        while (uuidSetIter.hasNext()) {
            uuid = uuidSetIter.next();
        }
        TPARequest tpaReq = this.tpaStack.get(uuid);
        this.tpaStack.remove(uuid);
        return tpaReq;
    }

    public TPARequest getTPARequestBySenderUUID(UUID senderUUID) {
        return this.tpaStack.get(senderUUID);
    }
}