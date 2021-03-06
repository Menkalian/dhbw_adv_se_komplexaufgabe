package dhbw.ase.app3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ParameterRange<T> implements Iterable<T> {
    private final T min;
    private final T max;
    private final List<T> values = new ArrayList<>();

    public ParameterRange(T min, T max, Function<T, T> step) {
        this.min = min;
        this.max = max;
        T obj = min;
        values.add(obj);

        while (!Objects.equals(obj, max)) {
            obj = step.apply(obj);
            values.add(obj);
        }
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    @Override
    public Iterator<T> iterator() {
        return new ParameterRangeIterator();
    }

    private class ParameterRangeIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex >= 0 && currentIndex < values.size();
        }

        @Override
        public T next() {
            return values.get(currentIndex++);
        }
    }
}
