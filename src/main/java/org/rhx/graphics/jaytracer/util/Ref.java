package org.rhx.graphics.jaytracer.util;

/**
 * C++ reference ;-)
 * @param <T>
 */
public class Ref<T> {
    private T ref;

    private Ref(T ref) {
        this.ref = ref;
    }

    public static <T> Ref<T> of (T ref) {
        return new Ref<>(ref);
    }

    public static <T> Ref<T> empty() {
        return new Ref<>(null);
    }

    public T get() {
        return ref;
    }

    public void set(T ref) {
        this.ref = ref;
    }

}
