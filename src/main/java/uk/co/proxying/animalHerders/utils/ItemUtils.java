package uk.co.proxying.animalHerders.utils;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.co.proxying.animalHerders.entities.AnimalHerder;
import uk.co.proxying.animalHerders.upgrades.UpgradeAmounts;

/**
 * Created by Kieran (Proxying) on 01-Aug-17.
 */
public class ItemUtils {

	public static ItemStack createHerderUpgradeItem(UpgradeAmounts amount) {
		ItemStack itemStack = new ItemStack(Material.GOLD_NUGGET, 1);

		InventoryUtils.editItem(itemStack, ChatColor.GREEN + "Herder Upgrade " + ChatColor.WHITE + "- " + ChatColor.RED + amount.getInteger() + ChatColor.GREEN + ".", new String[] {
				ChatColor.translateAlternateColorCodes('&', "&aUsed on an owned Herder to increase &cONE &aof its skills by" + amount.getDisplay() + "&a.")
		});

		ItemBuilder builder = new ItemBuilder(itemStack);
		builder.setString("HerderUpgrade", "ChristianValues");
		builder.setInt("UpgradeAmount", amount.getInteger());

		return builder.build();
	}

	public static ItemStack createHerderPlacementItem() {
		ItemStack itemStack = new ItemStack(Material.STRING, 1);

		InventoryUtils.editItem(itemStack, ChatColor.AQUA + "Animal Herder", new String[] {
				ChatColor.translateAlternateColorCodes('&', "&aUsed to place your very own Animal Herder."),
				ChatColor.translateAlternateColorCodes('&', "&aYour Herder will protect nearby animals from the purge."),
				ChatColor.translateAlternateColorCodes('&', "&aProtection Radius = 15x15x15.")
		});

		ItemBuilder builder = new ItemBuilder(itemStack);
		builder.setString("HerderPlacement", "WhiteLivesMatter");

		return builder.build();
	}

	public static ItemStack createHerderReplaceItem(AnimalHerder animalHerder) {
		ItemStack itemStack = new ItemStack(Material.GLOWSTONE_DUST, 1);

		InventoryUtils.editItem(itemStack, ChatColor.YELLOW + "Animal Herder Replacement", new String[] {
				ChatColor.translateAlternateColorCodes('&', "&aUsed to replace your Animal Herder."),
				ChatColor.translateAlternateColorCodes('&', "&aYour Herder will retain it's previous stats.")
		});

		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		animalHerder.b(nbtTagCompound);

		ItemBuilder builder = new ItemBuilder(itemStack);
		builder.setString("HerderReplacement", "ThePope");
		builder.setCompound("HerderInformation", nbtTagCompound);

		return builder.build();
	}
}
