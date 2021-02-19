package net.herobrine.gamecore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.core.Ranks;

public class StaffChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (HerobrinePVPCore.getFileManager().getRank(player).equals(Ranks.OWNER)
					|| HerobrinePVPCore.getFileManager().getRank(player).equals(Ranks.ADMIN)
					|| HerobrinePVPCore.getFileManager().getRank(player).equals(Ranks.MOD)
					|| HerobrinePVPCore.getFileManager().getRank(player).equals(Ranks.HELPER)) {
				if (args.length == 0) {
					player.sendMessage(ChatColor.RED
							+ "Invalid Usage! /sc <message> \nNOTE: You currently cannot toggle staff chat! Make sure you do /sc <message> for every SC message you need to send.");
				} else {
					int al = args.length;
					StringBuilder sb = new StringBuilder(args[0]);
					for (int i = 1; i < al; i++) {
						sb.append(' ').append(args[i]);
						// message is sb.toString();

					}

					for (Player players : Bukkit.getOnlinePlayers()) {
						if (HerobrinePVPCore.getFileManager().getRank(players).equals(Ranks.OWNER)
								|| HerobrinePVPCore.getFileManager().getRank(players).equals(Ranks.ADMIN)
								|| HerobrinePVPCore.getFileManager().getRank(players).equals(Ranks.MOD)
								|| HerobrinePVPCore.getFileManager().getRank(players).equals(Ranks.HELPER)) {
							Ranks rank = HerobrinePVPCore.getFileManager().getRank(player);
							players.sendMessage(ChatColor.AQUA + "[STAFF] " + rank.getColor() + rank.getName() + " "
									+ player.getName() + ": " + ChatColor.RESET + sb.toString());

						}

					}

				}
			}

			else {
				player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
			}

		} else {
			sender.sendMessage(ChatColor.RED + "Only a player can use this command!");
		}

		return false;
	}

}
