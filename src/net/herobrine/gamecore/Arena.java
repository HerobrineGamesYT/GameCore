package net.herobrine.gamecore;

import java.io.File;
import java.io.IOException;
import java.util.*;

import net.herobrine.core.LevelRewards;
import net.herobrine.wallsg.*;
import net.herobrine.wallsg.game.*;
import net.herobrine.wallsg.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.TreeMultimap;

import net.herobrine.blockhunt.BlockHuntGame;
import net.herobrine.blockhunt.ModifiedTypes;
import net.herobrine.clashroyale.Archer;
import net.herobrine.clashroyale.Bandit;
import net.herobrine.clashroyale.BattleHealer;
import net.herobrine.clashroyale.ClashRoyaleGame;
import net.herobrine.clashroyale.Fisherman;
import net.herobrine.clashroyale.Knight;
import net.herobrine.clashroyale.Lumberjack;
import net.herobrine.clashroyale.Witch;
import net.herobrine.clashroyale.Wizard;
import net.herobrine.core.HerobrinePVPCore;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Arena {
	private int id;
	private ArrayList<UUID> players;
	private HashMap<UUID, Teams> teams;
	private HashMap<UUID, Class> classes;
	private ArrayList<ModifiedTypes> bhModifiers;
	private ArrayList<UUID> spectators;
	private Location spawn;
	private GameState state;
	private GameType type;
	private Countdown countdown;
	private BlockHuntGame blockHuntGame;
	private ClashRoyaleGame clashRoyaleGame;
	private Game wallsSGGame;
	private boolean canJoin;

	public void freezeEntity(Entity en) {
		net.minecraft.server.v1_8_R3.Entity nmsEn = ((CraftEntity) en).getHandle();
		NBTTagCompound compound = new NBTTagCompound();
		nmsEn.c(compound);
		compound.setByte("NoAI", (byte) 1);
		nmsEn.f(compound);
	}

	public Arena(int id) {
		this.id = id;

		players = new ArrayList<>();
		teams = new HashMap<>();
		classes = new HashMap<>();
		type = GameType.VANILLA;
		spawn = net.herobrine.gamecore.Config.getArenaSpawn(id);

		if (!getGame(id).equals(Games.WALLS_SG)) {
			spawn.getWorld().setGameRuleValue("keepInventory", "true");
		}

		state = GameState.RECRUITING;
		countdown = new Countdown(this);
		spectators = new ArrayList<>();
		if (getGame(id).equals(Games.BLOCK_HUNT)) {
			bhModifiers = new ArrayList<>();
			blockHuntGame = new BlockHuntGame(this);
			if (getGame(id).requiresNewWorld()) {

				WorldCreator wc = new WorldCreator("bhMap" + id);

				wc.environment(Environment.NORMAL);

				wc.type(WorldType.NORMAL);

				wc.createWorld();
			}
		} else if (getGame(id).equals(Games.MLG_RUSH)) {
			// game = new MLGRushGame(this);
		}

		else if (getGame(id).equals(Games.SKYWARS)) {
			// game = new SkywarsGame(this);
		}

		else if (getGame(id).equals(Games.BEDWARS)) {
			// game = new BedwarsGame(this);
		} else if (getGame(id).equals(Games.CLASH_ROYALE)) {

			if (GameCoreMain.getInstance().getConfig().getString("arenas." + id + ".game-mod") != null) {
				if (GameType.valueOf(GameCoreMain.getInstance().getConfig().getString("arenas." + id + ".game-mod"))
						.equals(GameType.CLASH_ROYALE)) {

					setType(GameType.CLASH_ROYALE);
					clashRoyaleGame = new ClashRoyaleGame(this, true);
				} else {

					clashRoyaleGame = new ClashRoyaleGame(this, false);
				}
			}

			else {

				clashRoyaleGame = new ClashRoyaleGame(this, false);
			}

		}

		else if (getGame(id).equals(Games.WALLS_SG)) {

			setType(GameType.VANILLA);
			wallsSGGame = new Game(this);
		}

		else {
			System.out.println("[GAME CORE] URGENT ERROR! UNABLE TO INITIALIZE ARENA: " + id
					+ "\nReason: Unable to determine the game type");

		}

		canJoin = true;


	}

	public void start(GameType type) {
		if (getGame(id).equals(Games.BLOCK_HUNT)) {
			setType(type);
			blockHuntGame.startBlockHunt(type);
		}

		else if (getGame(id).equals(Games.MLG_RUSH)) {
			// mlgRushGame.start
		}

		else if (getGame(id).equals(Games.SKYWARS)) {
			// skywarsGame.start
		}

		else if (getGame(id).equals(Games.BEDWARS)) {
			// bedwarsGame.start
		} else if (getGame(id).equals(Games.CLASH_ROYALE)) {

			if (Config.getGameMod(id) != null) {

				if (Config.getGameMod(id).equals(GameType.CLASH_ROYALE)) {
					setType(GameType.CLASH_ROYALE);
					clashRoyaleGame.start(GameType.CLASH_ROYALE);
				}

			} else {
				setType(type);
				clashRoyaleGame.start(GameType.VANILLA);
			}

		} else if (getGame(id).equals(Games.WALLS_SG)) {
			setType(type);
			wallsSGGame.start();
		}

		else {
			sendMessage(ChatColor.RED + "Unable to start the game! Reason: Unable to determine game type");
			reset();
		}

	}

	public void reset() {


		for (UUID uuid : players) {
			removeClass(uuid);
			Player player = Bukkit.getPlayer(uuid);
			removeSpectator(player);
			player.setDisplayName(player.getName());
			player.getEquipment().setHelmet(null);
			player.getEquipment().setChestplate(null);
			player.getEquipment().setLeggings(null);
			player.getEquipment().setBoots(null);
			player.setExp(0.0F);
			player.setLevel(0);
			player.getInventory().clear();
			player.setHealth(20.0);
			player.getEnderChest().clear();
			player.setDisplayName(player.getName());

			for (PotionEffect effect : player.getActivePotionEffects()) {

				player.removePotionEffect(effect.getType());
			}
			ItemStack gameSelector = new ItemStack(Material.COMPASS, 1);
			ItemMeta selectorMeta = gameSelector.getItemMeta();
			selectorMeta.setDisplayName(ChatColor.GREEN + "Game Selector");
			gameSelector.setItemMeta(selectorMeta);
			ItemBuilder shop = new ItemBuilder(Material.EMERALD);
			shop.setDisplayName(ChatColor.AQUA + "Shop " + ChatColor.GRAY + "(Right Click)");

			ItemBuilder cosmetics = new ItemBuilder(Material.CHEST);

			cosmetics.setDisplayName(ChatColor.AQUA + "Cosmetics " + ChatColor.GRAY + "(Coming Soon)");

			ItemStack profile = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
			SkullMeta profileMeta = (SkullMeta) profile.getItemMeta();

			profileMeta.setOwner(player.getName());
			profileMeta.setDisplayName(ChatColor.GREEN + "My Profile " + ChatColor.GRAY + "(Right Click)");
			profile.setItemMeta(profileMeta);

			player.setHealth(20.0);
			player.setGameMode(GameMode.SURVIVAL);

			for (Player players : Bukkit.getOnlinePlayers()) {
				players.showPlayer(player);
			}

			HerobrinePVPCore.buildSidebar(player);
			player.teleport(net.herobrine.gamecore.Config.getLobbySpawn());

			player.getInventory().setItem(0, gameSelector);
			player.getInventory().setItem(1, profile);
			player.getInventory().setItem(3, shop.build());
			player.getInventory().setItem(4, cosmetics.build());

			if (getGame(id).requiresNewWorld()) {
				player.sendMessage(
						ChatColor.RED + "A new world is generating for the game you just played, lag incoming!");
			}

		}

		if (!getType().equals(GameType.CLASH_ROYALE)) {
			type = GameType.VANILLA;
		}

		state = GameState.RECRUITING;
		players.clear();
		teams.clear();
		if (getGame(id).equals(Games.BLOCK_HUNT)) {
			blockHuntGame.getList().clear();
			canJoin = false;
			if (getGame(id).requiresNewWorld()) {

				World world = Bukkit.getWorld("bhMap" + id);

				Bukkit.unloadWorld(Bukkit.getWorld("bhMap" + id), false);

				try {
					FileUtils.deleteDirectory(world.getWorldFolder());
				} catch (IOException e) {

					e.printStackTrace();
				}

				WorldCreator wc = new WorldCreator("bhMap" + id);

				wc.environment(Environment.NORMAL);

				wc.type(WorldType.NORMAL);

				wc.createWorld();
				Bukkit.getWorld("bhMap" + id).setGameRuleValue("keepInventory", "true");
			}

			// blockHuntGame = new BlockHuntGame(this);
			// is deprecated in favor of the new localized runnable system in each game
			// class
		}

		else if (getGame(id).equals(Games.CLASH_ROYALE)) {
			for (Entity ent : net.herobrine.gamecore.Config.getGameWorld(id).getEntities()) {if (!(ent instanceof Player)) ent.remove();}
			getBattleClash().kills.clear();
		}

		else if (getGame(id).equals(Games.MLG_RUSH)) {

			// game = new MLGRushGame(this);
		}

		else if (getGame(id).equals(Games.SKYWARS)) {

			// game = new SkywarsGame(this);
		}

		else if (getGame(id).equals(Games.BEDWARS)) {

			// game = new BedwarsGame(this);
		}

		else if (getGame(id).equals(Games.WALLS_SG)) {
			Game.resetChests();
			wallsSGGame.resetBlocks();

			for (Entity ent : net.herobrine.gamecore.Config.getGameWorld(id).getEntities()) {if (!(ent instanceof Player)) ent.remove();}

			Game.getAlivePlayers().clear();
			Game.kills.clear();
			getwallsSGGame().getCustomDeathCause().clear();

		}

		countdown = new Countdown(this);

		spawn = net.herobrine.gamecore.Config.getArenaSpawn(id);
		System.gc();
		canJoin = true;
	}

	public void softReset() {
		state = GameState.RECRUITING;
		countdown = new Countdown(this);
		canJoin = true;
		spawn = net.herobrine.gamecore.Config.getArenaSpawn(id);
	}

	public void sendMessage(String message) {
		for (UUID uuid : players) {
			Bukkit.getPlayer(uuid).sendMessage(message);
		}
	}

	public void playCountdownSounds() {
		if (state == GameState.COUNTDOWN) {

			for (UUID uuid : players) {
				Player player = Bukkit.getPlayer(uuid);
				player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
			}
		}
		if (state == GameState.RECRUITING) {
			for (UUID uuid : players) {
				Player player = Bukkit.getPlayer(uuid);
				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
			}
		}
	}

	public void playSound(Sound sound) {
		for (UUID uuid : players) {
			Player player = Bukkit.getPlayer(uuid);
			player.playSound(player.getLocation(), sound, 1f, 1f);
		}
	}

	public void playEndSounds(Teams team, Sound sound, Sound sound2) {
		for (UUID uuid : players) {
			Player player = Bukkit.getPlayer(uuid);
			if (getTeam(player).equals(team) && !getSpectators().contains(player.getUniqueId())) {
				player.playSound(player.getLocation(), sound, 1f, 1f);
			} else {
				player.playSound(player.getLocation(), sound2, 1f, 1f);
			}
		}
	}

	public void setCanJoin(boolean set) {
		canJoin = set;

	}

	public void addPlayer(Player player) {
		players.add(player.getUniqueId());
		if (player.getAllowFlight()) {
			player.setAllowFlight(false);
		}

		player.setLevel(0);
		player.setExp(0F);
		player.teleport(spawn);
		clearInventory(player);
		ItemStack leaveItem = new ItemStack(Material.REDSTONE);
		ItemMeta leaveItemMeta = leaveItem.getItemMeta();
		leaveItemMeta.setDisplayName(ChatColor.RED + "Leave");
		leaveItem.setItemMeta(leaveItemMeta);

		if (getGame(id).hasVoting()) {
			ItemStack voteItem = new ItemStack(Material.NOTE_BLOCK);
			ItemMeta voteItemMeta = voteItem.getItemMeta();
			voteItemMeta.setDisplayName(ChatColor.AQUA + "Vote for the game type!");
			voteItem.setItemMeta(voteItemMeta);

			player.getInventory().setItem(0, voteItem);
		}

		if (getGame(id).hasKits()) {

			ItemStack classSelector = new ItemStack(Material.BOOKSHELF);
			ItemMeta classSelectorMeta = classSelector.getItemMeta();
			classSelectorMeta.setDisplayName(ChatColor.AQUA + "Class Selector");
			classSelector.setItemMeta(classSelectorMeta);
			player.getInventory().setItem(0, classSelector);
		}
		if (getGame(id).equals(Games.BLOCK_HUNT)) {
			ItemStack howToPlay = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta howToPlayMeta = (BookMeta) howToPlay.getItemMeta();

			howToPlayMeta.setTitle(ChatColor.GREEN + "How To Play");
			howToPlayMeta.setAuthor(ChatColor.RED + "Herobrine Gaming Events");
			String page1 = "Welcome to " + ChatColor.GREEN + "Block Hunt!\n" + ChatColor.BLACK
					+ "Block Hunt is a game about teamwork and Minecraft skills.\n"
					+ "Your team will be assigned a block to find/craft using the world's resources and stand on within 5 minutes. \n Block Hunt is based on a best of 3 system.";
			String page2 = ChatColor.AQUA + "Useful Commands:\n" + ChatColor.BLACK
					+ "- /top: Teleport to the surface, or get unstuck\n"
					+ "- /teamtp OR /teamtp <teammate>: Teleport to a teammate\n"
					+ "- /recipes: View all recipes for Special Items, if the modifier is enabled.\n"
					+ "- /shout <message>: Talk to all players in-game!";
			String page3 = ChatColor.GOLD + "Modifiers:\n" + ChatColor.BLACK
					+ "- Should modifiers win the game type vote, 3 modifiers will be randomly selected and applied to the game. There are a lot of modifiers, such as Speed, Haste, Mob Damage Enabled, Special Items Enabled, and many more!";
			String page4 = ChatColor.RED + "Special Items Info\n" + ChatColor.BLACK
					+ "If the Special Items modifier is enabled, there are an assortment of items that become available which you can view by right clicking your recipe book or doing /recipes. There are also special blocks you use the items to obtain.";

			howToPlayMeta.addPage(page1, page2, page3, page4);
			howToPlay.setItemMeta(howToPlayMeta);
			player.getInventory().setItem(4, howToPlay);

		}
		player.getInventory().setItem(8, leaveItem);

		if (getGame(id).isTeamGame()) {
			TreeMultimap<Integer, Teams> count = TreeMultimap.create();
			int i = 1;
			for (Teams team : Teams.values()) {

				if (i > getGame(id).getTeamCount()) {
					break;
				} else {
					count.put(getTeamCount(team), team);
				}

				i++;
			}
			Teams selected = (Teams) count.values().toArray()[0];
			setTeam(player, selected);
			player.sendMessage(ChatColor.AQUA + "You are on the " + selected.getDisplay() + ChatColor.AQUA + " team");
			Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective obj = board.registerNewObjective("lobby", "dummy");
			obj.setDisplayName(getGame(id).getDisplay());
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);

			Score gameid = obj.getScore(ChatColor.translateAlternateColorCodes('&', getGame(id).getKey() + id));
			gameid.setScore(6);
			Score blank1 = obj.getScore(" ");
			blank1.setScore(5);

			Team team = board.registerNewTeam("team");
			team.addEntry(ChatColor.RED.toString());
			team.setPrefix(ChatColor.AQUA + "Team: ");
			team.setSuffix(selected.getDisplay());
			obj.getScore(ChatColor.RED.toString()).setScore(4);

			Score blank2 = obj.getScore("  ");
			blank2.setScore(3);
			Team playerCount = board.registerNewTeam("player_count");
			playerCount.addEntry(ChatColor.BLACK.toString());
			playerCount.setPrefix("Players: ");
			playerCount.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a") + players.size() + ChatColor.GREEN
					+ "/" + net.herobrine.gamecore.Config.getMaxPlayers(id));
			obj.getScore(ChatColor.BLACK.toString()).setScore(2);
			Score ip = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&cherobrinepvp.beastmc.com"));
			ip.setScore(1);
			player.setCustomName(getGame(id).getColor() + player.getName());
			player.setScoreboard(board);

		}

		else {
			Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective obj = board.registerNewObjective("lobby", "dummy");
			obj.setDisplayName(getGame(id).getDisplay());
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);

			Score gameid = obj.getScore(ChatColor.translateAlternateColorCodes('&', getGame(id).getKey() + id));
			gameid.setScore(6);
			Score blank1 = obj.getScore(" ");
			blank1.setScore(5);
			Team playerCount = board.registerNewTeam("player_count");
			playerCount.addEntry(ChatColor.BLACK.toString());
			playerCount.setPrefix("Players: ");
			playerCount.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a") + players.size() + ChatColor.GREEN
					+ "/" + net.herobrine.gamecore.Config.getMaxPlayers(id));
			obj.getScore(ChatColor.BLACK.toString()).setScore(4);
			Score ip = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&cherobrinepvp.beastmc.com"));
			ip.setScore(1);
			player.setCustomName(getGame(id).getColor() + player.getName());
			player.setScoreboard(board);
		}

		for (UUID uuid : players) {
			Player player2 = Bukkit.getPlayer(uuid);
			Bukkit.getPlayer(uuid)
					.sendMessage(HerobrinePVPCore.getFileManager().getRank(player).getColor() + player.getName()
							+ ChatColor.YELLOW + " has joined (" + ChatColor.AQUA + players.size() + ChatColor.YELLOW
							+ "/" + ChatColor.AQUA + net.herobrine.gamecore.Config.getMaxPlayers(id) + ChatColor.YELLOW
							+ ")!");

			player2.getScoreboard().getTeam("player_count").setSuffix(ChatColor.translateAlternateColorCodes('&', "&a")
					+ players.size() + ChatColor.GREEN + "/" + net.herobrine.gamecore.Config.getMaxPlayers(id));

		}
		if (players.size() >= net.herobrine.gamecore.Config.getRequiredPlayers(id)
				&& !getState().equals(GameState.COUNTDOWN)) {
			countdown.begin();
		}

		System.gc();
	}

	public void removePlayer(Player player) {
		players.remove(player.getUniqueId());
		if (getGame(id).equals(Games.WALLS_SG) && getState().equals(GameState.LIVE)) {

			Game game = getwallsSGGame();
			if (Game.getAlivePlayers().contains(player.getUniqueId())) {

				Game.getAlivePlayers().remove(player.getUniqueId());
				if (Game.alivePlayers1.containsKey(player.getUniqueId())) {

					Game.alivePlayers1.remove(player.getUniqueId());

					if (getTeam(player).equals(Teams.RED)) {

						game.aliveRedPlayers = game.aliveRedPlayers - 1;
					}

					else if (getTeam(player).equals(Teams.BLUE)) {

						game.aliveBluePlayers = game.aliveBluePlayers - 1;
					}

					else if (getTeam(player).equals(Teams.YELLOW)) {

						game.aliveYellowPlayers = game.aliveYellowPlayers - 1;
					}

					else if (getTeam(player).equals(Teams.GREEN)) {

						game.aliveGreenPlayers = game.aliveGreenPlayers - 1;

					}
				}
			}
		}

		removeTeam(player);
		removeClass(player.getUniqueId());
		removeSpectator(player);

		if (getState() != GameState.LIVE) {
			for (UUID uuid : players) {
				Player player2 = Bukkit.getPlayer(uuid);
				if (getState() != GameState.LIVE) {

					player2.getScoreboard().getTeam("player_count")
							.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a") + players.size()
									+ ChatColor.GREEN + "/" + net.herobrine.gamecore.Config.getMaxPlayers(id));
				} else {
					break;
				}

			}
		}

		if (player.isOnline()) {
			player.setDisplayName(player.getName());
			player.getEquipment().setHelmet(null);
			player.getEquipment().setChestplate(null);
			player.getEquipment().setLeggings(null);
			player.getEquipment().setBoots(null);
			player.setExp(0.0F);
			player.setLevel(0);
			player.getInventory().clear();
			player.setHealth(20.0);
			player.getEnderChest().clear();
			ItemStack gameSelector = new ItemStack(Material.COMPASS, 1);
			ItemMeta selectorMeta = gameSelector.getItemMeta();
			selectorMeta.setDisplayName(ChatColor.GREEN + "Game Selector");
			gameSelector.setItemMeta(selectorMeta);
			ItemBuilder shop = new ItemBuilder(Material.EMERALD);
			shop.setDisplayName(ChatColor.AQUA + "Shop " + ChatColor.GRAY + "(Right Click)");

			ItemBuilder cosmetics = new ItemBuilder(Material.CHEST);

			cosmetics.setDisplayName(ChatColor.AQUA + "Cosmetics " + ChatColor.GRAY + "(Coming Soon)");

			ItemStack profile = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
			SkullMeta profileMeta = (SkullMeta) profile.getItemMeta();

			profileMeta.setOwner(player.getName());
			profileMeta.setDisplayName(ChatColor.GREEN + "My Profile " + ChatColor.GRAY + "(Right Click)");
			profile.setItemMeta(profileMeta);

			player.teleport(net.herobrine.gamecore.Config.getLobbySpawn());

			player.getInventory().setItem(0, gameSelector);
			player.getInventory().setItem(1, profile);
			player.getInventory().setItem(3, shop.build());
			player.getInventory().setItem(4, cosmetics.build());

			player.setLevel(HerobrinePVPCore.getFileManager().getPlayerLevel(player.getUniqueId()));
			player.setExp(HerobrinePVPCore.getFileManager().getPlayerXP(player.getUniqueId())
					/ HerobrinePVPCore.getFileManager().getMaxXP(player.getUniqueId()));

			HerobrinePVPCore.buildSidebar(player);
		} else {
			return;
		}

		if (getState() != GameState.LIVE) {
			System.gc();
		}

		if (getState() == GameState.LIVE) {

			if (getPlayers().size() <= 1) {
				if (getPlayers().size() != 0) {
					sendMessage(ChatColor.RED + "The other players left, so the game has ended.");

				}
				reset();
			}

		}

	}

	public int getID() {
		return id;
	}

	public HashMap<UUID, Class> getClasses() {
		return classes;
	}

	public ArrayList<UUID> getSpectators() {
		return spectators;
	}

	public List<UUID> getPlayers() {
		return players;
	}

	public GameState getState() {
		return state;
	}

	public ClashRoyaleGame getBattleClash() {
		return clashRoyaleGame;
	}

	public void removeClass(UUID uuid) {
		if (classes.containsKey(uuid)) {

			if (getGame(id).equals(Games.WALLS_SG) && getType().equals(GameType.MODIFIER)) {
				if (getClass(uuid).equals(ClassTypes.ENGINEER)) {
					Engineer engineer = (Engineer) getClasses().get(uuid);
					engineer.undoSession(this);
				}
			}

			classes.get(uuid).remove();
			classes.remove(uuid);
		}
	}

	public void distributeRewards(Teams winner) {

		System.out.println("Giving rewards to: " + winner.getDisplay());

		for (UUID uuid: players) {

			Player player = Bukkit.getPlayer(uuid);

			if (getTeam(player) == winner) {

				LevelRewards prestige = HerobrinePVPCore.getFileManager().getPrestige(HerobrinePVPCore.getFileManager().getPlayerLevel(uuid));
				int earnedCoins = (int)Math.round(prestige.getGameCoinMultiplier() *  getGame(id).getBaseWinCoins());

				int earnedXP = (int) Math.round(prestige.getBaseXPBoost() * getGame(id).getBaseWinXP());

				HerobrinePVPCore.getFileManager().addCoins(player, earnedCoins);
 				HerobrinePVPCore.getFileManager().addTrophies(player, 1);

				 player.sendMessage(ChatColor.GOLD + "+1 Trophy! (Win)");
				 player.sendMessage(ChatColor.YELLOW + "+" + earnedCoins + " Coins! (Win)");

				if (HerobrinePVPCore.getFileManager().getPlayerLevel(player.getUniqueId()) < 100) {

					HerobrinePVPCore.getFileManager().addPlayerXP(uuid, earnedXP);
					player.sendMessage(ChatColor.AQUA + "+" + earnedXP + " XP! (Win)");
				}
				else {
					HerobrinePVPCore.getFileManager().addCoins(player, 100);
					player.sendMessage(ChatColor.YELLOW + "+100 Coins! (Max Level Bonus)");

				}

			}
			else {
				LevelRewards prestige = HerobrinePVPCore.getFileManager().getPrestige(HerobrinePVPCore.getFileManager().getPlayerLevel(uuid));
				int earnedCoins = (int)Math.round(prestige.getGameCoinMultiplier() *  getGame(id).getBaseCoins());

				int earnedXP = (int) Math.round(prestige.getBaseXPBoost() * getGame(id).getBaseXP());

				HerobrinePVPCore.getFileManager().addCoins(player, earnedCoins);


				player.sendMessage(ChatColor.YELLOW + "+" + earnedCoins + " Coins! (Playing)");

				if (HerobrinePVPCore.getFileManager().getPlayerLevel(player.getUniqueId()) < 100) {

					HerobrinePVPCore.getFileManager().addPlayerXP(uuid, earnedXP);
					player.sendMessage(ChatColor.AQUA + "+" + earnedXP + " XP! (Playing)");
				}
				else {
					HerobrinePVPCore.getFileManager().addCoins(player, 100);
					player.sendMessage(ChatColor.YELLOW + "+100 Coins! (Max Level Bonus)");

				}
			}

		}




	}

	public void distributeRewards(UUID winner) {

		for (UUID uuid: players) {

			Player player = Bukkit.getPlayer(uuid);

			if (uuid == winner) {

				LevelRewards prestige = HerobrinePVPCore.getFileManager().getPrestige(HerobrinePVPCore.getFileManager().getPlayerLevel(uuid));
				int earnedCoins = (int)Math.round(prestige.getGameCoinMultiplier() *  getGame(id).getBaseWinCoins());

				int earnedXP = (int) Math.round(prestige.getBaseXPBoost() * getGame(id).getBaseWinXP());

				HerobrinePVPCore.getFileManager().addCoins(player, earnedCoins);


				player.sendMessage(ChatColor.YELLOW + "+" + earnedCoins + " coins! (Win)");

				if (HerobrinePVPCore.getFileManager().getPlayerLevel(player.getUniqueId()) < 100) {
					player.sendMessage(ChatColor.AQUA + "+" + earnedXP + " XP! (Win)");
					HerobrinePVPCore.getFileManager().addPlayerXP(uuid, earnedXP);

				}
				else {
					HerobrinePVPCore.getFileManager().addCoins(player, 100);
					player.sendMessage(ChatColor.YELLOW + "+100 Coins! (Max Level Bonus)");

				}

			}
			else {
				LevelRewards prestige = HerobrinePVPCore.getFileManager().getPrestige(HerobrinePVPCore.getFileManager().getPlayerLevel(uuid));
				int earnedCoins = (int)Math.round(prestige.getGameCoinMultiplier() *  getGame(id).getBaseCoins());

				int earnedXP = (int) Math.round(prestige.getBaseXPBoost() * getGame(id).getBaseXP());

				HerobrinePVPCore.getFileManager().addCoins(player, earnedCoins);
				player.sendMessage(ChatColor.YELLOW + "+" + earnedCoins + " coins! (Playing)");
				if (HerobrinePVPCore.getFileManager().getPlayerLevel(player.getUniqueId()) < 100) {

					HerobrinePVPCore.getFileManager().addPlayerXP(uuid, earnedXP);
					player.sendMessage(ChatColor.AQUA + "+" + earnedXP + " XP! (Playing)");
				}
				else {
					HerobrinePVPCore.getFileManager().addCoins(player, 100);
					player.sendMessage(ChatColor.YELLOW + "+100 Coins! (Max Level Bonus)");

				}




			}

		}



	}

	public void setClass(UUID uuid, ClassTypes type) {
		removeClass(uuid);
		switch (type) {
			case BANDIT:
				classes.put(uuid, new Bandit(uuid));
				break;

			case WIZARD:
				classes.put(uuid, new Wizard(uuid));
				break;
			case KNIGHT:
				classes.put(uuid, new Knight(uuid));
				break;

			case ARCHER:
				classes.put(uuid, new Archer(uuid));
				break;

			case HEALER:
				classes.put(uuid, new BattleHealer(uuid));
				break;

			case WITCH:
				classes.put(uuid, new Witch(uuid));
				break;

			case FISHERMAN:
				classes.put(uuid, new Fisherman(uuid));
				break;

			case LUMBERJACK:
				classes.put(uuid, new Lumberjack(uuid));
				break;

			case MINER:
				classes.put(uuid, new Miner(uuid));
			break;
			case ENGINEER:
				classes.put(uuid, new Engineer(uuid));
				break;
			case ECONOMIST:
				classes.put(uuid, new Economist(uuid));
				break;

			case JUGGERNAUT:

				classes.put(uuid, new Juggernaut(uuid));
				break;


				default:
				break;
		}
	}

	public ClassTypes getClass(Player player) {

		return classes.get(player.getUniqueId()).getClassType();

	}

	public ClassTypes getClass(UUID uuid) {
		return classes.get(uuid).getClassType();
	}

	public GameType getType() {
		return type;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public void setTeam(Player player, Teams team) {
		removeTeam(player);
		teams.put(player.getUniqueId(), team);
		player.setDisplayName(team.getColor() + player.getName());

	}

	public void removeTeam(Player player) {
		if (teams.containsKey(player.getUniqueId())) {
			teams.remove(player.getUniqueId());
			player.setDisplayName(ChatColor.WHITE + player.getName());
		}
	}

	public Teams getTeam(Player player) {
		return teams.get(player.getUniqueId());
	}

	public int getTeamCount(Teams team) {
		int amount = 0;
		for (Teams t : teams.values()) {
			if (t.equals(team)) {
				amount++;
			}

		}
		return amount;
	}

	public Game getwallsSGGame() {
		return wallsSGGame;
	}

	public void setSpectator(Player player) {

		ItemStack spectate = new ItemStack(Material.COMPASS, 1);
		ItemMeta spectateMeta = spectate.getItemMeta();
		spectateMeta.setDisplayName(ChatColor.GREEN + "Spectate");
		spectate.setItemMeta(spectateMeta);
		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().clear();
		spectators.add(player.getUniqueId());
		for (UUID uuid : players) {
			Player player2 = Bukkit.getPlayer(uuid);
			player2.hidePlayer(player);
		}

		player.setAllowFlight(true);
		player.getInventory().setItem(0, spectate);
		player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999999, 3));
		player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(99999999, 3));
	}

	public void addAsSpectator(Player player, boolean isStaff, boolean canParticipate) {
		for (UUID uuid : players) {
			Player player2 = Bukkit.getPlayer(uuid);
			player2.hidePlayer(player);
		}
		Random rand = new Random();
		int randIndex = rand.nextInt(players.size());
		Player playerRandom = Bukkit.getPlayer(players.get(randIndex));
		players.add(player.getUniqueId());
		player.teleport(playerRandom);
	 	setSpectator(player);

		 if (!isStaff) sendMessage(HerobrinePVPCore.getFileManager().getRank(player).getColor() + player.getName() + ChatColor.AQUA + " joined as a spectator.");
	}

	public void addAsSpectator(Player player, Player playerToSpectate, boolean isStaff, boolean canParticipate) {
		for (UUID uuid : players) {
			Player player2 = Bukkit.getPlayer(uuid);
			player2.hidePlayer(player);
		}

		player.teleport(playerToSpectate);
		players.add(player.getUniqueId());
		setSpectator(player);
		if (!isStaff) sendMessage(HerobrinePVPCore.getFileManager().getRank(player).getColor() + player.getName() + ChatColor.AQUA + " joined as a spectator.");
	}


	public void removeSpectator(Player player) {

		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		spectators.remove(player.getUniqueId());
		for (UUID uuid : players) {
			Player player2 = Bukkit.getPlayer(uuid);
			player2.showPlayer(player);
		}
		for (PotionEffect effect : player.getActivePotionEffects()) {

			player.removePotionEffect(effect.getType());
		}

		if (getGame(id).equals(Games.BLOCK_HUNT) && getState().equals(GameState.LIVE)) {

			if (getBHModifiers().contains(ModifiedTypes.SPEED)) {
				player.addPotionEffect(PotionEffectType.SPEED.createEffect(99999999, 1));
			} else if (getBHModifiers().contains(ModifiedTypes.HASTE)) {
				player.addPotionEffect(PotionEffectType.FAST_DIGGING.createEffect(99999999, 1));
			}

		}

	}

	public void clearInventories() {
		for (UUID uuid : players) {
			Player player = Bukkit.getPlayer(uuid);
			player.getInventory().clear();
		}
	}

	public void clearInventory(Player player) {
		player.getInventory().clear();
	}

	public boolean canJoin() {
		return canJoin;
	}

	public void setJoinState(boolean state) {
		this.canJoin = state;
	}

	public Location getSpawn() {
		return spawn;
	}

	public ArrayList<ModifiedTypes> getBHModifiers() {

		return bhModifiers;
	}

	public boolean hasModifier(ModifiedTypes type) {

		if (bhModifiers.contains(type)) {
			return true;
		} else {
			return false;
		}
	}

	public Games getGame(int id) {

		String arenaKey = net.herobrine.gamecore.Config.getGameType(id);
		for (Games game : Games.values()) {
			if (game.getKey().equals(arenaKey)) {

				return game;

			}
		}

		return null;

	}

	public void sendMessage(String message, Teams team) {
		for (UUID uuid : getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if (getTeam(player).equals(team)) {

				player.sendMessage(message);
			}
		}

	}

	public void sendSpigotMessage(BaseComponent message, Teams team) {
		for (UUID uuid : getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if (getTeam(player).equals(team)) {

				player.spigot().sendMessage(message);
			}
		}

	}

	public void sendSpigotMessage(BaseComponent message) {
		for (UUID uuid : getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			player.spigot().sendMessage(message);
		}
	}

	private void deleteDirectory(File file) {
		for (File f : file.listFiles()) {
			if (f.isDirectory())
				deleteDirectory(f);
			else
				f.delete();
		}
		file.delete();
	}

	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {

		for (UUID uuid : players) {
			Player player = Bukkit.getPlayer(uuid);
			GameCoreMain.getInstance().sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
		}

	}

	public void sendActionBar(String text) {
		for (UUID uuid: players) {
			Player player = Bukkit.getPlayer(uuid);
			GameCoreMain.getInstance().sendActionBar(player, text);
		}
	}
}
