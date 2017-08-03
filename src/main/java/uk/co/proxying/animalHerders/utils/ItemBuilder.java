package uk.co.proxying.animalHerders.utils;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kieran (Proxying) on 02-Aug-17.
 */
public class ItemBuilder {

	public ItemStack itemStack;
	public ItemMeta itemMeta;
	public NBTTagCompound nbt;
	net.minecraft.server.v1_12_R1.ItemStack nmsStack;
	private ItemBuilder item;

	/**
	 * Initiates ItemStack with default Material as (Stone).
	 */
	public ItemBuilder() {
		itemStack = new ItemStack(Material.STONE);
		nmsStack = CraftItemStack.asNMSCopy(itemStack);
		nbt = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		itemMeta = itemStack.getItemMeta();
	}

	/**
	 * @param material The override material for the ItemStack.
	 */
	public ItemBuilder(Material material) {
		itemStack = new ItemStack(material);
		nmsStack = CraftItemStack.asNMSCopy(itemStack);
		nbt = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		itemMeta = itemStack.getItemMeta();
	}

	/**
	 * @param material The override material for the ItemStack.
	 * @param amount   The amount of the ItemStack to return.
	 */
	public ItemBuilder(Material material, int amount) {
		itemStack = new ItemStack(material, amount);
		nmsStack = CraftItemStack.asNMSCopy(itemStack);
		nbt = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		itemMeta = itemStack.getItemMeta();
	}

	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
		nmsStack = CraftItemStack.asNMSCopy(itemStack);
		nbt = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
		itemMeta = itemStack.getItemMeta();
	}

	public ItemBuilder setName(String name) {
		itemMeta.setDisplayName(name);
		syncItems();
		return this;
	}

	private void syncItems() {
		itemStack.setItemMeta(itemMeta);
		nmsStack = CraftItemStack.asNMSCopy(itemStack);
		nbt = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
	}

	public ItemBuilder setDamage(short damage) {
		itemStack.setDurability(damage);
		syncItems();

		return this;
	}


	public ItemBuilder setString(String nbtKey, String nbtValue) {
		nbt.setString(nbtKey, nbtValue);
		return this;
	}

	public ItemBuilder setInt(String nbtKey, int nbtValue) {
		nbt.setInt(nbtKey, nbtValue);
		return this;
	}

	public ItemBuilder setLong(String nbtKey, long nbtValue) {
		nbt.setLong(nbtKey, nbtValue);
		return this;
	}

	public ItemBuilder setShort(String nbtKey, short nbtValue) {
		nbt.setShort(nbtKey, nbtValue);
		return this;
	}

	public ItemBuilder setList(String nbtKey, NBTTagList list) {
		nbt.set(nbtKey, list);
		return this;
	}

	public ItemBuilder setCompound(String nbtKey, NBTTagCompound compound) {
		nbt.set(nbtKey, compound);
		return this;
	}

	public ItemBuilder setLore(String... lore) {
		itemMeta.setLore(Arrays.asList(lore));
		syncItems();

		return this;
	}


	public ItemBuilder setAmount(int amount) {
		itemStack.setAmount(amount);
		syncItems();

		return this;
	}

	public ItemStack build() {
		itemStack.setItemMeta(itemMeta);
		nmsStack = CraftItemStack.asNMSCopy(itemStack);
		nmsStack.setTag(nbt);
		return CraftItemStack.asBukkitCopy(nmsStack);
	}

	public net.minecraft.server.v1_12_R1.ItemStack buildNMS() {
		itemStack.setItemMeta(itemMeta);
		nmsStack = CraftItemStack.asNMSCopy(itemStack);
		nmsStack.setTag(nbt);

		return nmsStack;
	}

	public ItemBuilder getItem(Consumer<ItemStack> item) {
		item.accept(this.itemStack);
		return this;
	}

	public ItemBuilder getNMSItem(Consumer<net.minecraft.server.v1_12_R1.ItemStack> item) {
		item.accept(this.nmsStack);
		return this;

	}

	public ItemBuilder setBoolean(String nbtKey, boolean nbtValue) {
		nbt.setBoolean(nbtKey, nbtValue);
		return this;
	}

	public boolean getBoolean(String nbtKey) {
		if (nbt.hasKey(nbtKey)) {
			return nbt.getBoolean(nbtKey);
		}
		return false;
	}

	public int getInt(String nbtKey) {
		if (nbt.hasKey(nbtKey)) {
			return nbt.getInt(nbtKey);
		}
		return 0;
	}

	public String getString(String nbtKey) {
		if (nbt.hasKey(nbtKey)) {
			return nbt.getString(nbtKey);
		}
		return "";
	}

	public NBTTagCompound getCompound(String nbtKey) {
		if (nbt.hasKey(nbtKey)) {
			return nbt.getCompound(nbtKey);
		}
		return null;
	}

	public ItemBuilder setUnbreakable() {
		itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		syncItems();
		setBoolean("Unbreakable", true);
		return this;

	}

	public ItemBuilder addLore(String line) {
		List<String> lore = new ArrayList<>();
		if (itemMeta != null) {
			if (itemMeta.getLore() != null) {
				lore = itemMeta.getLore();
			}
			lore.add(line);
			itemMeta.setLore(lore);
		}
		return this;
	}

	public ItemBuilder setMaxStackSize(int size) {
		nmsStack.getItem().d(size);
		return this;
	}

	public ItemBuilder hideAttributes() {
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		syncItems();
		return  this;
	}
}
