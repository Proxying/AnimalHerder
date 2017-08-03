package uk.co.proxying.animalHerders;

import net.minecraft.server.v1_12_R1.EntityVillager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.proxying.animalHerders.commands.TestCommand;
import uk.co.proxying.animalHerders.entities.AnimalHerder;
import uk.co.proxying.animalHerders.listeners.HerderListener;
import uk.co.proxying.animalHerders.upgrades.HerderUpgrade;
import uk.co.proxying.animalHerders.utils.NMSUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public final class AnimalHerders extends JavaPlugin {

	private static AnimalHerders instance;
	private Random random;
	private Map<UUID, HerderUpgrade> upgradingHerders = new HashMap<>();

	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getServer().getPluginManager().registerEvents(new HerderListener(), this);
		NMSUtils nmsUtils = new NMSUtils();
		nmsUtils.registerEntity("AnimalHerder", 120, EntityVillager.class, AnimalHerder.class);
		getCommand("testspawn").setExecutor(new TestCommand());
		// Plugin startup logic
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			Bukkit.getServer().broadcastMessage("PURGE STARTING, PLEASE MOVE INDOORS...");
			Bukkit.getWorlds().get(0).getEntitiesByClass(Villager.class).forEach(villager -> {
				if (villager.getProfession() == Villager.Profession.NITWIT) {
					CraftLivingEntity cle = (CraftLivingEntity) villager;
					if (cle.getHandle() instanceof AnimalHerder) {
						AnimalHerder animalHerder = (AnimalHerder) cle.getHandle();
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
								if (currentAmount < animalHerder.getHerdingAmountOf(entity.getType())) {
									nearbyEntities.put(entity.getType(), currentAmount + 1);
									entity.setMetadata("AnimalHerder", new FixedMetadataValue(this, true));
								}
							}
						}
					}
				}
			});
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
				Bukkit.getWorlds().get(0).getEntities().forEach(entity -> {
					switch (entity.getType()) {
						case COW:
						case MUSHROOM_COW:
						case RABBIT:
						case SHEEP:
						case PIG:
						case CHICKEN:
							if (!entity.hasMetadata("AnimalHerder")) {
								entity.remove();
							} else {
								entity.removeMetadata("AnimalHerder", this);
							}
						default:
							break;
					}
				});
			}, 40L);
		},100L, 20 * 60 * 3L);//todo change to 30L (30mins)
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static AnimalHerders getInstance() {
		return instance;
	}

	public Random getRandom() {
		return random;
	}

	public Map<UUID, HerderUpgrade> getUpgradingHerders() {
		return upgradingHerders;
	}
}
