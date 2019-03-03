package gavilcode.choco.pairwise;

import static java.util.Arrays.asList;
import java.util.Random;
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
}

