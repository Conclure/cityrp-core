package me.conclure.cityrp.position;

import org.bukkit.World;

public class ImmutableLocationBuilder {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private World world;

    public ImmutableLocationBuilder x(double x) {
        this.x = x;
        return this;
    }

    public ImmutableLocationBuilder y(double y) {
        this.y = y;
        return this;
    }

    public ImmutableLocationBuilder z(double z) {
        this.z = z;
        return this;
    }

    public ImmutableLocationBuilder yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public ImmutableLocationBuilder pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public ImmutableLocationBuilder world(World world) {
        this.world = world;
        return this;
    }

    public ImmutableLocation build() {
        return new ImmutableLocation(this.x, this.y, this.z, this.yaw, this.pitch, this.world);
    }
}
