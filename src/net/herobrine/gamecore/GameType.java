package net.herobrine.gamecore;

import net.herobrine.deltacraft.game.Missions;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum GameType {

	VANILLA("Vanilla", new String[] {"Just a regular game."}, Material.GRASS, ChatColor.GREEN, false, null, null),
	MODIFIER("Modifier", new String[] {"A slightly tweaked version of a game."}, Material.REDSTONE, ChatColor.GOLD, false, null, null),
	CLASH_ROYALE("Towers", new String[] {"Attack enemy towers and defend your own!"}, Material.IRON_AXE, ChatColor.GREEN, false,null, Games.CLASH_ROYALE),
	// Deltacraft modes don't have extra information because it is contained within Deltacraft's mission system.
	MISSION1(null, new String[] {}, null, null, false, null, Games.DELTARUNE),
	DIMENTIO_TEST(null, new String[] {}, null, null,false,null, Games.DELTARUNE),
	ONE_V_ONE("1v1", new String[] {"A simple 1v1 Quirk duel!", "Last player standing wins!"}, Material.IRON_SWORD, ChatColor.AQUA, false,null, Games.QUIRK_BATTTLE),
	TWO_V_TWO("2v2",new String[] {"Go head-to-head in a 2v2 Quirk duel", "with a friend! Last team standing wins."}, Material.GOLD_SWORD, ChatColor.GOLD, true, new Teams[] {Teams.RED, Teams.BLUE}, Games.QUIRK_BATTTLE),
	THREE_V_THREE("3v3",new String[] {"Think you can handle 6 quirks", "at once? Jump into 3v3 and find out!"},Material.DIAMOND_SWORD, ChatColor.RED, true, new Teams[] {Teams.RED, Teams.BLUE}, Games.QUIRK_BATTTLE),
	FOUR_V_FOUR("4v4",new String[] {"Okay, 8 is too much. Just kidding!",
			"There's never enough! Jump into 4v4 Quirk Duels",
			"and take a chance at chaos!"}, Material.IRON_AXE, ChatColor.WHITE, true, new Teams[] {Teams.RED, Teams.BLUE}, Games.QUIRK_BATTTLE),
	HEROES_VS_VILLAINS("Heroes VS Villains", new String[]
			{"Jump into this mode as a" + ChatColor.GREEN + " Hero" + ChatColor.GRAY + " or " + ChatColor.RED + "Villain",
			"and fight alongside your team boss", "to save (or conquer) the world!"},
			Material.DIAMOND_AXE, ChatColor.RED, true, new Teams[] {Teams.HERO, Teams.VILLAIN}, Games.QUIRK_BATTTLE),
	FFA("FFA", new String[] {"An intense Free-For-All battle of four!", "Last player standing wins."}, Material.BOW, ChatColor.LIGHT_PURPLE, false, null, Games.QUIRK_BATTTLE);

	// The isTeamsMode boolean will be set to true if this arena modifier will enable team logic in a game that is not a team game by default.
	// For example, CLASH_ROYALE (Battle Clash Towers) has the isTeamsMode value set to false because Battle Clash is a team game by default.
	// The availableTeams array is going to be a simple list of teams that the arena can place players into. Could be as many as you want!
	private String display;
	private String[] description;
	private Material material;
	private ChatColor color;
	private boolean isTeamsMode;
	private Teams[] availableTeams;

	private Games game;

	private GameType(String display, String[] description, Material material, ChatColor color, boolean isTeamsMode, Teams[] availableTeams, Games game) {
		this.display = display;
		this.description = description;
		this.material = material;
		this.color = color;
		this.isTeamsMode = isTeamsMode;
		this.availableTeams = availableTeams;
		this.game = game;
	}

	public String getDisplay() {return display;}
	public String[] getDescription() {return description;}

	public Material getMaterial() {return material;}

	public ChatColor getColor() {return color;}

	public boolean isTeamsMode() {return isTeamsMode;}

	public Teams[] getAvailableTeams() {return availableTeams;}

	public Games getGame() {return game;}

}
