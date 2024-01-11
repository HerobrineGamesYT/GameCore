package net.herobrine.gamecore;

import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.core.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SummonCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Ranks rank = HerobrinePVPCore.getFileManager().getRank(player);

            if (!(rank.getPermLevel() >= 9)) {
                player.sendMessage(ChatColor.RED + "You can't use this!");
                return false;
            }

            if (!Manager.isPlaying(player)) {
                player.sendMessage(ChatColor.RED + "You aren't in a game to summon players to!");
                return false;
            }

            if (Bukkit.getOnlinePlayers().size() <= 1) {
                player.sendMessage(ChatColor.RED + "There's nobody but you in the server!");
                return false;
            }

            Arena arena = Manager.getArena(player);

            int alreadyInArena = 0;
            int success = 0;
            int alreadyInLiveGame = 0;

            player.sendMessage(ChatColor.GREEN + "Summonining players..." + ChatColor.RED + "\n The countdown has been paused.");
            arena.getCountdown().pause();
            for (Player players : Bukkit.getOnlinePlayers()) {
                boolean cantAdd = false;
                if (Manager.isPlaying(players)) {
                    if (arena.getID() == Manager.getArena(player).getID()) {
                        alreadyInArena = alreadyInArena + 1;
                        cantAdd = true;
                    }
                    else if (Manager.getArena(players).getState().equals(GameState.LIVE)
                            || Manager.getArena(players).getState().equals(GameState.LIVE_ENDING)) {
                        alreadyInLiveGame = alreadyInLiveGame + 1;
                        cantAdd = true;
                    }

                    else Manager.getArena(players).removePlayer(players);

                }

               if (!cantAdd) {
                   player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                   arena.addPlayer(players);
                   players.sendMessage(HerobrinePVPCore.translateString("&d&lYOINK! &eYou were sent to game &6" +
                           arena.getGame(arena.getID()).getKey() + arena.getID() +  "&r&e by &c" + player.getName()));
                   success = success + 1;
               }

            }

            int finalSuccess = success;
            int finalInArena = alreadyInArena;
            int finalInGame = alreadyInLiveGame;
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
                    player.sendMessage(ChatColor.AQUA + "Summon Done!");
                    player.sendMessage(ChatColor.AQUA + "Successfully summoned " + ChatColor.GREEN + finalSuccess + ChatColor.AQUA + " players");
                    player.sendMessage("" + ChatColor.RED + finalInArena +  ChatColor.AQUA + " players were already in your arena!");
                    player.sendMessage("" + ChatColor.RED + finalInGame + ChatColor.AQUA + " players were already in live games.");

                    arena.getCountdown().resume();
                }
            }.runTaskLater(GameCoreMain.getInstance(), 20L);

        }

        return false;
    }
}
