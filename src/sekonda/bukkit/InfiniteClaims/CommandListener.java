package sekonda.bukkit.InfiniteClaims;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class CommandListener implements Listener {

	//Register Events
	public CommandListener(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	//Check if user has a plot on login.
	@EventHandler
	public void onLogin(PlayerLoginEvent PLE) {
		
	}
	
	//Check player commands
	@EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	if(cmd.getName().equalsIgnoreCase("myplot")){ 
    		sender.sendMessage("You issues the myplot command correctly.");
    		return true;
    	} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
    	return false; 
    }


	
}

