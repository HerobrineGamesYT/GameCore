package net.herobrine.gamecore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
	private ItemStack stack;

	public ItemBuilder(Material mat) {
		stack = new ItemStack(mat);
	}

	public ItemBuilder(Material mat, short sh) {
		stack = new ItemStack(mat, 1, sh);
	}

	public ItemMeta getItemMeta() {
		return stack.getItemMeta();
	}

	public ItemBuilder setColor(Color color) {
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(color);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder setType(Material mat) {
		stack.setType(mat);
		return this;
	}

	public ItemBuilder setGlow(boolean glow) {
		if (glow) {
			addEnchant(Enchantment.DURABILITY, 1);
			addItemFlag(ItemFlag.HIDE_ENCHANTS);
		} else {
			ItemMeta meta = getItemMeta();
			for (Enchantment enchantment : meta.getEnchants().keySet()) {
				meta.removeEnchant(enchantment);
			}
		}
		return this;
	}

	public ItemBuilder updateLine(int index, String newLore) {
		ArrayList<String> lore1 = new ArrayList<String>(getItemMeta().getLore());
		lore1.set(index, newLore);
		setLore(lore1);
		return this;
	}

	public ItemBuilder setDurability(short durability) {
		stack.setDurability(durability);
		return this;
	}
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		ItemMeta meta = stack.getItemMeta();
		meta.spigot().setUnbreakable(unbreakable);
		stack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setBannerColor(DyeColor color) {
		BannerMeta meta = (BannerMeta) stack.getItemMeta();
		meta.setBaseColor(color);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		stack.setAmount(amount);
		return this;
	}

	public ItemBuilder setItemMeta(ItemMeta meta) {
		stack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setHead(String owner) {
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(owner);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder setDisplayName(String displayname) {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder setItemStack(ItemStack stack) {
		this.stack = stack;
		return this;
	}

	public ItemBuilder setLore(ArrayList<String> lore) {
		ItemMeta meta = getItemMeta();
		meta.setLore(lore);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		ItemMeta meta = getItemMeta();
		meta.setLore(lore);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(String lore) {
		ArrayList<String> loreList = new ArrayList<>();
		loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
		ItemMeta meta = getItemMeta();
		meta.setLore(loreList);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder addEnchant(Enchantment enchantment, int level) {
		ItemMeta meta = getItemMeta();
		meta.addEnchant(enchantment, level, true);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder addItemFlag(ItemFlag flag) {
		ItemMeta meta = getItemMeta();
		meta.addItemFlags(flag);
		setItemMeta(meta);
		return this;
	}

	public ItemStack build() {
		return stack;
	}
}
