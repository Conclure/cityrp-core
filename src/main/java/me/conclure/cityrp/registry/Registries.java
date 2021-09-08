package me.conclure.cityrp.registry;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.rarity.Rarity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public interface Registries {
    @SuppressWarnings("rawtypes")
    Registry<Registry> TOP_REGISTRY = new SimpleRegistry<>(Registry.class);
    @Key.Id("rarity")
    Registry<Rarity> RARITY_REGISTRY = new SimpleRegistry<>(Rarity.class);

    static <E> void registerReflectively(Class<?> clazz, Registry<E> registry) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (!field.getType().isAssignableFrom(registry.getBaseType())) {
                continue;
            }

            if (!field.isAnnotationPresent(Key.Id.class)) {
                continue;
            }

            Key.Id id = field.getAnnotation(Key.Id.class);

            try {
                registry.register(Key.of(id.value()),registry.getBaseType().cast(field.get(null)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
