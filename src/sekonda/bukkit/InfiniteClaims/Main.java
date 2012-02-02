package sekonda.bukkit.InfiniteClaims;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.generator.ChunkGenerator;
//import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.*;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Main extends JavaPlugin implements Listener {

	//private static final Listener CommandListener = null;
	
	Logger log = Logger.getLogger("Minecraft");
	Main plugin, instance;
	
	@Override
	public void onEnable(){
		
		FileConfiguration config = this.getConfig();
		//Try config, if not create one with the following values. 
		new File(getDataFolder() + "config.yml");
		try {
			//Extended Logs
			if(!config.contains("extendedLog")) {
				config.set("extendedLog", false);
			}
			//Plot Config
			if(!config.contains("plots.X-axis")) {
				config.set("plots.X-axis", 4);
			}
			if(!config.contains("plots.Z-axis")) {
				config.set("plots.Z-axis", 4);
			}
			if(!config.contains("plots.height")) {
				config.set("plots.height", 20);
			}
			//Sign Config
			if(!config.contains("signs.enabled")) {
				config.set("signs.enabled", true);
			}
			if(!config.contains("signs.placement")) {
				config.set("signs.placement", 0);
			}
			if(!config.contains("signs.prefix")) {
				config.set("signs.prefix", "Plot Owner:");
			}
			//Save Config
			saveConfig();
		} catch(Exception e1){
			e1.printStackTrace();
		}
		
		getServer().getPluginManager().registerEvents(this, this);
		
		//send to console
		logger("Has been enabled.","normal");
	}

	@Override
	public void onDisable(){
		//Reload and Save config before shutdown
		reloadConfig();
		saveConfig();
				
		//send to console
		logger("has been disabled.","normal");
	}	
	
	//Check player commands
	@EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		
		//getconfig for use in commands
		FileConfiguration config = this.getConfig();
		final int Xaxis = config.getInt("plot.X-axis");
		final int Zaxix = config.getInt("plots.Z-axis");
		final int plotHeight = config.getInt("plot.height"); 
    	final boolean signsEnabled = config.getBoolean("signs.enabled");
    	final int signsPlacement = config.getInt("signs.placement");
    	final String signsPrefix = config.getString("signs.prefix");
		//Get Player
    	String playerName = sender.getName();
    	Player player = Bukkit.getPlayerExact(playerName);
    	
    	
    	World w = player.getWorld();
		ChunkGenerator cg = w.getGenerator();
		
		WorldGuardPlugin wgp = plugin.getWorldGuard();
		WorldEditPlugin wep = plugin.getWorldEdit();
		
    	RegionManager rm = wgp.getRegionManager(w);
		int playerRegionCount = rm.getRegionCountOfPlayer(plugin.getWorldGuard().wrapPlayer(player));
    	
		//check for myplot / plothome
		if(cmd.getName().equalsIgnoreCase("myplot") || cmd.getName().equalsIgnoreCase("plothome")){ 
    		   		
    		Map<String, ProtectedRegion> ownedRegions = rm.getRegions();
			ProtectedRegion firstOwnedRegion = null;
			Set<String> keySet = ownedRegions.keySet();
			Object[] keys = keySet.toArray();
			
			for(int i = 0; i < keys.length; i++)
			{
				String key = keys[i].toString();
				
				if(key.startsWith(playerName.toLowerCase()))
				{
					firstOwnedRegion = ownedRegions.get(keys[i]);
					break;
				}
			}
			
			if(firstOwnedRegion != null)
			{
				sender.sendMessage(ChatColor.RED + "[InfiniteClaims] " + ChatColor.WHITE + "Owned region found!  Teleporting " + sender.getName() + "!");					
				BlockVector minPoint = firstOwnedRegion.getMinimumPoint();
				Location teleportLocation = new Location(w, minPoint.getX(), plotHeight + 1, minPoint.getZ());
				player.teleport(teleportLocation);	
				 
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "[InfiniteClaims] " + ChatColor.WHITE + "No owned region found!");
			}
    		
    		
    		return true;
    	} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
    	if(cmd.getName().equalsIgnoreCase("clearplot")) {
    		sender.sendMessage("You issues the "+ChatColor.RED + "clearplot" + ChatColor.WHITE + "command correctly.");
    		return true;
    	}
    	if(cmd.getName().equalsIgnoreCase("getplot")) {
    		sender.sendMessage("You issues the "+ChatColor.RED + "getplot" + ChatColor.WHITE + "command correctly.");
    		return true;
    	}
		
		return false; 
    }
	

	//Logger Method
	public void logger(String msg, String type) {
		
		//Pull config for extendedLogs
		FileConfiguration config = this.getConfig();
		final boolean extendedLog = config.getBoolean("extendedLog");
		//Create Constant Prefix
		PluginDescriptionFile pdf = this.getDescription();
		final String pluginPrefix = "[" + pdf.getName() + "] ";
		
		//Checks for extendedLogging
		if(type == "extended" && extendedLog == true) {
			this.log.info(pluginPrefix + msg); 
		} else if (type == "normal"){
			this.log.info(pluginPrefix + msg);
		}
	}
	
	public WorldGuardPlugin getWorldGuard()	{
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	
	public WorldEditPlugin getWorldEdit() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		
		//WorldEdit may not be loaded
		if(plugin == null || !(plugin instanceof WorldEditPlugin)) {
			return null;
		}
		
		return (WorldEditPlugin)plugin;
	}
	
}