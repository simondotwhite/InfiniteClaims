package hskrasek;

import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
//import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class InfiniteClaims extends JavaPlugin
{
	Logger log = Logger.getLogger("Minecraft");	
	InfiniteClaims plugin; 
	
	public ServerPlayerListener playerListener = null;
	public int roadOffsetX = 4;
	public int roadOffsetZ = 4;
	public int plotHeight = 2;
	public String ownerSignPrefix = "";
	public int signPlacementMethod = 0;
	public boolean enableHome = false; 
	public String setHome = "sethome";
	public String goHome = "home";
	
	@Override
	public void onDisable()
	{
		FileConfiguration configFile = this.getConfig();
		
		configFile.set("RoadOffsetXAxis", roadOffsetX);
		configFile.set("RoadOffsetZAxis", roadOffsetZ);
		configFile.set("PlotHeight", plotHeight);
		configFile.set("OwnerSignPrefix", ownerSignPrefix);
		configFile.set("SignPlacementMethod", signPlacementMethod);
		configFile.set("enableHome", enableHome); 
		configFile.set("setHome", setHome);
		configFile.set("goHome", goHome);
		//Checks for changes in the config
		this.reloadConfig();
		//Saves the config
		this.saveConfig();
		
		
	}
	
	@Override
	public void onEnable()
	{
		//PluginManager pm = getServer().getPluginManager();
		FileConfiguration configFile = this.getConfig();
		
		roadOffsetX = configFile.getInt("RoadOffsetXAxis", 4);
		roadOffsetZ = configFile.getInt("RoadOffsetZAxis", 4);
		plotHeight = configFile.getInt("PlotHeight", 20);
		ownerSignPrefix = configFile.getString("OwnerSignPrefix");
		signPlacementMethod = configFile.getInt("SignPlacementMethod", 0);
		//home related
		enableHome = configFile.getBoolean("enableHome");
		setHome = configFile.getString("setHome", setHome);
		goHome = configFile.getString("goHome", goHome);
		
		this.saveConfig();
				
		playerListener = new ServerPlayerListener(this);		
		getServer().getPluginManager().registerEvents(playerListener, this);
//		pm.registerEvent(Event.Type.PLAYER_CHANGED_WORLD, this.playerListener, Event.Priority.Normal, this);
		
		logger("","");
	}
	
	public WorldGuardPlugin getWorldGuard()
	{
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	    logger("WorldGuard is Enabled","extended");
	    return (WorldGuardPlugin) plugin;
	}
	
	public WorldEditPlugin getWorldEdit()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		
		//WorldEdit may not be loaded
		if(plugin == null || !(plugin instanceof WorldEditPlugin)) {
			return null;
		}
		logger("WorldEdit is Enabled","extended");
		return (WorldEditPlugin)plugin;

	}
	
	public void logger(String msg, String type) {		
		//Pull config for extendedLogs
		FileConfiguration config = plugin.getConfig();
		final boolean extendedLog = config.getBoolean("extendedLog");
		//Create Constant Prefix
		PluginDescriptionFile pdf = plugin.getDescription();
		final String pluginPrefix = "[" + pdf.getName() + "] ";

		//Checks for extendedLogging
		if(type == "extended" && extendedLog == true) {
			this.log.info(pluginPrefix + msg); 
		} else if (type == "normal"){
			this.log.info(pluginPrefix + msg);

		}
	}
}