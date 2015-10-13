package me.endureblackout.playerfreeze;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FreezeMain extends JavaPlugin implements Listener {
	
	public void onEnable() {
		getLogger().info("PlayerFreeze enabled");
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public ArrayList<String> frozen = new ArrayList<String>();
	int freeze = 0;
	int fc = 0;
	
	@EventHandler
	public void onPlayerChatEvent(PlayerChatEvent e) {
		
		if(fc == 1 && e.getPlayer().hasPermission("pfreeze.admin")) {
			return;
		}
		
		if(fc == 0) {
			return;
		} else if(fc == 1){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.DARK_RED + "Chat is currently muted!");
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		
		Player p = e.getPlayer();
		
		if((freeze == 1) && (p.hasPermission("pfreeze.admin"))) {
			return;
		}
		
		if(freeze == 0) {
			return;
		} else if(freeze == 1) {
			p.teleport(p);
		}
		
		if(this.frozen.contains(p.getName().toString())) {
			p.teleport(p);
			p.sendMessage(ChatColor.DARK_RED + "You are frozen!");
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("freeze")) {
			Player p = (Player) sender;
			
			if(args.length == 0) {
				if((freeze == 0) && (p.hasPermission("pfreeze.admin"))) {
					freeze = 1;
					p.sendMessage(ChatColor.AQUA + "Use the /freeze command again to unfreeze all players");
					this.getServer().broadcastMessage(ChatColor.DARK_RED + "All players frozen by " + p.getDisplayName());
				} else if((freeze == 1) && (p.hasPermission("pfreeze.admin"))) {
					freeze = 0;
					this.getServer().broadcastMessage(ChatColor.GREEN + "All players unfrozen.");
				}
			}
			
			if(args.length == 1) {
				if(!this.frozen.contains(args[1].toString())){
					this.frozen.add(args[1].toString());
					p.sendMessage(ChatColor.DARK_RED + "You have frozen the player " + args[1]);
				} else if(this.frozen.contains(args[1].toString())) {
					this.frozen.add(args[1].toString());
					p.sendMessage(ChatColor.GREEN + "You have unfrozen tha player " + args[1]);
				}
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("freezechat")) {
			Player p = (Player) sender;
			
			if(p.hasPermission("pfreeze.admin")) {
				if(fc == 0) {
					fc = 1;
					Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Chat frozen by " + p.getDisplayName());
				} else if(fc == 1) {
					fc = 0;
					Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Chat unfrozen!");
				}
			}
		}
		
		return false;
	}

}