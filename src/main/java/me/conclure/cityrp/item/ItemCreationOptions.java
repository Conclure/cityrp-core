package me.conclure.cityrp.item;

import me.conclure.cityrp.utility.MoreFunctions;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ItemCreationOptions {
    BiConsumer<Item,ItemStack> onCreation = MoreFunctions.emptyBiConsumer();

    BiFunction<Item,ItemStack,Boolean> preStackEdit = MoreFunctions.trueBiFunction();
    BiConsumer<Item,ItemStack> postStackEdit = MoreFunctions.emptyBiConsumer();

    BiFunction<Item,ItemStack,Boolean> beforeMetaEdit = MoreFunctions.trueBiFunction();
    BiFunction<Item,ItemMeta,Boolean> preMetaEdit = MoreFunctions.trueBiFunction();
    BiConsumer<Item,ItemMeta> postMetaEdit = MoreFunctions.emptyBiConsumer();
    BiConsumer<Item,ItemStack> afterMetaEdit = MoreFunctions.emptyBiConsumer();

    BiFunction<Item,ItemStack,Boolean> beforeNbtEdit = MoreFunctions.trueBiFunction();
    BiFunction<Item,NBTTagCompound,Boolean> preNbtEdit = MoreFunctions.trueBiFunction();
    BiConsumer<Item,NBTTagCompound> postNbtEdit = MoreFunctions.emptyBiConsumer();
    BiConsumer<Item,ItemStack> afterNbtEdit = MoreFunctions.emptyBiConsumer();

    public ItemCreationOptions onCreation(BiConsumer<Item, ItemStack> onCreation) {
        this.onCreation = onCreation;
        return this;
    }

    public ItemCreationOptions appendOnCreation(BiConsumer<Item, ItemStack> onCreation) {
        if (this.onCreation == MoreFunctions.EMPTY_BI_CONSUMER) {
            return this.onCreation(onCreation);
        }
        this.onCreation = this.onCreation.andThen(onCreation);
        return this;
    }

    public ItemCreationOptions prependOnCreation(BiConsumer<Item, ItemStack> onCreation) {
        if (this.onCreation == MoreFunctions.EMPTY_BI_CONSUMER) {
            return this.onCreation(onCreation);
        }
        this.onCreation = onCreation.andThen(this.onCreation);
        return this;
    }

    public ItemCreationOptions preStackEdit(BiConsumer<Item, ItemStack> preStackEdit) {
        this.preStackEdit = preStackEdit;
        return this;
    }

    public ItemCreationOptions appendPreStackEdit(BiConsumer<Item, ItemStack> preStackEdit) {
        if (this.preStackEdit == MoreFunctions.EMPTY_BI_CONSUMER) {
            return this.preStackEdit(preStackEdit);
        }
        this.preStackEdit = this.preStackEdit.andThen(preStackEdit);
        return this;
    }

    public ItemCreationOptions prependPreStackEdit(BiConsumer<Item, ItemStack> preStackEdit) {
        if (this.preStackEdit == MoreFunctions.EMPTY_BI_CONSUMER) {
            return this.preStackEdit(preStackEdit);
        }
        this.preStackEdit = preStackEdit.andThen(this.preStackEdit);
        return this;
    }

    public ItemCreationOptions postStackEdit(BiConsumer<Item, ItemStack> postStackEdit) {
        this.postStackEdit = postStackEdit;
        return this;
    }

    public ItemCreationOptions appendPostStackEdit(BiConsumer<Item, ItemStack> postStackEdit) {
        if (this.postStackEdit == MoreFunctions.EMPTY_BI_CONSUMER) {
            return this.postStackEdit(postStackEdit);
        }
        this.postStackEdit = this.postStackEdit.andThen(postStackEdit);
        return this;
    }

    public ItemCreationOptions prependPostStackEdit(BiConsumer<Item, ItemStack> postStackEdit) {
        if (this.postStackEdit == MoreFunctions.EMPTY_BI_CONSUMER) {
            return this.postStackEdit(postStackEdit);
        }
        this.postStackEdit = postStackEdit.andThen(this.postStackEdit);
        return this;
    }

    public ItemCreationOptions beforeMetaEdit(BiConsumer<Item, ItemStack> beforeMetaEdit) {
        this.beforeMetaEdit = beforeMetaEdit;
        return this;
    }

    public ItemCreationOptions preMetaEdit(BiConsumer<Item, ItemMeta> preMetaEdit) {
        this.preMetaEdit = preMetaEdit;
        return this;
    }

    public ItemCreationOptions postMetaEdit(BiConsumer<Item, ItemMeta> postMetaEdit) {
        this.postMetaEdit = postMetaEdit;
        return this;
    }

    public ItemCreationOptions afterMetaEdit(BiConsumer<Item, ItemStack> afterMetaEdit) {
        this.afterMetaEdit = afterMetaEdit;
        return this;
    }

    public ItemCreationOptions beforeNbtEdit(BiConsumer<Item, ItemStack> beforeNbtEdit) {
        this.beforeNbtEdit = beforeNbtEdit;
        return this;
    }

    public ItemCreationOptions preNbtEdit(BiConsumer<Item, NBTTagCompound> preNbtEdit) {
        this.preNbtEdit = preNbtEdit;
        return this;
    }

    public ItemCreationOptions postNbtEdit(BiConsumer<Item, NBTTagCompound> postNbtEdit) {
        this.postNbtEdit = postNbtEdit;
        return this;
    }

    public ItemCreationOptions afterNbtEdit(BiConsumer<Item, ItemStack> afterNbtEdit) {
        this.afterNbtEdit = afterNbtEdit;
        return this;
    }
}
