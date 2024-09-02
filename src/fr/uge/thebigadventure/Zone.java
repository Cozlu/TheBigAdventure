package fr.uge.thebigadventure;

import java.util.Objects;

/**
 * Representation of a zone in which the enemy can move
 * 
 * @param position the position of the up left corner in the grid
 * @param length the length of the zone
 * @param height the height of the zone
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public record Zone(Position position, int length, int height) {
	/**
   * Canonical constructor of Zone.
   * 
   * @param position the position of the up left corner in the grid
   * @param length the length of the zone
   * @param height the height of the zone
   */
  public Zone {
    Objects.requireNonNull(position);
    if(length < 1) {
      throw new IllegalArgumentException("length must be > 0");
    }
    if(height < 1) {
      throw new IllegalArgumentException("height must be > 0");
    }
  }
  
  /**
   * Returns if the position is in the current zone
   * 
   * @param testPosition the position to verify
   * @return true if testPosition is in the zone otherwise false
   */
  public boolean inZone(Position testPosition) {
  	Objects.requireNonNull(testPosition);
  	if (testPosition.x() >= position.x() && testPosition.x() <= position.x() + (length - 1) &&
  			testPosition.y() >= position.y() && testPosition.y() <= position.y() + (height - 1))
  		return true;
  	return false;
  }
}
