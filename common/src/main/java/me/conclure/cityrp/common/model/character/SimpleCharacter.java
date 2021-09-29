package me.conclure.cityrp.common.model.character;

import me.conclure.cityrp.common.model.DelegatingPlayerSender;
import me.conclure.cityrp.common.model.user.User;
import me.conclure.cityrp.common.model.user.UserRepository;
import org.jspecify.nullness.Nullable;

import java.util.UUID;

public class SimpleCharacter<SS> extends DelegatingPlayerSender<User<SS>,SS> implements Character<SS> {
    private final UserRepository<SS> userRepository;
    private final UUID ownerUniqueId;
    private final UUID uniqueId;
    @Nullable
    private String name;

    public SimpleCharacter(UserRepository<SS> userRepository, UUID ownerUniqueId, UUID uniqueId) {
        this.userRepository = userRepository;
        this.ownerUniqueId = ownerUniqueId;
        this.uniqueId = uniqueId;
    }

    @Override
    public User<SS> getOwner() {
        return this.userRepository.getOrCreate(this.ownerUniqueId);
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
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
    protected User<SS> getDelegate() {
        return this.getOwner();
    }
}
