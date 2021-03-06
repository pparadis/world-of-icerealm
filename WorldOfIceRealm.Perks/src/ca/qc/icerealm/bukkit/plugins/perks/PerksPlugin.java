package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.perks.archer.ArcherTree;
import ca.qc.icerealm.bukkit.plugins.perks.archer.FindWeaknessPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.LeatherExpertPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.LightningReflexesPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.MansBestFriendPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.PoisonedArrowPerk;
import ca.qc.icerealm.bukkit.plugins.perks.archer.WindRunPerk;
import ca.qc.icerealm.bukkit.plugins.perks.magic.ExecuteMagicEvent;
import ca.qc.icerealm.bukkit.plugins.perks.magic.FoodLevelRegenerationObserver;
import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicFoodLevelEvent;
import ca.qc.icerealm.bukkit.plugins.perks.magic.fire.FireDamageModifier;
import ca.qc.icerealm.bukkit.plugins.perks.magic.fire.FireTree;
import ca.qc.icerealm.bukkit.plugins.perks.magic.fire.FireStoper;
import ca.qc.icerealm.bukkit.plugins.perks.survivor.ExplorerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.survivor.GreenThumbPerk;
import ca.qc.icerealm.bukkit.plugins.perks.survivor.MercenaryPerk;
import ca.qc.icerealm.bukkit.plugins.perks.survivor.SurvivorTree;
import ca.qc.icerealm.bukkit.plugins.perks.survivor.StoneWorkerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.survivor.VassalPerk;
import ca.qc.icerealm.bukkit.plugins.perks.survivor.WoodmanPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.BerserkerPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.LastManStandingPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.LifeLeechPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.MeatShieldPerk;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.WarriorTree;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class PerksPlugin extends JavaPlugin {

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		registerEvents();
		
		getCommand("perk").setExecutor(new PerkCommandExecutor(this));
	}

	private void registerEvents() {
		// Settler
		getServer().getPluginManager().registerEvents(new StoneWorkerPerk(), this);
		getServer().getPluginManager().registerEvents(new VassalPerk(), this);
		getServer().getPluginManager().registerEvents(new GreenThumbPerk(), this);
		getServer().getPluginManager().registerEvents(new WoodmanPerk(), this);
		
		// Warrior
		getServer().getPluginManager().registerEvents(new MercenaryPerk(), this);
		getServer().getPluginManager().registerEvents(new MeatShieldPerk(), this);
		getServer().getPluginManager().registerEvents(new BerserkerPerk(), this);
		getServer().getPluginManager().registerEvents(new LifeLeechPerk(), this);
		getServer().getPluginManager().registerEvents(new LastManStandingPerk(), this);

		// Archer
		getServer().getPluginManager().registerEvents(new ExplorerPerk(), this);
		getServer().getPluginManager().registerEvents(new PoisonedArrowPerk(), this);
		getServer().getPluginManager().registerEvents(new FindWeaknessPerk(), this);
		WindRunPerk windRunPerk = new WindRunPerk();
		getServer().getPluginManager().registerEvents(windRunPerk, this);
		getServer().getPluginManager().registerEvents(new LightningReflexesPerk(windRunPerk), this);
		getServer().getPluginManager().registerEvents(new LeatherExpertPerk(), this);
		//MansBestFriendPerk mansBestFriendPerk = new MansBestFriendPerk(getServer());
		//Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(mansBestFriendPerk, 1000, 1000, TimeUnit.MILLISECONDS);
		//getServer().getPluginManager().registerEvents(mansBestFriendPerk, this);
		
		PerkService.getInstance().addTree(new SurvivorTree());
		PerkService.getInstance().addTree(new ArcherTree());
		PerkService.getInstance().addTree(new WarriorTree());
		
		// Magic 
		getServer().getPluginManager().registerEvents(new ExecuteMagicEvent(), this);
		getServer().getPluginManager().registerEvents(new FireStoper(), this);
		getServer().getPluginManager().registerEvents(new FireDamageModifier(), this);	
		getServer().getPluginManager().registerEvents(new MagicFoodLevelEvent(), this);	
		
		// Add a time observer for mana (food) regeneration
		TimeServer.getInstance().addListener(new FoodLevelRegenerationObserver(this.getServer()), 1000);
		
		PerkService.getInstance().addTree(new FireTree());
		
		// Other
		getServer().getPluginManager().registerEvents(PerkService.getInstance(), this);
		getServer().getPluginManager().registerEvents(new ClearPerk(), this);
		getServer().getPluginManager().registerEvents(new PerkNotifier(), this);
	}
	
}