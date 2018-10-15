package org.asmaster.fstrike;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import net.md_5.bungee.api.ChatColor;

public class FSMain extends JavaPlugin {
	
	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("strike")) {
			if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Only players may execute this command.");
			return true;
			}
		
		Faction faction = null;
		String Fac = "";
		Player p = (Player) sender;
		
		if(args.length == 0) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "/strike give [Faction] [Reason] | list [Faction]");
			return true;
		}
			
		else if(args[0].equalsIgnoreCase("give")) {
			if(p.hasPermission("afactionstrike.give")) {
				
				if (args.length == 1) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Please Specify a Faction.");
				}
				
				else {
			
				faction = Factions.getInstance().getByTag(args[1]);
				Fac = args[1];
				
				if (faction == null) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "That Faction Does Not Exist.");
					return true;
				}
			
				else if (args.length == 2) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Please Specify a Strike Reason.");
					return true;
				}
			
				String msg = "";
				for (int i = 2; i < args.length; i++) {
					msg += args[i] + " ";
				}
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
					if(fplayer.getFaction().getTag().equals(args[1])) {
						fplayer.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your Faction has been Striked by " + ChatColor.DARK_RED + p.getName() + ChatColor.RED + " for " + ChatColor.GREEN + msg);
					}
					else {
					//Do Nothing
					}
				}
				
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GREEN + args[1] + ChatColor.GREEN + " has Successfully been Given a Strike!");
				
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
							fplayer.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your Faction has Reached its Final Strike!");
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
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You do not have permission to execute that command!");
			}
		  }

			else if(args[0].equalsIgnoreCase("list")) {
				
				if (args.length == 1) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Please Specify a Faction.");
				}
				
				else {
			
				faction = Factions.getInstance().getByTag(args[1]);
				Fac = args[1];
					
					if (faction == null) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "That Faction Does Not Exist.");
						return true;
					}
					
				int strikeamount = 0;
				String msg1 = "";
				String msg2 = "";
				String msg3 = "";
				
				strikeamount = this.getConfig().getInt(args[1]);
				msg1 = this.getConfig().getString(args[1] + ": Reason 1: ", msg1);
				if (strikeamount == 1) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Their faction has recieved " + strikeamount + " strike");
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + msg1);
				}
				
				else if(strikeamount == 2) {
					msg1 = this.getConfig().getString(args[1] + ": Reason 1: ", msg1);
					msg2 = this.getConfig().getString(args[1] + ": Reason 2: ", msg2);
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Their faction has recieved " + strikeamount + " strikes");
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + msg1);
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + msg2);
				}
				
				
				else if(strikeamount == 3) {
					msg1 = this.getConfig().getString(args[1] + ": Reason 1: ", msg1);
					msg2 = this.getConfig().getString(args[1] + ": Reason 2: ", msg2);
					msg3 = this.getConfig().getString(args[1] + ": Reason 3: ", msg3);
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Their faction has recieved " + strikeamount + " strikes");
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + msg1);
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + msg2);
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + msg3);
				}
				
				else {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AFS" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Their faction has recieved no strikes!");
				}
			  }
		   }
		return true;
		}
	return false;
	}
}
