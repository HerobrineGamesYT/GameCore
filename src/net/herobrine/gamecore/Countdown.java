package net.herobrine.gamecore;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

	private Arena arena;
	private int seconds;
	private static ArrayList<UUID> vanillaVotes = new ArrayList<UUID>();
	private static ArrayList<UUID> modifiedVotes = new ArrayList<UUID>();
	private GameType winner;

	public Countdown(Arena arena) {
		this.arena = arena;
		this.seconds = Config.getCountdownSeconds();
	}

	public void begin() {
		arena.setState(GameState.COUNTDOWN);

		System.gc();
		if (arena.getGame(arena.getID()).hasVoting()) {
			startVote();
		}

		this.runTaskTimer(GameCoreMain.getInstance(), 0, 20);

	}

	public void startVote() {
		arena.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&d&lVOTE! &eA vote event has started! Vote for the game type before it starts."));
	}

	public static void registerVote(Player player, GameType type) {

		if (type.equals(GameType.VANILLA) && !vanillaVotes.contains(player.getUniqueId())
				&& !modifiedVotes.contains(player.getUniqueId())) {
			vanillaVotes.add(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + "You voted for a " + ChatColor.LIGHT_PURPLE + "VANILLA "
					+ ChatColor.GREEN + "game!");
		}

		if (type.equals(GameType.MODIFIER) && !modifiedVotes.contains(player.getUniqueId())
				&& !vanillaVotes.contains(player.getUniqueId())) {
			modifiedVotes.add(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + "You voted for a " + ChatColor.LIGHT_PURPLE + "MODIFIER "
					+ ChatColor.GREEN + "game!");
		} else {
			player.sendMessage(ChatColor.RED + "You already voted!");
		}

	}

	public void clearVotes() {
		vanillaVotes.clear();
		modifiedVotes.clear();
	}

	@Override
	public void run() {
		if (arena.getPlayers().size() < Config.getRequiredPlayers(arena.getID())) {
			arena.setState(GameState.RECRUITING);
			arena.playCountdownSounds();
			arena.sendMessage(ChatColor.RED + "Not enough players! Countdown cancelled.");
			clearVotes();
			arena.softReset();
			cancel();
			return;
		}

		if (arena.getState().equals(GameState.LIVE)) {
			cancel();
		}

		if (seconds == 0) {
			cancel();
			if (!arena.getGame(arena.getID()).hasVoting()) {

				arena.clearInventories();

				arena.start(GameType.VANILLA);

			}

			else {
				if (vanillaVotes.size() > modifiedVotes.size()) {
					winner = GameType.VANILLA;
					arena.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lVOTE! &eVanilla game wins!"));
					arena.sendMessage(ChatColor.GREEN + "Votes for vanilla: " + vanillaVotes.size());
					arena.sendMessage(ChatColor.GREEN + "Votes for modifier: " + modifiedVotes.size());
				}

				else if (vanillaVotes.size() < modifiedVotes.size()) {
					winner = GameType.MODIFIER;
					arena.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&d&lVOTE! &eModifier game wins! Picking modifiers..."));
					arena.sendMessage(ChatColor.GREEN + "Votes for vanilla: " + vanillaVotes.size());
					arena.sendMessage(ChatColor.GREEN + "Votes for modifier: " + modifiedVotes.size());

				} else if (vanillaVotes.size() == modifiedVotes.size()) {
					arena.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&d&lVOTE! &eThe poll was a tie. Since we must proceed, Vanilla wins by default."));
					arena.sendMessage(ChatColor.GREEN + "Votes for vanilla: " + vanillaVotes.size());
					arena.sendMessage(ChatColor.GREEN + "Votes for modifier: " + modifiedVotes.size());
					winner = GameType.VANILLA;

				} else {
					winner = GameType.VANILLA;
					arena.sendMessage(
							ChatColor.RED + "An unknown error has occured. Setting the game type to Vanilla...");
				}
				clearVotes();
				arena.clearInventories();
				arena.start(winner);
				return;
			}
		}

		if (seconds == 15 || seconds <= 10 || seconds % 10 == 0) {
			if (seconds == 15 || seconds > 15) {
				arena.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.GREEN + seconds + " seconds!");
				arena.playCountdownSounds();

			}
			if (seconds == 1) {
				arena.sendMessage(
						ChatColor.YELLOW + "The game starts in " + ChatColor.RED + "1" + ChatColor.YELLOW + " second!");
				arena.playCountdownSounds();
				String subtitle;
				if (arena.getGame(arena.getID()).equals(Games.BLOCK_HUNT)) {
					subtitle = "&aPrepare to hunt!";
				} else if (arena.getGame(arena.getID()).equals(Games.MLG_RUSH)) {
					subtitle = "&cPrepare to MLG!";
				} else {
					subtitle = "&ePrepare to fight!";
				}

				arena.sendTitle("&c" + seconds, subtitle, 0, 1, 0);
			} else if (seconds == 2 || seconds == 3 || seconds == 4 || seconds == 5) {
				arena.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + seconds + " seconds!");
				arena.playCountdownSounds();
				String subtitle;

				if (arena.getGame(arena.getID()).equals(Games.BLOCK_HUNT)) {
					subtitle = "&aPrepare to hunt!";
				} else if (arena.getGame(arena.getID()).equals(Games.MLG_RUSH)) {
					subtitle = "&cPrepare to MLG!";
				} else {
					subtitle = "&ePrepare to fight!";
				}

				arena.sendTitle("&c" + seconds, subtitle, 0, 1, 0);
			} else {
				if (seconds != 15 && seconds < 15) {
					arena.sendMessage(
							ChatColor.YELLOW + "The game starts in " + ChatColor.GOLD + seconds + " seconds!");
				}

			}
		}

		seconds--;
	}

}
