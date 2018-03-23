package de.sveh.simpleserverclient.server.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface ILogger {

	static void log(LogType type, String message) {
		
		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		System.out.println(time + " " + type.getName() + ": " + message);
	}
}
