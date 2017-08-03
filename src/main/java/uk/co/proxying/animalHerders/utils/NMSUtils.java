package uk.co.proxying.animalHerders.utils;

import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.MinecraftKey;

/**
 * Created by Kieran (Proxying) on 01-Aug-17.
 */
public class NMSUtils {

	public void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
		MinecraftKey entityKey = new MinecraftKey(name);
		EntityTypes.d.add(entityKey);
		EntityTypes.b.a(id, entityKey, customClass);
	}
}
