package com.pwncraftpvp.scarmor.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pwncraftpvp.scarmor.core.Main;

public class Utils {
	
	private static final Main main = Main.getInstance();
	
	/**
	 * Set up the plugin's economy for use
	 */
    public static void setupEconomy(){
    	if(main.getServer().getPluginManager().getPlugin("Vault") != null){
        	RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
        	if(rsp != null){
        		main.econ = rsp.getProvider();
        	}
    	}
    }
	
	/**
	 * Set the villager's location
	 * @param loc - The villager's location
	 */
	public static final void setVillagerLocation(Location loc){
		main.getConfig().set("villager.x", loc.getX());
		main.getConfig().set("villager.y", loc.getY());
		main.getConfig().set("villager.z", loc.getZ());
		main.getConfig().set("villager.world", loc.getWorld().getName());
		main.saveConfig();
	}
	
	/**
	 * Get the villager's location
	 * @return The villager's location
	 */
	public static final Location getVillagerLocation(){
		if(main.getConfig().getString("villager.world") != null){
			double x,y,z;
			x = main.getConfig().getDouble("villager.x");
			y = main.getConfig().getDouble("villager.y");
			z = main.getConfig().getDouble("villager.z");
			World world = Bukkit.getWorld(main.getConfig().getString("villager.world"));
			
			return new Location(world, x, y, z);
		}else{
			return null;
		}
	}
	
