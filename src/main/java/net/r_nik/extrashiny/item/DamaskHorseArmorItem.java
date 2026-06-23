package net.r_nik.extrashiny.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class DamaskHorseArmorItem extends AnimalArmorItem {

    public DamaskHorseArmorItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, BodyType.EQUESTRIAN, false, properties);
    }
}