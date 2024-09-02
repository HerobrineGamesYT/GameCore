package net.herobrine.gamecore;

import net.herobrine.deltacraft.game.Missions;

public enum GameType {

	VANILLA("Vanilla",false, null), MODIFIER("Modifier",false, null),
	CLASH_ROYALE("Towers",false,null), MISSION1(null, false, null), DIMENTIO_TEST(null,false,null),
	ONE_V_ONE("1v1", false,null),
	TWO_V_TWO("2v2",true, new Teams[] {Teams.RED, Teams.BLUE}),
	THREE_V_THREE("3v3",true, new Teams[] {Teams.RED, Teams.BLUE}),
	FOUR_V_FOUR("4v4",true, new Teams[] {Teams.RED, Teams.BLUE}),
	HEROES_VS_VILLAINS("Heroes VS Villains", true, new Teams[] {Teams.HERO, Teams.VILLAIN});

	// This boolean will be set to true if this arena modifier will enable team logic in a game that is not a team game by default.
	// For example, CLASH_ROYALE (Battle Clash Towers) has the isTeamsMode value set to false because Battle Clash is a team game by default.
	// The availableTeams array is going to be a simple list of teams that the game will pick from.
	private String display;
	private boolean isTeamsMode;
	private Teams[] availableTeams;

	private GameType(String display, boolean isTeamsMode, Teams[] availableTeams) {
		this.display = display;
		this.isTeamsMode = isTeamsMode;
		this.availableTeams = availableTeams;
	}

	public String getDisplay() {return display;}
	public boolean isTeamsMode() {return isTeamsMode;}
	public Teams[] getAvailableTeams() {return availableTeams;}

}
