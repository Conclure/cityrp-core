package me.conclure.cityrp.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import it.unimi.dsi.fastutil.objects.*;
import me.conclure.cityrp.utility.ItemStackHelper;
import me.conclure.cityrp.utility.MoreCollections;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.nullness.Nullable;

import java.util.*;

public class Item {
    public static final ItemEquialityFilter DEFAULT_FILTER;

    static {
        DEFAULT_FILTER = (testObject, item) -> {
            return testObject.getType() == item.material;
        };
    }

    private final Material material;
    private final boolean isUnstackable;
    private final boolean isUnbreakable;
    private final int durability;

    @Nullable
    private final Integer customModelData;
    @Nullable
    private final Component displayName;

    @UnmodifiableView
    @Unmodifiable
    private final List<Component> lore;
    @UnmodifiableView
    @Unmodifiable
    private final Set<ItemFlag> itemFlags;
    @UnmodifiableView
    @Unmodifiable
    private final Multimap<Attribute, AttributeModifier> attributes;
    @UnmodifiableView
    @Unmodifiable
    private final Object2IntMap<Enchantment> enchantments;

    public Item(ItemProperties properties) {
        Preconditions.checkNotNull(properties.material);
        Preconditions.checkArgument(!properties.material.isAir());
        Preconditions.checkArgument(properties.durability > 0);
        Preconditions.checkNotNull(properties.lore);
        Preconditions.checkNotNull(properties.itemFlags);
        Preconditions.checkNotNull(properties.attributes);
        Preconditions.checkNotNull(properties.enchantments);

        this.material = properties.material;
        this.isUnstackable = properties.isUnstackable;
        this.isUnbreakable = properties.isUnbreakable;
        this.durability = properties.durability;
        this.customModelData = properties.customModelData;
        this.displayName = properties.displayName;
        switch (properties.lore.length) {
            case 0: { this.lore = Collections.emptyList(); break; }
            case 1: { this.lore = Collections.singletonList(properties.lore[0]); break; }
            default: { this.lore = ImmutableList.copyOf(properties.lore); break; }
        }
        switch (properties.itemFlags.length) {
            case 0: { this.itemFlags = Collections.emptySet(); break; }
            case 1: { this.itemFlags = Collections.singleton(properties.itemFlags[0]); break; }
            default: {
                EnumSet<ItemFlag> tempSet = EnumSet.noneOf(ItemFlag.class);
                tempSet.addAll(Arrays.asList(properties.itemFlags));
                this.itemFlags = ImmutableSet.copyOf(tempSet);
                break;
            }
        }
        switch (properties.enchantments.size()) {
            case 0: { this.enchantments = Object2IntMaps.emptyMap(); break; }
            case 1: {
                ObjectSet<Object2IntMap.Entry<Enchantment>> entries = properties.enchantments.object2IntEntrySet();
                ObjectIterator<Object2IntMap.Entry<Enchantment>> iterator = entries.iterator();
                Object2IntMap.Entry<Enchantment> entry = iterator.next();
                this.enchantments = Object2IntMaps.singleton(entry.getKey(),entry.getIntValue());
                break;
            }
            default: {
                this.enchantments = Object2IntMaps.unmodifiable(new Object2IntOpenHashMap<>(properties.enchantments));
                break;
            }
        }
        if (properties.attributes.isEmpty()) {
            this.attributes = MoreCollections.emptyMultimap();
        } else {
            this.attributes = ImmutableMultimap.copyOf(properties.attributes);
        }
    }

    @Nullable
    public Component getDisplayName() {
        return this.displayName;
    }

    public boolean hasDisplayName() {
        return this.displayName != null;
    }

    public boolean isUnstackable() {
        return this.isUnstackable;
    }

    public boolean isUnbreakable() {
        return this.isUnbreakable;
    }

    public int getDurability() {
        return this.durability;
    }

    public Material getMaterial() {
        return this.material;
    }

    public boolean hasCustomModelData() {
        return this.customModelData != null;
    }

    @Nullable
    public Integer getCustomModelData() {
        return this.customModelData;
    }

