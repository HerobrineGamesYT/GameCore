package net.herobrine.gamecore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import com.mojang.authlib.properties.PropertyMap;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.herobrine.core.HerobrinePVPCore;

public class SkinCommand implements CommandExecutor {

	private static HashMap<Player, Double> health = new HashMap<>();
	private static HashMap<Player, Location> loc = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (HerobrinePVPCore.getFileManager().getRank(player).getPermLevel() >= 9) {

				if (args.length >= 1) {

					if (!(args[0].length() > 16)) {

						try {

							setNameSkin(player, args[0]);
							player.sendMessage(
									ChatColor.GREEN + "Your skin has been changed to " + args[0] + "'s skin!");

						} catch (Exception e) {
							player.sendMessage(ChatColor.RED + "Exception occurred. Try a different name?");
						}

					} else {
						player.sendMessage(ChatColor.RED + "Invalid player name! Length > 16");
						double tps = MinecraftServer.getServer().recentTps[0];
					}

				} else {
					player.sendMessage(ChatColor.RED + "Invalid arguments! /skin (name)");
				}

			} else {
				player.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
			}

		}

		return false;

	}

	public void setNameSkin(Player player, String skinName) throws Exception{
		SkinSettings skinSettings = new SkinSettings();

		String getUUID = skinSettings.get("https://api.mojang.com/users/profiles/minecraft/%s", skinName);
		String uuid = skinSettings.getUUID(getUUID);
		String skin = skinSettings.get("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid);

		String sig = skinSettings.getSig(skin);

		System.out.println("Sig: " + sig + "\n");

		String data = skinSettings.getData(skin);

		System.out.println("Data: " + data + "\n");

		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

		int dimension = player.getWorld().getEnvironment().getId();
		WorldSettings.EnumGamemode gamemode = WorldSettings.EnumGamemode.valueOf(player.getGameMode().name());
		EnumDifficulty difficulty = (EnumDifficulty) EnumDifficulty.class.getDeclaredMethod("getById", int.class).invoke(null, player.getWorld().getDifficulty().getValue());
		WorldType type = WorldType.getType(player.getWorld().getWorldType().getName());
		final GameMode gameMode = player.getGameMode();
		final boolean allowFlight = player.getAllowFlight();
		final boolean flying = player.isFlying();
		final Location location = player.getLocation();
		final float pitch = location.getPitch(), yaw = location.getYaw();
		final int heldItemSlot = player.getInventory().getHeldItemSlot();
		final int level = player.getLevel();
		final float xp = player.getExp();
		final double maxHealth = player.getMaxHealth();
		final double health = player.getHealth();

		GameCoreMain.getInstance().sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));

		GameProfile profile = entityPlayer.getProfile();

		PropertyMap map = profile.getProperties();
		map.clear();

		map.put("textures", new Property("textures", data, sig));

		Bukkit.getScheduler().runTaskLater(GameCoreMain.getInstance(), () -> {
			GameCoreMain.getInstance().sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
			GameCoreMain.getInstance().sendPacket(player, new PacketPlayOutRespawn(dimension, difficulty, type, gamemode));
			player.setGameMode(gameMode);
			player.setAllowFlight(allowFlight);
			player.setFlying(flying);
			player.updateInventory();
			player.getInventory().setHeldItemSlot(heldItemSlot);
			player.setLevel(level);
			player.setExp(xp);
			player.setMaxHealth(maxHealth);
			player.setHealth(health);

			location.setYaw(yaw);
			location.setPitch(pitch);
			player.teleport(location);

		}, 0);


		Bukkit.getOnlinePlayers().forEach(online -> {

			online.hidePlayer(player);
			online.showPlayer(player);

		});

	}

	static void sendPacket(Packet<?> packet) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}

	}

	static void sendPacketNotFor(Player player, Packet<?> packet) {
		for (Player targets : Bukkit.getOnlinePlayers()) {
			if (targets.getUniqueId().toString().equals(player.getUniqueId().toString()))
				continue;
			((CraftPlayer) targets).getHandle().playerConnection.sendPacket(packet);
		}

	}


	public String get(String url, String arg) {
		try {
			HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format(url, arg)).openConnection();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder stringBuilder = new StringBuilder();
				String output;
				while ((output = bufferedReader.readLine()) != null) {
					stringBuilder.append(output);
				}
				return stringBuilder.toString();
			}

		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
		return "error";
	}

	public String getSkin(String data) {
		final Pattern pattern = Pattern.compile("\"value\" : \"(.*?)\",");
		final Matcher matcher = pattern.matcher(data);
		matcher.find();
		return matcher.group(1);
	}

	public String getSig(String data) {
		final Pattern pattern = Pattern.compile("\"signature\" : \"(.*?)\"");
		final Matcher matcher = pattern.matcher(data);
		matcher.find();
		return matcher.group(1);
	}

	public String addCharToString(String str, char c, int pos) {
		StringBuilder stringBuilder = new StringBuilder(str);
		stringBuilder.insert(pos, c);
		return stringBuilder.toString();
	}

	// this is a manual way to format uuid I am sure there is better ways
	public String formatUUID(String id) {
		String uuid = addCharToString(id, '-', 8);
		uuid = addCharToString(uuid, '-', 13);
		uuid = addCharToString(uuid, '-', 18);
		uuid = addCharToString(uuid, '-', 23);
		return uuid;
	}

	public String getUUID(String body) {
		final Pattern pattern = Pattern.compile("id\" : \"(.*?)\"}");
		final Matcher matcher = pattern.matcher(body);
		matcher.find();
		String id = matcher.group(1);
		return formatUUID(id);
	}

}
