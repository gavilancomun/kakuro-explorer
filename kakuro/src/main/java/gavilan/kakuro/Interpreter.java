package gavilan.kakuro;

import gavq.kakuro.KakuroLexer;
import gavq.kakuro.KakuroParser;
import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Interpreter {

public static Optional<CreateModelListener> interpretRule(Reader r, Function<KakuroParser, ParseTree> rule) {
  try {
    var input = CharStreams.fromReader(r);
    var lexer = new KakuroLexer(input);
    var tokens = new CommonTokenStream(lexer);
    var parser = new KakuroParser(tokens);
    var tree = rule.apply(parser);
    var walker = new ParseTreeWalker();
    var modelListener = new CreateModelListener();
    walker.walk(modelListener, tree);
    return Optional.of(modelListener);
  }
  catch (IOException ex) {
    Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, "", ex);
    return Optional.empty();
  }
}

public static GridController interpret(Reader r) {
  return interpretRule(r, KakuroParser::grid)
    .map(CreateModelListener::getGridController)
    .orElse(new GridController());
}
}
