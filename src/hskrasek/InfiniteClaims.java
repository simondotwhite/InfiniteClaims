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
	public final Logger logger = Logger.getLogger("Minecraft");

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
		PluginDescriptionFile pdf = this.getDescription();
		FileConfiguration configFile = this.getConfig();
		
		configFile.set("RoadOffsetXAxis", roadOffsetX);
		configFile.set("RoadOffsetZAxis", roadOffsetZ);
		configFile.set("PlotHeight", plotHeight);
		configFile.set("OwnerSignPrefix", ownerSignPrefix);
		configFile.set("SignPlacementMethod", signPlacementMethod);
		configFile.set("enableHome", enableHome); 
		configFile.set("setHome", setHome);
		configFile.set("goHome", goHome);

		this.saveConfig();
		
		this.logger.info(pdf.getName() + " is now disabled.");
	}
	
	@Override
	public void onEnable()
	{
		//PluginManager pm = getServer().getPluginManager();
		FileConfiguration configFile = this.getConfig();
		PluginDescriptionFile pdf = this.getDescription();
		
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
		
		this.logger.info(pdf.getName() + " is enabled.  Version: " + pdf.getVersion());	
	}
	
	public WorldGuardPlugin getWorldGuard()
	{
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	    logger.info("WorldGuard Enabled");
	    return (WorldGuardPlugin) plugin;
	}
	
	public WorldEditPlugin getWorldEdit()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		
		//WorldEdit may not be loaded
		if(plugin == null || !(plugin instanceof WorldEditPlugin)) {
			return null;
		}
		logger.info("WorldEdit enabled");
		return (WorldEditPlugin)plugin;
	}
}