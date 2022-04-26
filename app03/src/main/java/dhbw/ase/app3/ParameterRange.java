package dhbw.ase.app3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class ParameterRange<T> implements Iterator<T>, Iterable<T> {
    private final List<T> values = new ArrayList<>();
    private int currentIndex = 0;

    public ParameterRange(T min, T max, Function<T, T> step) {
        T obj = min;
        values.add(obj);

        while (obj != max) {
            obj = step.apply(obj);
            values.add(obj);
        }
    }

    @Override
    public boolean hasNext() {
        return currentIndex > 0 && currentIndex < values.size();
    }

    @Override
    public T next() {
        return values.get(currentIndex++);
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }
}
