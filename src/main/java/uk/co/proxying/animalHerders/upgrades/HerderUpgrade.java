package uk.co.proxying.animalHerders.upgrades;

import org.bukkit.inventory.ItemStack;
import uk.co.proxying.animalHerders.entities.AnimalHerder;

import java.util.UUID;

/**
 * Created by Kieran (Proxying) on 02-Aug-17.
 */
public class HerderUpgrade {

	public UUID upgrader;
	public AnimalHerder herder;
	public ItemStack upgradeItem;
	public int upgradeAmount;

	public HerderUpgrade(UUID uuid, AnimalHerder herder, ItemStack upgradeItem, int upgradeAmount) {
		this.upgrader = uuid;
		this.herder = herder;
		this.upgradeItem = upgradeItem;
		this.upgradeAmount = upgradeAmount;
	}
}
