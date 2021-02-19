package net.herobrine.gamecore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class Config {
	private static GameCoreMain main;

	public Config(GameCoreMain main) {
		Config.main = main;
		main.getConfig().options().copyDefaults();
		main.saveDefaultConfig();
	}

	public static String getGameType(int id) {
		return main.getConfig().getString("arenas." + id + ".game-type");
	}

	public static int getRequiredPlayers(int id) {
		return main.getConfig().getInt("arenas." + id + ".required-players");
	}

	public static int getMaxPlayers(int id) {
		return main.getConfig().getInt("arenas." + id + ".max-players");
	}

	public static int getCountdownSeconds() {
		return main.getConfig().getInt("countdown-seconds");
	}

	public static Location getLobbySpawn() {
		return new Location(Bukkit.getWorld(main.getConfig().getString("lobby-spawn.world")),
				main.getConfig().getDouble("lobby-spawn.x"), main.getConfig().getDouble("lobby-spawn.y"),
				main.getConfig().getDouble("lobby-spawn.z"), main.getConfig().getInt("lobby-spawn.yaw"),
				main.getConfig().getInt("lobby-spawn.pitch"));

	}

	public static Location getLobbyNPCSpawn() {
		return new Location(Bukkit.getWorld(main.getConfig().getString("lobby-npc-spawn.world")),
				main.getConfig().getDouble("lobby-npc-spawn.x"), main.getConfig().getDouble("lobby-npc-spawn.y"),
				main.getConfig().getDouble("lobby-npc-spawn.z"), main.getConfig().getInt("lobby-npc-spawn.yaw"),
				main.getConfig().getInt("lobby-npc-spawn.pitch"));
	}

	public static Location getArenaSpawn(int id) {
		World world = Bukkit.createWorld(new WorldCreator(main.getConfig().getString("arenas." + id + ".world")));
		world.setAutoSave(false);
		return new Location(world, main.getConfig().getDouble("arenas." + id + ".x"),
				main.getConfig().getDouble("arenas." + id + ".y"), main.getConfig().getDouble("arenas." + id + ".z"),
				main.getConfig().getInt("arenas." + id + ".yaw"), main.getConfig().getInt("arenas." + id + ".pitch"));

	}

	public static World getArenaWorld(int id) {
		return (World) Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".world"));
	}

	public static int getArenaAmount() {
		return main.getConfig().getConfigurationSection("arenas.").getKeys(false).size();
	}

	public static World getGameWorld(int id) {
		return (World) Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".game-world"));
	}

	public static Location getRedTeamSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".red-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".red-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".red-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".red-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".red-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".red-spawn.yaw"));

	}

	public static Location getBlueTeamSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".blue-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".blue-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".blue-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".blue-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".blue-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".blue-spawn.yaw"));
	}

}