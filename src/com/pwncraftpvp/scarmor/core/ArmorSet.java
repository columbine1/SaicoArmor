package com.pwncraftpvp.scarmor.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import com.pwncraftpvp.scarmor.utils.Utils;

public class ArmorSet {
	
	private Main main = Main.getInstance();
	
	private List<PotionEffect> effects = new ArrayList<PotionEffect>();
	private List<CustomEnchant> swordEnchants = new ArrayList<CustomEnchant>();
	private int price;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	private ItemStack sword;
	private String swordname;
	
	@SuppressWarnings("deprecation")
	public ArmorSet(){
		try{
			ConfigurationSection sect = main.getConfig().getConfigurationSection("armor");
			for(String s : sect.getKeys(false)){
				if(s.equalsIgnoreCase("effects") == true){
					String[] split = main.getConfig().getString("armor.effects").replace("{", "").replace("}", "").split(",");
					for(int x = 0; x <= (split.length - 1); x++){
						String[] split2 = split[x].split(":");
						String potion = split2[0];
						int level = Integer.parseInt(split2[1]);
						effects.add(new PotionEffect(Utils.getPotion(potion), Integer.MAX_VALUE, (level - 1)));
					}
				}else if(s.equalsIgnoreCase("price") == true){
					price = main.getConfig().getInt("armor.price");
				}else{
					int itemid = main.getConfig().getInt("armor." + s + ".item");
					String name = main.getConfig().getString("armor." + s + ".name").replace("&", "§");
					if(s.equalsIgnoreCase("sword") == true){
						swordname = name;
					}
					ItemStack item = new ItemStack(Material.getMaterial(itemid), 1);
					ItemMeta meta = (ItemMeta) item.getItemMeta();
			    	meta.setDisplayName(name);
				    item.setItemMeta(meta);
					List<String> lore = new ArrayList<String>();
				    String[] split = main.getConfig().getString("armor." + s + ".enchantments").replace("{", "").replace("}", "").split(",");
					for(int x = 0; x <= (split.length - 1); x++){
						String[] split2 = split[x].split(":");
						String enchantment = split2[0];
						int level = Integer.parseInt(split2[1]);
						CustomEnchant custom = CustomEnchant.getCustomEnchantment(enchantment);
						if(custom == null){
							item.addUnsafeEnchantment(Utils.getEnchantment(enchantment), level);
						}else{
							swordEnchants.add(custom);
							lore.add(ChatColor.GRAY + WordUtils.capitalizeFully(enchantment) + " " + Utils.getRomanNumeral(level));
						}
					}
					String[] split2 = main.getConfig().getString("armor." + s + ".lore").replace("{", "").replace("}", "").split(",");
					for(int x = 0; x <= (split2.length - 1); x++){
						lore.add(split2[x].replace("&", "§"));
					}
					ItemMeta meta2 = (ItemMeta) item.getItemMeta();
			    	meta2.setLore(lore);
				    item.setItemMeta(meta2);
				    if(s.equalsIgnoreCase("helmet") == true){
				    	helmet = item;
				    }else if(s.equalsIgnoreCase("chestplate") == true){
				    	chestplate = item;
				    }else if(s.equalsIgnoreCase("leggings") == true){
				    	leggings = item;
				    }else if(s.equalsIgnoreCase("boots") == true){
				    	boots = item;
				    }else if(s.equalsIgnoreCase("sword") == true){
				    	sword = item;
				    }
				}
			}
		}catch (Exception ex){
			main.getLogger().info("The following error occured while initializing the monthly armor set:");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Get the set's permanent effects
	 * @return The set's permanent effects
	 */
	public List<PotionEffect> getPermanentEffects(){
		return effects;
	}
	
	/**
	 * Get the price of the set
	 * @return The price of the set
	 */
	public int getPrice(){
		return price;
	}
	
	/**
	 * Get the set's helmet
	 * @return The set's helmet
	 */
	public ItemStack getHelmet(){
		return helmet;
	}
	
	/**
	 * Get the set's chestplate
	 * @return The set's chestplate
	 */
	public ItemStack getChestplate(){
		return chestplate;
	}
	
	/**
	 * Get the set's leggings
	 * @return The set's leggings
	 */
	public ItemStack getLeggings(){
		return leggings;
	}
	
	/**
	 * Get the set's boots
	 * @return The set's boots
	 */
	public ItemStack getBoots(){
		return boots;
	}
	
	/**
	 * Get the set's sword
	 * @return The set's sword
	 */
	public ItemStack getSword(){
		return sword;
	}
	
	/**
	 * Get the sword's custom enchantments
	 * @return The sword's custom enchantments
	 */
	public List<CustomEnchant> getSwordEnchantments(){
		return swordEnchants;
	}
	
	/**
	 * Get the sword's name
	 * @return The sword's name
	 */
	public String getSwordName(){
		return swordname;
	}

}
