package net.herobrine.gamecore;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Class implements Listener {
	protected UUID uuid;
	protected ClassTypes type;

	public Class(UUID uuid, ClassTypes type) {
		this.uuid = uuid;
		this.type = type;
		Bukkit.getPluginManager().registerEvents(this, GameCoreMain.getInstance());

	}

	public UUID getUUID() {
		return uuid;
	}

	public ClassTypes getClassType() {
		return type;
	}

	public abstract void onStart(Player player);

	public void remove() {
		HandlerList.unregisterAll(this);
	}
}
