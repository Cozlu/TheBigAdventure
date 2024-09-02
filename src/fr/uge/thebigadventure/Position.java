package fr.uge.thebigadventure;

import java.util.Objects;
import java.util.Random;

/**
 * Representation of a position in the grid.
 * 
 * @param x the x coordinate or the column in the grid.
 * @param y the y coordinate or the line in the grid.
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public record Position(int x, int y) {
	
	/**
   * Canonical constructor of Position.
   * 
   * @param x the x coordinate or the column in the grid.
   * @param y the y coordinate or the line in the grid.
   */
  public Position {
    if(x < 0) {
      throw new IllegalArgumentException("x position must be >= 0");
    }
    if(y < 0) {
      throw new IllegalArgumentException("y position must be >= 0");
    }
  }
  
  /**
   * Picks a random direction between up, down, left and right
   * 
   * @return the chosen direction
   */
  public static Direction pullDirection(){
  	Direction direction = null;
  	Random rand = new Random();
  	var randNumber = rand.nextInt(4);
  	switch(randNumber) {
  		case 0 -> direction = Direction.UP;
  		case 1 -> direction = Direction.DOWN;
  		case 2 -> direction = Direction.LEFT;
  		case 3 -> direction = Direction.RIGHT;
  	}
  	return direction;
  }
  
  /**
   * Calculates a new position based on the direction and the previous position
   * 
   * @param newDirection the new direction for the entity
   * @param position the previous position
   * @return the new position calculated
   */
  public static Position newPosition(Direction newDirection, Position position) {
  	Objects.requireNonNull(newDirection);
  	Objects.requireNonNull(position);
  	int x = position.x();
  	int y = position.y();
  	Position newPosition = position;
  	switch(newDirection) {
  		case UP -> y--;
  		case DOWN -> y++;
  		case LEFT -> x--;
  		case RIGHT -> x++;
  	}
  	if (x >= 0 && y >= 0)
  		newPosition = new Position(x, y);
  	return newPosition;
  }
}
