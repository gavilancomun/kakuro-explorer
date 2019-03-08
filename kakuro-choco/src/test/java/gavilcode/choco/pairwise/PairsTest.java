package gavilcode.choco.pairwise;

import com.oracle.tools.packager.Platform;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Random;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.junit.Test;

public class PairsTest {

@Test
public void RandomPairsCoverage() {
  var prop1 = asList("0a", "0b");
  var prop2 = asList("1a", "1b", "1c", "1d");
  var prop3 = asList("2a", "2b", "2c");
  var properties = asList(prop1, prop2, prop3);
  var pairs = new Pairs(properties);
  var allPairs = pairs.allPairs;
  Debug.WriteLine("all count " + allPairs.size());
  var rand = new Random();
  var allPairsCount = allPairs.size();
  for (int i = 0; i < 1000; ++i) {
    var count = 0;
    pairs.Reset();
    while (!pairs.Validate()) {
      ++count;
      var pk = allPairs.get(rand.nextInt(allPairsCount));
      pairs.Count(pk);
    }
    Debug.WriteLine(count);
  }
}

@Test
public void RandomRowsCoverage() {
  var prop1 = asList("0a", "0b");
  var prop2 = asList("1a", "1b", "1c", "1d");
  var prop3 = asList("2a", "2b", "2c");
  var properties = asList(prop1, prop2, prop3);
  var pairs = new Pairs(properties);
  var allPairs = pairs.allPairs;
  Debug.WriteLine("all count " + allPairs.size());
  for (int i = 0; i < 1000; ++i) {
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
    Debug.WriteLine(count);
  }
}

@Test
public void RandomRowsCounts() {
  var prop1 = asList("0a", "0b");
  var prop2 = asList("1a", "1b", "1c", "1d");
  var prop3 = asList("2a", "2b", "2c");
  var properties = asList(prop1, prop2, prop3);
  var pairs = new Pairs(properties);
  var allPairs = pairs.allPairs;
  Debug.WriteLine("all count " + allPairs.size());
  Debug.WriteLine("Amount, Count");
  var results = Utils.CountRandoms(pairs, 1000);
  for (var k : results.keySet()) {
    Debug.WriteLine(k + ", " + results.get(k));
  }
}

@Test
public void Solve2Pairs() {
  Debug.WriteLine("Solve2Pairs");
  var prop1 = asList("0a", "0b");
  var prop2 = asList("1a", "1b", "1c", "1d");
  var prop3 = asList("2a", "2b", "2c");
  var properties = asList(prop1, prop2);
  var pairs = new Pairs(properties);
  var allPairs = pairs.allPairs;
  Debug.WriteLine("all count " + allPairs.size());
  Debug.WriteLine("Amount, Count");
  var results = pairs.Solve();
  for (var row : results) {
    Debug.WriteLine(String.join(", ", row));
  }
}

@Test
public void Solve3Pairs() {
  Debug.WriteLine("Solve3Pairs");
  var prop1 = asList("0a", "0b");
  var prop2 = asList("1a", "1b");
  var prop3 = asList("2a", "2b", "2c", "2d", "2e");
  var properties = asList(prop1, prop2, prop3);
  var pairs = new Pairs(properties);
  var allPairs = pairs.allPairs;
  Debug.WriteLine("all count " + allPairs.size());
  Debug.WriteLine("Amount, Count");
  var results = pairs.Solve();
  for (var row : results) {
    Debug.WriteLine(String.join(", ", row));
  }
}

@Test
public void IPOPaperExample() {
  var prop1 = asList("0a", "0b");
  var prop2 = asList("1a", "1b");
  var prop3 = asList("2a", "2b", "2c");
  var properties = asList(prop1, prop2, prop3);
  var pairs = new Pairs(properties);
  var allPairs = pairs.allPairs;
  Debug.WriteLine("all count " + allPairs.size());
  Debug.WriteLine("Amount, Count");
  var results = pairs.Solve();
  for (var row : results) {
    Debug.WriteLine(String.join(", ", row));
  }
}

@Test
public void ClassicAlgorithmPaperExample() {
  Debug.WriteLine("ClassicAlgorithmPaperExample");
  var prop1 = asList("0a", "0b", "0c");
  var prop2 = asList("1a", "1b", "1c");
  var prop3 = asList("2a", "2b");
  var properties = asList(prop1, prop2, prop3);
  var pairs = new Pairs(properties);
  var allPairs = pairs.allPairs;
  Debug.WriteLine("all count " + allPairs.size());
  Debug.WriteLine("Amount, Count");
  var results = pairs.Solve();
  for (var row : results) {
    Debug.WriteLine(String.join(", ", row));
  }
}

@Test
public void Solve4Pairs() {
  Debug.WriteLine("Solve4Pairs");
  var prop1 = asList("0a", "0b");
  var prop2 = asList("1a", "1b");
  var prop3 = asList("2a", "2b", "2c", "2d", "2e");
  var prop4 = asList("3a", "3b", "3c");
  var properties = asList(prop1, prop2, prop3, prop4);
  var pairs = new Pairs(properties);
  var allPairs = pairs.allPairs;
  Debug.WriteLine("all count " + allPairs.size());
  Debug.WriteLine("Amount, Count");
  var results = pairs.Solve();
  for (var row : results) {
    Debug.WriteLine(String.join(", ", row));
  }
}

@Test
public void testCombinations() {
  var paramLengths = new Integer[]{2, 2, 3};
  var numberOfParams = paramLengths.length;

  var model = new Model();
  var vs = model.intVarArray("vs", 2, 0, numberOfParams - 1);
  model.arithm(vs[0], "<", vs[1]).post();
  var solver = model.getSolver();
  int count = 0;
  while (solver.solve()) {
    ++count;
    System.out.println(count + ": " + vs[0].intVar() + ", " + vs[1].intVar());
  }
  var model2 = new Model();
  var params = new IntVar[numberOfParams];
  int pos = 0;
  for (var len : paramLengths) {
    params[pos] = model2.intVar(0, len - 1);
    ++pos;
  }
  var solver2 = model2.getSolver();
  count = 0;
  while (solver2.solve()) {
    ++count;
    System.out.print(count + ":");
    for (var v : params) {
      System.out.print(" " + v.intVar());
    }
    System.out.println();
  }
}

@Test
public void testElements() {
  var paramLengths = new Integer[]{2, 2, 3, 5, 6, 7};
  var min = Utils.getMinimum(paramLengths);
  for (int numberOfRows = min; numberOfRows < min * 2; ++numberOfRows) {
    var model = new Model();
    Utils.pairs(model, paramLengths, numberOfRows);
    var solver = model.getSolver();
    solver.showStatistics();
    var solution = solver.findSolution();
    if (solution != null) {
      System.out.println(solution.toString());
      solution = solver.findSolution();
      break;
    }
  }

}
}
