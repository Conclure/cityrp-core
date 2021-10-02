package me.conclure.cityrp.paper.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import me.conclure.cityrp.common.utility.Key;
import me.conclure.cityrp.common.utility.collections.MoreCollections;
import me.conclure.cityrp.paper.item.rarity.Rarity;
import me.conclure.cityrp.paper.item.repository.ItemRepository;
import me.conclure.cityrp.paper.utility.ItemStacks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.nullness.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

public class Item {
    public static final String UNSTACKABLE_KEY;
    public static final String UNIQUE_KEY;
    public static final String BYPASS_KEY;

    static {
        UNSTACKABLE_KEY = "crp:unstackableId";
        UNIQUE_KEY = "crp:key";
        BYPASS_KEY = "crp:bypass";
    }

    private final Material material;
    private final boolean isUnstackable;
    private final boolean isUnbreakable;
    private final int durability;

    @Nullable
    private final Integer customModelData;

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
    private final Rarity rarity;

    private final ItemRepository itemRepository;

    public Item(ItemRepository itemRepository, ItemProperties properties) {
        Preconditions.checkNotNull(properties.material);
        Preconditions.checkArgument(!properties.material.isAir());
        Preconditions.checkArgument(properties.durability >= 0);
        Preconditions.checkNotNull(properties.lore);
        Preconditions.checkNotNull(properties.itemFlags);
        Preconditions.checkNotNull(properties.attributes);
        Preconditions.checkNotNull(properties.enchantments);
        Preconditions.checkNotNull(properties.rarity);

        this.itemRepository = itemRepository;
        this.material = properties.material;
        this.isUnstackable = properties.isUnstackable;
        this.isUnbreakable = properties.isUnbreakable;
        this.durability = properties.durability;
        this.customModelData = properties.customModelData;
        switch (properties.lore.length) {
            case 0: {
                this.lore = Collections.emptyList();
                break;
            }
            case 1: {
                this.lore = Collections.singletonList(properties.lore[0]);
                break;
            }
            default: {
                this.lore = ImmutableList.copyOf(properties.lore);
                break;
            }
        }
        switch (properties.itemFlags.length) {
            case 0: {
                this.itemFlags = Collections.emptySet();
                break;
            }
            case 1: {
                this.itemFlags = Collections.singleton(properties.itemFlags[0]);
                break;
            }
            default: {
                EnumSet<ItemFlag> tempSet = EnumSet.noneOf(ItemFlag.class);
                tempSet.addAll(Arrays.asList(properties.itemFlags));
                this.itemFlags = ImmutableSet.copyOf(tempSet);
                break;
            }
        }
        switch (properties.enchantments.size()) {
            case 0: {
                this.enchantments = Object2IntMaps.emptyMap();
                break;
            }
            case 1: {
                ObjectSet<Object2IntMap.Entry<Enchantment>> entries = properties.enchantments.object2IntEntrySet();
                ObjectIterator<Object2IntMap.Entry<Enchantment>> iterator = entries.iterator();
                Object2IntMap.Entry<Enchantment> entry = iterator.next();
                this.enchantments = Object2IntMaps.singleton(entry.getKey(), entry.getIntValue());
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
        this.rarity = properties.rarity;
    }

    static void editMeta(ItemMeta meta, Item item) {
        meta.setCustomModelData(item.customModelData);
        meta.displayName(Component.text()
                .decoration(TextDecoration.ITALIC, false)
                .color(item.rarity.getColor())
                .append(Component.text(item.getKey().toString()))
                .build());
        meta.setUnbreakable(item.isUnbreakable);
        meta.lore(item.lore);

        if (meta instanceof Damageable) {
            ((Damageable) meta).setDamage(item.durability);
        }

        for (ItemFlag itemFlag : item.itemFlags) {
            meta.addItemFlags(itemFlag);
        }

        for (Object2IntMap.Entry<Enchantment> entry : item.enchantments.object2IntEntrySet()) {
            meta.addEnchant(entry.getKey(), entry.getIntValue(), true);
        }

        for (Map.Entry<Attribute, AttributeModifier> entry : item.attributes.entries()) {
            meta.addAttributeModifier(entry.getKey(), entry.getValue());
        }
    }

    private static void editNbt(NBTTagCompound tag, Item item) {
        if (item.isUnstackable) {
            tag.a(UNSTACKABLE_KEY, UUID.randomUUID());
        }
        tag.setString(UNIQUE_KEY, item.getKey().toString());
    }

    public Key getKey() {
        Key key = this.itemRepository.getKey(this);
        Preconditions.checkNotNull(key);
        return key;
    }

    public int getId() {
        OptionalInt id = this.itemRepository.getId(this);
        Preconditions.checkArgument(id.isPresent());
        return id.getAsInt();
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

    public Rarity getRarity() {
        return this.rarity;
    }

    public ItemStack newStack(int amount) {
        ItemStack stack = new ItemStack(this.material);
        stack.setAmount(amount);
        stack.editMeta(meta -> {
            Item.editMeta(meta, this);
        });
        stack = ItemStacks.copyAndEditNbt(stack, tag -> {
            Item.editNbt(tag, this);
        });
        return stack;
    }

    public ItemStack newStack(int amount, ItemCreationOptions options) {
        ItemStack stack = new ItemStack(this.material);
        options.getOnCreation().run(this, stack);

        if (!options.getPreStackEdit().process(false, this, stack)) {
            stack.setAmount(amount);
            options.getPostStackEdit().run(this, stack);
        }

        if (!options.getBeforeMetaEdit().process(false, this, stack)) {
            stack.editMeta(meta -> {

                if (!options.getPreMetaEdit().process(false, this, meta)) {
                    Item.editMeta(meta, this);
                    options.getPostMetaEdit().run(this, meta);
                }

            });
            options.getAfterMetaEdit().run(this, stack);
        }

        if (!options.getBeforeNbtEdit().process(false, this, stack)) {
            stack = ItemStacks.copyAndEditNbt(stack, tag -> {

                if (!options.getPreNbtEdit().process(false, this, tag)) {
                    Item.editNbt(tag, this);
                    options.getPostNbtEdit().run(this, tag);
                }

            });
            options.getAfterNbtEdit().run(this, stack);
        }

        return stack;
    }

    public ItemStack newStack(ItemCreationOptions options) {
        return this.newStack(1, options);
    }

    public ItemStack newStack() {
        return this.newStack(1);
    }
}
