package me.conclure.cityrp.common.model.user;

import me.conclure.cityrp.common.model.DelegatingPlayerSender;
import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.SenderTranformer;
import me.conclure.cityrp.common.utility.PlayerObtainer;

import java.util.UUID;

public abstract class AbstractUser
        extends DelegatingPlayerSender
        implements User {
    private final UUID uniqueId;
    private String name;
    private int characterAmount, maxCharacterAmount;

    protected AbstractUser(
            UUID uniqueId
    ) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public int getCharacterAmount() {
        return this.characterAmount;
    }

    @Override
    public void setCharacterAmount(int characterAmount) {
        this.characterAmount = characterAmount;
    }

    @Override
    public int getMaxCharacterAmount() {
        return this.maxCharacterAmount;
    }

    @Override
    public void setMaxCharacterAmount(int maxCharacterAmount) {
        this.maxCharacterAmount = maxCharacterAmount;
    }
}
