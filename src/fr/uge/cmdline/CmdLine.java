package fr.uge.cmdline;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * The class used to parse the command line before the program launches
 * 
 * @author BERNIER Aurélien
 * @author BLONDEL Lucas
 */
public class CmdLine {
	/**
   * The command line parsed
   */
	private final HashMap<String, String> cmdLine;
	
	/**
   * Constructor of CmdLine
   */
	public CmdLine() {
		this.cmdLine = new HashMap<>();
	}
	
	/**
   * Returns if the string is an argument or not
   * 
   * @param arg the string to test
   * @return true if it is an argument, false otherwise
   */
	private static boolean isOption(String arg) {
		Objects.requireNonNull(arg);
		return !arg.equals("--level") && !arg.equals("--validate") && !arg.equals("--dry-run");
	}
	
	/**
   * Parses the command line
   * 
   * @param args the arguments of the program
   */
	public void parseCmdLine(String[] args) {
		var set = new HashSet<String>();
		for (int i = 0; i < args.length; i++) {
			if (!set.add(args[i]))
				throw new IllegalArgumentException(args[i] + " : duplicate argument");
			String argument = null;
			if (args[i].equals("--level") && args.length > (i + 1) && isOption(args[i + 1]))
				argument = args[i + 1];
			if (!isOption(args[i]))
				cmdLine.put(args[i], argument);
		}
	}
	
	/**
   * Checks if all of the options in the command line are correct 
   * and if it contains at least --validate or --level
   */
	public void checkCmdLine() {
		for (var arg : cmdLine.entrySet()) {
			if (arg.getKey().equals("--level") && (arg.getValue() == null || !arg.getValue().endsWith(".map")))
				throw new IllegalArgumentException("--level must have a map file specified with .map format");
		}
		if (!cmdLine.containsKey("--level")) 
			throw new IllegalArgumentException("Must have --level <map> to start the game");
	}
	
	/**
   * Returns if the command line contains the specified option
   * 
   * @param option the option to search
   * @return true if it contains the option specified
   */
	public boolean containsOption(String option) {
		Objects.requireNonNull(option);
		return cmdLine.containsKey(option);
	}
	
	/**
   * Returns the argument corresponding to the specified option
   * 
   * @param option the option to search
   * @return the argument of the option specified if it exists
   */
	public String getArgument(String option) {
		Objects.requireNonNull(option);
		return cmdLine.getOrDefault(option, null);
	}
}
