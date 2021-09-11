package me.conclure.cityrp.registry;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TypeAwareRegistry<E,C extends TypeAwareRegistry.RegistryContext<? extends E>>
        extends Registry<E,C>{
    private final Map<TypeToken<? extends E>,E> tokenMap;

    public TypeAwareRegistry(Class<E> clazz) {
        super(clazz);
        this.tokenMap = new HashMap<>();
    }

    static <E> RegistryContext<E> context(Key key, E value, TypeToken<E> typeToken) {
        return new RegistryContext<>(value,key,typeToken);
    }

    public <T extends E> T getByToken(TypeToken<T> token) {
        return (T) tokenMap.get(token);
    }

    public RegistrationsResult<E> register(C context) {
        this.tokenMap.put(context.getTypeToken(),context.getValue());
        return super.register(context);
    }

    public static class RegistryContext<E> extends Registry.RegistryContext<E> {
        final TypeToken<E> typeToken;

        RegistryContext(E value, Key key, TypeToken<E> typeToken) {
            super(value, key);
            this.typeToken = typeToken;
        }

        public TypeToken<E> getTypeToken() {
            return this.typeToken;
        }

        public Type getType() {
            return this.typeToken.getType();
        }
    }
}
