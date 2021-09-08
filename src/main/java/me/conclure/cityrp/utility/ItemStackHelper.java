package me.conclure.cityrp.utility;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemStackHelper extends Unconstructable{

    public static ItemStack copyAndEditNbt(ItemStack stack, Consumer<? super NBTTagCompound> consumer) {
        net.minecraft.server.v1_16_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsCopy.getOrCreateTag();
        consumer.accept(tag);
        return CraftItemStack.asCraftMirror(nmsCopy);
    }
}
