package ca.qc.icerealm.bukkit.plugins.zone;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;

public class ZoneServer implements ZoneSubject {
	public static final Logger logger = Logger.getLogger(("Minecraft"));
	private List<ZoneObserver> _observers;
	private Server _server;
	private Thread _thread;
	private ZoneProber _prober;

	private static ZoneServer _instance;
	
	protected ZoneServer() {
		_observers = new ArrayList<ZoneObserver>();
		_thread = new Thread(new ZoneProber());
		_thread.start();
	}
	
	public static ZoneServer getInstance() {
		if (_instance ==  null) {
			_instance = new ZoneServer();
			logger.info("ZONE SERVER GETINSTANCE!!!!");
		}
		return _instance;
	}
	
	public void setServer(Server s) {
		_server = s;
	}
	
	public Server getServer() {
		return _server;
	}
	
	public List<ZoneObserver> getObservers() {
		
		List<ZoneObserver> copy = new ArrayList<ZoneObserver>();
		for (ZoneObserver o : _observers)  {
			copy.add(o);
		}
		return copy;
	}
	
	@Override
	public void addListener(ZoneObserver obs) {
		if (obs != null) {
			_observers.add(obs);
			displayInfo("addlistener - zone: " + obs.getWorldZone().toString());
		}
	}

	@Override
	public void removeListener(ZoneObserver obs) {
		if (obs != null && _observers.contains(obs)) {
			_observers.remove(obs);
			displayInfo("removeListener - zone: " + obs.getWorldZone().toString());
		}
	}

	@Override
	public void stopListening() {
		_prober.setStop(true);
	}

	@Override
	public void startListening() {
		_prober.setStop(false);
	}
	
	private void displayInfo(String info) {
		this.logger.info("[ZoneServer] " + info);
	}
}
