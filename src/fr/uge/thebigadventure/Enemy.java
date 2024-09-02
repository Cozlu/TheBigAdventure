package fr.uge.thebigadventure;

import java.util.Objects;
import java.util.Random;

/**
 * Representation of an enemy in the grid
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class Enemy {
  /**
   * The name of the enemy
   */
	private final String name;
	 
	/**
	 * The skin used to draw the enemy
   */
	private final String skin;
	
	/**
	 * The position of the enemy in the grid
   */	
	private Position position;

	/**
	 * The zone in which the enemy can walk
   */
	private final Zone zone;
	
	/**
	 * The current health of the enemy
   */
	private int health; 

	/**
	 * The initial health of the enemy
   */
	private final int maxHealth;

	/**
	 * The behavior used to move the enemy
   */
	private final String behavior;

	/**
   * The damages inflicted to the player
   */
	private final int damage;

	/**
	* The direction the enemy faces
  */
	private Direction direction;
	
	/**
   * Constructor of Enemy
   * 
   * @param name the enemy's name
   * @param skin the enemy's skin
   * @param position the current position
   * @param zone the zone in which the enemy can move 
   * @param health the current health
   * @param maxHealth the initial health
   * @param behavior the enemy's behavior
   * @param damage the damages the enemy can dealt to the player
   * @param direction the current direction the enemy faces
   */
  public Enemy(String name, String skin, Position position, Zone zone, int health, int maxHealth, String behavior, int damage, Direction direction) {
    Objects.requireNonNull(skin);
    Objects.requireNonNull(position, "position is null for " + skin);
    Objects.requireNonNull(zone, "zone is null for " + skin);
    Objects.requireNonNull(behavior, "behavior is null for " + skin);
    Objects.requireNonNull(direction);
    if(damage < 0) {
      throw new IllegalArgumentException("damage must be >= 0 for " + skin);
    }
    if(health < 1) {
      throw new IllegalArgumentException("health must be > 0 for " + skin);
    }
    if(maxHealth < health) {
      throw new IllegalArgumentException("maxHealth must be > health for " + skin);
    }
    this.name = name;
    this.skin = skin;
    this.position = position;
    this.zone = zone;
    this.health = health;
    this.maxHealth = maxHealth;
    this.behavior = behavior;
    this.damage = damage;
    this.direction = direction;
  }
  
  /**
   * Changes the health of the enemy
   * 
   * @param newHealth the new heath of the enemy
   */
  public void changeHealth(int newHealth) {
  	if (newHealth > 0)
  		this.health = newHealth;
  }
  
  /**
   * Returns if the enemy can attack the player
   * 
   * @param player the player
   * @return true if the player is in front of the enemy otherwise false
   */
  public boolean canAttack(Player player) {
  	Objects.requireNonNull(player);
  	Position attackPosition = new Position(position.x(), position.y());
    switch(direction) {
  		case UP -> attackPosition = new Position(position.x(), position.y() - 1);
  		case DOWN -> attackPosition = new Position(position.x(), position.y() + 1);
  		case LEFT -> attackPosition = new Position(position.x() - 1, position.y());
  		case RIGHT -> attackPosition = new Position(position.x() + 1, position.y());
  	}
    if (player.position().x() == attackPosition.x() && player.position().y() == attackPosition.y())
    		return true;
    return false;
  }
  
  /**
   * Attacks the player by removing the quantity of damages to his health
   * 
   * @param player the player to attack
   * @return the new health of the player
   */
  public int attackPlayer(Player player) {
  	Objects.requireNonNull(player);
  	return player.health() - damage;
  }
  
  /**
   * Returns if the enemy has to move by randomly drawing a number between 0 and 100, the enemy has a 20% chance to move
   * 
   * @return true if the enemy has to move otherwise false
   */
  public boolean willMove() {
  	Random rand = new Random();
  	var randNumber = rand.nextInt(100);
  	if (randNumber < 20)
  		return true;
  	return false;
  }
  
  /**
   * Changes the direction of the enemy
   * 
   * @param direction the new direction for the nenemy
   */
  public void changeDirection(Direction direction) {
  	Objects.requireNonNull(direction);
  	this.direction = direction;
  }
  
  /**
   * Moves the enemy in a direction
   * 
   * @param newDirection the enemy's new direction
   * @param newPosition the enemy's new position
   */
  public void move(Direction newDirection, Position newPosition) {
  	Objects.requireNonNull(newDirection);
  	Objects.requireNonNull(newPosition);
		this.direction = newDirection;
		this.position = newPosition;
  }

	/**
   * Returns the current position of the enemy
   * 
   * @return the position
   */
	public Position position() {
		return position;
	}

	/**
   * Returns the current health of the enemy
   * 
   * @return the health
   */
	public int health() {
		return health;
	}

	/**
   * Returns the direction of the enemy
   * 
   * @return the direction
   */
	public Direction direction() {
		return direction;
	}

	/**
   * Returns the skin of the enemy
   * 
   * @return the skin
   */
	public String skin() {
		return skin;
	}

	/**
   * Returns the initial health of the enemy
   * 
   * @return the initial health
   */
	public int maxHealth() {
		return maxHealth;
	}
	
	/**
   * Returns the zone in which the enemy can move
   * 
   * @return the enemy's zone
   */
	public Zone zone() {
		return zone;
	}
	
	/**
   * Returns the enemy's name
   * 
   * @return the enemy's name
   */
	public String name() {
		return name;
	}
	
  @Override
	public int hashCode() {
		return Objects.hash(behavior, damage, direction, health, maxHealth, name, position, skin, zone);
	}

  @Override
  public boolean equals(Object o) {
  return o instanceof Enemy enemy
  	&& name.equals(enemy.name)
  	&& skin.equals(enemy.skin)
  	&& name.equals(enemy.name)
  	&& position.equals(enemy.position)
  	&& zone.equals(enemy.zone)
  	&& health == enemy.health
  	&& maxHealth == enemy.maxHealth
  	&& behavior.equals(enemy.behavior)
  	&& damage == enemy.damage
  	&& direction == enemy.direction;
  }
  
}
