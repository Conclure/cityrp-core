package me.conclure.cityrp.utility;

public abstract class Unconstructable {
    protected Unconstructable() {
        throw new AssertionError("This class cannot be instantiated!");
    }
}