	/**
	 * Spawn the store villager
	 * @return The store villager (null if the location is null)
	 */
	public static final Villager spawnVillager(){
		if(main.location != null){
			Villager villager = (Villager) main.location.getWorld().spawnEntity(main.location, EntityType.VILLAGER);
			villager.setCustomName(ChatColor.GREEN + "Monthly Armor Store");
			villager.setCustomNameVisible(true);
			villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 20));
			return villager;
		}else{
			return null;
		}
	}
	
	/**
	 * Get the store inventory
	 * @return The monthly armor store inventory
	 */
	public static final Inventory getStoreInventory(){
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "Monthly Armor Store");
		ItemStack accept = Utils.renameItem(new ItemStack(Material.WOOL, 1, (short) 5), ChatColor.GREEN + "Purchase", ChatColor.GRAY + "Click to purchase (" + ChatColor.YELLOW + "$" + 
		Utils.getCommas(main.set.getPrice()) + ChatColor.GRAY + ").");
		ItemStack deny = Utils.renameItem(new ItemStack(Material.WOOL, 1, (short) 14), ChatColor.DARK_RED + "Decline", ChatColor.GRAY + "Click to decline.");
		for(int x = 0; x <= 2; x++){
			inv.setItem(x, accept);
		}
		
		List<String> lore = new ArrayList<String>();
		if(main.set.getPermanentEffects().size() > 0){
			lore.add(ChatColor.RED + "Potion Effects:");
			for(PotionEffect e : main.set.getPermanentEffects()){
				lore.add(ChatColor.GRAY + " " + WordUtils.capitalizeFully(e.getType().getName().replace("_", " ")) + " " + Utils.getRomanNumeral(e.getAmplifier()));
			}
		}
		lore.add("");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "This armor is changed every month,");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "featuring exciting new pieces and");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "enchantments.");
		inv.setItem(4, Utils.renameItem(new ItemStack(Material.BOOK, 1), ChatColor.YELLOW + "" + ChatColor.BOLD + "Price: " + ChatColor.DARK_GREEN + ChatColor.BOLD + "$" + Utils.getCommas(main.set.getPrice()), 
				lore));
			
		for(int x = 6; x <= 8; x++){
			inv.setItem(x, deny);
		}
		inv.setItem(11, main.set.getHelmet());
		inv.setItem(12, main.set.getChestplate());
		inv.setItem(13, main.set.getLeggings());
		inv.setItem(14, main.set.getBoots());
		inv.setItem(15, main.set.getSword());
		return inv;
	}
	
	/**
	 * Get the potion effect type from a string
	 * @param type - The string
	 * @return The potion effect type (null if it does not exist)
	 */
	public static final PotionEffectType getPotion(String type){
		PotionEffectType potion = null;
		try{
			if(type.equalsIgnoreCase("resistance") == true){
				potion = PotionEffectType.DAMAGE_RESISTANCE;
			}else if(type.equalsIgnoreCase("strength") == true){
				potion = PotionEffectType.INCREASE_DAMAGE;
			}else{
				potion = PotionEffectType.getByName(type.toUpperCase());
			}
		}catch (Exception ex){
			potion = null;
		}
		return potion;
	}
	
	/**
	 * Get the enchantment from a string
	 * @param enchant - The string enchantment
	 * @return The enchantment enum from the string
	 */
	public static final Enchantment getEnchantment(String enchant){
		Enchantment enchantment = null;
		if(enchant.equalsIgnoreCase("protection") == true){
			enchantment = Enchantment.PROTECTION_ENVIRONMENTAL;
		}else if(enchant.equalsIgnoreCase("knockback") == true){
			enchantment = Enchantment.KNOCKBACK;
		}else if(enchant.equalsIgnoreCase("punch") == true){
			enchantment = Enchantment.ARROW_KNOCKBACK;
		}else if(enchant.equalsIgnoreCase("fireaspect") == true){
			enchantment = Enchantment.FIRE_ASPECT;
		}else if(enchant.equalsIgnoreCase("sharpness") == true){
			enchantment = Enchantment.DAMAGE_ALL;
		}else if(enchant.equalsIgnoreCase("power") == true){
			enchantment = Enchantment.ARROW_DAMAGE;
		}else if(enchant.equalsIgnoreCase("unbreaking") == true){
			enchantment = Enchantment.DURABILITY;
		}else if(enchant.equalsIgnoreCase("smite") == true){
			enchantment = Enchantment.DAMAGE_UNDEAD;
		}else if(enchant.equalsIgnoreCase("looting") == true){
			enchantment = Enchantment.LOOT_BONUS_MOBS;
		}else if(enchant.equalsIgnoreCase("aquaaffinity") == true){
			enchantment = Enchantment.OXYGEN;
		}else if(enchant.equalsIgnoreCase("blastprotection") == true){
			enchantment = Enchantment.PROTECTION_EXPLOSIONS;
		}else if(enchant.equalsIgnoreCase("projectileprotection") == true){
			enchantment = Enchantment.PROTECTION_PROJECTILE;
		}else if(enchant.equalsIgnoreCase("fireprotection") == true){
			enchantment = Enchantment.PROTECTION_FIRE;
		}else if(enchant.equalsIgnoreCase("fortune") == true){
			enchantment = Enchantment.LOOT_BONUS_BLOCKS;
		}else if(enchant.equalsIgnoreCase("infinity") == true || enchant.equalsIgnoreCase("infinite") == true){
			enchantment = Enchantment.ARROW_INFINITE;
		}else if(enchant.equalsIgnoreCase("flame") == true){
			enchantment = Enchantment.ARROW_FIRE;
		}
		return enchantment;
	}
	
	/**
	 * Get a number with commas
	 * @param number - The number to get with commas
	 * @return The number with commas
	 */
	public static final String getCommas(int number){
		return NumberFormat.getInstance().format(number);
	}
	
	/**
	 * Get the roman numeral of a number
	 * @param number - The number to get the roman numeral of
	 * @return The roman numeral of the number
	 */
	public static final String getRomanNumeral(int number){
		String numeral = "I";
		if(number == 2){
			numeral = "II";
		}else if(number == 3){
			numeral = "III";
		}else if(number == 4){
			numeral = "IV";
		}else if(number == 5){
			numeral = "V";
		}
		return numeral;
	}
	
	/**
	 * Rename an itemstack
	 * @param item - The itemstack to rename
	 * @param name - The new name of the itemstack
	 * @param lore - The lore for the itemstack
	 * @return The renamed itemstack
	 */
	public static final ItemStack renameItem(ItemStack item, String name, String... lore){
	    ItemMeta meta = (ItemMeta) item.getItemMeta();
    	meta.setDisplayName(name);
    	List<String> desc = new ArrayList<String>();
    	for(int x = 0; x <= (lore.length - 1); x++){
    		desc.add(lore[x]);
    	}
	    meta.setLore(desc);
	    item.setItemMeta(meta);
	    return item;
	}
	
	/**
	 * Rename an itemstack
	 * @param item - The itemstack to rename
	 * @param name - The new name of the itemstack
	 * @param lore - The lore for the itemstack
	 * @return The renamed itemstack
	 */
	public static final ItemStack renameItem(ItemStack item, String name, List<String> lore){
	    ItemMeta meta = (ItemMeta) item.getItemMeta();
    	meta.setDisplayName(name);
	    meta.setLore(lore);
	    item.setItemMeta(meta);
	    return item;
	}
	
	/**
	 * Rename an itemstack
	 * @param item - The itemstack to rename
	 * @param name - The new name of the itemstack
	 * @return The renamed itemstack
	 */
	public static final ItemStack renameItem(ItemStack item, String name){
	    ItemMeta meta = (ItemMeta) item.getItemMeta();
    	meta.setDisplayName(name);
	    item.setItemMeta(meta);
	    return item;
	}
	
	/**
	 * Check if a material is armor
	 * @param material - The material to check
	 * @return True if the material is armor, false if not
	 */
	public static final boolean isArmor(Material material){
		if(material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE || material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS || 
				material == Material.GOLD_HELMET || material == Material.GOLD_CHESTPLATE || material == Material.GOLD_LEGGINGS || material == Material.GOLD_BOOTS ||
				material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE || material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS ||
				material == Material.CHAINMAIL_HELMET || material == Material.CHAINMAIL_CHESTPLATE || material == Material.CHAINMAIL_LEGGINGS || material == Material.CHAINMAIL_BOOTS ||
				material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE || material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS){
			return true;
		}else{
			return false;
		}
	}

}
