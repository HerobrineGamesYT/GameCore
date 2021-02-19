package net.herobrine.gamecore;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.herobrine.core.HerobrinePVPCore;

public class Listeners implements Listener {

	@EventHandler

	public void onJoin(PlayerJoinEvent e) {
		e.getPlayer().teleport(Config.getLobbySpawn());

	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().hasItemMeta()) {

			if (e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)
					&& e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Spectate")) {

				if (Manager.isPlaying(e.getPlayer())) {
					Player player = e.getPlayer();

					Arena arena = Manager.getArena(player);
					Inventory menu = Bukkit.createInventory(null, 9,
							ChatColor.translateAlternateColorCodes('&', "&aChoose who to spectate!"));
					int i = 0;
					for (UUID uuid : Manager.getArena(e.getPlayer()).getPlayers()) {

						Player target = Bukkit.getPlayer(uuid);
						if (target.getUniqueId() != player.getUniqueId() && !arena.getSpectators().contains(uuid)) {
							ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
							SkullMeta meta = (SkullMeta) skull.getItemMeta();

							meta.setOwner(target.getName());
							if (arena.getGame(arena.getID()).isTeamGame()) {
								meta.setDisplayName(arena.getTeam(target).getColor() + target.getName());
							} else {
								meta.setDisplayName(HerobrinePVPCore.getFileManager().getRank(target).getColor()
										+ target.getName());
							}

							skull.setItemMeta(meta);
							menu.setItem(i, skull);
							i++;
						}
					}
					player.openInventory(menu);
				} else {
					e.getPlayer().sendMessage(ChatColor.RED + "You are not playing a game!");
				}

			}

		}

	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (Manager.isPlaying(player)) {

			if (Manager.getArena(player).getSpectators().contains(player.getUniqueId())) {

				e.setCancelled(true);

			}
		}

		if (ChatColor.translateAlternateColorCodes('&', e.getClickedInventory().getTitle())
				.equals(ChatColor.translateAlternateColorCodes('&', "&aChoose who to spectate!"))) {
			e.setCancelled(true);

			if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {

				Player target = Bukkit
						.getPlayerExact(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
				player.closeInventory();
				player.teleport(target.getLocation());

			}

		}

	}

	@EventHandler

	public void onDrop(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		if (Manager.isPlaying(player)) {

			if (Manager.getArena(player).getSpectators().contains(player.getUniqueId())) {
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		Player player = e.getPlayer();
		if (Manager.isPlaying(player)) {
			if (Manager.getArena(player).getSpectators().contains(player.getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player player = (Player) e.getDamager();

			if (Manager.isPlaying(player)) {
				if (Manager.getArena(player).getSpectators().contains(player.getUniqueId())) {
					e.setCancelled(true);
				}

			}

		}

	}

}
