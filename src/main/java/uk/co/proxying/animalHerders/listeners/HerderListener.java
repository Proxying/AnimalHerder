package uk.co.proxying.animalHerders.listeners;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import uk.co.proxying.animalHerders.AnimalHerders;
import uk.co.proxying.animalHerders.entities.AnimalHerder;
import uk.co.proxying.animalHerders.upgrades.HerderUpgrade;
import uk.co.proxying.animalHerders.utils.InventoryUtils;
import uk.co.proxying.animalHerders.utils.ItemBuilder;
import uk.co.proxying.animalHerders.utils.ItemUtils;

/**
 * Created by Kieran (Proxying) on 01-Aug-17.
 */
public class HerderListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamaged(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.VILLAGER) {
			Villager villager = (Villager) event.getEntity();
			if (villager.getProfession() == Villager.Profession.NITWIT) {
				CraftLivingEntity cle = (CraftLivingEntity) villager;
				if (cle.getHandle() instanceof AnimalHerder) {
					event.setCancelled(true);
					event.setDamage(0);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntityType() == EntityType.VILLAGER) {
			Villager villager = (Villager) event.getEntity();
			if (villager.getProfession() == Villager.Profession.NITWIT) {
				CraftLivingEntity cle = (CraftLivingEntity) villager;
				if (cle.getHandle() instanceof AnimalHerder) {
					event.setCancelled(true);
					event.setDamage(0);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void interactHerder(PlayerInteractEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return;
		if (event.getRightClicked().getType() != EntityType.VILLAGER) return;
		Player player = event.getPlayer();
		Villager villager = (Villager) event.getRightClicked();
		if (villager.getProfession() != Villager.Profession.NITWIT) return;
		CraftLivingEntity cle = (CraftLivingEntity) villager;
		if (cle.getHandle() instanceof AnimalHerder) {
			AnimalHerder animalHerder = (AnimalHerder) cle.getHandle();
			if (animalHerder.getOwnerUUID().equals(player.getUniqueId())) {
				if (player.getEquipment().getItemInMainHand() == null || player.getEquipment().getItemInMainHand().getType() == Material.AIR) {
					if (player.isSneaking()) {
						player.getInventory().addItem(ItemUtils.createHerderReplaceItem(animalHerder));
						animalHerder.getBukkitEntity().remove();
					} else {
						InventoryUtils.showMainHerderInventory(player, animalHerder);
					}
				} else if (player.getEquipment().getItemInMainHand().getType() == Material.GOLD_NUGGET) {
					if (!AnimalHerders.getInstance().getUpgradingHerders().containsKey(player.getUniqueId())) {
						ItemBuilder builder = new ItemBuilder(player.getEquipment().getItemInMainHand());
						if (builder.getString("HerderUpgrade").equalsIgnoreCase("")) {
							return;
						}
						int upgradeAmount = builder.getInt("UpgradeAmount");
						InventoryUtils.showHerderUpgradeInventory(player, animalHerder, upgradeAmount);
						HerderUpgrade herderUpgrade = new HerderUpgrade(player.getUniqueId(), animalHerder, player.getEquipment().getItemInMainHand(), upgradeAmount);
						if (player.getEquipment().getItemInMainHand().getAmount() == 1) {
							player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
						} else {
							player.getEquipment().getItemInMainHand().setAmount((player.getEquipment().getItemInMainHand().getAmount() - 1));
						}
						AnimalHerders.getInstance().getUpgradingHerders().put(player.getUniqueId(), herderUpgrade);
					} else {
						//todo: Return the item I guess?
					}
				}
			} else {
				if (player.isSneaking()) {
					player.sendMessage(ChatColor.RED + "This Herder does not belong to you, it is owned by " + animalHerder.getOwnerName() + ".");
				} else {
					InventoryUtils.showMainHerderInventory(player, animalHerder);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlaceHerder(PlayerInteractEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK);
		if (event.getItem() == null || event.getItem().getType() == Material.AIR) return;
		Player player = event.getPlayer();
		if (event.getItem().getType() == Material.STRING) {
			ItemBuilder builder = new ItemBuilder(event.getItem());
			if (builder.getString("HerderPlacement").equalsIgnoreCase("")) {
				return;
			}
			boolean canPlace = true;
			for (Entity entity : event.getPlayer().getNearbyEntities(25, 25, 25)) {
				if (entity.getType() == EntityType.VILLAGER) {
					Villager villager = (Villager) entity;
					if (villager.getProfession() == Villager.Profession.NITWIT) {
						CraftLivingEntity cle = (CraftLivingEntity) villager;
						if (cle.getHandle() instanceof AnimalHerder) {
							canPlace = false;
							break;
						}
					}
				}
			}
			if (!canPlace) {
				event.getPlayer().sendMessage(ChatColor.RED +  "You cannot place a Herder within a 25 block radius of another.");
				return;
			}
			if (player.getEquipment().getItemInMainHand().getAmount() == 1) {
				player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
			} else {
				player.getEquipment().getItemInMainHand().setAmount((player.getEquipment().getItemInMainHand().getAmount() - 1));
			}
			AnimalHerder animalHerder = new AnimalHerder(((CraftWorld) player.getWorld()).getHandle(), player.getUniqueId(), player.getName());
			Location location = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);
			animalHerder.setLocation(location.getX(), location.getY(), location.getZ(), 0 ,0);
			((CraftWorld) location.getWorld()).getHandle().addEntity(animalHerder, CreatureSpawnEvent.SpawnReason.CUSTOM);
			animalHerder.setLocation(location.getX(), location.getY(), location.getZ(), 0 ,0);
		} else if (event.getItem().getType() == Material.GLOWSTONE_DUST) {
			ItemBuilder builder = new ItemBuilder(event.getItem());
			if (builder.getString("HerderReplacement").equalsIgnoreCase("")) {
				return;
			}
			boolean canPlace = true;
			for (Entity entity : event.getPlayer().getNearbyEntities(25, 25, 25)) {
				if (entity.getType() == EntityType.VILLAGER) {
					Villager villager = (Villager) entity;
					if (villager.getProfession() == Villager.Profession.NITWIT) {
						CraftLivingEntity cle = (CraftLivingEntity) villager;
						if (cle.getHandle() instanceof AnimalHerder) {
							canPlace = false;
							break;
						}
					}
				}
			}
			if (!canPlace) {
				event.getPlayer().sendMessage(ChatColor.RED +  "You cannot place a Herder within a 25 block radius of another.");
				return;
			}
			if (player.getEquipment().getItemInMainHand().getAmount() == 1) {
				player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
			} else {
				player.getEquipment().getItemInMainHand().setAmount((player.getEquipment().getItemInMainHand().getAmount() - 1));
			}
			AnimalHerder animalHerder = new AnimalHerder(((CraftWorld) player.getWorld()).getHandle(), player.getUniqueId(), player.getName());
			NBTTagCompound nbtTagCompound = builder.getCompound("HerderInformation");
			animalHerder.a(nbtTagCompound);
			Location location = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);
			animalHerder.setLocation(location.getX(), location.getY(), location.getZ(), 0 ,0);
			((CraftWorld) location.getWorld()).getHandle().addEntity(animalHerder, CreatureSpawnEvent.SpawnReason.CUSTOM);
			animalHerder.setLocation(location.getX(), location.getY(), location.getZ(), 0 ,0);
		}
	}



	@EventHandler
	public void onMenuClicked(InventoryClickEvent event) {
		String invName = ChatColor.stripColor(event.getInventory().getName());
		if (invName.contains("Animal Herder")) {
			event.setCancelled(true);
		} else if (invName.equalsIgnoreCase("Upgrade Herder?")) {
			event.setCancelled(true);
			if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
				if (event.getCurrentItem().hasItemMeta()) {
					Player player = (Player) event.getWhoClicked();
					if (AnimalHerders.getInstance().getUpgradingHerders().containsKey(player.getUniqueId())) {
						ItemBuilder builder = new ItemBuilder(event.getCurrentItem());
						HerderUpgrade herderUpgrade = AnimalHerders.getInstance().getUpgradingHerders().get(player.getUniqueId());
						try {
							EntityType entityType = EntityType.valueOf(builder.getString("EntityType"));
							herderUpgrade.herder.setHerdingAmountOf(entityType, (herderUpgrade.herder.getHerdingAmountOf(entityType) + herderUpgrade.upgradeAmount));
							player.closeInventory();
							AnimalHerders.getInstance().getUpgradingHerders().remove(player.getUniqueId());
							String displayName;
							switch (entityType) {
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
							player.sendMessage(ChatColor.YELLOW + displayName + " upgraded by " + herderUpgrade.upgradeAmount + ".");
							herderUpgrade = null;
						} catch (Exception e) {
							AnimalHerders.getInstance().getLogger().warning("Menu Clicked cant cast name to EntityType + " + builder.getString("EntityType"));
						}
					} else {
						player.sendMessage(ChatColor.RED + "Something has went wrong with your upgrade. Please try again.");
						player.closeInventory();
					}
				}
				//todo: They've selected their upgrade choice, get it and upgrade it.
			}
		}
	}

	@EventHandler
	public void onMenuClosed(InventoryCloseEvent event) {
		if (event.getInventory().getName().equalsIgnoreCase("Upgrade Herder?")) {
			Player player = (Player) event.getPlayer();
			Bukkit.getScheduler().runTaskLater(AnimalHerders.getInstance(), () -> {
				if (AnimalHerders.getInstance().getUpgradingHerders().containsKey(player.getUniqueId())) {
					HerderUpgrade herderUpgrade = AnimalHerders.getInstance().getUpgradingHerders().remove(player.getUniqueId());
					player.getInventory().addItem(herderUpgrade.upgradeItem);
					player.sendMessage(ChatColor.YELLOW + "Herder Upgrade Cancelled.");
				}
			}, 5L);
		}
	}
}
