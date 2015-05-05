package com.flowlikeariver.kakuro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class WhileCollector implements Collector<Cell, List<EmptyCell>, List<EmptyCell>> {

AtomicBoolean done = new AtomicBoolean(false);

@Override
public Supplier<List<EmptyCell>> supplier() {
  return ArrayList::new;
}

@Override
public BiConsumer<List<EmptyCell>, Cell> accumulator() {
  return (list, item) -> {
    if (!done.get() && (item instanceof EmptyCell)) {
      list.add((EmptyCell) item);
    }
    else {
      done.set(true);
    }
  };
}

@Override
public BinaryOperator<List<EmptyCell>> combiner() {
  return (list1, list2) -> {
    list1.addAll(list2);
    return list1;
  };
}

@Override
public Function<List<EmptyCell>, List<EmptyCell>> finisher() {
  return acc -> {
    System.out.println("finisher: " + acc.size());
    return acc;
  };
}

@Override
public Set<Characteristics> characteristics() {
  return Collections.EMPTY_SET;
}

}
