package net.herobrine.gamecore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import net.herobrine.wallsg.ModifiedTypes;

public class Manager {

	private static ArrayList<Arena> arenas;

	public Manager() {
		arenas = new ArrayList<>();

		for (int i = 0; i <= Config.getArenaAmount() - 1; i++) {
			arenas.add(new Arena(i));
		}
	}

	public static List<Arena> getArenas() {
		return arenas;
	}

	public static Games getGame(Arena arena) {
		String arenaKey = Config.getGameType(arena.getID());
		for (Games game : Games.values()) {
			if (game.getKey().equals(arenaKey)) {

				return game;

			}
		}

		return null;

	}

	public static boolean hasArena(Games game) {

		for (Arena arena : arenas) {
			if (arena.getGame(arena.getID()).equals(game)) {

				return true;

			}

		}

		return false;

	}

	public static boolean isPlaying(Player player) {
		for (Arena arena : arenas) {
			if (arena.getPlayers().contains(player.getUniqueId())) {
				return true;
			}
		}
		return false;
	}

	public static Arena getArena(Player player) {
		for (Arena arena : arenas) {
			if (arena.getPlayers().contains(player.getUniqueId())) {
				return arena;
			}
		}
		return null;
	}

	public static Arena getArena(int id) {
		for (Arena arena : arenas) {
			if (arena.getID() == id) {
				return arena;
			}
		}
		return null;
	}

	public static boolean isRecruiting(int id) {
		return getArena(id).getState() == GameState.RECRUITING;
	}

	public static boolean isGameWorld(World world) {
		for (Arena arena : arenas) {

			if ((Config.getGameWorld(arena.getID()) == world)) {

				return true;
			}
		}

		return false;
	}

	public static boolean isArenaWorld(World world) {
		for (Arena arena : arenas) {
			if (arena.getSpawn().getWorld().getName().equals(world.getName())) {

				return true;
			} else if (Config.getGameWorld(arena.getID()).getName() + arena.getID() == world.getName()) {

				return true;
			}
		}
		return false;
	}

	public static Arena getArena(World world) {
		for (Arena arena : arenas) {
			if (arena.getSpawn().getWorld().getName().equals(world.getName())) {
				return arena;
			} else if (Config.getGameWorld(arena.getID()) == world) {
				return arena;
			}
		}
		return null;
	}

	public static ArrayList<ModifiedTypes> getBHModifiers(int id) {

		return getArena(id).getBHModifiers();

	}

	public static void setArenaType(GameType type, int id) {

		Arena arena = getArena(id);

		arena.setType(type);
	}

}
