package me.conclure.cityrp.item;

import org.bukkit.inventory.ItemStack;

public interface ItemEqualityComparator {
    boolean test(ItemStack testObject, Item item);

    default ItemEqualityComparator or(ItemEqualityComparator filter) {
        return (testObject, item) -> this.test(testObject, item) || filter.test(testObject, item);
    }

    default ItemEqualityComparator and(ItemEqualityComparator filter) {
        return (testObject, item) -> this.test(testObject, item) && filter.test(testObject, item);
    }

    default ItemEqualityComparator negate() {
        return (testObject, item) -> !this.test(testObject,item);
    }

    static ItemEqualityComparator not(ItemEqualityComparator filter) {
        return (testObject, item) -> !filter.test(testObject, item);
    }
}
