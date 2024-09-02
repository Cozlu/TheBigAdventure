package fr.uge.graph;

import java.util.Objects;

import fr.uge.thebigadventure.Direction;
import fr.uge.thebigadventure.Inventory;
import fr.uge.thebigadventure.Item;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.Event.Action;

/**
 * The class that is used to manage the player's actions
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class PlayerActions {
	/**
   * Manages the keyboard events linked to the movements of the player
   * 
   * @param key the keyboard key
   * @return the new direction of the player
   */
	public static Direction movementActions(KeyboardKey key) {
		Objects.requireNonNull(key);
  	return switch(key) {
  		case KeyboardKey.RIGHT -> Direction.RIGHT;
  		case KeyboardKey.LEFT -> Direction.LEFT;
  		case KeyboardKey.UP -> Direction.UP;
  		case KeyboardKey.DOWN -> Direction.DOWN;
  		default -> null;
  	};
	}
	
	/**
   * Changes the selection(number) in the inventory
   * 
   * @param selection the number that represent the selected item
   * @param key keyboard input from the player
   * @return the number of the new selected item
   */
	private static int changeSelected(int selection, KeyboardKey key) {
		Objects.requireNonNull(key);
		var newSelection = selection;
		switch(key) {
			case KeyboardKey.RIGHT -> newSelection++;
			case KeyboardKey.LEFT -> newSelection--;
			case KeyboardKey.UP -> newSelection -= 10;
			case KeyboardKey.DOWN -> newSelection += 10;
			default -> {return selection;}
		};
		if (newSelection < 0 || newSelection >= 30)
			return selection;
		return newSelection;
	}
	
	/**
   * Manages the main inventory loop to select an item from the inventory
   * 
   * @param inventory the player's inventory
   * @param GI the graphic interface
   * @return the new selected item from the inventory
   */
	public static Item inventoryLoop(Inventory inventory, GraphicInterface GI) {
		Objects.requireNonNull(inventory);
		Objects.requireNonNull(GI);
		var initialItem = inventory.selectItem();
		Event event = GI.context().pollOrWaitEvent(150);
    if (event == null) return null;
    Action action = event.getAction();
    KeyboardKey key = event.getKey();
		while(true) {
			GI.drawInventory(inventory);
			event = GI.context().pollOrWaitEvent(150);
	    if (event == null) continue;
	    action = event.getAction();
	    key = event.getKey();
	    if (action == Action.KEY_PRESSED) {
        if (key == KeyboardKey.I)
        	return initialItem;
        var newSelection = changeSelected(inventory.selection(), key);
        inventory.changeSelection(newSelection);
        if (key == KeyboardKey.Q) GI.context().exit(0);
        if (key == KeyboardKey.SPACE)
        	return inventory.selectItem();
	    }
		}
	}
}
