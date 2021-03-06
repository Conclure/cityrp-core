package me.conclure.cityrp.paper.position;

import me.conclure.cityrp.common.model.position.AbstractPositionRegistry;
import me.conclure.cityrp.common.model.position.Position;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.stream.Stream;

public class BukkitPositionRegistry extends AbstractPositionRegistry<Entity, World> {
    public BukkitPositionRegistry(Stream<Position<Entity, World>> positions) {
        super(positions);
    }

    public static class Builder {
        private final Stream.Builder<Position<Entity, World>> positions = Stream.builder();

        public Builder add(Position<Entity, World> position) {
            this.positions.add(position);
            return this;
        }

        public BukkitPositionRegistry build() {
            return new BukkitPositionRegistry(this.positions.build());
        }
    }
}
