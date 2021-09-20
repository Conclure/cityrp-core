package me.conclure.cityrp.utility;

import me.conclure.cityrp.utility.function.Obj2IntResultFunction;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ItemStacks extends Unconstructable{

    public static final ItemStack AIR_STACK = new UnmodifiableItemStack(Material.AIR);

    public static ItemStack copyAndEditNbt(ItemStack stack, Consumer<? super NBTTagCompound> consumer) {
        net.minecraft.server.v1_16_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsCopy.getOrCreateTag();
        consumer.accept(tag);
        return CraftItemStack.asCraftMirror(nmsCopy);
    }

    public static NBTTagCompound getNbtTag(ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack).getOrCreateTag();
    }

    public static <T> T getObjectFromNbt(ItemStack stack, Function<? super NBTTagCompound,? extends T> function) {
        return function.apply(ItemStacks.getNbtTag(stack));
    }

    public static boolean testNbt(ItemStack stack, Predicate<? super NBTTagCompound> predicate) {
        return predicate.test(ItemStacks.getNbtTag(stack));
    }

    public static int getIntFromNbt(ItemStack stack, Obj2IntResultFunction<? super NBTTagCompound> function) {
        return function.apply(ItemStacks.getNbtTag(stack));
    }

    static class UnmodifiableItemStack extends ItemStack {
        UnmodifiableItemStack(Material material) {
            super(material);
        }

        @Override
        public void setType(@NotNull Material type) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAmount(int amount) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setData(@Nullable MaterialData data) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setDurability(short durability) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addEnchantment(@NotNull Enchantment ench, int level) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addUnsafeEnchantment(@NotNull Enchantment ench, int level) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int removeEnchantment(@NotNull Enchantment ench) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean editMeta(@NotNull Consumer<? super ItemMeta> consumer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setItemMeta(@Nullable ItemMeta itemMeta) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ItemStack add() {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ItemStack add(int qty) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ItemStack subtract() {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ItemStack subtract(int qty) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLore(@Nullable List<String> lore) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void lore(@Nullable List<Component> lore) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addItemFlags(@NotNull ItemFlag... itemFlags) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean canRepair(@NotNull ItemStack toBeRepaired) {
            throw new UnsupportedOperationException();
        }
    }
}
