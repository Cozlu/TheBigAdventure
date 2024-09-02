package fr.uge.lexer;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.joining;

/**
 * Class used to make lexical analysis of a text
 * 
 * @author FORAX Rémi
 */
public class Lexer {
  
  /**
   * A List which represents all the tokens in the token enumeration
   */
  private static final List<Token> TOKENS = List.of(Token.values());
  /**
   * A Pattern made with the regex values of the tokens list
   */
  private static final Pattern PATTERN = Pattern.compile(
      TOKENS.stream()
          .map(token -> "(" + token.regex() + ")")
          .collect(joining("|")));

  /**
   * An String which represents the text to analyze
   */
  private final String text;
  /**
   * An Matcher which represents the pattern matcher of the text to analyze
   */
  private final Matcher matcher;

  /**
   * Constructor of Lexer
   * 
   * @param text the text to analyze
   */
  public Lexer(String text) {
    this.text = Objects.requireNonNull(text);
    this.matcher = PATTERN.matcher(text);
  }

  /**
   * Returns the next result find by the matcher
   * 
   * @return the next result find by the matcher or null if there is none
   */
  public Result nextResult() {
    var matches = matcher.find();
    if (!matches) {
      return null;
    }
    for (var group = 1; group <= matcher.groupCount(); group++) {
      var start = matcher.start(group);
      if (start != -1) {
        var end = matcher.end(group);
        var content = text.substring(start, end);
        return new Result(TOKENS.get(group - 1), content);
      }
    }
    throw new AssertionError();
  }
}
