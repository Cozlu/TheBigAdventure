package fr.uge.thebigadventure;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * Representation of the player's inventory
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class Inventory {
	
  /**
   * The player's inventory
   */
	private final HashMap<Item, Integer> inventory;
	
  /**
   * The selected item in the inventory (equal to the item in the player's hand)
   */
	private int selection;
	
	/**
   * Constructor of Inventory
   */
	public Inventory() {
		this.inventory = new HashMap<>();
		this.selection = 0;
	}
	
	/**
   * Adds an item to the inventory
   * 
   * @param item the item to add
   */
	public void add(Item item) {
		Objects.requireNonNull(item);
		inventory.merge(item, 1, Math::addExact);
	}
	
	/**
   * Deletes an item from the inventory
   * 
   * @param item the item to delete
   */
	public void delete(Item item) {
		Objects.requireNonNull(item);
		var quantity = inventory.getOrDefault(item, null);
		if (quantity > 1)
			inventory.put(item, quantity - 1);
		else
			inventory.remove(item);
	}
	
	/**
   * Changes the number that represents the selected item
   * 
   * @param selection the new selection
   */
	public void changeSelection(int selection) {
		this.selection = selection;
	}
	
	/**
   * Selects the item in the inventory based on the selection number
   * 
   * @return the selected item
   */
	public Item selectItem() {
		int i = 0;
		for (var items : inventory.entrySet()) {
			if (i == selection)
				return items.getKey();
			i++;
		}
		return null;
	}
	
	/**
   * Returns the number of items the inventory contains
   * 
   * @return the number of items
   */
	public int numberOfItems() {
		return inventory.size();
	}
	
	/**
   * Returns the number that represent the selected item
   * 
   * @return the number that represent the selected item
   */
	public int selection() {
		return selection;
	}
	
	/**
   * Converts the inventory's HashMap to a Set
   * 
   * @return the Set that represents the inventory
   */
	public Set<Entry<Item, Integer>> toSet() {
		return inventory.entrySet();
	}
}
