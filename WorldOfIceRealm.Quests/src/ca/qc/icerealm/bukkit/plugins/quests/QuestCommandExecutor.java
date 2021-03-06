package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.quests.persistance.QuestLogPersister;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLog;
import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;

public class QuestCommandExecutor implements CommandExecutor {
	
	private static final String QuestParamHelp = "help";
	private static final String QuestCommandName = "q";
	private static final String QuestParamRandom = "random";
	private static final String QuestParamLog = "log";
	private static final String QuestParamGive = "give";
	private static final String QuestParamList = "list";
	private static final String QuestParamInfo = "info";
	private static final String QuestParamGiveAll = "giveall";
	private final QuestPlugin quests;

	public QuestCommandExecutor(QuestPlugin quests) {
		this.quests = quests;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] params) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			
			if (commandName.equalsIgnoreCase(QuestCommandName)) {
				
				if (params == null || params.length == 0 || (params.length > 0 && params[0].equalsIgnoreCase(QuestParamRandom))) {
					giveRandomQuest(player);
				} else if (params[0].equalsIgnoreCase(QuestParamLog)) {
					displayQuestLog(player);
				} else if (params[0].equalsIgnoreCase(QuestParamGiveAll)) {
					giveAll(player);
				} else if (params.length >= 2 && params[0].equalsIgnoreCase(QuestParamGive)) {
					giveQuest(player, params[1]);
				} else if (params[0].equalsIgnoreCase(QuestParamHelp)) {
					displayHelp(player);
				} else if (params[0].equalsIgnoreCase(QuestParamList)) {
					displayList(player);
				} else if (params.length >= 2 && params[0].equalsIgnoreCase(QuestParamInfo)) {
					displayQuestInfo(player, params[1]);
				} else if (params.length == 1) {
					giveQuest(player, params[0]);
				}
			}
			
			return true;
		} else {
			return false;
		}
	}

	private void giveAll(Player player) {
		this.quests.getScriptedQuestService().giveAll(player);
	}

	private void displayQuestInfo(Player player, String questId) {
		QuestLog questLog = QuestLogService.getInstance().getQuestLogForPlayer(player.getName());
		Quest quest = questLog.getQuestByKey(questId);
		
		if (quest != null) {
			quest.info();
		}
	}

	private void displayList(Player player) {
		this.quests.getScriptedQuestService().listQuest(player);
	}

	private void displayHelp(Player player) {
		player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Quest help");
		player.sendMessage(ChatColor.LIGHT_PURPLE + "[ " + ChatColor.YELLOW + "random, give [name], log, info [name], list, help" + ChatColor.LIGHT_PURPLE + " ]");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/q " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "gives random quest.");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/q random " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "gives random quest.");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/q log          " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "displays the quest log.");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/q give [name] " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "gives a quest by its name.");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/q info [name] " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "displays information about given quest name.");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/q list " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "lists available quests.");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/q help " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "this blob.");		
	}

	private void giveQuest(Player player, String questId) {
		Quest quest = this.quests.getScriptedQuestService().assignQuest(player, questId);
		
		if (quest != null) {
			quest.info();
		}
	}

	private void displayQuestLog(Player player) {
		QuestLogService.getInstance().displayLogForPlayer(player);
	}

	private void giveRandomQuest(Player player) {
		this.quests.getRandomQuestService().assignRandomQuest(player);
	}
}
