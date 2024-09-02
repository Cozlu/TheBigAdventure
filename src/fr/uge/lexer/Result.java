package fr.uge.lexer;

import java.util.Objects;

/**
 * This Record is used to store a token and its content from the parsed file
 * 
 * @param token the token extracted from the parse 
 * @param content the content of the token
 * 
 * @author FORAX Rémi
 */
public record Result(Token token, String content) {
	/**
	 * Canonical constructor of Result
	 * 
	 * @param token the token extracted from the parse 
	 * @param content the content of the token
	 */
  public Result {
    Objects.requireNonNull(token);
    Objects.requireNonNull(content);
  }
}