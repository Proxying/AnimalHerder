package uk.co.proxying.animalHerders.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import uk.co.proxying.animalHerders.entities.AnimalHerder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Kieran (Proxying) on 01-Aug-17.
 */
public class InventoryUtils {

	public static void showMainHerderInventory(Player player, AnimalHerder animalHerder) {
		Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GREEN + animalHerder.getOwnerName() + "(s)" + ChatColor.YELLOW + " Animal Herder");

		Stream<Map.Entry<EntityType , Integer>> sorted = animalHerder.getHerdingAmounts().entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

		Map<EntityType, Integer> nearbyEntities = new HashMap<>();

		nearbyEntities.put(EntityType.CHICKEN, 0);
		nearbyEntities.put(EntityType.PIG, 0);
		nearbyEntities.put(EntityType.COW, 0);
		nearbyEntities.put(EntityType.SHEEP, 0);
		nearbyEntities.put(EntityType.RABBIT, 0);
		nearbyEntities.put(EntityType.MUSHROOM_COW, 0);

		for (Entity entity : animalHerder.getBukkitEntity().getNearbyEntities(15, 15, 15)) {
			if (nearbyEntities.containsKey(entity.getType())) {
				int currentAmount = nearbyEntities.get(entity.getType());
				nearbyEntities.put(entity.getType(), currentAmount + 1);
			}
		}

		final int[] toPlaceLocation = { 10 };

		sorted.forEach(entry -> {
			if (toPlaceLocation[0] == 13) {
				toPlaceLocation[0] = 14;
			}
			String displayName;
			switch (entry.getKey()) {
				case COW:
					displayName = "Cow";
					break;
				case SHEEP:
					displayName = "Sheep";
					break;
				case RABBIT:
					displayName = "Rabbit";
					break;
				case MUSHROOM_COW:
					displayName = "Mushroom Cow";
					break;
				case PIG:
					displayName = "Pig";
					break;
				case CHICKEN:
					displayName = "Chicken";
					break;
				default:
					displayName = "Unknown";
					break;
			}
			ItemStack itemStack = new ItemStack(Material.MONSTER_EGG, 1);
			SpawnEggMeta spawnEggMeta = (SpawnEggMeta) itemStack.getItemMeta();
			spawnEggMeta.setSpawnedType(entry.getKey());
			itemStack.setItemMeta(spawnEggMeta);
			String displayMessage = ChatColor.AQUA + "Nearby amount " + ChatColor.WHITE + "- " + ChatColor.RESET;
			int nearbyAmount = nearbyEntities.getOrDefault(entry.getKey(), 0);
			if (nearbyAmount > entry.getValue()) {
				displayMessage += ChatColor.RED + String.valueOf(nearbyAmount);
			} else if (nearbyAmount == entry.getValue()) {
				displayMessage += ChatColor.YELLOW + String.valueOf(nearbyAmount);
			} else {
				displayMessage += ChatColor.GREEN + String.valueOf(nearbyAmount);
			}
			displayMessage += ChatColor.WHITE + "/" + ChatColor.LIGHT_PURPLE + entry.getValue() + ChatColor.AQUA + ".";
			editItem(itemStack, ChatColor.YELLOW + displayName, new String[] {
					displayMessage
			});
			ItemBuilder itemBuilder = new ItemBuilder(itemStack);
			itemBuilder.setString("EntityType", entry.getKey().name());
			inventory.setItem(toPlaceLocation[0], itemBuilder.build());
			toPlaceLocation[0]++;
		});

