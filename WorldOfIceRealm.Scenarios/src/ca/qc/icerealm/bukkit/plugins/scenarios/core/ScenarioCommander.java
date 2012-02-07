package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


class ScenarioCommander implements CommandExecutor {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private ScenarioEngine _engine;
	private Server _server;
	
	public ScenarioCommander(ScenarioEngine engine, Server s) {
		_engine = engine;
		_server = s;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		Player p = _server.getPlayer(arg0.getName());
		
		if (arg3.length > 0 && arg3[0].contains("info")) {

			Scenario s = _engine.findScenarioByPlayer(p);
			if (s != null) {
				p.sendMessage(ChatColor.GREEN + "You are in the " + s.getName());
			}
			else {
				p.sendMessage(ChatColor.RED +"You are not in a scenario");
			}
			
		}
		else if (arg3.length > 0 && arg3[0].contains("list")) {
			
			List<Scenario> list = _engine.getScenarios();
			if (list.size() == 0) {
				p.sendMessage(ChatColor.RED + "Scenarios not available");
			}
			else {
				p.sendMessage(ChatColor.GOLD + "Scenarios available:");
			
			}
			for (Scenario s : list) {
				p.sendMessage(ChatColor.GOLD + s.getName() + " at: " + s.getZone().getCentralPointAt(100));
			}
			
		}
		else if (arg3.length > 0 && arg3[0].contains("killhard")) {
			
			if (arg0.isOp()) {
				for (LivingEntity e : _server.getWorld("world").getLivingEntities()) {
					e.remove();
					
					if (e instanceof EnderDragon) {
						e.remove();
						_server.broadcastMessage("An EnderDragon has been removed by an admin");
					}
					
					if (e instanceof Ghast) {
						e.remove();
						_server.broadcastMessage("A Ghast has been removed by an admin");
					}
					
				}
			}
			else {
				arg0.sendMessage("You are not an operator. This will be reported.");
			}
				
			
		}
		
		return false;
	}

}
