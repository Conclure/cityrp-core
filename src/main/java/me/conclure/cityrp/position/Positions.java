package me.conclure.cityrp.position;

import me.conclure.cityrp.registry.Key.Id;

public interface Positions {
    @Id("spawn")
    Position SPAWN = new Position();
}
