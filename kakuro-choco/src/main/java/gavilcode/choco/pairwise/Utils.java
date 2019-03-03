package gavilcode.choco.pairwise;

import java.util.Map;
import java.util.TreeMap;

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
}
