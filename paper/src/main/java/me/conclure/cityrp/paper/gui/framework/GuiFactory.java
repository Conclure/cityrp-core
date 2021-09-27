package me.conclure.cityrp.paper.gui.framework;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.utility.function.Functions;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.nullness.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class GuiFactory implements Iterable<GuiCell> {
    private final int size;
    private final GuiCell[] cellArray;
    private final Consumer<? super InventoryOpenEvent> openAction;
    private final Consumer<? super InventoryCloseEvent> closeAction;
    private final Consumer<? super InventoryDragEvent> dragAction;

    GuiFactory(int size, GuiCell[] cellArray, Consumer<? super InventoryOpenEvent> openAction, Consumer<? super InventoryCloseEvent> closeAction, Consumer<? super InventoryDragEvent> dragAction) {
        this.size = size;
        this.cellArray = cellArray;
        this.openAction = openAction;
        this.closeAction = closeAction;
        this.dragAction = dragAction;
    }

    public static Builder newBuilder(int rows) {
        return new GuiFactory.Builder(rows);
    }

    public void onOpen(InventoryOpenEvent event) {
        this.openAction.accept(event);
    }

    public void onClose(InventoryCloseEvent event) {
        this.closeAction.accept(event);
    }

    public void onDrag(InventoryDragEvent event) {
        this.dragAction.accept(event);
    }

    public Gui newGui(Component title) {
        return new Gui(this, title);
    }

    public int getSize() {
        return this.size;
    }

    public int getRows() {
        return this.size * 9;
    }

    @Nullable
    public GuiCell getCell(int slot) {
        return this.cellArray[slot];
    }

    @NotNull
    @Override
    public Iterator<GuiCell> iterator() {
        return Arrays.asList(this.cellArray).iterator();
    }

    public static class Builder {
        final int size;
        final GuiCell.Builder[] cellArray;
        Consumer<? super InventoryOpenEvent> openAction = Functions.emptyConsumer();
        Consumer<? super InventoryCloseEvent> closeAction = Functions.emptyConsumer();
        Consumer<? super InventoryDragEvent> dragAction = Functions.emptyConsumer();

        Builder(int rows) {
            Preconditions.checkArgument(rows >= 1);
            Preconditions.checkArgument(rows <= 6);

            this.size = rows * 9;
            this.cellArray = new GuiCell.Builder[this.size];
        }

        public Builder cell(int slot, UnaryOperator<GuiCell.Builder> transformer) {
            Preconditions.checkArgument(slot >= 0);
            Preconditions.checkArgument(slot < this.size);
            this.cellArray[slot] = transformer.apply(new GuiCell.Builder());
            return this;
        }

        public Builder closeAction(Consumer<? super InventoryCloseEvent> closeAction) {
            this.closeAction = closeAction;
            return this;
        }

        public Builder dragAction(Consumer<? super InventoryDragEvent> dragAction) {
            this.dragAction = dragAction;
            return this;
        }

        public Builder openAction(Consumer<? super InventoryOpenEvent> openAction) {
            this.openAction = openAction;
            return this;
        }

        public GuiFactory build() {
            GuiCell[] cellArray = new GuiCell[this.size];
            for (int i = 0; i < this.cellArray.length; i++) {
                GuiCell.Builder builder = this.cellArray[i];

                if (builder == null) {
                    continue;
                }

                cellArray[i] = builder.build(i);
            }
            return new GuiFactory(this.size, cellArray, this.openAction, this.closeAction, this.dragAction);
        }
    }
}
