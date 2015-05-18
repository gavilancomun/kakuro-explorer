package com.flowlikeariver.kakuro;

import com.flowlikeariver.kakuro.cell.ValueCell;
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

public class WhileValueCell implements Collector<Cell, List<ValueCell>, List<ValueCell>> {

AtomicBoolean done = new AtomicBoolean(false);

@Override
public Supplier<List<ValueCell>> supplier() {
  return ArrayList::new;
}

@Override
public BiConsumer<List<ValueCell>, Cell> accumulator() {
  return (list, item) -> {
    if (!done.get() && (item instanceof ValueCell)) {
      list.add((ValueCell) item);
    }
    else {
      done.set(true);
    }
  };
}

@Override
public BinaryOperator<List<ValueCell>> combiner() {
  return (list1, list2) -> {
    list1.addAll(list2);
    return list1;
  };
}

@Override
public Function<List<ValueCell>, List<ValueCell>> finisher() {
  return acc -> acc;
}

@Override
public Set<Characteristics> characteristics() {
  return Collections.EMPTY_SET;
}

}
