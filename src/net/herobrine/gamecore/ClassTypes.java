package net.herobrine.gamecore;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum ClassTypes {

	BANDIT(ChatColor.GREEN + "Bandit", Material.STICK,
			new String[] { ChatColor.GRAY + "Kit Items:", ChatColor.BLUE + "1x Stick (Sharpness 9)",
					ChatColor.GOLD + "Ability: Dash!", ChatColor.GREEN + "- Dash forward, dealing damage",
					ChatColor.GREEN + "- to players on impact. Invulnerable while dashing. (30s)",
					ChatColor.GREEN + "- Permanent Speed 2 (Passive)", " ",
					ChatColor.GRAY + "Tower Hitspeed: " + ChatColor.RED + "1s" },
			Games.CLASH_ROYALE, false, 6, 1000, 0, 0, false),
	WIZARD(ChatColor.GOLD + "Wizard", Material.FIREBALL,
			new String[] { ChatColor.LIGHT_PURPLE + "Magical Fighter", ChatColor.GOLD + "Abilities:",
					ChatColor.LIGHT_PURPLE + "- Can Double Jump (Cooldown 7s)",
					ChatColor.LIGHT_PURPLE + "- Can shoot Fireballs (Cooldown 3s)",
					ChatColor.LIGHT_PURPLE + "- 25% Resistance to Melee Attacks (Passive)", " ",
					ChatColor.GRAY + "Tower Hitspeed: " + ChatColor.RED + "1.5s" },
			Games.CLASH_ROYALE, false, 8, 1500, 0, 0, false),
	KNIGHT(ChatColor.BLUE + "Knight", Material.GOLD_SWORD,
			new String[] { ChatColor.GRAY + "Kit Items:", ChatColor.BLUE + "1x Gold Sword (Sharpness 5)",
					ChatColor.BLUE + "Full Gold Armor", " ",
					ChatColor.GRAY + "Tower Hitspeed: " + ChatColor.RED + "1s" },
			Games.CLASH_ROYALE, false, 5, 1000, 0, 0, false),
	ARCHER(ChatColor.RED + "Archer", Material.BOW,
			new String[] { ChatColor.GRAY + "Kit Items:", ChatColor.BLUE + "1x Bow (Power 9)", " ",
					ChatColor.GRAY + "Tower Hitspeed: " + ChatColor.RED + "1.3s",
					ChatColor.GRAY + "Projectile Range: " + ChatColor.RED + "10" },
			Games.CLASH_ROYALE, false, 7, 1300, 10, 0, false),
	HEALER(ChatColor.GOLD + "Battle Healer", Material.GOLDEN_APPLE,
			new String[] { ChatColor.GOLD + "Healer", ChatColor.GOLD + "- Attacks heal teammates in a 3 block radius",
					"", ChatColor.GRAY + "Tower Hitspeed: " + ChatColor.RED + "1.4s" },
			Games.CLASH_ROYALE, false, 4, 1400, 0, 0, false),
	WITCH(ChatColor.LIGHT_PURPLE + "Witch", Material.BONE,
			new String[] { ChatColor.LIGHT_PURPLE + "Summoner", ChatColor.GOLD + "Abilities:",
					ChatColor.LIGHT_PURPLE + "- Can Summon Skeletons (Cooldown 20s)",
					ChatColor.LIGHT_PURPLE + "- Can Shoot Magic (Cooldown 2s)", "",
					ChatColor.GRAY + "Tower Hitspeed: " + ChatColor.RED + "2s" },
			Games.CLASH_ROYALE, false, 6, 2000, 0, 0, false),
	FISHERMAN(ChatColor.AQUA + "Fisherman", Material.COOKED_FISH,
			new String[] { ChatColor.GRAY + "Kit Items:", ChatColor.BLUE + "1x Fish (Sharpness 9)",
					ChatColor.GOLD + "Ability: Fisherman Hook!",
					ChatColor.AQUA + "- Can hook enemies and pull them towards you (Cooldown 15s)", "",
					ChatColor.GRAY + "Tower Hitspeed: " + ChatColor.RED + "1.2s" },
			Games.CLASH_ROYALE, false, 6, 1200, 0, 0, false),
	LUMBERJACK(ChatColor.LIGHT_PURPLE + "Lumberjack", Material.IRON_AXE,
			new String[] { ChatColor.GRAY + "Kit Items:", ChatColor.BLUE + "1x Iron Axe (Sharpness 6)",
					ChatColor.GOLD + "Ability: Death Rage!", ChatColor.LIGHT_PURPLE + "- Grants all teammates in a 3 block radius",
					ChatColor.LIGHT_PURPLE + "- Speed and Haste for 5 seconds on death",
					ChatColor.LIGHT_PURPLE + "Permanent Speed 2 (Passive)", "",
					ChatColor.GRAY + "Tower Hitspeed: " + ChatColor.RED + "1.3s" },
			Games.CLASH_ROYALE, true, 8, 1300, 0, 0, false),
	MONK(ChatColor.DARK_GREEN + "Monk", Material.NETHER_STAR, new String[] {ChatColor.GOLD + "Ability: Pensive Protection" + ChatColor.GRAY + " (Cooldown 20s)", ChatColor.DARK_GREEN + "- Deflect projectiles and take ",
			ChatColor.DARK_GREEN + "80% less damage for 5 seconds.", ChatColor.DARK_GREEN + "- Deals damage with strong punches that knock", ChatColor.DARK_GREEN + "back players every 3 hits"},
			Games.CLASH_ROYALE, true, 10, 1000, 0, 0, true),
	JUGGERNAUT(ChatColor.RED + "Juggernaut", Material.DIAMOND_AXE, new String[] {ChatColor.BLUE + "Special Items:", ChatColor.RED + "Juggernaut Axe (Sharpness 2)", "",
			ChatColor.GREEN + "After every kill, your " + ChatColor.RED + "axe " + ChatColor.GREEN + "gets a higher", ChatColor.GREEN + "level of sharpness."},
			Games.WALLS_SG, false, 0, 0, 0, 0, false),
	ECONOMIST(ChatColor.GREEN + "Emerus's Heir", Material.EMERALD,
			new String[] {ChatColor.BLUE + "Special Items:", ChatColor.GREEN + "10x " + ChatColor.DARK_GRAY + "Coal", ChatColor.GREEN + "6x " + ChatColor.GRAY + "Iron",
					ChatColor.GREEN + "5x " + ChatColor.GOLD + "Gold", ChatColor.GREEN + "4x " + ChatColor.AQUA + "Diamonds", ChatColor.GREEN + "10x " + ChatColor.BLUE + "Lapis",
					ChatColor.GREEN + "1x Emerald", "", ChatColor.GREEN + "Perk:", ChatColor.GREEN + "Everything in the shop is 25% cheaper!"},
			Games.WALLS_SG, false, 0, 0, 0, 0.25, false),
	MINER(ChatColor.AQUA + "Miner", Material.DIAMOND_PICKAXE, new String[] {ChatColor.BLUE + "Special Items:", ChatColor.AQUA + "Diamond Pickaxe (Efficiency 2)", "",
			ChatColor.GREEN + "Perk:", ChatColor.GREEN + "For each ore " + ChatColor.AQUA + "mined" + ChatColor.GREEN + ", there is a 15% chance", ChatColor.GREEN +
			"that the drops will be doubled."}, Games.WALLS_SG, false, 0, 0, 0, 0, false),
	ENGINEER(ChatColor.GOLD + "Engineer", Material.FLINT_AND_STEEL, new String[] {ChatColor.BLUE + "Special Items:", ChatColor.GOLD + "- 1x Placeable Cannon", "",
			ChatColor.GREEN + "Perk:", ChatColor.GREEN +"Can place a " + ChatColor.GOLD + "cannon " + ChatColor.GREEN + "that lets you", ChatColor.GREEN +
			"shoot cannonballs at enemies!"}, Games.WALLS_SG, false, 0, 0, 0, 0, false),
	BERSERK(ChatColor.GREEN + "Berserker", Material.DIAMOND_SWORD, new String[] {""},
			Games.DELTARUNE, false, 0,0,0,0,false),
	ARCHER_DELTACRAFT(ChatColor.LIGHT_PURPLE + "Archer", Material.BOW, new String[] {},
			Games.DELTARUNE, false, 0 ,0 ,0 ,0 , false),
	MAGE(ChatColor.BLUE + "Mage", Material.BLAZE_ROD, new String[] {},
			Games.DELTARUNE, false, 0 ,0 ,0 ,0, false),
	TANK(ChatColor.RED + "Tank", Material.DIAMOND_CHESTPLATE,
			new String[] {}, Games.DELTARUNE, false,0,0,0,0,false),
	HEALER_DELTACRAFT(ChatColor.GOLD + "Healer", Material.GOLDEN_APPLE, new String[] {},
			Games.DELTARUNE, false,0 ,0,0,0,  false);

	// The display name of the class.
	private String display;
	// Material used to represent the class in the GUI.
	private Material material;

	// GUI Description.
	private String[] description;

	// what game is it usable in?
	private Games game;

	// can it be bought in the shop?
	private boolean isUnlockable;

	// USED IN BATTLE CLASH - may be changed based on item you are holding + ability you attack with, but
	// this will be the damage of your primary attack
	private int baseDamage;

	//USED IN BATTLE CLASH - 0 is unlimited
	private int projectileRange;

	// USED IN BATTLE CLASH - hitspeed is in ms
	private long hitSpeed;

	private double shopDiscount;

	private boolean isDisabled;

	private ClassTypes(String display, Material material, String[] description, Games game, boolean isUnlockable,
			int baseDamage, long hitSpeed, int projectileRange, double shopDiscount, boolean isDisabled) {
		this.display = display;
		this.material = material;
		this.description = description;
		this.game = game;
		this.isUnlockable = isUnlockable;
		this.baseDamage = baseDamage;
		this.hitSpeed = hitSpeed;
		this.projectileRange = projectileRange;
		this.shopDiscount = shopDiscount;
		this.isDisabled = isDisabled;
	}

	public String getDisplay() {
		return display;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public long getHitSpeed() {
		return hitSpeed;
	}

	public int getProjectileRange() {
		return projectileRange;
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
	public double getShopDiscount() {return shopDiscount;}

	public boolean isDisabled() {return isDisabled;}
}