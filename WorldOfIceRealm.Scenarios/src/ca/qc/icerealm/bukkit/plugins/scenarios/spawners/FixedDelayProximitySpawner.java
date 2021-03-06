package ca.qc.icerealm.bukkit.plugins.scenarios.spawners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.ScenarioServerProxy;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class FixedDelayProximitySpawner implements Runnable, ZoneObserver {

	private long _cooldownInterval = 60000; // 1 minute
	private long _intervalbetweenSpawn = 500; // 0.5sec
	private int _radius = 10;
	private int _nbOfMonsters = 1;
	private Location _location; 
	private Server _server;	
	private boolean _coolDownActive = false;
	private WorldZone _activationZone;
	private String[] _monsters = { "zombie", "spider", "skeleton" }; // le set de monstres par defaut
	private List<LivingEntity> _monstersList;
	private double _modifier = 1.0;
	private int _monsterSpawned = 0;
	private ZoneSubject _zoneServer;
	private ScheduledExecutorService _executor;
	
	public FixedDelayProximitySpawner(Server s) {
		_server = s;
		_monstersList = new ArrayList<LivingEntity>();
		_zoneServer = ScenarioServerProxy.getInstance().getZoneServer();
		_executor = Executors.newSingleThreadScheduledExecutor();
		
	}
	
	public void setConfiguration(String config) {
		// on parse la string!
		
	}
	
	public void setLocation(Location l) {
		_location = l;
		_modifier = Frontier.getInstance().calculateGlobalModifier(l);
		_activationZone = new WorldZone(l, _radius);
		_zoneServer.addListener(this);
	}
	
	public void disable() {
		_coolDownActive = true;
		_executor.shutdown();
	}
	
	public void enable() {
		_coolDownActive = false;
	}
	
	public void reset() {
		_coolDownActive = false;
		_monsterSpawned = 0;
		_monstersList.clear();
	}

	@Override
	public void run() {
		if (!_coolDownActive && _location != null) {
			
			World w = _location.getWorld();
			
			if(_intervalbetweenSpawn == 0) {
				// on spawn les mosntres!
				for (int i = 0; i < _nbOfMonsters; i++) {
					LivingEntity monster = (LivingEntity) ScenarioService.getInstance().spawnCreature(w, _location, pickRandomMonster(), _modifier, false);
					_monstersList.add(monster);
					_monsterSpawned++;
				}
				_coolDownActive = true;
				_executor.schedule(new SpawnerActivator(this), _cooldownInterval, TimeUnit.MILLISECONDS);
			}
			else if (_monsterSpawned < _nbOfMonsters){
				LivingEntity monster = (LivingEntity) ScenarioService.getInstance().spawnCreature(w, _location, pickRandomMonster(), _modifier, false);
				_monstersList.add(monster);
				_monsterSpawned++;
				_executor.schedule(this, _intervalbetweenSpawn, TimeUnit.MILLISECONDS);
			}
			else {
				// on reactive ce spawner la!
				_coolDownActive = true;
				_executor.schedule(new SpawnerActivator(this), _cooldownInterval, TimeUnit.MILLISECONDS);
			}
		}
	}

	@Override
	public void setWorldZone(WorldZone z) {
		_activationZone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _activationZone;
	}

	@Override
	public void playerEntered(Player p) {
		run();
	}

	@Override
	public void playerLeft(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
	private String[] parseMonsters(String m) {
		String[] parsed = m.split("+");
		if (parsed.length > 0) {
			return parsed;
		}
		return new String[] { "zombie", "spider", "skeleton" };
	}
	
	private EntityType pickRandomMonster() {
		return EntityUtilities.getEntityType(_monsters[RandomUtil.getRandomInt(_monsters.length)]);
	}
}

class SpawnerActivator implements Runnable {

	private FixedDelayProximitySpawner _spawner;
	
	public SpawnerActivator(FixedDelayProximitySpawner s) {
		_spawner = s;
	}
	
	@Override
	public void run() {
		_spawner.reset();
	}
	
}
