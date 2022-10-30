package net.herobrine.gamecore;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Games {

	// Games without stat keys here were either scrapped or not developed yet.
	BLOCK_HUNT(ChatColor.GREEN + "Block Hunt", "bh", ChatColor.GREEN, false, true, true, true, false, 2, 30, 100, 50, 100, false, Material.GRASS, null, null, true),
	MLG_RUSH(ChatColor.RED + "MLG Rush", "mlg", ChatColor.RED, false, false, false, false, false, 0, 25, 100, 15, 30, false, Material.WATER_BUCKET, null, null, false),
	BEDWARS(ChatColor.LIGHT_PURPLE + "Bedwars", "bw", ChatColor.LIGHT_PURPLE, true, true, false, false, false, 0, 150, 300, 45, 156, false, Material.BED, null, null, false),
	SKYWARS(ChatColor.GOLD + "Skywars", "sw", ChatColor.GOLD, true, false, false, false, true, 0, 20, 100, 25, 45, false, Material.ENDER_PEARL, null, null, true),
	FALL_CRAFT(ChatColor.AQUA + "Fall Craft", "fc", ChatColor.AQUA, false, false, false, false, false, 0, 150, 500, 150, 350, false, Material.FEATHER, null, null, false),
	CLASH_ROYALE(ChatColor.AQUA + "Battle Clash", "bc", ChatColor.AQUA, true, true, false, false, true, 2, 35, 100, 15, 50, true, Material.DIAMOND_SWORD, new String[] {"wins", "kills", "roundsPlayed"}, new String[] {"Wins", "Kills", "Rounds Played"}, false),
	WALLS_SG(ChatColor.YELLOW + "Walls SG", "wsg", ChatColor.YELLOW, true, true, true, false, false, 4, 50, 100, 20, 60, true, Material.DIAMOND_AXE, new String[] {"wins", "kills", "roundsPlayed"}, new String[] {"Wins", "Kills", "Rounds Played"}, false),
	CLASH_MINI(ChatColor.GOLD + "Clash Mini", "cm", ChatColor.GOLD, false, false, false, false, false, 0, 60, 120, 25, 65, false, Material.NETHER_STAR, null, null, false),
	DELTARUNE(ChatColor.RED + "Delta Craft", "dc", ChatColor.RED, false, false, false, false, false, 0, 100, 1000, 100, 1000, false, Material.EYE_OF_ENDER, null, null, false);

	private String display;
	private String key;
	private ChatColor color;
	private boolean isPVPGame;
	private boolean isTeamGame;
	private boolean hasVoting;
	private boolean newWorldEachGame;
	private boolean hasKits;
	private int teamCount;
	private int baseCoins;
	private int baseWinCoins;
	private int baseXP;
	private int baseWinXP;
	private boolean showStats;
	private Material statsItem;
	private String[] statKeys;
	private String[] friendlyStatKeys;
	private boolean hasCrafting;
	private Games(String display, String key, ChatColor color, boolean isPVPGame, boolean isTeamGame, boolean hasVoting,
			boolean newWorldEachGame, boolean hasKits, int teamCount, int baseCoins, int baseWinCoins, int baseXP, int baseWinXP, boolean showStats, Material statsItem, String[] statKeys, String[] friendlyStatKeys, boolean hasCrafting) {
		this.display = display;
		this.key = key;
		this.color = color;
		this.isPVPGame = isPVPGame;
		this.isTeamGame = isTeamGame;
		this.hasVoting = hasVoting;
		this.newWorldEachGame = newWorldEachGame;
		this.hasKits = hasKits;
		this.teamCount = teamCount;
		this.baseCoins  = baseCoins;
		this.baseWinCoins = baseWinCoins;
		this.baseXP = baseXP;
		this.baseWinXP = baseWinXP;
		this.showStats = showStats;
		this.statsItem = statsItem;
		this.statKeys = statKeys;
		this.friendlyStatKeys = friendlyStatKeys;
		this.hasCrafting = hasCrafting;

	}

	public boolean hasCrafting() {return hasCrafting;}
	public String getDisplay() {
		return display;
	}

	public String getKey() {
		return key;
	}

	public int getTeamCount() {
		return teamCount;
	}

	public ChatColor getColor() {
		return color;
	}

	public boolean isPVPGame() {
		return isPVPGame;
	}

	public boolean isTeamGame() {
		return isTeamGame;
	}

	public boolean hasVoting() {
		return hasVoting;
	}

	public boolean requiresNewWorld() {
		return newWorldEachGame;
	}

	public boolean hasKits() {
		return hasKits;
	}

	public int getBaseCoins() {return baseCoins;}

	public int getBaseWinCoins() {return  baseWinCoins;}

	public int getBaseXP() {return baseXP;}

	public int getBaseWinXP() {return baseWinXP;}

	public boolean areStatsShown() {return showStats;}

	public Material getStatsItem() { return statsItem;}

	public String[] getStatKeys() {return statKeys;}

	public String[] getFriendlyStatKeys() {return friendlyStatKeys;}

}
