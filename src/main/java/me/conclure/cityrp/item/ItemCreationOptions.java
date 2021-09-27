package me.conclure.cityrp.item;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCreationOptions {
    private static final ItemStackCallback DEFAULT_ITEM_STACK_CALLBACK = (item, stack) -> {
    };
    private static final ItemStackProcessor DEFAULT_ITEM_STACK_PROCESSOR = (shouldCancel, item, stack) -> shouldCancel;
    private static final ItemMetaCallback DEFAULT_ITEM_META_CALLBACK = (item, meta) -> {
    };
    private static final ItemMetaProcessor DEFAULT_ITEM_META_PROCESSOR = (shouldCancel, item, meta) -> shouldCancel;
    private static final NbtCallback DEFAULT_NBT_CALLBACK = (item, nbt) -> {
    };
    private static final NbtProcessor DEFAULT_NBT_PROCESSOR = (shouldCancel, item, nbt) -> shouldCancel;
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

    public ItemStackCallback getOnCreation() {
        return this.onCreation;
    }

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

    public ItemStackProcessor getPreStackEdit() {
        return this.preStackEdit;
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

    public ItemStackCallback getPostStackEdit() {
        return this.postStackEdit;
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

    public ItemStackProcessor getBeforeMetaEdit() {
        return this.beforeMetaEdit;
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

    public ItemMetaProcessor getPreMetaEdit() {
        return this.preMetaEdit;
    }

    public ItemCreationOptions preMetaEdit(ItemMetaProcessor callback) {
        this.preMetaEdit = this.preMetaEdit;
        return this;
    }

    public ItemMetaCallback getPostMetaEdit() {
        return this.postMetaEdit;
    }

    public ItemCreationOptions postMetaEdit(ItemMetaCallback callback) {
        this.postMetaEdit = this.postMetaEdit;
        return this;
    }

    public ItemStackCallback getAfterMetaEdit() {
        return this.afterMetaEdit;
    }

    public ItemCreationOptions afterMetaEdit(ItemStackCallback callback) {
        this.afterMetaEdit = this.afterMetaEdit;
        return this;
    }

    public ItemStackProcessor getBeforeNbtEdit() {
        return this.beforeNbtEdit;
    }

    public ItemCreationOptions beforeNbtEdit(ItemStackProcessor callback) {
        this.beforeNbtEdit = this.beforeNbtEdit;
        return this;
    }

    public NbtProcessor getPreNbtEdit() {
        return this.preNbtEdit;
    }

    public ItemCreationOptions preNbtEdit(NbtProcessor callback) {
        this.preNbtEdit = this.preNbtEdit;
        return this;
    }

    public NbtCallback getPostNbtEdit() {
        return this.postNbtEdit;
    }

    public ItemCreationOptions postNbtEdit(NbtCallback callback) {
        this.postNbtEdit = this.postNbtEdit;
        return this;
    }

    public ItemStackCallback getAfterNbtEdit() {
        return this.afterNbtEdit;
    }

    public ItemCreationOptions afterNbtEdit(ItemStackCallback callback) {
        this.afterNbtEdit = this.afterNbtEdit;
        return this;
    }

    @FunctionalInterface
    public interface ItemStackCallback {
        private static ItemStackCallback of(ItemStackCallback first, ItemStackCallback second) {
            return (item, stack) -> {
                first.run(item, stack);
                second.run(item, stack);
            };
        }

        void run(Item item, ItemStack itemStack);

        default ItemStackCallback append(ItemStackCallback other) {
            return ItemStackCallback.of(this, other);
        }

        default ItemStackCallback prepend(ItemStackCallback other) {
            return ItemStackCallback.of(other, this);
        }
    }

    @FunctionalInterface
    public interface ItemStackProcessor {
        private static ItemStackProcessor of(ItemStackProcessor first, ItemStackProcessor second) {
            return (shouldCancel, item, stack) -> {
                shouldCancel = first.process(shouldCancel, item, stack);
                return second.process(shouldCancel, item, stack);
            };
        }

        boolean process(boolean shouldCancel, Item item, ItemStack itemStack);

        default ItemStackProcessor append(ItemStackProcessor other) {
            return ItemStackProcessor.of(this, other);
        }

        default ItemStackProcessor prepend(ItemStackProcessor other) {
            return ItemStackProcessor.of(other, this);
        }
    }

    @FunctionalInterface
    public interface ItemMetaCallback {
        private static ItemMetaCallback of(ItemMetaCallback first, ItemMetaCallback second) {
            return (item, meta) -> {
                first.run(item, meta);
                second.run(item, meta);
            };
        }

        void run(Item item, ItemMeta itemMeta);

        default ItemMetaCallback append(ItemMetaCallback other) {
            return ItemMetaCallback.of(this, other);
        }

        default ItemMetaCallback prepend(ItemMetaCallback other) {
            return ItemMetaCallback.of(other, this);
        }
    }

    @FunctionalInterface
    public interface ItemMetaProcessor {
        private static ItemMetaProcessor of(ItemMetaProcessor first, ItemMetaProcessor second) {
            return (shouldCancel, item, meta) -> {
                shouldCancel = first.process(shouldCancel, item, meta);
                return second.process(shouldCancel, item, meta);
            };
        }

        boolean process(boolean shouldCancel, Item item, ItemMeta itemMeta);

        default ItemMetaProcessor append(ItemMetaProcessor other) {
            return ItemMetaProcessor.of(this, other);
        }

        default ItemMetaProcessor prepend(ItemMetaProcessor other) {
            return ItemMetaProcessor.of(other, this);
        }
    }

    @FunctionalInterface
    public interface NbtCallback {
        private static NbtCallback of(NbtCallback first, NbtCallback second) {
            return (item, nbt) -> {
                first.run(item, nbt);
                second.run(item, nbt);
            };
        }

        void run(Item item, NBTTagCompound nbt);

        default NbtCallback append(NbtCallback other) {
            return NbtCallback.of(this, other);
        }

        default NbtCallback prepend(NbtCallback other) {
            return NbtCallback.of(other, this);
        }
    }

    @FunctionalInterface
    public interface NbtProcessor {
        private static NbtProcessor of(NbtProcessor first, NbtProcessor second) {
            return (shouldCancel, item, nbt) -> {
                shouldCancel = first.process(shouldCancel, item, nbt);
                return second.process(shouldCancel, item, nbt);
            };
        }

        boolean process(boolean shouldCancel, Item item, NBTTagCompound nbt);

        default NbtProcessor append(NbtProcessor other) {
            return NbtProcessor.of(this, other);
        }

        default NbtProcessor prepend(NbtProcessor other) {
            return NbtProcessor.of(other, this);
        }
    }
}
