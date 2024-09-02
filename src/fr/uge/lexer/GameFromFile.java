package fr.uge.lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import fr.uge.thebigadventure.Decoration;
import fr.uge.thebigadventure.Direction;
import fr.uge.thebigadventure.Enemy;
import fr.uge.thebigadventure.EnemiesGrid;
import fr.uge.thebigadventure.Exchangeable;
import fr.uge.thebigadventure.Game;
import fr.uge.thebigadventure.Grid;
import fr.uge.thebigadventure.ItemGrid;
import fr.uge.thebigadventure.Nutrient;
import fr.uge.thebigadventure.Obstacle;
import fr.uge.thebigadventure.Player;
import fr.uge.thebigadventure.Position;
import fr.uge.thebigadventure.Weapon;
import fr.uge.thebigadventure.Zone;

/**
 * Class used to create a Game from a file path
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class GameFromFile {
  /**
   * Main loop to parse the file
   * 
   * @param path the path of the file
   * @return the game created with the file
   * 
   * @throws IOException if the method can't open the file of the map
   */
  public static Game gameFromFile(Path path) throws IOException{
  	System.out.println("Validation of " + path + "...");
    ArrayList<Result> results = GameFromFile.resultsFromFile(path);
    if(results.isEmpty()) {
      throw new IllegalArgumentException("Empty File");
    }
    Player p = null;
    var items = new ItemGrid();
    var enemies = new EnemiesGrid();
    var obstacles = new HashMap<Position, Obstacle>();
    Grid grid = null;
    
    while(!results.isEmpty()) {
      if(checkIdentBracket(results, "grid")) {
        grid = readGrid(results);
      } else if(checkIdentBracket(results, "element")) {
        if(p == null) {
          p = readElement(results, items, enemies, obstacles);
        } else {
          readElement(results, items, enemies, obstacles);
        }
      } else {
        throw new IllegalArgumentException("Invalid file format");
      }
    }
    addObstacles(grid, obstacles);
    if (p == null)
    	throw new IllegalArgumentException("There is no player defined");
    System.out.println("OK");
    return new Game(grid, items, p, enemies);    
  }

  /**
   * Adds all Obstacles stored to the grid
   * 
   * @param grid the grid to fill
   * @param obstacles the map with the stored obstacles
   */
  private static void addObstacles(Grid grid, HashMap<Position, Obstacle> obstacles) {
    for(var entry: obstacles.entrySet()) {
      grid.add(entry.getKey(), entry.getValue());
    }
  }
  
  /**
   * Analyzes the different fields of the current element and add them in the right list
   * 
   * @param results the list of Result
   * @param items the items list to fill 
   * @param enemies the enemies list to fill
   * @param obstacles the obstacles list to fill
   * @return a Player if the element is a Player, else null
   */
  private static Player readElement(ArrayList<Result> results, ItemGrid items, EnemiesGrid enemies, HashMap<Position, Obstacle> obstacles) {
    String name = null;
    boolean player = false;
    String skin = null;
    Position position = null;
    int health = -1;
    String kind = null;
    Zone zone = null;
    String behavior = null;
    int damage = -1;
    while(!results.isEmpty() && results.get(0).token() == Token.IDENTIFIER
        && results.get(1).token() == Token.COLON) {
      String content = results.get(0).content();
      removeFromResults(results, 2);
      switch(content) {
      case "name": name = results.get(0).content(); removeFromResults(results, 1);
        break;
      case "player": player = Boolean.parseBoolean(results.get(0).content()); removeFromResults(results, 1);
        break;
      case "skin": skin = results.get(0).content(); removeFromResults(results, 1);
        break;
      case "position": position = checkPosition(results); removeFromResults(results, 5);
        break;
      case "health": health = Integer.parseInt(results.get(0).content()); removeFromResults(results, 1);
        break;
      case "kind": kind = results.get(0).content(); removeFromResults(results, 1);
        break;
      case "zone": zone = checkZone(results); removeFromResults(results, 5);
        break;
      case "behavior": behavior = results.get(0).content(); removeFromResults(results, 1);
        break;
      case "damage": damage = Integer.parseInt(results.get(0).content()); removeFromResults(results, 1);
        break;
      default:
        throw new IllegalArgumentException("The field \"" + content + "\" does not exist for the elements");
      }
    }
    if(player) {
      if(!(skin.equals("BABA") || skin.equals("BADBAD") || skin.equals("FOFO") || skin.equals("IT"))) {
        throw new IllegalArgumentException("Player skin must be BABA, BADBAD, FOFO or IT");
      }
      return new Player(name, skin, position, health, health, null, Direction.RIGHT);
    } else {
      switch(kind) {
      case "obstacle" : 
      	try {
          Enum.valueOf(Obstacles.class, skin);
          obstacles.put(position, new Obstacle(skin));
        } catch (Exception e) {
      		throw new IllegalArgumentException("the skin '" + skin + "' doesn't exist");
        }
        break;
      case "item":
        if(skin.equals("STICK") || skin.equals("SHOVEL") || skin.equals("SWORD")) {
          items.add(position, new Weapon(name, skin, damage));           
        } else {
          try {
            Enum.valueOf(Nutrients.class, skin);
            items.add(position, new Nutrient(name, skin));
          } catch (Exception e) {
            try {
              Enum.valueOf(Exchangeables.class, skin);
              items.add(position, new Exchangeable(name, skin));
            } catch(Exception f) {
              throw new IllegalArgumentException("the skin '" + skin + "' doesn't exist");
            }
          } 
        }
        break;
      case "enemy":
        try {
          Enum.valueOf(Enemies.class, skin);
          enemies.add(new Enemy(name, skin, position, zone, health, health, behavior, damage, Direction.RIGHT));
        } catch (Exception e) {
        	throw new IllegalArgumentException("the skin '" + skin + "' doesn't exist");
        } 
        break;
      default:
        throw new IllegalArgumentException("The field 'kind' is not defined or incorrect");
      }
    }
    return null;
  }
  
  /**
   * Checks if the syntax of the following results matches the syntax of a Zone
   * 
   * @param results the list of Result
   * @return a Zone if the element syntax is a Zone
   */
  private static Zone checkZone(ArrayList<Result> results) {
    Position pos = checkPosition(results);
    removeFromResults(results, 5);
    if(isSize(results)) {
      return new Zone(pos, Integer.parseInt(results.get(1).content()), Integer.parseInt(results.get(3).content()));
    }
    throw new IllegalArgumentException("Expected '(width x height)'");
  }

  /**
   * Checks if the syntax of the following results matches the syntax of a Position
   * 
   * @param results the list of Result
   * @return a Position if the element syntax is a Position
   */
  private static Position checkPosition(ArrayList<Result> results) {
    if(results.get(0).token() == Token.LEFT_PARENS 
        && results.get(1).token() == Token.NUMBER
        && results.get(2).token() == Token.COMMA
        && results.get(3).token() == Token.NUMBER
        && results.get(4).token() == Token.RIGHT_PARENS) {
      return new Position(Integer.valueOf(results.get(1).content()), Integer.valueOf(results.get(3).content()));
    }
    throw new IllegalArgumentException("Expected '(x,y)'");
  }

  /**
   * Gives the file to the Lexer and get the result in a list of Result
   * 
   * @param path the path of the file
   * @return a list of Result made from the Lexer result
   */
  private static ArrayList<Result> resultsFromFile(Path path) throws IOException {
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    var results = new ArrayList<Result>();
    Result result;
    while((result = lexer.nextResult()) != null) {
      results.add(result);
    }
    return results;
  }
  
  /**
   * Removes from the list of Result a given number of elements
   * 
   * @param results the list of Result
   * @param nb the number of elements to remove
   */
  private static void removeFromResults(ArrayList<Result> results, int nb) {
    for(int i = 0; i < nb; i++) {
      results.removeFirst();
    }
  }
  
  /**
   * Checks if the syntax of the following results matches the syntax of an identifier between brackets
   * 
   * @param results the list of Result
   * @return the identifier between the brackets
   */
  private static String checkBracket(List<Result> results) {
    if(results.size() >= 2 && results.get(0).token() == Token.LEFT_BRACKET
        && results.get(1).token() == Token.IDENTIFIER
        && results.get(2).token() == Token.RIGHT_BRACKET) {
      return results.get(1).content();
    }
    System.out.println(results);
    throw new IllegalArgumentException("Expected '[ident]'");
  }
  
  /**
   * Checks if the given identifier is between brackets
   * 
   * @param results the list of Result
   * @param ident the identifier to check
   * @return true if the given identifier is between brackets, else false
   */
  private static boolean checkIdentBracket(ArrayList<Result> results, String ident) {
    if(checkBracket(results).equals(ident)) {
      removeFromResults(results, 3);
      return true;
    }
    return false;
  }
  
  /**
   * Checks if the given identifier is before colon
   * 
   * @param results the list of Result
   * @param ident the identifier to check
   * @return true if the given identifier is before colon
   */
  private static boolean checkIdentColon(List<Result> results, String ident) {
    if(results.size() >= 2 && results.get(0).token() == Token.IDENTIFIER && results.get(0).content().equals(ident)
        && results.get(1).token() == Token.COLON) {
      return true;
    }
    throw new IllegalArgumentException("Expected '" + ident + ":'");
  }
  
  
  /**
   * Analyzes the different fields to create the grid
   * 
   * @param results the list of Result
   * @return the created Grid 
   */
  private static Grid readGrid(ArrayList<Result> results) {
    Grid grid = null;
    if(checkIdentColon(results, "size")) {
      removeFromResults(results, 2);
    }
    if(isSize(results)) {
      grid = new Grid(Integer.valueOf(results.get(1).content()), Integer.valueOf(results.get(3).content()));
      removeFromResults(results, 5);
    }
    if(checkIdentColon(results, "encodings")) {
      removeFromResults(results, 2);
    }
    HashMap<Character, String> encodings = new HashMap<Character, String>();
    while(results.size() > 3 && newEncoding(encodings, results));

    if(encodings.isEmpty()) {
      throw new IllegalArgumentException("Need at least 1 enconding");
    }
    if(checkIdentColon(results, "data")) {
      removeFromResults(results, 2);
      fillGrid(grid, results, encodings);
    } else {
      throw new IllegalArgumentException("Data is missing from grid");
    }
    return grid;
  }
  
  /**
   * Checks if the syntax of the following results matches the syntax of a Size
   * 
   * @param results the list of Result
   * @return true if the element syntax is a Size
   */
  private static boolean isSize(ArrayList<Result> results) {
    if(results.size() >= 5 && results.get(0).token() == Token.LEFT_PARENS 
        && results.get(1).token() == Token.NUMBER
        && results.get(2).token() == Token.IDENTIFIER && results.get(2).content().equals("x")
        && results.get(3).token() == Token.NUMBER
        && results.get(4).token() == Token.RIGHT_PARENS) {
      return true;
    }
    throw new IllegalArgumentException("Expected '(width x height)'");
  }
  
  /**
   * Check if the next results is a valid encoding and add it to the encodings map
   * 
   * @param encodings the encodings map to fill 
   * @param results the list of Result
   * @return true if the encoding is valid
   */
  private static boolean newEncoding(HashMap<Character, String> encodings, ArrayList<Result> results) {
    try {
      checkIdentColon(results, "data");
      return false;
    } catch (IllegalArgumentException e) {
    }
    if(checkEncoding(results)) {
      if(encodings.putIfAbsent(results.get(2).content().charAt(0), results.get(0).content()) != null) {
        throw new IllegalArgumentException("Encoding '" + results.get(2).content().charAt(0) + "' defined twice");
      } else {
        removeFromResults(results, 4);
        return true;
      }
    }
    return false;
  }
  
  /**
   * Checks if there is an identifier between parens
   * 
   * @param results the list of Result
   * @return true if there is an identifier between parens
   */
  private static boolean checkEncoding(ArrayList<Result> results) {
    if(results.size() >= 4 && results.get(0).token() == Token.IDENTIFIER
        && results.get(1).token() == Token.LEFT_PARENS
        && results.get(2).token() == Token.IDENTIFIER && results.get(2).content().length() == 1
        && results.get(3).token() == Token.RIGHT_PARENS) {
      return true;
    }
    throw new IllegalArgumentException("Expected 'SKIN(code)' where 'code' is a char");
  }
  
  /**
   * Read the text containing the map data to fill the grid
   * 
   * @param grid the grid to fill
   * @param results the list of Result
   * @param encodings the map of the defined encodings to read the map
   */
  private static void fillGrid(Grid grid, ArrayList<Result> results, HashMap<Character, String> encodings) {
    if(results.get(0).token() == Token.QUOTE) {
      int nbLine = 0;
      Scanner scanner = new Scanner(results.get(0).content());
      int size = 0;
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        line = line.trim();
        if(line.startsWith("\"\"\"")) {
          continue;
        }
        size += line.length();
        for(int i = 0; i < line.length(); i++) {
          char key = line.charAt(i);
          if(encodings.containsKey(key)) {
            try {
              Enum.valueOf(Obstacles.class, encodings.get(key));
              grid.add(new Position(i, nbLine), new Obstacle(encodings.get(key)));
            } catch (Exception e) {
              try {
                Enum.valueOf(Decorations.class, encodings.get(key));
                grid.add(new Position(i, nbLine), new Decoration(encodings.get(key)));
              } catch (Exception f) {
                throw new IllegalArgumentException("Unknown tile for encoding " + encodings.get(key));
              } 
            } 
          }
        }
        nbLine++;
      }
      scanner.close();
      if(size != grid.height() * grid.length()) {
        throw new IllegalArgumentException("Invalid size of data");
      }
    }
    removeFromResults(results, 1);
  }
}
