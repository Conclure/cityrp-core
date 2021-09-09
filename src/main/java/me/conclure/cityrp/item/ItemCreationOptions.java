package me.conclure.cityrp.item;

import me.conclure.cityrp.utility.MoreFunctions;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.BiConsumer;

public class ItemCreationOptions {
    private static final ItemStackCallback DEFAULT_ITEM_STACK_CALLBACK = (item,stack) -> {};
    private static final ItemStackProcessor DEFAULT_ITEM_STACK_PROCESSOR = (shouldCancel, item,stack) -> shouldCancel;
    private static final ItemMetaCallback DEFAULT_ITEM_META_CALLBACK = (item,meta) -> {};
    private static final ItemMetaProcessor DEFAULT_ITEM_META_PROCESSOR = (shouldCancel, item,meta) -> shouldCancel;
    private static final NbtCallback DEFAULT_NBT_CALLBACK = (item,nbt) -> {};
    private static final NbtProcessor DEFAULT_NBT_PROCESSOR = (shouldCancel, item,nbt) -> shouldCancel;

    @FunctionalInterface
    public interface ItemStackCallback {
        void run(Item item, ItemStack itemStack);

        private static ItemStackCallback of(ItemStackCallback first, ItemStackCallback second) {
            return (item, stack) -> {
                first.run(item,stack);
                second.run(item,stack);
            };
        }

        default ItemStackCallback append(ItemStackCallback other) {
            return ItemStackCallback.of(this,other);
        }

        default ItemStackCallback prepend(ItemStackCallback other) {
            return ItemStackCallback.of(other,this);
        }
    }

    @FunctionalInterface
    public interface ItemStackProcessor {
        boolean process(boolean shouldCancel, Item item, ItemStack itemStack);

        private static ItemStackProcessor of(ItemStackProcessor first, ItemStackProcessor second) {
            return (shouldCancel, item, stack) -> {
                shouldCancel = first.process(shouldCancel, item,stack);
                return second.process(shouldCancel,item,stack);
            };
        }

        default ItemStackProcessor append(ItemStackProcessor other) {
            return ItemStackProcessor.of(this,other);
        }

        default ItemStackProcessor prepend(ItemStackProcessor other) {
            return ItemStackProcessor.of(other,this);
        }
    }

    @FunctionalInterface
    public interface ItemMetaCallback {
        void run(Item item, ItemMeta itemMeta);

        private static ItemMetaCallback of(ItemMetaCallback first, ItemMetaCallback second) {
            return (item, meta) -> {
                first.run(item,meta);
                second.run(item,meta);
            };
        }

        default ItemMetaCallback append(ItemMetaCallback other) {
            return ItemMetaCallback.of(this,other);
        }

        default ItemMetaCallback prepend(ItemMetaCallback other) {
            return ItemMetaCallback.of(other,this);
        }
    }

    @FunctionalInterface
    public interface ItemMetaProcessor {
        boolean process(boolean shouldCancel, Item item, ItemMeta itemMeta);

        private static ItemMetaProcessor of(ItemMetaProcessor first, ItemMetaProcessor second) {
            return (shouldCancel, item, meta) -> {
                shouldCancel = first.process(shouldCancel, item,meta);
                return second.process(shouldCancel,item,meta);
            };
        }

        default ItemMetaProcessor append(ItemMetaProcessor other) {
            return ItemMetaProcessor.of(this,other);
        }

        default ItemMetaProcessor prepend(ItemMetaProcessor other) {
            return ItemMetaProcessor.of(other,this);
        }
    }
    @FunctionalInterface
    public interface NbtCallback {
        void run(Item item, NBTTagCompound nbt);

        private static NbtCallback of(NbtCallback first, NbtCallback second) {
            return (item, nbt) -> {
                first.run(item,nbt);
                second.run(item,nbt);
            };
        }

        default NbtCallback append(NbtCallback other) {
            return NbtCallback.of(this,other);
        }

        default NbtCallback prepend(NbtCallback other) {
            return NbtCallback.of(other,this);
        }
    }

    @FunctionalInterface
    public interface NbtProcessor {
        boolean process(boolean shouldCancel, Item item, NBTTagCompound nbt);

        private static NbtProcessor of(NbtProcessor first, NbtProcessor second) {
            return (shouldCancel, item, nbt) -> {
                shouldCancel = first.process(shouldCancel, item,nbt);
                return second.process(shouldCancel,item,nbt);
            };
        }

        default NbtProcessor append(NbtProcessor other) {
            return NbtProcessor.of(this,other);
        }

        default NbtProcessor prepend(NbtProcessor other) {
            return NbtProcessor.of(other,this);
        }
    }

    ItemStackCallback onCreation = DEFAULT_ITEM_STACK_CALLBACK;

    ItemStackProcessor preStackEdit = DEFAULT_ITEM_STACK_PROCESSOR;
    ItemStackCallback postStackEdit = DEFAULT_ITEM_STACK_CALLBACK;

