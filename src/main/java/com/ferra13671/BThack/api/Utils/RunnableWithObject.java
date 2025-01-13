package com.ferra13671.BThack.api.Utils;

/**
 * @Deprecated in the next update, the use of this class will be replaced by {@link java.util.function.Consumer}
 *
 * @see java.util.function.Consumer
 */
@Deprecated(forRemoval = true)
public interface RunnableWithObject<T> {
    void run(T object);
}
