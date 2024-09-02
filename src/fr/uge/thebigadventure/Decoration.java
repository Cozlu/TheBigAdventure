package fr.uge.thebigadventure;

import java.util.Objects;

/**
 * Representation of a tile with a decoration
 * 
 * @param skin the skin used to draw the tile
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public record Decoration(String skin) implements Tile {
	/**
   * Canonical constructor of Decoration.
   * 
   * @param skin the skin used to draw the tile
   */
  public Decoration {
    Objects.requireNonNull(skin);
  }

	@Override
	public boolean isObstacle() {
		return false;
	}
}
