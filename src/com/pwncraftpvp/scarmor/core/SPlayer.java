package com.pwncraftpvp.scarmor.core;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import com.pwncraftpvp.scarmor.utils.TextUtils;

public class SPlayer {
	
	private Main main = Main.getInstance();
	private String yellow = ChatColor.YELLOW + "";
	private String gray = ChatColor.GRAY + "";
	
	private Player player;
	public SPlayer(Player player){
		this.player = player;
	}
	
	/**
	 * Send a message header to the player
	 * @param header - The header to be sent
	 */
	public void sendMessageHeader(String header){
		player.sendMessage(TextUtils.centerText(yellow + "-=-(" + gray + TextUtils.getDoubleArrow() + yellow + ")-=-" + "  " + gray + header + "  " + yellow + "-=-(" + gray + TextUtils.getBackwardsDoubleArrow()
				+ yellow + ")-=-"));
	}
	
	/**
	 * Send a message to the player
	 * @param message - The message to be sent
	 */
	public void sendMessage(String message){
		player.sendMessage(yellow + message);
	}
	
	/**
	 * Send an error message to the player
	 * @param error - The error message to be sent
	 */
	public void sendError(String error){
		player.sendMessage(ChatColor.DARK_RED + error);
	}
	
	/**
	 * Check if the player has the monthly armor on
	 * @return True if the player does, false if not
	 */
	public boolean hasArmorEquipped(){
		boolean hasArmor = false;
		ItemStack helmet = player.getInventory().getHelmet();
		ItemStack chestplate = player.getInventory().getChestplate();
		ItemStack leggings = player.getInventory().getLeggings();
		ItemStack boots = player.getInventory().getBoots();
		if(helmet != null && chestplate != null && leggings != null && boots != null){
			ItemMeta helmMeta = helmet.getItemMeta();
			ItemMeta chestMeta = chestplate.getItemMeta();
			ItemMeta leggingsMeta = leggings.getItemMeta();
			ItemMeta bootsMeta = boots.getItemMeta();
			if(helmMeta != null && chestMeta != null && leggingsMeta != null && bootsMeta != null){
				if(helmMeta.getDisplayName().equalsIgnoreCase(main.set.getHelmet().getItemMeta().getDisplayName()) == true && 
						chestMeta.getDisplayName().equalsIgnoreCase(main.set.getChestplate().getItemMeta().getDisplayName()) == true && 
						leggingsMeta.getDisplayName().equalsIgnoreCase(main.set.getLeggings().getItemMeta().getDisplayName()) == true && 
						bootsMeta.getDisplayName().equalsIgnoreCase(main.set.getBoots().getItemMeta().getDisplayName()) == true){
					hasArmor = true;
				}
			}
		}
		return hasArmor;
	}
	
	/**
	 * Check if the player has a potion effect
	 * @param effect - The effect to check
	 * @return True if the player has the potion effect, false if not
	 */
	public boolean hasPotionEffect(PotionEffect effect){
		boolean has = false;
		for(PotionEffect e : player.getActivePotionEffects()){
			if(e.getType() == effect.getType() && e.getAmplifier() == effect.getAmplifier() && (e.getDuration() > 100000 && effect.getDuration() > 100000)){
				has = true;
				break;
			}
		}
		return has;
	}
	
}
