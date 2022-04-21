package org.mabartos.meetmethere.api.codecs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetHolder<T> {
    private final Set<T> set;

    @SafeVarargs
    public SetHolder(final T... data) {
        this.set = new HashSet<>(Arrays.asList(data));
    }

    public SetHolder(final Set<T> set) {
        this.set = set;
    }

    public Set<T> getSet() {
        return set;
    }

    @Override
    public String toString() {
        return "SetHolder{" +
                "set=" + set +
                '}';
    }
}
