package net.herobrine.gamecore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.herobrine.core.HerobrinePVPCore;

public class LobbyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (Manager.isPlaying(player)) {

				if (!Manager.getArena(player).getSpectators().contains(player.getUniqueId())) {
					Manager.getArena(player).sendMessage(HerobrinePVPCore.getFileManager().getRank(player).getColor()
							+ player.getName() + ChatColor.YELLOW + " has left!");
				}

				Manager.getArena(player).removePlayer(player);


			} else {
				player.sendMessage(ChatColor.RED + "You are not in a game!");
			}

		} else {
			sender.sendMessage(ChatColor.RED + "You can only use this command as a player!");
		}

		return false;
	}

}
