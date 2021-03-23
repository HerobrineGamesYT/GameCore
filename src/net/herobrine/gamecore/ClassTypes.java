package net.herobrine.gamecore;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum ClassTypes {

	BANDIT(ChatColor.GREEN + "Bandit", Material.STICK,
			new String[] { ChatColor.GRAY + "Kit Items:", ChatColor.BLUE + "1x Stick (Sharpness 9)",
					ChatColor.GOLD + "Ability:", ChatColor.GREEN + "- Dash forward, dealing damage",
					ChatColor.GREEN + "- to players on impact. Invulnerable while dashing. (30s)",
					ChatColor.BLUE + "- Permanent Speed 2 (Passive)" },
			Games.CLASH_ROYALE, false),
	WIZARD(ChatColor.GOLD + "Wizard", Material.FIREBALL,
			new String[] { ChatColor.LIGHT_PURPLE + "Magical Fighter", ChatColor.GOLD + "Abilities:",
					ChatColor.LIGHT_PURPLE + "- Can Double Jump (Cooldown 7s)",
					ChatColor.LIGHT_PURPLE + "- Can shoot Fireballs (Cooldown 3s)",
					ChatColor.LIGHT_PURPLE + "- 25% Resistance to Melee Attacks (Passive)" },
			Games.CLASH_ROYALE, false),
	KNIGHT(ChatColor.BLUE + "Knight", Material.GOLD_SWORD,
			new String[] { ChatColor.GRAY + "Kit Items:", ChatColor.BLUE + "1x Gold Sword (Sharpness 5)",
					ChatColor.BLUE + "Full Gold Armor" },
			Games.CLASH_ROYALE, false),
	ARCHER(ChatColor.RED + "Archer", Material.BOW,
			new String[] { ChatColor.GRAY + "Kit Items:", ChatColor.BLUE + "1x Bow (Power 13)" }, Games.CLASH_ROYALE,
			false);

	private String display;
	private Material material;
	private String[] description;
	private Games game;
	private boolean isUnlockable;

	private ClassTypes(String display, Material material, String[] description, Games game, boolean isUnlockable) {
		this.display = display;
		this.material = material;
		this.description = description;
		this.game = game;
		this.isUnlockable = isUnlockable;
	}

	public String getDisplay() {
		return display;
	}

	public Material getMaterial() {
		return material;
	}

	public String[] getDescription() {
		return description;
	}

	public Games getGame() {
		return game;
	}

	public boolean isUnlockable() {
		return isUnlockable;
	}
}