package edu.kit.iti.formal.psdbg;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (02.01.19)
 */
@Data
@AllArgsConstructor
public final class Pair<A, B> {
    private final A first;
    private final B second;

    public A getKey() {
        return first;
    }

    public B getValue() {
        return second;
    }
}
