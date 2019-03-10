package gavilcode.choco.stars;

import java.util.Set;
import org.chocosolver.solver.variables.IntVar;

public class Cell {

Cell top;
Cell right;
Cell bottom;
Cell left;
IntVar intVar;
Set<Cell> group;
}
