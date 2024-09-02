package fr.uge.lexer;

/**
 * An enumeration of all tokens from the parsed file
 * 
 * @author FORAX Rémi
 */
public enum Token {
  IDENTIFIER("[A-Za-z]+"),
  NUMBER("[0-9]+"),
  LEFT_PARENS("\\("),
  RIGHT_PARENS("\\)"),
  LEFT_BRACKET("\\["),
  RIGHT_BRACKET("\\]"),
  COMMA(","),
  COLON(":"),
  QUOTE("\"\"\"[^\"]+\"\"\""),
  ;

	/**
   * A String that represents the regex from the token
   */
  private final String regex;

  /**
   * Canonical constructor of Token
   * 
   * @param regex the regex of the token
   */
  Token(String regex) {
    this.regex = regex;
  }
  
  /**
   * Returns the regex from the token
   * 
   * @return the regex in a form of a String
   */
  public String regex() {
    return regex;
  }
}