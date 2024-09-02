package fr.uge.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javax.imageio.ImageIO;

import fr.uge.thebigadventure.*;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.ScreenInfo;

/**
 * Class used to draw the interface of the game and to manage keyboard events
 * from the player
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class GraphicInterface {
  /**
   * The context of the application
   */
	private ApplicationContext context;
	
	/**
   * The images of the tiles in the grid
   */
	private final HashMap<String, BufferedImage> imagesGame;
	
  /**
   * Constructor of GraphicInterface
   * 
   * @param context the context of the application
   */
	public GraphicInterface(ApplicationContext context) {
		Objects.requireNonNull(context);
		this.context = context;
		this.imagesGame = new HashMap<>();
	}
	
  /**
   * Loads the image specified by the skin
   * 
   * @param skin the name of the image to load
   * @return the loaded image in the form of a BufferedImage
   * 
   * @throws IOException if the image cannot be opened
   */
	private static BufferedImage loadImage(String skin) throws IOException {
    Objects.requireNonNull(skin);
    BufferedImage image;
    try(var input = GraphicInterface.class.getResourceAsStream("/images/" + skin + "_0.png")) {
      image = ImageIO.read(input);
    }
    return image;
  }
	
  /**
   * Loads the images of the map specified by the skin
   * 
   * @param game the game to load
   * 
   * @throws IOException if the image cannot be opened
   */
	public void loadGameImages(Game game) throws IOException{
		Objects.requireNonNull(game);
		var tilesList = game.grid().toList();
		var itemsList = game.items().toList();
		var enemyList = game.enemies().toList();
		for (var tile : tilesList)
			imagesGame.put(tile.skin(), loadImage(tile.skin()));
		for (var item : itemsList)
			imagesGame.put(item.skin(), loadImage(item.skin()));
		for (var enemy : enemyList)
			imagesGame.put(enemy.skin(), loadImage(enemy.skin()));
		imagesGame.put(game.player().skin(), loadImage(game.player().skin()));
	}
	
  /**
   * Calculates the size of the tiles based on the size of the screen
   * 
   * @param grid the grid of the game
   * @return the size of the tiles on the screen
   */
	private int tileSize(Grid grid) {
		Objects.requireNonNull(grid);
		ScreenInfo screenInfo = context.getScreenInfo();
		float width = screenInfo.getWidth();
		float height = screenInfo.getHeight();
		var maxTilesOnX = 40;
		var maxTilesOnY = 20;
		if (grid.length() < 40)
			maxTilesOnX = grid.length();
		if (grid.height() < 20)
			maxTilesOnY = grid.height();
		var size = (int) width/maxTilesOnX;
		if (size > height/maxTilesOnY)
			return (int) height/maxTilesOnY;
		return size;
	}

  /**
   * Calculates the coefficients to scroll the game
   * 
   * @param grid the grid of the game
   * @param player the player in the game
   * @param tileSize the size of a tile
   * @return the x and y coefficients to scroll the tiles
   */
	private int[] calcScroll(Grid grid, Player player, int tileSize) {
		Objects.requireNonNull(grid);
		Objects.requireNonNull(player);
		int[] scroll = {0, 0};
		ScreenInfo screenInfo = context.getScreenInfo();
		float width = screenInfo.getWidth();
		float height = screenInfo.getHeight();
		var x = player.position().x()*tileSize;
		var y = player.position().y()*tileSize;
		// Scroll by default
		scroll[0] = (int) (width/2) - x;
		scroll[1] = (int) (height/2) - y;
		
		// Conditions to apply a specific srcoll
		if (x <= width/2 || tileSize*grid.length() <= width)
			scroll[0] = 0;
		else if ((width - scroll[0]) > grid.length()*tileSize)
			scroll[0] += (int) (width - scroll[0]) - grid.length()*tileSize;
		
		if (y <= height/2 || tileSize*grid.height() <= height)
			scroll[1] = 0;
		else if ((height - scroll[1]) > grid.height()*tileSize)
			scroll[1] += (int) (height - scroll[1]) - grid.height()*tileSize;
		return scroll;
	}
	
  /**
   * Draws the grid with decorations and obstacles elements
   * 
   * @param graphics Graphics2D object used to draw
   * @param grid the grid to draw
   * @param tileSize the size of a tile
   */
	private void drawGrid(Graphics2D graphics, Grid grid, int tileSize) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(grid);
		graphics.setColor(new Color(43, 23, 46));
    graphics.fill(new Rectangle2D.Float(0, 0, (grid.length() + 1)*tileSize, grid.height()*tileSize));
		var mapGrid = grid.copy();
		for (var tile : mapGrid.entrySet()) {
			var image = imagesGame.get(tile.getValue().skin());
			graphics.drawImage(image, tile.getKey().x()*tileSize, tile.getKey().y() * tileSize, 
												 tileSize, tileSize, null);
		}																										
	}
	
  /**
   * Draws the items on the grid
   * 
   * @param graphics Graphics2D object used to draw
   * @param items the items on the grid to draw
   * @param tileSize the size of a tile
   */
	private void drawItems(Graphics2D graphics, ItemGrid items, int tileSize) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(items);
		var mapItems = items.copy();
		for (var item : mapItems.entrySet()) {
			var image = imagesGame.get(item.getValue().skin());
			graphics.drawImage(image, item.getKey().x()*tileSize, item.getKey().y() * tileSize, 
												 tileSize, tileSize, null);
		}
	}
	
  /**
   * Draws the player
   * 
   * @param graphics Graphics2D object used to draw
   * @param player the player to draw
   * @param tileSize the size of a tile
   */
	private void drawPlayer(Graphics2D graphics, Player player, int tileSize) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(player);
		var imagePlayer = imagesGame.get(player.skin());
		if (player.direction() == Direction.LEFT)
			graphics.drawImage(imagePlayer, player.position().x()*tileSize + tileSize, 
												 player.position().y()*tileSize, -tileSize, tileSize, null);
		else
			graphics.drawImage(imagePlayer, player.position().x()*tileSize, 
												 player.position().y()*tileSize, tileSize, tileSize, null);
		if (player.holdItem() != null) {
			var imageWeapon = imagesGame.get(player.holdItem().skin());
			if (player.direction() == Direction.LEFT) {
				graphics.drawImage(imageWeapon, player.position().x()*tileSize - (tileSize/4) + tileSize, 
													 player.position().y()*tileSize + (tileSize/4), -tileSize, tileSize, null);
			} else
				graphics.drawImage(imageWeapon, player.position().x()*tileSize + (tileSize/4), 
													 player.position().y()*tileSize + (tileSize/4), tileSize, tileSize, null);
		}
		// the hp bar
    graphics.setColor(Color.YELLOW);
    graphics.fill(new Rectangle2D.Float(player.position().x()*tileSize + (tileSize/4),  
    							player.position().y()*tileSize - (tileSize/3), 
    							player.health()*(tileSize/2)/player.maxHealth(), tileSize/10));
    graphics.setColor(Color.WHITE);
    graphics.draw(new Rectangle2D.Float(player.position().x()*tileSize + (tileSize/4),  
    							player.position().y()*tileSize - (tileSize/3), tileSize/2, tileSize/10));
    // the name of the player
  	if (player.name() != null) {
    	graphics.setFont(new Font("Arial", Font.BOLD, 15));
      graphics.drawString(player.name(), player.position().x()*tileSize + player.name().length(), 
      										player.position().y()*tileSize + 2);
  	}
	}
	
  /**
   * Draws the enemies
   * 
   * @param graphics Graphics2D object used to draw
   * @param enemies the enemies to draw
   * @param tileSize the size of a tile
   */
	private void drawEnemies(Graphics2D graphics, EnemiesGrid enemies, int tileSize) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(enemies);
		var enemyList = enemies.toList();
		for (var enemy : enemyList) {
			var imageEnemy = imagesGame.get(enemy.skin());
			if (enemy.direction() == Direction.LEFT)
				graphics.drawImage(imageEnemy, enemy.position().x()*tileSize + tileSize, 
													 enemy.position().y()*tileSize, -tileSize, tileSize, null);
			else
				graphics.drawImage(imageEnemy, enemy.position().x()*tileSize, 
													 enemy.position().y()*tileSize, tileSize, tileSize, null);
			// the hp bar
			graphics.setColor(Color.YELLOW);
	    graphics.fill(new Rectangle2D.Float(enemy.position().x()*tileSize + (tileSize/4),  
	    							enemy.position().y()*tileSize - (tileSize/3),
	    							enemy.health() * (tileSize / 2) / enemy.maxHealth(), tileSize / 10));
	    graphics.setColor(Color.WHITE);
	    graphics.draw(new Rectangle2D.Float(enemy.position().x() * tileSize + (tileSize/4),  
	    							enemy.position().y() * tileSize - (tileSize/3), 
	    							tileSize/2, tileSize/10));
	    // the name of the nemy
	    if (enemy.name() != null) {
  	  	graphics.setFont(new Font("Arial", Font.BOLD, 15));
  	    graphics.drawString(enemy.name(), enemy.position().x()*tileSize + enemy.name().length(), 
  	    										enemy.position().y()*tileSize + 2);
	    }
		}
	}
	
  /**
   * Draws and renders all of the elements of the game
   * 
   * @param game the game to render
   */
	public void drawGame(Game game){
		Objects.requireNonNull(game);
		var tileSize = tileSize(game.grid());
		var scrollVal = calcScroll(game.grid(), game.player(), tileSize);
    context.renderFrame(graphics -> {
    	var scroll = new AffineTransform();
    	scroll.translate(scrollVal[0], scrollVal[1]);
    	graphics.transform(scroll);
    	drawGrid(graphics, game.grid(), tileSize);
    	drawItems(graphics, game.items(), tileSize);
    	drawPlayer(graphics, game.player(), tileSize);
    	drawEnemies(graphics, game.enemies(), tileSize);
    });
	}
	
  /**
   * Calculates the size of a tile in the inventory
   * 
   * @param context the application context
   * @return the calculated size
   */
	private static int calculateInvSize(ApplicationContext context) {
		ScreenInfo screenInfo = context.getScreenInfo();
		float width = screenInfo.getWidth();
		float height = screenInfo.getHeight();
		var size = (int) width/(2*12);
		if (size > height/(2*3))
			return (int) height/(2*3);
		return size;
	}
	
	/**
   * Draws the inventory's background
   * 
   * @param graphics Graphics2D object used to draw
   * @param size the size of a tile in the inventory
   */
	private static void drawInvBackground(Graphics2D graphics, int size) {
		Objects.requireNonNull(graphics);
		graphics.setColor(Color.WHITE);
    graphics.fill(new Rectangle2D.Float(10,  10, size*10 + 20, size*3 + 20));
    graphics.setColor(new Color(100, 54, 107));
    graphics.fill(new Rectangle2D.Float(20,  20, size*10, size*3));
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 10; j++) {
				graphics.setColor(Color.GRAY);
	    	graphics.drawRect(j*size + 20, i*size + 20, size, size);
			}
		graphics.setColor(Color.WHITE);
    graphics.fill(new Rectangle2D.Float(size*10 + 20,  10, size*2 + 20, size*3 + 20));
    graphics.setColor(new Color(100, 54, 107));
    graphics.fill(new Rectangle2D.Float(size*10 + 30, 20, size*2, size*3));
	}
	
	/**
   * Draws the inventory
   * 
   * @param inventory the inventory to draw
   */
	public void drawInventory(Inventory inventory){
		Objects.requireNonNull(inventory);
		var size = calculateInvSize(context);
    context.renderFrame(graphics -> {
    	var i = 0; var j = 0;
	    drawInvBackground(graphics, size);
	    for(var items : inventory.toSet()) {
	    	var imageItem = imagesGame.get(items.getKey().skin());
				graphics.drawImage(imageItem, j*size + 20, i*size + 20, size - 5, size - 5, null);
	    	graphics.setColor(Color.WHITE);
	    	graphics.setFont(new Font("Arial", Font.BOLD, 15));
	      graphics.drawString("x" + items.getValue(), j*size + 22, i*size + size + 15);
	      if ((i*9 + j) == inventory.selection()) {
	      	if (items.getKey().name() == null)
	      		graphics.drawString("Nom : Aucun", size*10 + 40, 50);
	      	else
	      		graphics.drawString("Nom : " + items.getKey().name(), size*10 + 40, 50);
		      graphics.drawString("Quantité : " + items.getValue(), size*10 + 40, 70);
		      switch(items.getKey()) {
		      	case Weapon w -> graphics.drawString("Dégâts : " + items.getKey().damage(), size*10 + 40, 90);
		      	case Nutrient n -> graphics.drawString("Soigne 25% max PV", size*10 + 40, 90);
		      	case Exchangeable e -> graphics.drawString("Ne fait rien", size*10 + 40, 90);
		      }
	      }
	    	if (j >= 9) {
	    		j = 0; i++;
	    	} else
	    		j++;
	    }
	    graphics.setColor(Color.WHITE);
    	graphics.drawRect((inventory.selection()%10)*size + 20, 
    									  (inventory.selection()/10)*size + 20, size, size);
    });
	}
	
	/**
   * Returns the ApplicationContext of GraphicInterface
   * 
   * @return the ApplicationContext
   */
	public ApplicationContext context() {
		return context;
	}
	
	/**
   * Draws the end screen if the player won or lost
   * 
   * @param win true if the player just won
   */
	public void drawEndScreen(boolean win) {
		ScreenInfo screenInfo = context.getScreenInfo();
		float width = screenInfo.getWidth();
		float height = screenInfo.getHeight();
		context.renderFrame(graphics -> {
  		graphics.setColor(new Color(43, 23, 46));
      graphics.fill(new Rectangle2D.Float(0,  0, width, height));
      graphics.setColor(Color.WHITE);
    	graphics.setFont(new Font("Arial", Font.BOLD, 20));
    	if (win) {
    		graphics.drawString("Vous avez gagné !", width/2 - 200, height/2);
        graphics.drawString("Vous avez tué tous les enemis de la carte !", 
        										 width/2 - 200, height/2 + 50);
    	} else {
        graphics.drawString("Vous êtes mort !", width/2 - 200, height/2);
        graphics.drawString("Vous aurez plus de chance la prochaine fois...", 
        										 width/2 - 200, height/2 + 50);
    	}
    	graphics.drawString("Appuyez sur n'importe quelle touche pour fermer", 
					 width/2 - 200, height/2 + 100);
		});
	}
}
