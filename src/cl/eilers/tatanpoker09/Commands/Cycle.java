package cl.eilers.tatanpoker09.Commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cl.eilers.tatanpoker09.Scrimmage;
import cl.eilers.tatanpoker09.Utils.FileUtils;




public class Cycle implements CommandExecutor {
	private Scrimmage plugin;
	public Cycle(Scrimmage instance) {
		plugin = instance;
	}
	int countdown;
	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		//Countdown and stuff :3
		countdown = Integer.parseInt(args[0]);
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				if(countdown != -1){
					if(countdown != 0){
						Bukkit.broadcastMessage("Cycling to " +plugin.getConfig().getString("TatanPGM.NextMap")+" in "+countdown);
						countdown--;
					} else {
						//Random variables needed.
						Player playerSender = (Player)sender;
						World mapBefore = playerSender.getWorld();
						String beforeMap = mapBefore.getName();
						File mapDone = new File(beforeMap);
						File src = new File("maps/"+plugin.getConfig().getString("TatanPGM.NextMap"));
						File dest = new File(plugin.getConfig().getString("TatanPGM.NextMap"));
						World nextWorld = new WorldCreator(plugin.getConfig().getString("TatanPGM.NextMap")).createWorld();

						//Copies the map from /maps to the main folder.
						try {
							FileUtils.copyFolder(src, dest);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(Player playersOnWorld : Bukkit.getOnlinePlayers()){
							if(playersOnWorld.getWorld().getName().equalsIgnoreCase(beforeMap)){
								playersOnWorld.teleport(nextWorld.getSpawnLocation());
							}
						}
						//Unloads last played world
						Bukkit.unloadWorld(mapBefore, true);
						//Last played world is deleted
						FileUtils.delete(mapDone);
						countdown--;
					}
				}
			}
		}, 0L, 20L);
		return true;
	}
}