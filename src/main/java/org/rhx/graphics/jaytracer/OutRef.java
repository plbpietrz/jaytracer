package org.rhx.graphics.jaytracer;

public class OutRef<T> {
    private T ref;

    private OutRef(T ref) {
        this.ref = ref;
    }

    public static <T> OutRef<T> of (T ref) {
        return new OutRef<>(ref);
    }

    public static <T> OutRef<T> empty() {
        return new OutRef<>(null);
    }

    public T get() {
        return ref;
    }

    public void set(T ref) {
        this.ref = ref;
    }
}
