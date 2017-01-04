
import java.util.ArrayList;
import org.jacop.constraints.Or;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.XeqY;
import org.jacop.constraints.XgtC;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.junit.Test;

public class TestJacop {

@Test
public void testJaCop() {
  Store store = new Store();
  IntVar x = new IntVar(store, 1, 10);
  IntVar y = new IntVar(store, 2, 10);
  IntVar[] vars = {x, y};
  store.impose(new XeqY(x, y));
  store.impose(new XgtC(x, 4));
  PrimitiveConstraint c1 = new XgtC(x, 6);
  PrimitiveConstraint c2 = new XgtC(y, 6);
  ArrayList<PrimitiveConstraint> c = new ArrayList<>();
  c.add(c1);
  c.add(c2);
  store.impose(new Or(c));
  Search<IntVar> label = new DepthFirstSearch<>();
  SelectChoicePoint<IntVar> select
          = new InputOrderSelect<>(store,
                  vars, new IndomainMin<IntVar>());
  boolean result = label.labeling(store, select);
  label.printAllSolutions();
}
}
