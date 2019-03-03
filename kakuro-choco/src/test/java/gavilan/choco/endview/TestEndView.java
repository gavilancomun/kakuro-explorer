package gavilan.choco.endview;

import static java.util.Arrays.asList;
import org.junit.Test;

public class TestEndView {

@Test
public void testEndView1() {
  var gridPic = asList(
          "  DCA  ",
          " ..... ",
          "B.....D",
          " ..... ",
          " .....D",
          " .....A",
          "  A    "
  );
  new EndView().solve(gridPic);
}

@Test
public void testEndView2() {
  var gridPic2 = asList(
          "  AD D  ",
          "D...... ",
          " ...... ",
          " ......C",
          " ...... ",
          " ......B",
          " ...... ",
          " C CC   "
  );
  new EndView().solve(gridPic2);
}

@Test
public void testEndView3() {
  var gridPic3 = asList(
          "  C     ",
          " ......C",
          " ......A",
          "B...... ",
          " ......A",
          " ......A",
          " ...... ",
          " B   A  "
  );
  new EndView().solve(gridPic3);
}

}
