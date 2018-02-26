package info.elexis;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ElexisServer {

	public static final Path getElexisServerHomeDirectory() {
		 String userHomeProp = System.getProperty("user.home");
		return Paths.get(userHomeProp, "elexis-server");
	}
	
}
