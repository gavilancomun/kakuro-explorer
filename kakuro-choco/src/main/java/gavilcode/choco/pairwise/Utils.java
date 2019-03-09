package gavilcode.choco.pairwise;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Map;
import java.util.TreeMap;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Utils {

public static Map<Integer, Integer> CountRandoms(Pairs pairs, int n) {
  var results = new TreeMap<Integer, Integer>();
  var allPairs = pairs.allPairs;
  for (int i = 0; i < n; ++i) {
    var count = 0;
    pairs.Reset();
    while (!pairs.Validate()) {
      ++count;
      var row = pairs.RandomRow();
      var ps = pairs.GetRowPairs(row);
      for (var pk : ps) {
        pairs.Count(pk);
      }
    }
    if (results.containsKey(count)) {
      results.put(count, results.get(count) + 1);
    }
    else {
      results.put(count, 1);
    }
  }
  return results;
}

// all pairs of two properties
public static void AllPairs(Model model, Integer[] paramLengths, IntVar[][] p, int i, int j) {
  var numberOfRows = p[0].length;
  for (int x = 0; x < paramLengths[i]; ++x) {
    for (int y = 0; y < paramLengths[j]; ++y) {
      var index = model.intVar("i" + x + y, 0, numberOfRows);
      model.element(model.intVar(x), p[i], index, 0).post();
      model.element(model.intVar(y), p[j], index, 0).post();
    }
  }
}

public static void ConstrainPairs(Model model, Integer[] paramLengths, IntVar[][] p) {
  int numberOfParams = paramLengths.length;
  for (int px = 0; px < numberOfParams - 1; ++px) {
    for (int py = px + 1; py < numberOfParams; ++py) {
      AllPairs(model, paramLengths, p, px, py);
    }
  }
}

public static int getMinimum(Integer[] lengths) {
  var values = new ArrayList<Integer>(asList(lengths));
  var m1 = values.stream().mapToInt(x -> x).max();
  for (int i = 0; i < values.size(); ++i) {
    if (values.get(i) == m1.orElse(1)) {
      values.remove(i);
      break;
    }
  }
  var m2 = values.stream().mapToInt(x -> x).max();
  return m1.orElse(1) * m2.orElse(1);
}

public static IntVar[][] pairs(Model model, Integer[] paramLengths, int numberOfRows) {
  int max1pos = 0;
  int max1 = 0;
  int max2pos = 0;
  for (int i = 0; i < paramLengths.length; ++i) {
    if (paramLengths[i] >= max1) {
      max2pos = max1pos;
      max1pos = i;
      max1 = paramLengths[i];
    }
  }
  System.out.println("maxs " + max1pos + " " + max2pos);
  var numberOfParams = paramLengths.length;
  var p = new IntVar[numberOfParams][];
  for (int i = 0; i < numberOfParams; ++i) {
    var param = model.intVarArray("p" + i, numberOfRows, 0, paramLengths[i] - 1);
    p[i] = param;
  }
  int pos = 0;
  for (int i = 0; i < paramLengths[max2pos]; ++i) {
    for (int j = 0; j < paramLengths[max1pos]; ++j) {
      model.element(model.intVar(i), p[max2pos], model.intVar(pos), 0).post();
      model.element(model.intVar(j), p[max1pos], model.intVar(pos), 0).post();
      ++pos;
    }
  }
  ConstrainPairs(model, paramLengths, p);
  return p;
}
}
