package me.conclure.cityrp.registry;

import me.conclure.cityrp.item.Item;
import me.conclure.cityrp.item.rarity.Rarity;
import me.conclure.cityrp.registry.Registry.RegistryContext;
import me.conclure.cityrp.utility.MoreFunctions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

public class Registries {
    public static final Registry<Registry, RegistryContext<? extends Registry>> REGISTRIES;
    public static final Registry<Rarity,RegistryContext<? extends Rarity>> RARITIES;
    public static final Registry<Item,RegistryContext<? extends Item>> ITEMS;

    static {
        REGISTRIES = new Registry<>(Registry.class);
        RARITIES = new Registry<>(Rarity.class);
        ITEMS = new Registry<>(Item.class);
    }

    public static <E> void registerReflectively(Class<?> clazz, Registry<E,RegistryContext<? extends E>> registry) {
        Registries.registerReflectively(clazz,registry, MoreFunctions.emptyConsumer());
    }

    public static <E> void registerReflectively(
            Class<?> clazz,
            Registry<E,RegistryContext<? extends E>> registry,
            Consumer<Registry.RegistrationsResult<E>> resultConsumer
    ) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (!registry.getBaseType().isAssignableFrom(field.getType())) {
                continue;
            }

            if (!field.isAnnotationPresent(Key.Id.class)) {
                continue;
            }

            Key.Id id = field.getAnnotation(Key.Id.class);
            E value;

            try {
                value = (E) field.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            Registry.RegistrationsResult<E> result = registry.register(new RegistryContext<>(value, Key.of(id.value())));
            resultConsumer.accept(result);
        }
    }
}
