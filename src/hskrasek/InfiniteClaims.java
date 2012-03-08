package hskrasek;

import java.io.File;
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
		this.reloadConfig();
		//Saves the config
		this.saveConfig();
		logger("config file has been saved.", "extended");
		
		logger("has been disabled.","normal");
	}
	
	@Override
	public void onEnable()
	{
		//PluginManager pm = getServer().getPluginManager();		
		FileConfiguration config = this.getConfig();
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
			//Sign Config
			if(!config.contains("homes.enabled")) {
				config.set("homes.enabled", true);
			}
			if(!config.contains("signs.sethome")) {
				config.set("home.sethome", "sethome");
			}
			if(!config.contains("home.gohome")) {
				config.set("home.gohome", "home");
			}
			//Save Config
			saveConfig();
		} catch(Exception e1){
			e1.printStackTrace();
		}

		roadOffsetX = config.getInt("plots.X-axis");
		roadOffsetZ = config.getInt("plots.Y-axis");
		plotHeight = config.getInt("plots.height");
		ownerSignPrefix = config.getString("signs.prefix");
		signPlacementMethod = config.getInt("signs.placement");
		//home related
		enableHome = config.getBoolean("homes.enabled");
		setHome = config.getString("homes.sethome");
		goHome = config.getString("homes.gohome");
		
		playerListener = new ServerPlayerListener(this);		
		getServer().getPluginManager().registerEvents(playerListener, this);
//		pm.registerEvent(Event.Type.PLAYER_CHANGED_WORLD, this.playerListener, Event.Priority.Normal, this);
		
		logger("has been enabled.","normal");
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