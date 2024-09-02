package fr.uge.thebigadventure;

import java.util.Objects;


/**
 * Representation of the player in the game
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class Player {
	/**
	 * The name of the player
	 */
	private final String name;
	 
	/**
	 * The skin used to draw the player
	 */
  private final String skin;
  
	/**
	 * The position of the player in the grid
	 */
	private Position position;
	
	/**
	 * The current health of the player
	 */
	private int health;
	
	/**
	 * The initial health of the player
	 */
	private final int maxHealth;
	
	/**
	 * The item in the player's hand which can be used
	 */
	private Item holdItem;
	
	/**
	 * The direction the player faces
	 */
	private Direction direction;
	
	/**
	 * The player's inventory
	 */
	private final Inventory inventory;
	
	/**
   * Constructor of Player
   * 
   * @param name the player's name
   * @param skin the player's skin
   * @param position the current position
   * @param health the current health
   * @param maxHealth the initial health
   * @param holdItem the item in the player's hand, can be null
   * @param direction the current direction the player faces
   */
  public Player(String name, String skin, Position position, int health, int maxHealth, Item holdItem, Direction direction) {
    Objects.requireNonNull(skin);
    Objects.requireNonNull(position, "position is null for " + skin);
    Objects.requireNonNull(direction);
    if(health < 0) {
      throw new IllegalArgumentException("health must be > 0 for " + skin);
    }
    if(maxHealth < health) {
      throw new IllegalArgumentException("maxHealth must be > health for " + skin);
    }
    this.name = name;
    this.skin = skin;
    this.position = position;
    this.health = health;
    this.maxHealth = maxHealth;
    this.holdItem = holdItem;
    this.direction = direction;
    this.inventory = new Inventory();
  }
  
  /**
   * Changes the health of the player
   * 
   * @param newHealth the new heath of the player
   */
  public void changeHealth(int newHealth) {
  	if (newHealth <= maxHealth)
  		this.health = newHealth;
  }
  
  /**
   * Attacks the enemy by removing the quantity of damages of his weapon to his health
   * 
   * @param enemy the enemy to attack
   * @return the new health of the enemy due to the attack
   */
  public int attackEnemy(Enemy enemy) {
  	Objects.requireNonNull(enemy);
  	return enemy.health() - holdItem.damage();
  }
  
  /**
   * Moves the player in a direction
   * 
   * @param newDirection the player's new direction
   * @param newPosition the player's new position
   */
  public void move(Direction newDirection, Position newPosition) {
  	Objects.requireNonNull(newDirection);
  	Objects.requireNonNull(newPosition);
  	this.direction = newDirection;
  	this.position = newPosition;
  }
  
  /**
   * Moves the player in the specified direction if it is possible
   * 
   * @param grid the grid used to test if the movement is possible
   * @param enemies the enemies used to test if the movement is possible
   * @param direction the player's new direction (can be null)
   */
  public void movingAction(Grid grid, EnemiesGrid enemies, Direction direction) {
  	Objects.requireNonNull(grid);
  	Objects.requireNonNull(enemies);
		if (direction != null) {
			var newPosition = Position.newPosition(direction, position);
			if (grid.canMove(newPosition) && enemies.enemyAtPosition(newPosition) == null)
				move(direction, newPosition);
			else
				this.direction = direction; // change the direction anyway
  	}
  }
  
  /**
   * Changes the item hold in the player's hand (can be null)
   * 
   * @param item the new item to change
   */
  public void holdNewItem(Item item) {
  	this.holdItem = item;
  }

  /**
   * Takes an item from the grid and give it to the player
   * 
   * @param newItem the new item to take (can be null)
   */
  public void takeItem(Item newItem) {
  	if (newItem != null) {
			inventory.add(newItem);
			if (inventory.numberOfItems() == 1) {
				holdNewItem(newItem);
				inventory.changeSelection(0);
			}
		}
  }
  
  /**
   * Heals the player with his item in his hand (if it is a nutrient)
   * The healing equals to 30% of the player's max health
   */
  public void heal() {
  	var heal = 30*maxHealth/100;
  	if ((health + heal) < maxHealth)
  		health += heal;
  	else
  		health += (maxHealth - health);
  	inventory.delete(holdItem);
  	holdItem = null;
  }
  
	/**
   * Returns the current direction of the player
   * 
   * @return the direction
   */
	public Direction direction() {
		return direction;
	}
	
	/**
   * Returns the current position of the player
   * 
   * @return the position
   */
	public Position position() {
		return position;
	}

	/**
   * Returns the item which the player hold in their hand
   * 
   * @return the hold item
   */
	public Item holdItem() {
		return holdItem;
	}

	/**
   * Returns the skin of the player
   * 
   * @return the skin
   */
	public String skin() {
		return skin;
	}

	/**
   * Returns the current health of the player
   * 
   * @return the health
   */
	public int health() {
		return health;
	}

	/**
   * Returns the initial health of the player
   * 
   * @return the initial health
   */
	public int maxHealth() {
		return maxHealth;
	} 
	
	/**
   * Returns the player's inventory
   * 
   * @return the player's inventory
   */
	public Inventory inventory() {
		return inventory;
	}
	
	/**
   * Returns the player's name
   * 
   * @return the player's name
   */
	public String name() {
		return name;
	}
}
