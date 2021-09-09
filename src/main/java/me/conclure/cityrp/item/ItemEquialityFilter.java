package me.conclure.cityrp.item;

import org.bukkit.inventory.ItemStack;

public interface ItemEquialityFilter {
    boolean test(ItemStack testObject, Item item);

    default ItemEquialityFilter or(ItemEquialityFilter filter) {
        return (testObject, item) -> this.test(testObject, item) || filter.test(testObject, item);
    }

    default ItemEquialityFilter and(ItemEquialityFilter filter) {
        return (testObject, item) -> this.test(testObject, item) && filter.test(testObject, item);
    }

    default ItemEquialityFilter negate() {
        return (testObject, item) -> !this.test(testObject,item);
    }

    static ItemEquialityFilter not(ItemEquialityFilter filter) {
        return (testObject, item) -> !filter.test(testObject, item);
    }
}
