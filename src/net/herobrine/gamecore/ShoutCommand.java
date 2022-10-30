package net.herobrine.gamecore;


import java.util.UUID;

import net.herobrine.core.ItemTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.herobrine.core.Emotes;
import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.core.Ranks;

public class ShoutCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (!HerobrinePVPCore.getFileManager().isMuted(player.getUniqueId())) {

                if (Manager.isPlaying(player)) {

                    if (Manager.getArena(player).getState().equals(GameState.LIVE)
                            && Manager.getGame(Manager.getArena(player)).isTeamGame()) {

                        if (args.length > 0) {
                            int al = args.length;
                            StringBuilder sb = new StringBuilder(args[0]);
                            for (int i = 1; i < al; i++) {
                                sb.append(' ').append(args[i]);
                            }

                            Ranks rank = HerobrinePVPCore.getFileManager().getRank(player);
                            String message = sb.toString();

                            for (Emotes emote : Emotes.values()) {
                                if (message.contains(emote.getKey()) && emote.isUnlockRequired()) {
                                    if (HerobrinePVPCore.getFileManager().isItemUnlocked(ItemTypes.EMOTE, emote.name(), player.getUniqueId())) message = message.replaceAll(emote.getKey(), emote.getDisplay());
                                }
                                else if (message.contains(emote.getKey()) && rank.getPermLevel() >= 5) message = message.replaceAll(emote.getKey(), emote.getDisplay());
                            }

                            for (UUID uuid : Manager.getArena(player).getPlayers()) {
                                Player onlinePlayers = Bukkit.getPlayer(uuid);

                                if (rank.equals(Ranks.MEMBER)) onlinePlayers.sendMessage(ChatColor.GOLD + "[SHOUT] " + rank.getColor() + rank.getName() + " " + player.getName() + ": " + ChatColor.GRAY + message);
                                else if (rank.getPermLevel() >= 9) onlinePlayers.sendMessage(ChatColor.GOLD + "[SHOUT] " + rank.getColor() + rank.getName() + " " + player.getName() + ChatColor.RESET + ": " + ChatColor.translateAlternateColorCodes('&', message));
                                else  if (rank.hasPlusColor()) onlinePlayers.sendMessage(ChatColor.GOLD + "[SHOUT] " + rank.getColor() + rank.getName() + HerobrinePVPCore.translateString(HerobrinePVPCore.getFileManager().getPlusColor(player.getUniqueId()) + "+") + rank.getColor() + " " + player.getName() + ": " + ChatColor.RESET + message);
                                else onlinePlayers.sendMessage(ChatColor.GOLD + "[SHOUT] " + rank.getColor() + rank.getName() + " " + player.getName() + ": " + ChatColor.RESET + message);

                                }
                            }

                        else player.sendMessage(ChatColor.RED + "Invalid usage! Usage: /shout <message>");
                        }

                    else player.sendMessage(ChatColor.RED + "You must be in a live team game to use this command!");
                    }

                else player.sendMessage(ChatColor.RED + "You must be in a live team game to use this command!");
                }

            else player.sendMessage(ChatColor.RED + "You cannot use this command while muted!");
            }
        else sender.sendMessage(ChatColor.RED + "Only players can use this command!");

        return false;
    }

}