    ItemStackProcessor beforeMetaEdit = DEFAULT_ITEM_STACK_PROCESSOR;
    ItemMetaProcessor preMetaEdit = DEFAULT_ITEM_META_PROCESSOR;
    ItemMetaCallback postMetaEdit = DEFAULT_ITEM_META_CALLBACK;
    ItemStackCallback afterMetaEdit = DEFAULT_ITEM_STACK_CALLBACK;

    ItemStackProcessor beforeNbtEdit = DEFAULT_ITEM_STACK_PROCESSOR;
    NbtProcessor preNbtEdit = DEFAULT_NBT_PROCESSOR;
    NbtCallback postNbtEdit = DEFAULT_NBT_CALLBACK;
    ItemStackCallback afterNbtEdit = DEFAULT_ITEM_STACK_CALLBACK;

    public ItemCreationOptions onCreation(ItemStackCallback callback) {
        this.onCreation = callback;
        return this;
    }

    public ItemCreationOptions appendOnCreation(ItemStackCallback callback) {
        if (this.onCreation == DEFAULT_ITEM_STACK_CALLBACK) {
            return this.onCreation(callback);
        }
        this.onCreation = this.onCreation.append(callback);
        return this;
    }

    public ItemCreationOptions prependOnCreation(ItemStackCallback callback) {
        if (this.onCreation == DEFAULT_ITEM_STACK_CALLBACK) {
            return this.onCreation(callback);
        }
        this.onCreation = this.onCreation.prepend(callback);
        return this;
    }

    public ItemCreationOptions preStackEdit(ItemStackProcessor callback) {
        this.preStackEdit = callback;
        return this;
    }

    public ItemCreationOptions appendPreStackEdit(ItemStackProcessor callback) {
        if (this.preStackEdit == DEFAULT_ITEM_STACK_PROCESSOR) {
            return this.preStackEdit(callback);
        }
        this.preStackEdit = this.preStackEdit.append(callback);
        return this;
    }

    public ItemCreationOptions prependPreStackEdit(ItemStackProcessor callback) {
        if (this.preStackEdit == DEFAULT_ITEM_STACK_PROCESSOR) {
            return this.preStackEdit(callback);
        }
        this.preStackEdit = this.preStackEdit.prepend(callback);
        return this;
    }

    public ItemCreationOptions postStackEdit(ItemStackCallback callback) {
        this.postStackEdit = callback;
        return this;
    }

    public ItemCreationOptions appendPostStackEdit(ItemStackCallback callback) {
        if (this.postStackEdit == DEFAULT_ITEM_STACK_CALLBACK) {
            return this.postStackEdit(callback);
        }
        this.postStackEdit = this.postStackEdit.append(callback);
        return this;
    }

    public ItemCreationOptions prependPostStackEdit(ItemStackCallback callback) {
        if (this.postStackEdit == DEFAULT_ITEM_STACK_CALLBACK) {
            return this.postStackEdit(callback);
        }
        this.postStackEdit = this.postStackEdit.prepend(callback);
        return this;
    }

    public ItemCreationOptions beforeMetaEdit(ItemStackProcessor callback) {
        this.beforeMetaEdit = callback;
        return this;
    }

    public ItemCreationOptions appendBeforeMetaEdit(ItemStackProcessor callback) {
        if (this.beforeMetaEdit == DEFAULT_ITEM_STACK_PROCESSOR) {
            return this.beforeMetaEdit(callback);
        }
        this.beforeMetaEdit = this.beforeNbtEdit.append(callback);
        return this;
    }

    public ItemCreationOptions prependBeforeMetaEdit(ItemStackProcessor callback) {
        if (this.beforeMetaEdit == DEFAULT_ITEM_STACK_PROCESSOR) {
            return this.beforeMetaEdit(callback);
        }
        this.beforeMetaEdit = this.beforeMetaEdit.prepend(callback);
        return this;
    }

    public ItemCreationOptions preMetaEdit(ItemMetaProcessor callback) {
        this.preMetaEdit = preMetaEdit;
        return this;
    }

    public ItemCreationOptions postMetaEdit(ItemMetaCallback callback) {
        this.postMetaEdit = postMetaEdit;
        return this;
    }

    public ItemCreationOptions afterMetaEdit(ItemStackCallback callback) {
        this.afterMetaEdit = afterMetaEdit;
        return this;
    }

    public ItemCreationOptions beforeNbtEdit(ItemStackProcessor callback) {
        this.beforeNbtEdit = beforeNbtEdit;
        return this;
    }

    public ItemCreationOptions preNbtEdit(NbtProcessor callback) {
        this.preNbtEdit = preNbtEdit;
        return this;
    }

    public ItemCreationOptions postNbtEdit(NbtCallback callback) {
        this.postNbtEdit = postNbtEdit;
        return this;
    }

    public ItemCreationOptions afterNbtEdit(ItemStackCallback callback) {
        this.afterNbtEdit = afterNbtEdit;
        return this;
    }
}
