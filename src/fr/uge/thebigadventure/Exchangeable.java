package fr.uge.thebigadventure;

import java.util.Objects;

/**
 * Representation of an exchangeable item on the grid, in the inventory or in the player's hand
 *
 * @param name the name of the item
 * @param skin the skin used to draw the item
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public record Exchangeable(String name, String skin) implements Item {
	/**
   * Canonical constructor of Exchangeable.
   * 
   * @param name the name of the item
   * @param skin the skin used to draw the item
   */
  public Exchangeable {
    Objects.requireNonNull(skin);
  }
}
