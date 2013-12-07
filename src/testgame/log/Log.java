package testgame.log;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	private final static Logger logger = Logger.getLogger(Log.class
			.getName());
	private static ConsoleHandler consoleHandler = null;

	public static void init() {
		consoleHandler = new ConsoleHandler();
		Logger logger = Logger.getLogger("");
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);
		logger.setLevel(Level.CONFIG);
	}
	
	public static void info(String message) {
		Log.init();
		logger.log(Level.INFO, message);
	}
}