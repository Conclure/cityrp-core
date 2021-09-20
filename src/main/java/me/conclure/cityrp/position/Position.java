package me.conclure.cityrp.position;

public class Position {
    private ImmutableLocation location;

    public boolean isValid() {
        if (!this.hasLocation()) {
            return false;
        }
        return this.location.isValid();
    }

    public boolean hasLocation() {
        return this.location != null;
    }

    public void setLocation(ImmutableLocation location) {
        this.location = location;
    }

    public ImmutableLocation getLocation() {
        return this.location;
    }
}
