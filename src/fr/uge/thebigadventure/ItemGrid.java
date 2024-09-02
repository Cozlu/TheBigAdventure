package fr.uge.thebigadventure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Representation of the list of item on the grid
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class ItemGrid {
  /**
   * An HashMap which represents the items on the grid
   */
	private final HashMap<Position, Item> items;
	
  /**
   * Constructor of ItemGrid
   */
	public ItemGrid() {
		this.items = new HashMap<>();
	}
	
  /**
   * Adds an item to the HashMap
   * 
   * @param position the position of the item on the map
   * @param item the item to add
   */
	public void add(Position position, Item item) {
		Objects.requireNonNull(position, "Position of " + item.skin() + " is null");
		Objects.requireNonNull(item);
		items.put(position, item);
	}
	
  /**
   * Deletes an item from the HashMap
   * 
   * @param position the position of the item
   */
	public void delete(Position position) {
		Objects.requireNonNull(position);
		items.remove(position);
	}
	
  /**
   * Returns the item the player can take if he is one the same tile as the said item
   * 
   * @param player the player who can take the item
   * @return the item which the player can take
   */
	public Item itemToTake(Player player) {
		Objects.requireNonNull(player);
		var itemToTake = items.getOrDefault(player.position(), null);
		items.entrySet().removeIf(item -> item.getKey().equals(player.position()));
		return itemToTake;
	}

  /**
   * Returns the items on the grid in a list
   * 
   * @return a list composed of the items
   */
	public List<Item> toList() {
		return items.values()
        .stream()
        .toList();
	}
	
  /**
   * Returns a copy of the items HashMap
   * 
   * @return an unmodifiable map of items
   */
	public Map<Position, Item> copy() {
		return Map.copyOf(items);
	}
	
  /**
   * Returns the item at the position specified
   * 
   * @param position the position of the item
   * @return the item at the position specified if there is one otherwise null
   */
	public Item itemAtPosition(Position position) {
		return items.getOrDefault(position, null);
	}
}
