package net.herobrine.gamecore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 1) {
				player.sendMessage(ChatColor.RED + "Invalid usage! Usage: /join <mapID> OR /join.");
				player.sendMessage(ChatColor.RED
						+ "Unless there are a good amount of players on the server, it's best to do /join <mapID>");
				player.sendMessage(ChatColor.AQUA + "For reference, here's all the mapIDs you can join:");
				for (Arena arena : Manager.getArenas()) {
					player.sendMessage(ChatColor.AQUA + "- " + arena.getID());
				}

			}
			if (args.length == 1) {
				if (!Manager.isPlaying(player)) {
					try {
						int id = Integer.parseInt(args[0]);
						if (id >= 0 && id <= Config.getArenaAmount() - 1) {
							if (Manager.isRecruiting(id) || Manager.getArena(id).getState().equals(GameState.COUNTDOWN)
									&& Manager.getArena(id).getPlayers().size() < Config.getMaxPlayers(id)) {
								if (!Manager.getArena(id).getState().equals(GameState.LIVE)
										&& Manager.getArena(id).canJoin()) {
									Manager.getArena(id).addPlayer(player);
									player.sendMessage(
											ChatColor.GREEN + "You've been sent to game " + ChatColor.GOLD + id);
								} else {
									player.sendMessage(ChatColor.RED + "You cannot join game " + ChatColor.AQUA + id
											+ " right now!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "You cannot join game " + ChatColor.AQUA + id
										+ " because the lobby is full or the game has already started.");
							}

						}
					} catch (NumberFormatException e) {
						player.sendMessage(ChatColor.RED + "Invalid mapID!");
						player.sendMessage(ChatColor.AQUA + "For reference, here's all the mapIDs you can join:");
						for (Arena arena : Manager.getArenas()) {
							player.sendMessage(ChatColor.AQUA + "- " + arena.getID());
						}

					}
				} else {
					player.sendMessage(ChatColor.RED + "You are already in a game!");
				}

			}

			else {
				player.sendMessage(ChatColor.RED + "Invalid usage! Usage: /join <mapID>");
				player.sendMessage(ChatColor.RED
						+ "Alternativley, you can also just select the game you want to play with the game selector.");
			}
		} else {
			sender.sendMessage("Only players can use this command!");
		}

		return false;
	}
}
