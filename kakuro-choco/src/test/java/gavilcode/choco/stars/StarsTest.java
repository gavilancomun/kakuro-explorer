package gavilcode.choco.stars;

import static java.util.Arrays.asList;
import java.util.List;
import org.junit.Test;

public class StarsTest {

List<String> stars1 = asList(
        "-------------------------------------",
        "|               |               |   |",
        "|   .---.---.---.   .---.---.---.   |",
        "|   |               |           |   |",
        "|---.---.   .   .   .   .   .---.   |",
        "|       |           |       |       |",
        "|   .   .---.   .   .   .   .   .---|",
        "|           |       |       |   |   |",
        "|   .   .   .---.---.   .   .   .   |",
        "|           |       |       |   |   |",
        "|   .   .   .---.   .   .---.---.   |",
        "|               |   |   |           |",
        "|---.---.---.   .   .---.   .   .   |",
        "|           |   |   |   |           |",
        "|   .---.---.   .---.   .---.   .   |",
        "|   |           |           |       |",
        "|   .---.---.---.   .   .   .   .   |",
        "|               |           |       |",
        "-------------------------------------");

//(deftest stars-test
//  (let [grid (sb/grid-from-string stars1)]
//    (->> grid
//         stars/solve-draw
//pp/pprint)))

@Test
public void starsTest() {
  var gb = new GridBuilder();
  var grid = gb.gridFromString(stars1);
  stars1.forEach(System.out::println);
  gb.solve();
}
}
