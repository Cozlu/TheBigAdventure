package fr.uge.thebigadventure;

import java.io.IOException;
import java.util.Objects;

import fr.uge.graph.GraphicInterface;
import fr.uge.graph.PlayerActions;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.Event.Action;

/**
 * This class is used to manipulates and represents the entire game
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class Game {
	
  /**
   * The grid with tiles of the game
   */
	private final Grid grid;
	
  /**
   * The items on the grid
   */
	private final ItemGrid items;
	
  /**
   * The player of the game
   */
	private Player player;
	
  /**
   * The enemies on the grid
   */
	private EnemiesGrid enemies;
	
	
	/**
   * A boolean that represent a dry run or not
   */
	private boolean dryRun;
	
  /**
   * Constructor of Game
   * 
   * @param grid the grid of the game
   * @param items the items on the grid
   * @param player the player in the game
   * @param enemies the enemies on the grid
   */
	public Game(Grid grid, ItemGrid items, Player player, EnemiesGrid enemies) {
		Objects.requireNonNull(grid);
		Objects.requireNonNull(items);
		Objects.requireNonNull(player);
		Objects.requireNonNull(enemies);
		this.grid = grid;
		this.items = items;
		this.player = player;
		this.enemies = enemies;
		this.dryRun = false;
	}
	
  /**
   * Main loop of the game where the game is drawn and the events are managed
   * 
   * @param context the ApplicationContext
   * 
   * @throws IOException if the method can't open the file of the map
   * @throws InterruptedException if there is a problem with the sleep method
   */
	public void MainLoop(ApplicationContext context) throws IOException, InterruptedException{
		var GI = new GraphicInterface(context);
		GI.loadGameImages(this);
		while(player.health() > 0 && enemies.numberOfEnemies() > 0) {
			var startTime = System.currentTimeMillis(); 
			GI.drawGame(this);
			if (!dryRun) enemies = enemies.moveEnemies(grid, player);
			player.changeHealth(enemies.attackPlayer(player)); // test if the player can be attacked by an enemy
			Item newItem = items.itemToTake(player);	// test if the player can take an item from the map	
			player.takeItem(newItem);
			Event event = context.pollOrWaitEvent(150);
	    if (event == null) continue;
	    KeyboardKey key = event.getKey();
	    if (event.getAction() == Action.KEY_PRESSED) {
	    	var direction = PlayerActions.movementActions(key);
				player.movingAction(grid, enemies, direction);
	    	switch(key) {
	    		case KeyboardKey.SPACE -> {
	    			switch(player.holdItem()) {
	    				case Weapon w -> enemies.attackEnemies(player);
	    				case Nutrient n -> player.heal();
	    				case Exchangeable e -> {continue;}
	    				case null -> {continue;}
	    			}
	    		}
	    		case KeyboardKey.Q -> context.exit(0);
	    		case KeyboardKey.I -> {
	    			var itemSelected = PlayerActions.inventoryLoop(player.inventory(), GI);
	    			player.holdNewItem(itemSelected);
	    		}
	    		default -> {continue;}
	    	}
			}
	    var endTime = System.currentTimeMillis(); 
	    var frameTime = endTime - startTime;
	    if (frameTime < 16) // limit to 60 frames per seconds (16 milliseconds = 1/60 seconds)
	    	Thread.sleep(16 - frameTime);
		}
		boolean win;
		if (player.health() <= 0)
			win = false;
		else
			win = true;
		GI.drawEndScreen(win);
		var event = context.pollOrWaitEvent(10);
		while(true) {
			event = context.pollOrWaitEvent(10);
			if (event == null) continue;
			if (event.getAction() == Action.KEY_PRESSED) break;
		}
		context.exit(0);
  }

	/**
   * Returns the grid of the game
   * 
   * @return the grid
   */
	public Grid grid() {
		return grid;
	}

	/**
   * Returns the player of the game
   * 
   * @return the player
   */
	public Player player() {
		return player;
	}

	/**
   * Returns the items on the grid
   * 
   * @return the items
   */
	public ItemGrid items() {
		return items;
	}

	/**
   * Returns the enemies on the grid
   * 
   * @return the enemies
   */
	public EnemiesGrid enemies() {
		return enemies;
	}
	
	/**
   * Changes the status of the current run
   * 
   * @param isDryRun true if it is a dry-un, false otherwise
   */
	public void changeRun(boolean isDryRun) {
		this.dryRun = isDryRun;
	}
}
