package com.pwncraftpvp.scarmor.core;

public enum CustomEnchant {
	
	POISON;
	
	/**
	 * Get a custom enchantment from its name
	 * @param enchantment - The name of the enchantment
	 * @return The custom enchantment (null if it could not be found)
	 */
	public static final CustomEnchant getCustomEnchantment(String enchantment){
		CustomEnchant custom = null;
		for(CustomEnchant e : values()){
			if(e.toString().equalsIgnoreCase(enchantment) == true){
				custom = e;
				break;
			}
		}
		return custom;
	}

}
