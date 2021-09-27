package me.conclure.cityrp.common.position;

import me.conclure.cityrp.common.utility.Key;
import org.jspecify.nullness.Nullable;

public class PositionInfo {
    @Nullable
    private final String permission;

    private PositionInfo(String permission) {
        this.permission = permission;
    }

    @Nullable
    public String getPermission() {
        return this.permission;
    }

    public static class Builder {
        @Nullable
        private String permission;

        public Builder permission(@Nullable String permission) {
            this.permission = permission;
            return this;
        }

        public PositionInfo build() {
            return new PositionInfo(this.permission);
        }
    }
}
