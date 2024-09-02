package fr.uge.main;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;

import fr.uge.thebigadventure.Game;
import fr.uge.cmdline.CmdLine;
import fr.uge.lexer.GameFromFile;
import fr.umlv.zen5.Application;

/**
 * Class used for the main of the program
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class Main {
	
	/**
	 * The main method of the game
	 * 
	 * @param args the command line entered by the user
	 */
  public static void main(String[] args) {
  	var cmdLine = new CmdLine();
  	cmdLine.parseCmdLine(args);
  	cmdLine.checkCmdLine();
  	// Validation of map
    var wrapper = new Object(){ Game game = null; };
    var path = Path.of(cmdLine.getArgument("--level"));
    try {
    	wrapper.game = GameFromFile.gameFromFile(path);
		} catch (IOException e) {
			System.err.println(e.getMessage() + " is not a map or does not exists");
			System.exit(1);
		}
    if (cmdLine.containsOption("--validate")) 
    	System.exit(0); // validation only
    System.out.println("Current map used : " + path);
  	if (cmdLine.containsOption("--dry-run"))
  		wrapper.game.changeRun(true);
  	// Start of the game
    Application.run(new Color(43, 23, 46), context -> {
    	try {
				wrapper.game.MainLoop(context);
			} catch (IOException | InterruptedException e) {
				System.err.println(e.getMessage());
				context.exit(1);
				System.exit(1);
			}
    });
  }
}
