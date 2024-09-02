package fr.uge.thebigadventure;

import java.util.Objects;

/**
 * Representation of a tile with a obstacle
 * 
 * @param skin the skin used to draw the tile
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public record Obstacle(String skin) implements Tile {
	/**
   * Canonical constructor of Obstacle.
   * 
   * @param skin the skin used to draw the tile
   */
  public Obstacle {
    Objects.requireNonNull(skin);
  }

	@Override
	public boolean isObstacle() {
		return true;
	}
}
