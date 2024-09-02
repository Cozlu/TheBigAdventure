package fr.uge.thebigadventure;

/**
 * Interface used to create a super type for Obstacle and Decoration and to represent the grid
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 *
 */
public sealed interface Tile permits Obstacle, Decoration {
  /**
   * Tests if the tile is an obstacle
   * 
   * @return true if it is an obstacle otherwise false
   */
	boolean isObstacle();
	
	/**
   * Returns the skin of the tile
   * 
   * @return a string that represents the skin
   */
	String skin();
}
