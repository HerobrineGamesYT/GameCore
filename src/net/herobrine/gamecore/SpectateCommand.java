package net.herobrine.gamecore;

import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.core.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (HerobrinePVPCore.getFileManager().getRank(player).getPermLevel() >= 8) {


                if (args.length == 1) {

                    if (!Manager.isPlaying(player)) {

                        try {
                            int arenaID = Integer.parseInt(args[0]);

                            if (Manager.getArena(arenaID) != null) {

                                Arena arena = Manager.getArena(arenaID);

                                if (arena.getState() == GameState.LIVE) {
                                    player.teleport(arena.getSpawn());
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        if (HerobrinePVPCore.getFileManager().getRank(player).getPermLevel() >= 7) {
                                            Ranks rank = HerobrinePVPCore.getFileManager().getRank(player);
                                            players.sendMessage(ChatColor.AQUA + "[STAFF] " + rank.getColor() + rank.getName() + " "
                                                    + player.getName() + ChatColor.RESET +  " is now spectating game " + ChatColor.GOLD + arena.getID());

                                        }

                                        arena.addAsSpectator(player, true, false);

                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "That game is not live!");
                                }


                            } else {
                                player.sendMessage(ChatColor.RED + "Invalid Arena ID!");
                                player.sendMessage(ChatColor.GREEN + "Here's all of the arena IDs you can currently spectate: ");
                                for (Arena arena : Manager.getArenas()) {
                                    if (arena.getState() == GameState.LIVE) {
                                        player.sendMessage(arena.getGame(arena.getID()).getDisplay() + ChatColor.GRAY + "-" + ChatColor.GREEN + arena.getID());
                                    }
                                }

                            }

                        } catch (NumberFormatException e) {


                            Player target = Bukkit.getPlayer(args[0]);

                            if (target == null) {
                                player.sendMessage(ChatColor.RED + "Invalid player!");
                                return true;
                            }


                            if (Manager.isPlaying(target)) {
                                Arena arena = Manager.getArena(target);

                                if (arena.getState().equals(GameState.LIVE)) {


                                    player.teleport(target);
                                    player.sendMessage(ChatColor.GREEN + "You are now spectating " + ChatColor.GOLD + target.getName());

                                    arena.addAsSpectator(player, true, false);
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        if (HerobrinePVPCore.getFileManager().getRank(player).getPermLevel() >= 7) {
                                            Ranks rank = HerobrinePVPCore.getFileManager().getRank(player);
                                            players.sendMessage(ChatColor.AQUA + "[STAFF] " + rank.getColor() + rank.getName() + " "
                                                    + player.getName() + ChatColor.RESET + " is now spectating " + ChatColor.GOLD + target.getName());

                                        }


                                    }


                                }


                            }
                            else{
                                player.sendMessage(ChatColor.RED + "That player is not in a live game!");
                            }


                        }

                    }
                    else{
                        player.sendMessage(ChatColor.RED + "You cannot use this command while in a game!");
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Invalid Usage! Usage: /spectate <player> OR /spectate <arenaID>");

                }



            }
            else {
                player.sendMessage(ChatColor.RED + "You do not have permission to do ths!");
            }

        }

        return false;
    }
}
