package steam;


import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;

public class SteamMain {

	public boolean init() {

		try {
		SteamAPI.loadLibraries();
		
			if(SteamAPI.restartAppIfNecessary(480)) {
				System.err.println("Restarting through steam...");
				return false;
			}

			if (!SteamAPI.init()) {
		        System.err.println( "Steamworks initialization error, e.g. Steam client not running");
		        return false;
		    }
		} catch (SteamException e) {
		    // Error extracting or loading native libraries
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public void update() {
		if (SteamAPI.isSteamRunning()) {
		    SteamAPI.runCallbacks();
		}
	}
	
	public void destroy() {
//		stats.dispose();
		SteamAPI.shutdown();
	}
	
}
