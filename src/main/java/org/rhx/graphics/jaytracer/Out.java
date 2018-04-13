package org.rhx.graphics.jaytracer;

public class Out<T> {
    private T ref;

    private Out(T ref) {
        this.ref = ref;
    }

    public static <T> Out<T> of (T ref) {
        return new Out<>(ref);
    }

    public static <T> Out<T> empty() {
        return new Out<>(null);
    }

    public T get() {
        return ref;
    }

    public void set(T ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return String.format("&%s", ref);
    }
}
