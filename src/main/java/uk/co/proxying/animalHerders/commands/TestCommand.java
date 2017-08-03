package uk.co.proxying.animalHerders.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.proxying.animalHerders.upgrades.UpgradeAmounts;
import uk.co.proxying.animalHerders.utils.ItemUtils;

import java.util.Random;

/**
 * Created by Kieran (Proxying) on 01-Aug-17.
 */
public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.getInventory().addItem(ItemUtils.createHerderPlacementItem());
			player.getInventory().addItem(ItemUtils.createHerderUpgradeItem(UpgradeAmounts.values()[new Random().nextInt(4)]));
		}
		return false;
	}
}