		player.openInventory(inventory);
	}

	public static void showHerderUpgradeInventory(Player player, AnimalHerder animalHerder, int upgradeAmount) {
		Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Upgrade Herder?");

		Stream<Map.Entry<EntityType , Integer>> sorted = animalHerder.getHerdingAmounts().entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

		Map<EntityType, Integer> nearbyEntities = new HashMap<>();

		nearbyEntities.put(EntityType.CHICKEN, 0);
		nearbyEntities.put(EntityType.PIG, 0);
		nearbyEntities.put(EntityType.COW, 0);
		nearbyEntities.put(EntityType.SHEEP, 0);
		nearbyEntities.put(EntityType.RABBIT, 0);
		nearbyEntities.put(EntityType.MUSHROOM_COW, 0);

		for (Entity entity : animalHerder.getBukkitEntity().getNearbyEntities(15, 5, 15)) {
			if (nearbyEntities.containsKey(entity.getType())) {
				int currentAmount = nearbyEntities.get(entity.getType());
				nearbyEntities.put(entity.getType(), currentAmount + 1);
			}
		}

		final int[] toPlaceLocation = { 10 };

		sorted.forEach(entry -> {
			if (toPlaceLocation[0] == 13) {
				toPlaceLocation[0] = 14;
			}
			String displayName;
			switch (entry.getKey()) {
				case COW:
					displayName = "Cow";
					break;
				case SHEEP:
					displayName = "Sheep";
					break;
				case RABBIT:
					displayName = "Rabbit";
					break;
				case MUSHROOM_COW:
					displayName = "Mushroom Cow";
					break;
				case PIG:
					displayName = "Pig";
					break;
				case CHICKEN:
					displayName = "Chicken";
					break;
				default:
					displayName = "Unknown";
					break;
			}
			ItemStack itemStack = new ItemStack(Material.MONSTER_EGG, 1);
			SpawnEggMeta spawnEggMeta = (SpawnEggMeta) itemStack.getItemMeta();
			spawnEggMeta.setSpawnedType(entry.getKey());
			itemStack.setItemMeta(spawnEggMeta);
			String displayMessage = ChatColor.AQUA + "Current amount " + ChatColor.WHITE + "- " + ChatColor.RESET;
			String newMessage = ChatColor.AQUA + "Upgraded amount " + ChatColor.WHITE + "- " + ChatColor.RESET;
			int nearbyAmount = nearbyEntities.getOrDefault(entry.getKey(), 0);
			if (nearbyAmount > entry.getValue()) {
				displayMessage += ChatColor.RED + String.valueOf(nearbyAmount);
				newMessage += ChatColor.RED + String.valueOf(nearbyAmount);
			} else if (nearbyAmount == entry.getValue()) {
				displayMessage += ChatColor.YELLOW + String.valueOf(nearbyAmount);
				newMessage += ChatColor.RED + String.valueOf(nearbyAmount);
			} else {
				displayMessage += ChatColor.GREEN + String.valueOf(nearbyAmount);
				newMessage += ChatColor.RED + String.valueOf(nearbyAmount);
			}
			displayMessage += ChatColor.WHITE + "/" + ChatColor.LIGHT_PURPLE + entry.getValue() + ChatColor.AQUA + ".";
			newMessage += ChatColor.WHITE + "/" + ChatColor.LIGHT_PURPLE + (entry.getValue() + upgradeAmount) + ChatColor.AQUA + ".";
			editItem(itemStack, ChatColor.YELLOW + displayName, new String[] {
					displayMessage
			});
			editItem(itemStack, ChatColor.YELLOW + displayName, new String[] {
					displayMessage,
					newMessage
			});
			ItemBuilder itemBuilder = new ItemBuilder(itemStack);
			itemBuilder.setString("EntityType", entry.getKey().name());
			inventory.setItem(toPlaceLocation[0], itemBuilder.build());
			toPlaceLocation[0]++;
		});

		player.openInventory(inventory);
	}

	public static ItemStack editItem(String playerName, String name, String[] lore) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(playerName);
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack editItem(ItemStack itemStack, String name, String[] lore) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		itemStack.setItemMeta(meta);
		itemStack.setAmount(1);
		return itemStack;
	}

	public static ItemStack editItemWithShort(ItemStack itemStack, short shortID, String name, String[] lore) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setDurability(shortID);
		meta.setLore(Arrays.asList(lore));
		itemStack.setItemMeta(meta);
		itemStack.setAmount(1);
		return itemStack;
	}

	public static ItemStack editItem(ItemStack itemStack, String[] lore) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		itemStack.setItemMeta(meta);
		itemStack.setAmount(1);
		return itemStack;
	}
}
