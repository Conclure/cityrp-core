package me.conclure.cityrp.common.model.position;

import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.utility.Key;

public abstract class AbstractPosition<PlatformEntity, PlatformWorld>
        implements Position<PlatformEntity, PlatformWorld> {
    private final Key key;
    private final PositionInfo info;
    private volatile LocationInfo locationInfo;

    protected AbstractPosition(Key key, PositionInfo info) {
        this.key = key;
        this.info = info;
    }

    @Override
    public PositionInfo getInfo() {
        return this.info;
    }

    @Override
    public boolean hasPermission(Sender sender) {
        String permission = this.info.getPermission();

        if (permission == null) {
            return true;
        }

        return sender.hasPermission(permission);
    }

    protected abstract String parseWorldName(PlatformWorld world);

    @Override
    public Key getKey() {
        return this.key;
    }

    @Override
    public boolean hasLocation() {
        return this.locationInfo != null;
    }

    @Override
    public double getX() {
        return this.locationInfo.x;
    }

    @Override
    public double getY() {
        return this.locationInfo.y;
    }

    @Override
    public double getZ() {
        return this.locationInfo.z;
    }

    @Override
    public String getWorldName() {
        return this.locationInfo.worldName;
    }

    @Override
    public float getYaw() {
        return this.locationInfo.yaw;
    }

    @Override
    public float getPitch() {
        return this.locationInfo.pitch;
    }

    @Override
    public void configure(double x, double y, double z, PlatformWorld world, float yaw, float pitch) {
        this.locationInfo = new LocationInfo(x, y, z, this.parseWorldName(world), yaw, pitch);
    }

    protected record LocationInfo(double x, double y, double z, String worldName, float yaw, float pitch) {
    }
}
