package me.conclure.cityrp.item;

import com.google.common.collect.Collections2;
import com.google.common.collect.MoreCollectors;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.conclure.cityrp.rarity.Rarities;
import me.conclure.cityrp.rarity.Rarity;
import me.conclure.cityrp.utility.MoreCollections;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jspecify.nullness.Nullable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ItemProperties {

    Material material;
    boolean isUnstackable;
    boolean isUnbreakable;
    int durability;

    @Nullable
    Integer customModelData;
    @Nullable
    Component displayName;

    Component[] lore = new Component[0];
    ItemFlag[] itemFlags = new ItemFlag[0];
    Multimap<Attribute, AttributeModifier> attributes;
    Object2IntMap<Enchantment> enchantments;

    ItemEquialityFilter equialityFilter;
    Rarity rarity = Rarities.COMMON;

    public ItemProperties() {
        this.attributes = MoreCollections.emptyMultimap();
        this.enchantments = Object2IntMaps.emptyMap();
    }

    public ItemProperties displayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemProperties material(Material material) {
        this.material = material;
        return this;
    }

    public ItemProperties customModelData(Integer customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemProperties durability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemProperties unbreakable(boolean unbreakable) {
        this.isUnbreakable = unbreakable;
        return this;
    }

    public ItemProperties unstackable(boolean unstackable) {
        this.isUnstackable = unstackable;
        return this;
    }

    public ItemProperties lore(Component... components) {
        this.lore = components;
        return this;
    }

    public ItemProperties itemFlags(ItemFlag... itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemProperties attributes(Multimap<Attribute, AttributeModifier> attributes) {
        this.attributes = attributes;
        return this;
    }

    public ItemProperties enchantments(Object2IntMap<Enchantment> map) {
        this.enchantments = map;
        return this;
    }

    public ItemProperties equalityFilter(ItemEquialityFilter equialityFilter) {
        this.equialityFilter = equialityFilter;
        return this;
    }

    public ItemProperties editEqualityFilter(UnaryOperator<ItemEquialityFilter> operator) {
        this.equialityFilter = operator.apply(this.equialityFilter);
        return this;
    }

    public ItemProperties rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }
}
