package net.herobrine.gamecore;

import org.bukkit.ChatColor;

public enum Teams {
	RED(ChatColor.RED + "RED", ChatColor.RED), BLUE(ChatColor.BLUE + "BLUE", ChatColor.BLUE),
	GREEN(ChatColor.GREEN + "GREEN", ChatColor.GREEN), YELLOW(ChatColor.YELLOW + "YELLOW", ChatColor.YELLOW),
	HERO(ChatColor.GREEN  + "Hero", ChatColor.GREEN), VILLAIN(ChatColor.RED + "Villain", ChatColor.RED),
	PLACEHOLDER(ChatColor.WHITE + "PLACEHOLDER", ChatColor.WHITE);

	private String display;
	private ChatColor color;

	private Teams(String display, ChatColor color) {
		this.display = display;
		this.color = color;

	}

	public String getDisplay() {
		return display;
	}

	public ChatColor getColor() {
		return color;
	}

}
