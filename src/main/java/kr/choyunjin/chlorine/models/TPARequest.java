package kr.choyunjin.chlorine.models;

import java.util.UUID;
import java.time.LocalDateTime;

public class TPARequest {
    private final UUID sender;
    private final UUID receiver;
    private boolean isTpahere;
    private LocalDateTime issuedAt;
    
    public TPARequest(UUID sender, UUID receiver, boolean isTpahere) {
        this.sender = sender;
        this.receiver = receiver;
        this.isTpahere = isTpahere;
        this.issuedAt = LocalDateTime.now();
    }

    public UUID sender() {
        return this.sender;
    }

    public UUID receiver() {
        return this.receiver;
    }

    public boolean isTpahere() {
        return this.isTpahere;
    }

    public void isTpahere(boolean val) {
        this.isTpahere = val;
    }

    public LocalDateTime issuedAt() {
        return this.issuedAt;
    }

    public void refresh() {
        this.issuedAt = LocalDateTime.now();
    }
}