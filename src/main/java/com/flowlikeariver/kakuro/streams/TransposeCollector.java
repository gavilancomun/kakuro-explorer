package com.flowlikeariver.kakuro.streams;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import static java.util.stream.Collector.Characteristics.CONCURRENT;
import java.util.stream.IntStream;

public class TransposeCollector<T> implements Collector<List<T>, Map<Integer, Set<T>>, Map<Integer, Set<T>>> {

@Override
public Supplier<Map<Integer, Set<T>>> supplier() {
  return TreeMap::new;
}

@Override
public BiConsumer<Map<Integer, Set<T>>, List<T>> accumulator() {
  return (m, item) -> {
    IntStream.range(0, item.size()).forEach(i -> {
      if (!m.containsKey(i)) {
        m.put(i, new TreeSet<>());
      }
      m.get(i).add(item.get(i));
    });
  };
}

@Override
public BinaryOperator<Map<Integer, Set<T>>> combiner() {
  return (m1, m2) -> {
    m2.keySet().stream().forEach(k -> {
      if (!m1.containsKey(k)) {
        m1.put(k, new TreeSet<>());
      }
      m1.get(k).addAll(m2.get(k));
    });
    return m1;
  };
}

@Override
public Function<Map<Integer, Set<T>>, Map<Integer, Set<T>>> finisher() {
  return acc -> acc;
}

@Override
public Set<Characteristics> characteristics() {
  return Collections.unmodifiableSet(EnumSet.of(CONCURRENT));
}

}
