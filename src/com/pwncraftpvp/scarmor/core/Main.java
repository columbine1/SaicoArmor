package com.pwncraftpvp.scarmor.core;

import java.io.File;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.pwncraftpvp.scarmor.utils.Utils;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	public Economy econ;
	public ArmorSet set = null;
	public Inventory store = null;
	public Location location = null;
	public Villager villager = null;
	
	/**
	 * Get the instance of this class
	 * @return The instance of this class
	 */
	public static final Main getInstance(){
		return instance;
	}
	
	public void onEnable(){
		instance = this;
		Utils.setupEconomy();
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		File file = new File(this.getDataFolder(), "config.yml");
		if(file.exists() == false){
			this.getConfig().set("armor.effects", "{speed:1,resistance:1}");
			this.getConfig().set("armor.price", 100000);
			
			this.getConfig().set("armor.helmet.item", 310);
			this.getConfig().set("armor.helmet.enchantments", "protection:9,unbreaking:3");
			this.getConfig().set("armor.helmet.name", "&6Super Helmet");
			this.getConfig().set("armor.helmet.lore", "{&7This is the first line of lore,&7This is the second line of lore}");
			
			this.getConfig().set("armor.chestplate.item", 311);
			this.getConfig().set("armor.chestplate.enchantments", "protection:9,unbreaking:3");
			this.getConfig().set("armor.chestplate.name", "&6Super Chestplate");
			this.getConfig().set("armor.chestplate.lore", "{&7This is the first line of lore,&7This is the second line of lore}");
			
			this.getConfig().set("armor.leggings.item", 312);
			this.getConfig().set("armor.leggings.enchantments", "protection:9,unbreaking:3");
			this.getConfig().set("armor.leggings.name", "&6Super Leggings");
			this.getConfig().set("armor.leggings.lore", "{&7This is the first line of lore,&7This is the second line of lore}");
			
			this.getConfig().set("armor.boots.item", 313);
			this.getConfig().set("armor.boots.enchantments", "protection:9,unbreaking:3");
			this.getConfig().set("armor.boots.name", "&6Super Boots");
			this.getConfig().set("armor.boots.lore", "{&7This is the first line of lore,&7This is the second line of lore}");
			
			this.getConfig().set("armor.sword.item", 276);
			this.getConfig().set("armor.sword.enchantments", "poison:1");
			this.getConfig().set("armor.sword.name", "&6Super Sword");
			this.getConfig().set("armor.sword.lore", "{&7This is the first line of lore,&7This is the second line of lore}");
			
			this.saveConfig();
		}
		set = new ArmorSet();
		store = Utils.getStoreInventory();
		location = Utils.getVillagerLocation();
		if(location != null){
			for(Entity e : location.getWorld().getEntities()){
				if(e instanceof Villager){
					Villager v = (Villager) e;
					if(v.getCustomName() != null && v.getCustomName().contains("Monthly Armor Store") == true){
						e.remove();
					}
				}
			}
			villager = Utils.spawnVillager();
		}
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				if(location != null){
					villager.teleport(location);
				}
			}
		}, 0, 20);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			SPlayer splayer = new SPlayer(player);
			if(cmd.getName().equalsIgnoreCase("saicoarmor")){
				if(player.hasPermission("saicoarmor.commands") == true){
					if(args.length == 1){
						if(args[0].equalsIgnoreCase("setvillager") == true){
							Utils.setVillagerLocation(player.getLocation());
							location = player.getLocation();
							if(villager != null){
								villager.remove();
							}
							villager = Utils.spawnVillager();
							splayer.sendMessage("You have set the villager's location.");
						}else if(args[0].equalsIgnoreCase("reload") == true){
							this.reloadConfig();
							this.saveConfig();
							set = new ArmorSet();
							store = Utils.getStoreInventory();
							location = Utils.getVillagerLocation();
							if(villager != null){
								villager.remove();
							}
							villager = Utils.spawnVillager();
							splayer.sendMessage("You have reloaded the configuration.");
						}else{
							splayer.sendError("Usage: /" + cmd.getName() + " setvillager, /" + cmd.getName() + " reload");
						}
					}else{
						splayer.sendError("Usage: /" + cmd.getName() + " setvillager, /" + cmd.getName() + " reload");
					}
				}else{
					splayer.sendError("You do not have permission to do this.");
				}
			}
		}
		return false;
	}

}
