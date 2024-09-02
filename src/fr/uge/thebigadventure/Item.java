package fr.uge.thebigadventure;

/**
 * Interface used to create a super type for Weapon, Nutrient or Exchangeable
 * These are items that can be on the grid, in the player's hand or in the player's inventory
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public sealed interface Item permits Weapon, Nutrient, Exchangeable {
  /**
   * Returns the damages of the item
   * 
   * @return by default 0
   */
	default int damage() {
		return 0;
	}
	
	/**
   * Returns the skin of the item
   * 
   * @return a string that represents the skin
   */
	String skin();
	
	/**
   * Returns the name of the item
   * 
   * @return a string that represents the name
   */
	String name();
}
