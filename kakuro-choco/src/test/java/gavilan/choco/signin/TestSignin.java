package gavilan.choco.signin;

import static java.util.Arrays.asList;
import java.util.List;
import org.junit.Test;

public class TestSignin {
  
@Test
public void test1() {
  List<String> gridPic = asList(
          "X.X+X+X+X+X",
          "-x.x.x-x-x.",
          "X-X.X.X+X.X",
          ".x.x.x.x.x.",
          "X+X+X.X+X.X",
          "+x.x+x.x-x-",
          "X.X.X.X.X.X",
          "+x-x.x.x.x.",
          "X.X.X.X.X.X",
          "+x.x.x.x-x.",
          "X-X.X+X.X+X"
  );
  new SignIn().solve(gridPic);
}
  
@Test
public void test2() {
  List<String> gridPic = asList(
          "X+X.X-X.X.X",
          ".x-x-x.x-x.",
          "X.X.X.X.X+X",
          "-x.x.x-x-x-",
          "X+X.X+X.X+X",
          ".x.x+x.x.x.",
          "X.X.X.X.X-X",
          "-x.x+x.x.x.",
          "X+X.X+X.X.X",
          "-x-x+x.x+x.",
          "X+X.X.X.X-X"
  );
  new SignIn().solve(gridPic);
}

}
