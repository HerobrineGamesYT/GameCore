package net.herobrine.gamecore;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.core.Ranks;

public class ArenaCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (HerobrinePVPCore.getFileManager().getRank(player).equals(Ranks.OWNER)
					|| HerobrinePVPCore.getFileManager().getRank(player).equals(Ranks.ADMIN)) {
				if (args.length == 0) {
					player.sendMessage(ChatColor.RED + "Invalid usage! Do /arena help to see all of your options.");
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("help")) {
						player.sendMessage(ChatColor.AQUA + "Arena Management Commands");
						player.sendMessage(ChatColor.GREEN
								+ "/arena start <gametype> - Force the arena you are in to start the game");
						player.sendMessage(
								ChatColor.GREEN + "/arena stop - Force the arena you are in to stop the game");
						player.sendMessage(ChatColor.GREEN
								+ "/arena status <id> - Check the arena status (gamestate, players, etc)");

					} else if (args[0].equalsIgnoreCase("start")) {
						if (Manager.isPlaying(player)) {
							if (!Manager.getArena(player).getState().equals(GameState.LIVE)) {
								Manager.getArena(player).start(GameType.VANILLA);
							} else {
								player.sendMessage(ChatColor.RED + "This game is already live!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You must be in an arena to use this command!");
						}
					} else if (args[0].equalsIgnoreCase("stop")) {
						if (Manager.isPlaying(player)) {
							if (Manager.getArena(player).getState().equals(GameState.LIVE)) {
								for (UUID uuid : Manager.getArena(player).getPlayers()) {
									Player players = Bukkit.getPlayer(uuid);
									players.sendMessage(ChatColor.GREEN
											+ "Your game was stopped by an administrator. Sending you to the lobby...");

								}
								Manager.getArena(player).reset();
							} else {
								player.sendMessage(ChatColor.RED + "This game is not live!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You must be in an arena to use this command!");
						}
					} else if (args[0].equalsIgnoreCase("status")) {
						player.sendMessage(ChatColor.RED + "Invalid usage! Usage: /arena status <id>");

					}
				}

				else if (args.length > 1) {
					if (args[0].equalsIgnoreCase("status")) {
						try {
							int arenaID = Integer.parseInt(args[1]);
							if (Manager.getArena(arenaID) != null) {
								Arena arena = Manager.getArena(arenaID);
								if (arena.getState().equals(GameState.LIVE)) {
									player.sendMessage(ChatColor.AQUA + "Arena ID: " + arena.getID());
									player.sendMessage(
											ChatColor.AQUA + "Game Type: " + arena.getGame(arena.getID()).getDisplay());
									player.sendMessage(ChatColor.AQUA + "Game Status: " + ChatColor.GREEN + "LIVE");
									player.sendMessage(ChatColor.AQUA + "Instance Type: " + arena.getType());
									player.sendMessage(ChatColor.AQUA + "Player Count: " + ChatColor.AQUA
											+ arena.getPlayers().size() + "/" + Config.getMaxPlayers(arenaID));
								} else if (arena.getState().equals(GameState.COUNTDOWN)) {
									player.sendMessage(ChatColor.AQUA + "Arena ID: " + arena.getID());
									player.sendMessage(ChatColor.AQUA + "Game Status: " + ChatColor.GOLD + "COUNTDOWN");
									player.sendMessage(
											ChatColor.AQUA + "Game Type: " + arena.getGame(arena.getID()).getDisplay());
									player.sendMessage(ChatColor.AQUA + "Player Count: " + ChatColor.AQUA
											+ arena.getPlayers().size() + "/" + Config.getMaxPlayers(arenaID));
								} else if (arena.getState().equals(GameState.RECRUITING)) {
									player.sendMessage(ChatColor.AQUA + "Arena ID: " + arena.getID());
									player.sendMessage(
											ChatColor.AQUA + "Game Type: " + arena.getGame(arena.getID()).getDisplay());
									player.sendMessage(
											ChatColor.AQUA + "Game Status: " + ChatColor.YELLOW + "RECRUITING");
									player.sendMessage(ChatColor.AQUA + "Player Count: " + ChatColor.AQUA
											+ arena.getPlayers().size() + "/" + Config.getMaxPlayers(arenaID));
								}

								else if (arena.getState().equals(GameState.LIVE_ENDING)) {
									player.sendMessage(ChatColor.AQUA + "Arena ID: " + arena.getID());
									player.sendMessage(
											ChatColor.AQUA + "Game Type: " + arena.getGame(arena.getID()).getDisplay());
									player.sendMessage(
											ChatColor.AQUA + "Game Status: " + ChatColor.GREEN + "LIVE_ENDING");
									player.sendMessage(ChatColor.AQUA + "Instance Type: " + arena.getType());
									player.sendMessage(ChatColor.AQUA + "Player Count: " + ChatColor.AQUA
											+ arena.getPlayers().size() + "/" + Config.getMaxPlayers(arenaID));
								}

								else {
									player.sendMessage(ChatColor.AQUA + "Arena ID: " + arena.getID());
									player.sendMessage(
											ChatColor.AQUA + "Game Type: " + arena.getGame(arena.getID()).getDisplay());
									player.sendMessage(ChatColor.AQUA + "Game Status: " + ChatColor.RED + "UNKNOWN");
									player.sendMessage(ChatColor.AQUA + "Player Count: " + ChatColor.AQUA
											+ arena.getPlayers().size() + "/" + Config.getMaxPlayers(arenaID));
								}
							} else {
								player.sendMessage(ChatColor.RED + "Invalid arena id!");
								player.sendMessage(ChatColor.AQUA + "For reference, here's all the arena IDS:");
								for (Arena arena : Manager.getArenas()) {
									player.sendMessage(ChatColor.AQUA + "- " + arena.getID());
								}
							}
						} catch (NumberFormatException e) {
							player.sendMessage(ChatColor.RED + "Invalid arena id!");
							player.sendMessage(ChatColor.AQUA + "For reference, here's all the arena IDS:");
							for (Arena arena : Manager.getArenas()) {
								player.sendMessage(ChatColor.AQUA + "- " + arena.getID());
							}
						}
					}

					else if (args[0].equalsIgnoreCase("start")) {
						if (Manager.isPlaying(player)) {
							String gameType = args[1];
							if (!Manager.getArena(player).getState().equals(GameState.LIVE)) {
								if (gameType.equalsIgnoreCase("vanilla") || gameType.equalsIgnoreCase("v")
										|| gameType.equalsIgnoreCase("default")) {
									Manager.getArena(player).start(GameType.VANILLA);
								}

								else if (gameType.equalsIgnoreCase("modifier") || gameType.equalsIgnoreCase("m")
										|| gameType.equalsIgnoreCase("mod") || gameType.equalsIgnoreCase("modified")) {
									Manager.getArena(player).start(GameType.MODIFIER);
								}

								else {
									player.sendMessage(
											ChatColor.RED + "Invalid game type! Valid game types: vanilla, modifier");
									player.sendMessage(ChatColor.AQUA
											+ "You can also use 'v' and 'default' in the argument for vanilla, and 'm', 'mod', and 'modified' for modifier.\nVanilla games are the default with /arena start. ");
								}
							} else {
								player.sendMessage(ChatColor.RED + "This game is already live!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You must be in an arena to use this command!");
						}

					}

					else {
						player.sendMessage(ChatColor.RED + "Invalid usage! Do /arena help to see all of your options.");
					}

				}
			} else {
				player.sendMessage(ChatColor.RED + "You must be admin or higher to use this command!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "This command is only available in game!");
		}

		return false;
	}
}