    public boolean hasLore() {
        return !this.lore.isEmpty();
    }

    @UnmodifiableView
    @Unmodifiable
    public List<Component> getLore() {
        return this.lore;
    }

    public boolean hasItemFlags() {
        return this.itemFlags != null;
    }

    public boolean hasItemFlag(ItemFlag itemFlag) {
        return this.itemFlags.contains(itemFlag);
    }

    @UnmodifiableView
    @Unmodifiable
    public Set<ItemFlag> getItemFlags() {
        return this.itemFlags;
    }

    public boolean hasAttributes() {
        return !this.attributes.isEmpty();
    }

    @UnmodifiableView
    @Unmodifiable
    public Multimap<Attribute, AttributeModifier> getAttributes() {
        return this.attributes;
    }

    public boolean hasEnchantments() {
        return !this.enchantments.isEmpty();
    }

    @UnmodifiableView
    @Unmodifiable
    public Object2IntMap<Enchantment> getEnchantments() {
        return this.enchantments;
    }

    public boolean hasEnchantment(Enchantment enchantment) {
        return this.enchantments.containsKey(enchantment);
    }

    @Nullable
    public Integer getEnchantmentLevel(Enchantment enchantment) {
        if (!this.enchantments.containsKey(enchantment)) {
            return null;
        }

        return this.enchantments.getInt(enchantment);
    }

    private static ItemMeta editMeta(ItemMeta meta, Item item) {
        meta.setCustomModelData(item.customModelData);
        meta.displayName(item.displayName);
        meta.setUnbreakable(item.isUnbreakable);
        meta.lore(item.lore);

        if (meta instanceof Damageable) {
            ((Damageable) meta).setDamage(item.durability);
        }

        for (ItemFlag itemFlag : item.itemFlags) {
            meta.addItemFlags(itemFlag);
        }

        for (Object2IntMap.Entry<Enchantment> entry : item.enchantments.object2IntEntrySet()) {
            meta.addEnchant(entry.getKey(),entry.getIntValue(),true);
        }

        for (Map.Entry<Attribute, AttributeModifier> entry : item.attributes.entries()) {
            meta.addAttributeModifier(entry.getKey(),entry.getValue());
        }

        return meta;
    }

    public ItemStack newStack(int amount) {
        ItemStack stack = new ItemStack(this.material);
        stack.setAmount(amount);
        stack.editMeta(meta -> {
            Item.editMeta(meta,this);
        });
        stack = ItemStackHelper.copyAndEditNbt(stack, tag -> {
            tag.setUUID("crp:unstackableId",UUID.randomUUID());
        });
        return stack;
    }

    public ItemStack newStack(int amount, ItemCreationOptions options) {
        ItemStack stack = new ItemStack(this.material);
        options.onCreation.accept(this,stack);

        if (options.preStackEdit.apply(this,stack) == Boolean.TRUE) {
            stack.setAmount(amount);
            options.postStackEdit.accept(this,stack);
        }

        if (options.beforeMetaEdit.apply(this,stack) == Boolean.TRUE) {
            stack.editMeta(meta -> {

                if (options.preMetaEdit.apply(this,meta) == Boolean.TRUE) {
                    Item.editMeta(meta,this);
                    options.postMetaEdit.accept(this,meta);
                }

            });
            options.afterMetaEdit.accept(this,stack);
        }

        if (options.beforeNbtEdit.apply(this,stack) == Boolean.TRUE) {
            stack = ItemStackHelper.copyAndEditNbt(stack, tag -> {

                if (options.preNbtEdit.apply(this, tag) == Boolean.TRUE) {
                    tag.setUUID("crp:unstackableId", UUID.randomUUID());
                    options.postNbtEdit.accept(this, tag);
                }

            });
            options.afterMetaEdit.accept(this, stack);
        }

        return stack;
    }

    public ItemStack newStack(ItemCreationOptions options) {
        return this.newStack(1,options);
    }

    public ItemStack newStack() {
        return this.newStack(1);
    }
}
