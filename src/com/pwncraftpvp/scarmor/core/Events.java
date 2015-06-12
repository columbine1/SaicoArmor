package com.pwncraftpvp.scarmor.core;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pwncraftpvp.scarmor.utils.Utils;

public class Events implements Listener {
	
	private Main main = Main.getInstance();
	private String yellow = ChatColor.YELLOW + "";
	private String gray = ChatColor.GRAY + "";
	private Random rand = new Random();
	
	@EventHandler
	public void playerInteractEntity(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		if(event.getRightClicked() instanceof Villager){
			Villager villager = (Villager) event.getRightClicked();
			if(villager.getCustomName() != null && villager.getCustomName().contains("Monthly Armor Store") == true){
				event.setCancelled(true);
				player.openInventory(main.store);
			}
		}
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent event){
		if(main.location != null){
			Player player = event.getPlayer();
			Location loc = player.getLocation();
			double diffX = Math.abs(loc.getX() - main.location.getX());
			double diffY = Math.abs(loc.getY() - main.location.getY());
			double diffZ = Math.abs(loc.getZ() - main.location.getZ());
			if(diffX <= 1 && diffY <= 1 && diffZ <= 1){
				player.teleport(loc.subtract(loc.getDirection().normalize().multiply(1)));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void inventoryClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player){
			final Player player = (Player) event.getWhoClicked();
			final SPlayer splayer = new SPlayer(player);
			ItemStack item = event.getCurrentItem();
			if(event.getInventory().getTitle().contains("Monthly Armor Store") == true){
				event.setCancelled(true);
				if(item != null){
					if(item.getType() == Material.WOOL){
						if(item.getData().getData() == (byte) 5){
							if(main.econ.getBalance(player) >= main.set.getPrice()){
								player.getInventory().addItem(main.set.getHelmet());
								player.getInventory().addItem(main.set.getChestplate());
								player.getInventory().addItem(main.set.getLeggings());
								player.getInventory().addItem(main.set.getBoots());
								player.getInventory().addItem(main.set.getSword());
								main.econ.withdrawPlayer(player, main.set.getPrice());
								splayer.sendMessage("You have purchased this month's armor for " + gray + "$" + Utils.getCommas(main.set.getPrice()) + yellow + ".");
								player.closeInventory();
							}else{
								splayer.sendError("You do not have enough money to purchase this.");
								player.closeInventory();
							}
						}else{
							splayer.sendMessage("You have declined the purchase.");
							player.closeInventory();
						}
					}
				}
			}else if(item != null && (Utils.isArmor(item.getType()) == true || Utils.isArmor(event.getCursor().getType()) == true)){
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
					public void run(){
						if(splayer.hasArmorEquipped() == true){
							for(PotionEffect e : main.set.getPermanentEffects()){
								player.addPotionEffect(e);
							}
						}else{
							for(PotionEffect t : main.set.getPermanentEffects()){
								for(PotionEffect e : player.getActivePotionEffects()){
									if(e.getType().getName().equalsIgnoreCase(t.getType().getName()) && e.getAmplifier() == t.getAmplifier() && (e.getDuration() > 100000 && t.getDuration() > 100000)){
										player.removePotionEffect(t.getType());
									}
								}
							}
						}
					}
				}, 1);
			}
		}
	}
	
	@EventHandler
	public void entityDamageByEntity(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity){
			Player damager = (Player) event.getDamager();
			LivingEntity entity = (LivingEntity) event.getEntity();
			if(entity.getType() == EntityType.VILLAGER && entity.getCustomName() != null && entity.getCustomName().contains("Monthly Armor Store") == true){
				event.setCancelled(true);
			}else{
				ItemStack item = damager.getItemInHand();
				if(main.set.getSwordEnchantments().size() > 0){
					if(item.hasItemMeta() == true && item.getItemMeta().hasDisplayName() == true && item.getItemMeta().getDisplayName().equalsIgnoreCase(main.set.getSwordName()) == true){
						if(main.set.getSwordEnchantments().contains(CustomEnchant.POISON) == true){
							if(rand.nextFloat() <= 0.3F){
								if(entity.hasPotionEffect(PotionEffectType.POISON) == true){
									entity.removePotionEffect(PotionEffectType.POISON);
								}
								entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void playerInteract(PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			final Player player = event.getPlayer();
			final SPlayer splayer = new SPlayer(player);
			ItemStack item = player.getItemInHand();
			if(item != null && Utils.isArmor(item.getType()) == true){
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
					public void run(){
						if(splayer.hasArmorEquipped() == true){
							for(PotionEffect e : main.set.getPermanentEffects()){
								player.addPotionEffect(e);
							}
						}else{
							for(PotionEffect t : main.set.getPermanentEffects()){
								for(PotionEffect e : player.getActivePotionEffects()){
									if(e.getType().getName().equalsIgnoreCase(t.getType().getName()) && e.getAmplifier() == t.getAmplifier() && (e.getDuration() > 100000 && t.getDuration() > 100000)){
										player.removePotionEffect(t.getType());
									}
								}
							}
						}
					}
				}, 1);
			}
		}
	}

}
