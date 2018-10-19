package org.asmaster.fstrike;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;

import net.md_5.bungee.api.ChatColor;

public class FSMain extends JavaPlugin {
	
	public void onEnable() {
		PluginDescriptionFile pdf = this.getDescription();
		addConfigDefaults();
		translateConfigStrings();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.isOp()) {
				player.sendMessage(prefix + ChatColor.RED + "You're running" + ChatColor.DARK_RED + " AFactionStrike V" + pdf.getDescription());
			}
		}
	}
	
	public String prefix = this.getConfig().getString("prefix");
	
	public void addConfigDefaults() {
		getConfig().options().header("The prefix is the message that appears before the /strike command. The final-strike-cmd is the command that gets executed once a faction reaches 3 strikes.");
		this.getConfig().addDefault("prefix", "&8[&4AFS&8] ");
		this.getConfig().addDefault("final-strike-cmd", "f disband %faction%");
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.reloadConfig();
	}
	
	public void translateConfigStrings() {
		prefix = ChatColor.translateAlternateColorCodes('&', prefix);
	}
	
	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Faction faction = null;
		String Fac = "";
		
		if(cmd.getName().equalsIgnoreCase("strike")) {
			
		if(args.length == 0) {
			sender.sendMessage(prefix + ChatColor.RED + "/strike give [Faction] [Reason] | list [Faction]");
			return true;
		}
			
		else if((sender instanceof ConsoleCommandSender) || args[0].equalsIgnoreCase("give")) {
				if(sender.hasPermission("afactionstrike.give")) {
				
				if (args.length == 1) {
					sender.sendMessage(prefix + ChatColor.RED + "Please Specify a Faction.");
				}
				
				else {
			
				faction = Factions.getInstance().getByTag(args[1]);
				Fac = args[1];
				
				if (faction == null) {
					sender.sendMessage(prefix + ChatColor.RED + "That Faction Does Not Exist.");
					return true;
				}
			
				else if (args.length == 2) {
					sender.sendMessage(prefix + ChatColor.RED + "Please Specify a Strike Reason.");
					return true;
				}
			
				String msg = "";
				for (int i = 2; i < args.length; i++) {
					msg += args[i] + " ";
				}
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
					if(fplayer.getFaction().getTag().equals(args[1])) {
						fplayer.sendMessage(prefix + ChatColor.RED + "Your Faction has been Striked by " + ChatColor.DARK_RED + sender.getName() + ChatColor.RED + " for  " + ChatColor.GREEN + msg);
					}
					else {
					//Do Nothing
					}
				}
				
				sender.sendMessage(prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.GREEN + " has Successfully been Given a Strike!");
				
				Object level = this.getConfig().get(args[1]);
				Object message = this.getConfig().get(args[1] + ": Reason 1: ");
				Object message1 = this.getConfig().get(args[1] + ": Reason 2: ");
				Object message2 = this.getConfig().get(args[1] + ": Reason 3: ");
			
				if(level == null) {
					String msg1 = msg;
					String placeholder = "";
					this.getConfig().set(args[1], 1);
					this.getConfig().set(args[1] + ": Reason 1: ", msg1);
					this.getConfig().set(args[1] + ": Reason 2: ", placeholder);
					this.getConfig().set(args[1] + ": Reason 3: ", placeholder);
					this.saveConfig();
					return true;
				}
			
				int l = Integer.parseInt(level.toString());
			
				if(l == 1) {
					String msg2 = msg;
					this.getConfig().set(args[1], 2);
					this.getConfig().set(args[1] + ": Reason 2: ", msg2);
					this.saveConfig();
					return true;
				}
			
				if(l == 2) {
					String msg3 = msg;
					for(Player player : Bukkit.getOnlinePlayers()) {
						FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
						if(fplayer.getFaction().getTag().equals(args[1])) {
							fplayer.sendMessage(prefix + ChatColor.RED + "Your Faction has Reached its Final Strike!");
							
							String strikecommand = this.getConfig().getString("final-strike-cmd");
							strikecommand = strikecommand.replace("%faction%", args[1]);
							strikecommand = strikecommand.replace("%player%", sender.getName());
							getServer().dispatchCommand(getServer().getConsoleSender(), strikecommand);
						}
						else {
							//Do Nothing
						}
					}
					this.getConfig().set(args[1], 3);
					this.getConfig().set(args[1] + ": Reason 3: ", msg3);
					this.saveConfig();
					return true;
				}
			  }
			}
			else {
				sender.sendMessage(prefix+ ChatColor.RED + "You do not have permission to execute that command!");
			}
		  }

			else if(args[0].equalsIgnoreCase("list")) {
				if((sender instanceof ConsoleCommandSender) || sender.hasPermission("afactionstrike.list")) {
				
				if (args.length == 1) {
					sender.sendMessage(prefix + ChatColor.RED + "Please Specify a Faction.");
				}
				
				else {
			
				faction = Factions.getInstance().getByTag(args[1]);
				Fac = args[1];
					
					if (faction == null) {
						sender.sendMessage(prefix + ChatColor.RED + "That Faction Does Not Exist.");
						return true;
					}
					
				int strikeamount = 0;
				String msg1 = "";
				String msg2 = "";
				String msg3 = "";
				
				strikeamount = this.getConfig().getInt(args[1]);
				msg1 = this.getConfig().getString(args[1] + ": Reason 1: ", msg1);
				if (strikeamount == 1) {
				sender.sendMessage(prefix + ChatColor.RED + "Their faction has recieved " + strikeamount + " strike");
				sender.sendMessage(prefix + ChatColor.RED + msg1);
				}
				
				else if(strikeamount == 2) {
					msg1 = this.getConfig().getString(args[1] + ": Reason 1: ", msg1);
					msg2 = this.getConfig().getString(args[1] + ": Reason 2: ", msg2);
					sender.sendMessage(prefix + ChatColor.RED + "Their faction has recieved " + strikeamount + " strikes");
					sender.sendMessage(prefix + ChatColor.RED + msg1);
					sender.sendMessage(prefix + ChatColor.RED + msg2);
				}
				
				
				else if(strikeamount == 3) {
					msg1 = this.getConfig().getString(args[1] + ": Reason 1: ", msg1);
					msg2 = this.getConfig().getString(args[1] + ": Reason 2: ", msg2);
					msg3 = this.getConfig().getString(args[1] + ": Reason 3: ", msg3);
					sender.sendMessage(prefix + ChatColor.RED + "Their faction has recieved " + strikeamount + " strikes");
					sender.sendMessage(prefix + ChatColor.RED + msg1);
					sender.sendMessage(prefix + ChatColor.RED + msg2);
					sender.sendMessage(prefix + ChatColor.RED + msg3);
				}
				
				else {
					sender.sendMessage(prefix + ChatColor.RED + "Their faction has recieved no strikes!");
				}
			  }
		   }
				else {
					//Broken ATM Fix 
					if(sender instanceof Player) {
						FPlayer fp = FPlayers.getInstance().getByPlayer((Player) sender);
						String userfaction = fp.getFaction().getTag();
						String strikelistcommand = "strike list " + userfaction;
						if(userfaction == null) {
							sender.sendMessage(prefix + ChatColor.RED + "You do not have a faction!");
							return true;
						}
						else {
							String msg1 = "";
							String msg2 = "";
							String msg3 = "";
							int strikeamount = 0;
							
							strikeamount = this.getConfig().getInt(userfaction);
							msg1 = this.getConfig().getString(userfaction + ": Reason 1: ", msg1);
							if (strikeamount == 1) {
							sender.sendMessage(prefix + ChatColor.RED + "Your faction has recieved " + strikeamount + " strike");
							sender.sendMessage(prefix + ChatColor.RED + msg1);
							}
							
							else if(strikeamount == 2) {
								msg1 = this.getConfig().getString(userfaction + ": Reason 1: ", msg1);
								msg2 = this.getConfig().getString(userfaction + ": Reason 2: ", msg2);
								sender.sendMessage(prefix + ChatColor.RED + "Your faction has recieved " + strikeamount + " strikes");
								sender.sendMessage(prefix + ChatColor.RED + msg1);
								sender.sendMessage(prefix + ChatColor.RED + msg2);
							}
							
							
							else if(strikeamount == 3) {
								msg1 = this.getConfig().getString(userfaction + ": Reason 1: ", msg1);
								msg2 = this.getConfig().getString(userfaction + ": Reason 2: ", msg2);
								msg3 = this.getConfig().getString(userfaction + ": Reason 3: ", msg3);
								sender.sendMessage(prefix + ChatColor.RED + "Your faction has recieved " + strikeamount + " strikes");
								sender.sendMessage(prefix + ChatColor.RED + msg1);
								sender.sendMessage(prefix + ChatColor.RED + msg2);
								sender.sendMessage(prefix + ChatColor.RED + msg3);
							}
							
							else {
								sender.sendMessage(prefix + ChatColor.RED + "Your faction has recieved no strikes!");
							}
						}
					}
				}
		}
		return true;
		}
	return false;
	}
}
