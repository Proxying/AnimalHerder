package uk.co.proxying.animalHerders.entities;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import uk.co.proxying.animalHerders.AnimalHerders;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Kieran (Proxying) on 01-Aug-17.
 */
public class AnimalHerder extends EntityVillager {

	private UUID ownerUUID;
	private String ownerName;
	private int currentCap;
	private int maxCap;
	private Map<EntityType, Integer> herdingAmounts = new HashMap<>();
	private int bK;
	private int bL;
	private boolean bH;

	public AnimalHerder(World world) {
		super(world);
		herdingAmounts.put(EntityType.CHICKEN, 1);
		herdingAmounts.put(EntityType.PIG, 1);
		herdingAmounts.put(EntityType.COW, 1);
		herdingAmounts.put(EntityType.SHEEP, 1);
		herdingAmounts.put(EntityType.RABBIT, 1);
		herdingAmounts.put(EntityType.MUSHROOM_COW, 1);
		this.canPickUpLoot = false;
		this.persistent = true;
		this.ageLocked = true;
		this.setNoAI(true);
		this.collides = false;
	}

	public AnimalHerder(World world, UUID owner, String ownerName) {
		super(world, 5);
		this.herdingAmounts.put(EntityType.CHICKEN, 0);
		this.herdingAmounts.put(EntityType.PIG, 0);
		this.herdingAmounts.put(EntityType.COW, 0);
		this.herdingAmounts.put(EntityType.SHEEP, 0);
		this.herdingAmounts.put(EntityType.RABBIT, 0);
		this.herdingAmounts.put(EntityType.MUSHROOM_COW, 0);
		this.canPickUpLoot = false;
		this.persistent = true;
		this.ownerUUID = owner;
		this.ownerName = ownerName;
		this.currentCap = 50;
		this.maxCap = 200;
		this.ageLocked = true;
		this.setNoAI(true);
		this.setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.LEAD));
		this.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6&lHERDER " + ChatColor.WHITE + "(" + ChatColor.DARK_GRAY + ownerName + ChatColor.WHITE + ")"));
		this.setCustomNameVisible(true);
		this.collides = false;
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		nbttagcompound.setInt("Profession", this.getProfession());
		nbttagcompound.setInt("Riches", this.riches);
		nbttagcompound.setInt("Career", this.bK);
		nbttagcompound.setInt("CareerLevel", this.bL);
		nbttagcompound.setBoolean("Willing", false);
		nbttagcompound.setString("OwnerUUID", ownerUUID.toString());
		nbttagcompound.setString("OwnerName", ownerName);
		nbttagcompound.setInt("CurrentCap", currentCap);
		nbttagcompound.setInt("MaxCap", maxCap);

		NBTTagCompound mapCompound = new NBTTagCompound();
		for (Map.Entry<EntityType, Integer> entries : getHerdingAmounts().entrySet()) {
			mapCompound.setInt(entries.getKey().toString(), entries.getValue());
		}

		nbttagcompound.set("HerdingAmounts", mapCompound);

		NBTTagList nbttaglist = new NBTTagList();

		for(int i = 0; i < this.inventory.getSize(); ++i) {
			ItemStack itemstack = this.inventory.getItem(i);
			if(!itemstack.isEmpty()) {
				nbttaglist.add(itemstack.save(new NBTTagCompound()));
			}
		}

		nbttagcompound.set("Inventory", nbttaglist);
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		this.setProfession(nbttagcompound.getInt("Profession"));
		this.riches = nbttagcompound.getInt("Riches");
		this.bK = nbttagcompound.getInt("Career");
		this.bL = nbttagcompound.getInt("CareerLevel");
		this.bH = nbttagcompound.getBoolean("Willing");
		this.ownerUUID = UUID.fromString(nbttagcompound.getString("OwnerUUID"));
		this.ownerName = nbttagcompound.getString("OwnerName");
		this.currentCap = nbttagcompound.getInt("CurrentCap");
		this.maxCap = nbttagcompound.getInt("MaxCap");

		NBTTagCompound mapCompound = nbttagcompound.getCompound("HerdingAmounts");
		for (String string : mapCompound.c()) {
			try {
				EntityType entityType = EntityType.valueOf(string);
				herdingAmounts.put(entityType, mapCompound.getInt(string));
			} catch (Exception e) {
				AnimalHerders.getInstance().getLogger().warning("ERROR OH NO BOYS. +++ " + string);
			}
		}

		NBTTagList nbttaglist = nbttagcompound.getList("Inventory", 10);

		for(int i = 0; i < nbttaglist.size(); ++i) {
			ItemStack itemstack = new ItemStack(nbttaglist.get(i));
			if(!itemstack.isEmpty()) {
				this.inventory.a(itemstack);
			}
		}
		this.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6&lHERDER " + ChatColor.WHITE + "(" + ChatColor.DARK_GRAY + ownerName + ChatColor.WHITE + ")"));
	}

	@Override
	protected void r() {
		this.goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
	}

	@Override
	protected void p() {
	}

	@Override
	protected void M() {
	}

	@Override
	public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
		return false;
	}

	@Override
	public void a(@Nullable EntityLiving entityliving) {
	}

	@Override
	public boolean r(boolean flag) {
		return false;
	}

	@Override
	public void a(MerchantRecipe merchantrecipe) {
	}

	@Override
	public void a(ItemStack itemstack) {
	}

	@Override
	public EntityVillager b(EntityAgeable entityageable) {
		return null;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityageable) {
		return null;
	}

	@Override
	public void onLightningStrike(EntityLightning entitylightning) {
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public Map<EntityType, Integer> getHerdingAmounts() {
		return herdingAmounts;
	}

	public int getHerdingAmountOf(EntityType entityType) {
		return herdingAmounts.getOrDefault(entityType, 0);
	}

	public void setHerdingAmountOf(EntityType entityType, int amount) {
		if (herdingAmounts.containsKey(entityType)) {
			herdingAmounts.put(entityType, amount);
		}
	}
}
