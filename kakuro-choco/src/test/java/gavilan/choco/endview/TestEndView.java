/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gavilan.choco.endview;

import static java.util.Arrays.asList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author sgf
 */
public class TestEndView {
  
@Test
public void testEndView1() {
  
  List<String> gridPic = asList(
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
  List<String> gridPic2 = asList(
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
  List<String> gridPic3 = asList(
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
