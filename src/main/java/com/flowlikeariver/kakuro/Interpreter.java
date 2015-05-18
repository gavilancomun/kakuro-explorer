package com.flowlikeariver.kakuro;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Interpreter {

public static Optional<CreateModelListener> interpretRule(Reader r, Function<KakuroParser, ParseTree> rule) {
  try {
    ANTLRInputStream input = new ANTLRInputStream(r);
    KakuroLexer lexer = new KakuroLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    KakuroParser parser = new KakuroParser(tokens);
    ParseTree tree = rule.apply(parser);
    ParseTreeWalker walker = new ParseTreeWalker();
    CreateModelListener modelListener = new CreateModelListener();
    walker.walk(modelListener, tree);
    return Optional.of(modelListener);
  }
  catch (IOException ex) {
    Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, "", ex);
    return Optional.empty();
  }
}

public static GridController interpret(Reader r) {
  return interpretRule(r, parser -> parser.grid())
    .map(ml -> ml.getGridController())
    .orElse(new GridController());
}
}
