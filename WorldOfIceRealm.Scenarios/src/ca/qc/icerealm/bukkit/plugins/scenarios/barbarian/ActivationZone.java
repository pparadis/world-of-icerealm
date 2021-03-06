package ca.qc.icerealm.bukkit.plugins.scenarios.barbarian;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class ActivationZone implements ZoneObserver {

	private WorldZone _zone;
	private ScenarioEvent _scenario;
	
	public ActivationZone(ScenarioEvent s, WorldZone z) {
		_scenario = s;
		_zone = z;
	}
	
	@Override
	public void setWorldZone(WorldZone z) {
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		_scenario.trigger();
	}

	@Override
	public void playerLeft(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Server getCurrentServer() {
		return _scenario.getServer();
	}

	
}
