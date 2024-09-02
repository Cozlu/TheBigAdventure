package fr.uge.thebigadventure;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Representation of the enemies on the grid
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class EnemiesGrid {
  /**
   * An HashMap which represents the enemies on the grid
   */
	private final HashMap<Position, Enemy> enemiesGrid;
	
  /**
   * Constructor of EnemyList
   */
	public EnemiesGrid() {
		this.enemiesGrid = new HashMap<>();
	}
	
  /**
   * Adds an enemy to the HashMap
   * 
   * @param enemy the enemy to add
   */
	public void add(Enemy enemy) {
		Objects.requireNonNull(enemy);
		enemiesGrid.put(enemy.position(), enemy);
	}
	
  /**
   * Deletes the enemy at the position specified
   * 
   * @param position the enemy's position
   */
	public void delete(Position position) {
		enemiesGrid.remove(position);
	}
	
  /**
   * Returns the enemy the player can attack if there is one in front of him
   * 
   * @param player the player
   * @return an enemy if there is one in front of the player otherwise null
   */
  public Enemy enemyToAttack(Player player) {
  	Objects.requireNonNull(player);
  	Position attackPosition = new Position(player.position().x(), player.position().y());
    switch(player.direction()) {
  		case UP -> attackPosition = new Position(player.position().x(), player.position().y() - 1);
  		case DOWN -> attackPosition = new Position(player.position().x(), player.position().y() + 1);
  		case LEFT -> attackPosition = new Position(player.position().x() - 1, player.position().y());
  		case RIGHT -> attackPosition = new Position(player.position().x() + 1, player.position().y());
  	}
    return enemiesGrid.getOrDefault(attackPosition, null);
  }
  
  /**
   * Test if the current enemy can move on the grid
   * 
   * @param movedEnemyList the enemies on the map
   * @param enemy the enemy to test
   * @param grid the grid of the game
   * @param newPosition the new position of the enemy to test
   * @return true or false
   */
  private boolean enemyCanMove(EnemiesGrid movedEnemyList, Enemy enemy, Grid grid, Position newPosition) {
  	Objects.requireNonNull(movedEnemyList);
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(grid);
  	Objects.requireNonNull(newPosition);
  	return enemy.willMove() && grid.canMove(newPosition) && enemy.zone().inZone(newPosition) &&
  			   movedEnemyList.enemyAtPosition(newPosition) == null && enemiesGrid.getOrDefault(newPosition, null) == null;
  }
  
  /**
   * Moves the enemies on the grid
   * 
   * @param grid the grid of the game
   * @param player the player of the game
   * @return the map with the moved enemies
   */
  public EnemiesGrid moveEnemies(Grid grid, Player player) {
  	Objects.requireNonNull(player);
  	Objects.requireNonNull(grid);
  	var movedEnemyList = new EnemiesGrid();
  	Enemy enemyToMove;
  	for (var enemie : enemiesGrid.entrySet()) {
  		enemyToMove = enemie.getValue();
  		var newDirection = Position.pullDirection();
    	var newPosition = Position.newPosition(newDirection, enemyToMove.position());
    	if (newPosition.equals(player.position()))
    		enemyToMove.changeDirection(newDirection);
    	else if (enemyCanMove(movedEnemyList, enemyToMove, grid, newPosition))
    		enemyToMove.move(newDirection, newPosition);
  		movedEnemyList.add(enemyToMove);
  	}
  	return movedEnemyList;
  }
  
  /**
   * Returns the enemies on the grid in a list
   * 
   * @return a list composed of the enemies
   */
  public List<Enemy> toList() {
		return enemiesGrid.values()
        .stream()
        .toList();
  }
  
  /**
   * Attacks if it is possible, an enemy from the HashMap
   * 
   * @param player the player who attacks
   */
  public void attackEnemies(Player player) {
  	Objects.requireNonNull(player);
		Enemy attackEnemy = enemyToAttack(player);
		if (attackEnemy != null && player.holdItem() != null) {
			int newHealth = player.attackEnemy(attackEnemy);
	  	if (newHealth <= 0)
	  		delete(attackEnemy.position());
	  	else {
	  		attackEnemy.changeHealth(newHealth);
	  		add(attackEnemy);
	  	}
	  }
  }
  
  /**
   * Attack if it is possible the player
   * The enemy has 20% chance to deal damages to the player
   * 
   * @param player the player to attack
   * @return the new health of the player if they were attacked or just the unchanged health
   */
  public int attackPlayer(Player player) {
  	Objects.requireNonNull(player);
  	Random rand = new Random();
  	for (var enemy : enemiesGrid.values()) {
    	var randNumber = rand.nextInt(100);
  		if (randNumber < 20 && enemy.canAttack(player))
  			return enemy.attackPlayer(player);
  	}
  	return player.health();
  }
  
  /**
   * Returns the enemy at the specified position
   * 
   * @param position the enemy's position
   * @return the enemy at the position specified otherwise null
   */
  public Enemy enemyAtPosition(Position position) {
  	Objects.requireNonNull(position);
  	return enemiesGrid.getOrDefault(position, null);
  }
  
  /**
   * Returns the number of enemies on the grid
   * 
   * @return the number of enemies on the grid
   */
  public int numberOfEnemies() {
  	return enemiesGrid.size();
  }
}
