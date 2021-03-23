package net.herobrine.gamecore;

import org.bukkit.ChatColor;

public enum Games {

	BLOCK_HUNT(ChatColor.GREEN + "Block Hunt", "bh", ChatColor.GREEN, false, true, true, true, false),
	MLG_RUSH(ChatColor.RED + "MLG Rush", "mlg", ChatColor.RED, false, false, false, false, false),
	BEDWARS(ChatColor.LIGHT_PURPLE + "Bedwars", "bw", ChatColor.LIGHT_PURPLE, true, true, false, false, false),
	SKYWARS(ChatColor.GOLD + "Skywars", "sw", ChatColor.GOLD, true, false, false, false, true),
	FALL_CRAFT(ChatColor.AQUA + "Fall Craft", "fc", ChatColor.AQUA, false, false, false, false, false),
	CLASH_ROYALE(ChatColor.AQUA + "Battle Clash", "bc", ChatColor.AQUA, true, true, false, false, true);

	private String display;
	private String key;
	private ChatColor color;
	private boolean isPVPGame;
	private boolean isTeamGame;
	private boolean hasVoting;
	private boolean newWorldEachGame;
	private boolean hasKits;

	private Games(String display, String key, ChatColor color, boolean isPVPGame, boolean isTeamGame, boolean hasVoting,
			boolean newWorldEachGame, boolean hasKits) {
		this.display = display;
		this.key = key;
		this.color = color;
		this.isPVPGame = isPVPGame;
		this.isTeamGame = isTeamGame;
		this.hasVoting = hasVoting;
		this.newWorldEachGame = newWorldEachGame;
		this.hasKits = hasKits;

	}

	public String getDisplay() {
		return display;
	}

	public String getKey() {
		return key;
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

}
