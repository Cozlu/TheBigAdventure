package fr.uge.thebigadventure;

import java.util.Objects;

/**
 * Representation of a weapon on the grid, in the inventory or in the player's hand
 * 
 * @param name the name of the item
 * @param skin the skin used to draw the item
 * @param damage the damage of the item
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public record Weapon(String name, String skin, int damage) implements Item{
	/**
   * Canonical constructor of Weapon.
   * 
   * @param name the name of the item
   * @param skin the skin used to draw the item
   * @param damage the damage of the item
   */
  public Weapon {
    Objects.requireNonNull(skin);
    if(damage < 0) {
      throw new IllegalArgumentException("damage must be >= 0 for " + skin);
    }
  }
}
