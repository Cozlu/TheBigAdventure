package fr.uge.thebigadventure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Representation of the grid
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class Grid {
  /**
   * An HashMap which represents the grid
   */
	private final HashMap<Position, Tile> grid;
  /**
   * An integer which represents the length of the grid
   */
	private final int length;
  /**
   * An integer which represents the height of the grid
   */
	private final int height;
	
  /**
   * Constructor of Grid
   * 
   * @param length the length of the grid
   * @param height the height of the grid
   */
	public Grid(int length, int height) {
		if (length <= 0)
			throw new IllegalArgumentException("length must be > 0");
		if (height <= 0)
			throw new IllegalArgumentException("height must be > 0");
		this.length = length;
		this.height = height;
		this.grid = new HashMap<>();
	}
	
  /**
   * Adds a tile to the HashMap
   * 
   * @param position the position of the tile in the grid
   * @param tile the tile to add
   */
	public void add(Position position, Tile tile) {
		Objects.requireNonNull(position, "Position is null for " + tile.skin());
		Objects.requireNonNull(tile);
		grid.put(position, tile);
	}
	
  /**
   * Returns if the position is in the grid
   * 
   * @param testPosition the position to test
   * @return true if the position is in the grid otherwise false
   */
  public boolean inGrid(Position testPosition) {
  	Objects.requireNonNull(testPosition);
  	if (testPosition.x() <= (length - 1) && testPosition.y() <= (height - 1))
  		return true;
  	return false;
  }
  
  /**
   * Returns if there is an obstacle on the position or if the position is not in the grid
   * 
   * @param newPosition the position to test
   * @return true if the position is valid otherwise false
   */
  public boolean canMove(Position newPosition) {
  	Tile nextTile = grid.getOrDefault(newPosition, null);
  	if (nextTile == null)
  		return inGrid(newPosition);
  	return !nextTile.isObstacle();
  }

  /**
   * Returns the length of the grid
   * 
   * @return the length
   */
	public int length() {
		return length;
	}

  /**
   * Returns the height of the grid
   * 
   * @return the height
   */
	public int height() {
		return height;
	}
	
  /**
   * Returns the tiles of the grid in a list
   * 
   * @return a list composed of the tiles
   */
	public List<Tile> toList() {
		return grid.values()
        .stream()
        .toList();
	}
	
  /**
   * Returns a copy of the tiles HashMap
   * 
   * @return an unmodifiable map of tiles
   */
	public Map<Position, Tile> copy() {
		return Map.copyOf(grid);
	}
	
  /**
   * Returns the tile at the position
   * 
   * @param position the position of the tile
   * @return the tile at the position specified if there is one otherwise null
   */
	public Tile tileAtPosition(Position position) {
		return grid.getOrDefault(position, null);
	}
}
