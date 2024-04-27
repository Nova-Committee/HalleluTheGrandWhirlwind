package committee.nova.hallelugw.util;

import java.util.Optional;
import java.util.function.Consumer;

public class Utilities {
    public static <T> Optional<T> toOptional(T instance) {
        return Optional.ofNullable(instance);
    }

    public static <T> void modifyIfPresent(T instance, Consumer<T> mod) {
        toOptional(instance).ifPresent(mod);
    }
}
