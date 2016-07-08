package com.flowlikeariver.kakuro.streams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

public class FlattenCollector<T> implements Collector<Object, List<Object>, List<T>> {

@Override
public Supplier<List<Object>> supplier() {
  return ArrayList::new;
}

@Override
public BiConsumer<List<Object>, Object> accumulator() {
  return (list, item) -> {
    if (item instanceof Stream) {
      list.addAll(((Stream<Object>) item).collect(new FlattenCollector<>()));
    }
    else if (item instanceof Collection) {
      list.addAll(((Collection<Object>) item).stream().collect(new FlattenCollector<>()));
    }
    else {
      list.add(item);
    }
  };
}

@Override
public BinaryOperator<List<Object>> combiner() {
  return (list1, list2) -> {
    list1.addAll(list2);
    return list1;
  };
}

@Override
public Function<List<Object>, List<T>> finisher() {
  return acc -> acc.stream().map(t -> (T) t).collect(toList());
}

@Override
public Set<Characteristics> characteristics() {
  return Collections.unmodifiableSet(EnumSet.of(CONCURRENT));
}

}
