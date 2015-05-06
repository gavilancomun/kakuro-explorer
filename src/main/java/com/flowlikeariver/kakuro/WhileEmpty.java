package com.flowlikeariver.kakuro;

import com.flowlikeariver.kakuro.cell.EmptyCell;
import com.flowlikeariver.kakuro.cell.Cell;
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

public class WhileEmpty implements Collector<Cell, List<EmptyCell>, List<EmptyCell>> {

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
  return acc -> acc;
}

@Override
public Set<Characteristics> characteristics() {
  return Collections.EMPTY_SET;
}

}
