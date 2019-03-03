package gavilcode.choco.pairwise;

import java.util.List;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import static java.util.stream.Collectors.toList;

public class Pairs {

private final Random random = new Random();
public Map<PairKey, Integer> counts = new TreeMap<>();
public List<List<String>> properties;

public List<PairKey> allPairs;

public Pairs(List<List<String>> properties) {
  this.properties = properties;
  allPairs = AllPairs();
}

// all pairs of all properties
private List<PairKey> AllPairs() {
  var result = new ArrayList<PairKey>();
  var propertyCount = properties.size();
  for (int i = 0; i < propertyCount - 1; ++i) {
    var prop1 = properties.get(i);
    for (int j = i + 1; j < propertyCount; ++j) {
      var prop2 = properties.get(j);
      result.addAll(AllPairs(i, prop1, j, prop2));
    }
  }
  return result;
}

// all pairs of two properties
private static List<PairKey> AllPairs(int i, List<String> prop1, int j, List<String> prop2) {
  var results = new ArrayList<PairKey>();
  for (int x = 0; x < prop1.size(); ++x) {
    for (int y = 0; y < prop2.size(); ++y) {
      var pk = new PairKey();
      pk.Property1Id = i;
      pk.Property1Value = prop1.get(x);
      pk.Property2Id = j;
      pk.Property2Value = prop2.get(y);
      results.add(pk);
    }
  }
  return results;
}

// all pairs of the first two properties
private static List<PairKey> AllPairs(List<String> prop1, List<String> prop2) {
  return AllPairs(0, prop1, 1, prop2);
}

public static void Dump(List<PairKey> pairKeys) {
  for (var pk : pairKeys) {
    System.err.println(pk);
  }
}

public void DumpCounts() {
  for (var k : allPairs) {
    System.err.println(k + " " + GetCount(k));
  }
}

public int Count(PairKey pk) {
  if (counts.containsKey(pk)) {
    counts.put(pk, counts.get(pk) + 1);
  }
  else {
    counts.put(pk, 1);
  }
  return counts.get(pk);
}

public int GetCount(PairKey pk) {
  if (counts.containsKey(pk)) {
    return counts.get(pk);
  }
  else {
    counts.put(pk, 0);
    return 0;
  }
}

public boolean Validate() {
  return allPairs.stream().allMatch(k -> GetCount(k) > 0);
}

public void Reset() {
  counts.clear();
}

// random value for each property
public List<String> RandomRow() {
  var result = properties.stream()
          .map(p -> p.get(random.nextInt(p.size())))
          .collect(toList());
  return result;
}

// All pairs from one row
public List<PairKey> GetRowPairs(List<String> row) {
  var result = new ArrayList<PairKey>();
  for (int i = 0; i < row.size() - 1; ++i) {
    for (int j = i + 1; j < row.size(); ++j) {
      var pk = new PairKey();
      pk.Property1Id = i;
      pk.Property1Value = row.get(i);
      pk.Property2Id = j;
      pk.Property2Value = row.get(j);
      result.add(pk);
    }
  }
  return result;
}

public List<List<String>> Solve() {
  var results = new ArrayList<List<String>>();
  if (properties.size() > 1) {
    var pairedRows = new ArrayList<List<String>>();
    var firstPairs = AllPairs(properties.get(0), properties.get(1));
    for (var pk : firstPairs) {
      var row = new ArrayList<String>(asList(pk.Property1Value, pk.Property2Value));
      pairedRows.add(row);
    }
    var currentTests = new ArrayList<List<String>>(pairedRows);
    if (properties.size() > 2) {
      var outstanding = new HashSet<PairKey>(allPairs);
      Debug.WriteLine("initial outstanding " + outstanding.size());
      for (var row : currentTests) {
        for (var pk : GetRowPairs(row)) {
          outstanding.remove(pk);
        }
      }
      Debug.WriteLine("after first 2 pairs " + outstanding.size());
      for (int i = 2; i < properties.size(); ++i) {
        var nextProp = properties.get(i);
        var q = nextProp.size();
        if (currentTests.size() <= q) {
          for (int j = 0; j < currentTests.size(); ++j) {
            var currentTest = currentTests.get(j);
            currentTest.add(nextProp.get(j));
            var rowPairs = GetRowPairs(currentTest);
            for (var pk : rowPairs) {
              outstanding.remove(pk);
            }
            Debug.WriteLine("after next prop " + i + " " + j + " " + outstanding.size());
          }
        }
        else {
          for (int j = 0; j < q; ++j) {
            var currentTest = currentTests.get(j);
            currentTest.add(nextProp.get(j));
            var rowPairs = GetRowPairs(currentTest);
            for (var pk : rowPairs) {
              outstanding.remove(pk);
            }
            Debug.WriteLine("after first q next prop " + i + " " + j + " " + outstanding.size());
          }
          for (int j = q; j < currentTests.size(); ++j) {
            var bestRow = new ArrayList<String>();
            int bestTestPairsCount = -1;
            String bestValue = "-";
            var currentTest = currentTests.get(j);
            for (int k = 0; k < nextProp.size(); ++k) {
              var testRow = new ArrayList<String>(currentTest);
              var propValue = nextProp.get(k);
              testRow.add(propValue);
              var testPairs = GetRowPairs(testRow);
              var newPairsCount = (int) testPairs.stream().filter(p -> outstanding.contains(p)).count();
              if (newPairsCount > bestTestPairsCount) {
                bestRow = testRow;
                bestTestPairsCount = newPairsCount;
                bestValue = propValue;
              }
              Debug.WriteLine("test row " + String.join(", ", testRow) + " count " + newPairsCount);
            }
            currentTest.add(bestValue);
            var bestPairs = GetRowPairs(bestRow);
            for (var pk : bestPairs) {
              outstanding.remove(pk);
            }
            Debug.WriteLine("after best fit q next prop " + i + " " + j + " " + outstanding.size());
          }
        }
        final var id = i;
        var needsVertical = outstanding.stream().anyMatch(pk -> pk.Property2Id == id);
        if (needsVertical) {
          var tPrime = new ArrayList<List<String>>();
          for (var pk : outstanding) {
            if (pk.Property2Id == i) {
              Debug.WriteLine("outstanding " + pk);
            }
          }
          for (var pk : outstanding) {
            if (pk.Property2Id != i) {
              Debug.WriteLine("other outstanding " + pk);
            }
          }
          for (var pk : outstanding) {
            if (pk.Property2Id == i) {
              var done = false;
              for (var r : tPrime) {
                if (("-".equals(r.get(pk.Property1Id))) && (r.get(pk.Property2Id).equals(pk.Property2Value))) {
                  r.add(pk.Property1Id, pk.Property1Value);
                  done = true;
                }
              }
              if (!done) {
                var newTest = new ArrayList<String>();
                for (int j = 1; j <= i + 1; ++j) {
                  newTest.add("-");
                }
                newTest.add(pk.Property1Id, pk.Property1Value);
                newTest.add(pk.Property2Id, pk.Property2Value);
                tPrime.add(newTest);
              }
            }
          }
          for (var r : tPrime) {
            var pairs = GetRowPairs(r);
            for (var pk : pairs) {
              outstanding.remove(pk);
            }
            currentTests.add(r);
            Debug.WriteLine("tprime " + String.join(", ", r) + " outstanding " + outstanding.size());
          }
        }
      }
      for (var r : currentTests) {
        Debug.WriteLine("Test " + String.join(", ", r));
      }
      return currentTests;
    }
    else {
      return currentTests;
    }
  }
  return results;
}
}
