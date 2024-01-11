package net.herobrine.gamecore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.herobrine.blockhunt.BlockHuntMain;
import net.herobrine.core.HerobrinePVPCore;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class GameCoreMain extends JavaPlugin {
	private static GameCoreMain instance;

	private Config config;

	public static GameCoreMain getInstance() {

		return instance;
	}

	@Override
	public void onEnable() {
		GameCoreMain.instance = this;
		config = new Config(this);

		if (getCustomAPI() == null) {
			System.out.println(
					"[GAME CORE] The Herobrine PVP Core was not found. HBPVP-Core is required for the plugin to function, disabling.");
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			System.out.println("[GAME CORE] Successfully hooked into Herobrine PVP Core Plugin!");
		}

		if (getBlockHuntAPI() == null) {
			System.out.println("[GAME CORE] Missing Game Plugin: Block Hunt");
			System.out.println("[GAME CORE] You need the game plugins to use the core!");
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			System.out.println("[GAME CORE] Hooked into the game plugin: Block Hunt");
		}

		new Manager();

		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		getCommand("join").setExecutor(new JoinCommand());

		getCommand("arena").setExecutor(new ArenaCommand());

		getCommand("staffchat").setExecutor(new StaffChatCommand());

		getCommand("skin").setExecutor(new SkinCommand());

		getCommand("lobby").setExecutor(new LobbyCommand());

		getCommand("spectate").setExecutor(new SpectateCommand());

		getCommand("shout").setExecutor(new ShoutCommand());


		getCommand("summonplayers").setExecutor(new SummonCommand());

	}

	public HerobrinePVPCore getCustomAPI() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("HBPVP-Core");
		if (plugin instanceof HerobrinePVPCore) {
			return (HerobrinePVPCore) plugin;
		} else {
			return null;
		}

	}

	public BlockHuntMain getBlockHuntAPI() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BlockHunt");

		if (plugin instanceof BlockHuntMain) {
			return (BlockHuntMain) plugin;
		} else {
			return null;
		}
	}

	public void startQueue(Player player, Games game, GameType type) {
		int timesLooped = 0;
		for (Arena arena : Manager.getArenas()) {
			if (timesLooped == 0) {
				player.sendMessage(ChatColor.GRAY + "Searching for a game of " + game.getDisplay());

			} else if (timesLooped == Manager.getArenas().size()) {

				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
				player.sendMessage(ChatColor.RED
						+ "Couldn't find a match! All servers may be full or unavailable. If you believe this is in error, please contact staff.");
				break;
			}

			if (arena.getState() == GameState.RECRUITING
					|| arena.getState() == GameState.COUNTDOWN && arena.getGame(arena.getID()).equals(game)) {
				if (arena.getPlayers().size() < Config.getMaxPlayers(arena.getID())) {

					if (!arena.getState().equals(GameState.LIVE) && arena.canJoin()
							&& arena.getGame(arena.getID()).equals(game) && arena.getType().equals(type)) {

						arena.addPlayer(player);
						player.sendMessage(ChatColor.GREEN + "Game found! You've been sent to game " + ChatColor.GOLD
								+ game.getKey() + arena.getID());
						break;
					}

				}
			}

			timesLooped++;
			if (timesLooped == Manager.getArenas().size()) {

				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
				player.sendMessage(ChatColor.RED
						+ "Couldn't find a match! All servers may be full or unavailable. If you believe this is in error, please contact staff.");
				break;
			}
		}

	}

	// Send a title & subtitle to a player, times are in seconds. If you need one
	// blank, put a blank string for which value you'd like blank.

	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		fadeIn = fadeIn * 20;
		stay = stay * 20;
		fadeOut = fadeOut * 20;

		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE,
				ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', title) + "\"}"), fadeIn,
				stay, fadeOut);

		PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
				ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}"),
				fadeIn, stay, fadeOut);

		((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(titlePacket);
		((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(subTitlePacket);

	}

	public void sendPacket(Player player, Packet<?> packet) {

		((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);

	}

//
// title is reset on join before the join title is sent. This allows for the rest of our titles to work, even if the player previously joined another server w/ titles.
	public void resetTitle(Player player) {
		((CraftPlayer) player).resetTitle();
	}

	public void sendActionBar(Player player, String text) {

		PacketPlayOutChat packet = new PacketPlayOutChat(
				ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', text) + "\"}"), (byte) 2);

		((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
	}

	public Config getConfigInstance(){return config;}

}
