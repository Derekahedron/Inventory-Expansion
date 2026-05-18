package derekahedron.invexp.client.gui.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds functions for rendering container item tooltips. Most of the default values
 * can be modified by overriding functions. You can do this to allow as many rows as you wish
 * as well as increase items in a row. This will probably never be used as the default values work well.
 */
public interface ContainerItemTooltipComponent {
    DecimalFormat TOOLTIP_FRACTION_FORMAT = new DecimalFormat("#.##");
    int DEFAULT_MAX_ROWS = 3;
    int MIN_MAX_ROWS = 3; // Hard coded minimum. If less rows are displayed, odd behavior may occur
    int DEFAULT_SLOT_LENGTH = 24;
    int DEFAULT_ROW_WIDTH = 4;
    int DEFAULT_PROGRESS_BAR_PADDING = 4;
    int DEFAULT_PROGRESS_BAR_HEIGHT = 13;
    int DEFAULT_DESCRIPTION_PADDING = 1;
    int EXTRA_ITEMS_TEXT_COLOR = 0xFFFFFF;
    int PROGRESS_BAR_TEXT_COLOR = 0xFFFFFF;
    int DESCRIPTION_TEXT_COLOR = 0xAAAAAA;

    /**
     * Gets the tooltip width needed to render the tooltip.
     *
     * @return the width that the tooltip takes up
     */
    default int getTooltipWidth() {
        return getRowWidth() * getSlotLength();
    }

    /**
     * Gets the max rows that the tooltip can display before cutting off stacks.
     *
     * @return the max number of rows that can be displayed at a time
     */
    default int getMaxRows() {
        return DEFAULT_MAX_ROWS;
    }

    /**
     * Gets the number of items that can be displayed per row.
     *
     * @return the max number of items that are displayed in a row
     */
    default int getRowWidth() {
        return DEFAULT_ROW_WIDTH;
    }

    /**
     * Gets how long a slot should render as.
     *
     * @return the length in pixels that an item slot takes up
     */
    default int getSlotLength() {
        return DEFAULT_SLOT_LENGTH;
    }

    /**
     * Gets how tall the progress bar should render as.
     *
     * @return the height of the progress bar in pixels
     */
    default int getProgressBarHeight() {
        return DEFAULT_PROGRESS_BAR_HEIGHT;
    }

    /**
     * Gets how much padding should be rendered between the progress bar and the surrounding content.
     *
     * @return the padding in pixels surrounding the progress bar
     */
    default int getProgressBarPadding() {
        return DEFAULT_PROGRESS_BAR_PADDING;
    }

    /**
     * Gets how much padding should be rendered between the description and the surrounding content.
     *
     * @return the padding in pixels beneath the description
     */
    default int getDescriptionPadding() {
        return DEFAULT_DESCRIPTION_PADDING;
    }

    /**
     * Gets the list of stacks in the container item.
     *
     * @return the list of stacks that are in the container item
     */
    List<ItemStack> getStacks();

    /**
     * Gets the index of the selected stack.
     *
     * @return the selected index; or <code>-1</code> if there is none
     */
    int getSelectedIndex();

    /**
     * Clamps the given index to the size of the stacks in the container item.
     *
     * @param index an index to clamp to the size of the stacks
     * @return the clamped index
     */
    int clampIndex(int index);

    /**
     * Gets the fullness fraction for how full the container is.
     *
     * @return a Fraction depicting how full the progress bar should be
     */
    Fraction getFillFraction();

    /**
     * Gets the progress bar texture.
     *
     * @return the texture location for the progress bar
     */
    ResourceLocation getProgressBarFillTexture();

    /**
     * Gets the progress bar border texture.
     *
     * @return the texture location for the progress bar border
     */
    ResourceLocation getProgressBarBorderTexture();

    /**
     * Gets the slot background texture.
     *
     * @return the texture location for the slot background texture
     */
    ResourceLocation getSlotBackgroundTexture();

    /**
     * Gets the slot highlight back texture.
     *
     * @return the texture location for highlighted back texture
     */
    ResourceLocation getSlotHighlightBackTexture();

    /**
     * Gets the slot highlight front texture.
     *
     * @return the texture location for highlighted front texture
     */
    ResourceLocation getSlotHighlightFrontTexture();

    /**
     * Generates text to display over the progress bar.
     *
     * @return the text to overlay on the progress bar; can be <code>null</code>
     */
    @Nullable
    Component getProgressBarLabel();

    /**
     * Renders contents of the container. Display the row with the selected item
     * and the surrounding rows. If there are items past the rows that are displayed,
     * render the amount of items at the start or end of the bottom/top row.
     *
     * @param font the font helper
     * @param x the x position to draw contents at
     * @param y the y position to draw contents at
     * @param width the width of the entire tooltip; can be more than the default width
     * @param top the y position of the top of the tooltip
     * @param guiGraphics the graphics helper
     */
    default void drawContents(
            Font font,
            int x,
            int y,
            int width,
            int top,
            GuiGraphics guiGraphics) {
        // Calculate row values
        int numRows = getNumRows();
        int maxRows = Math.max(getMaxRows(), MIN_MAX_ROWS);
        int prevVisibleRows = maxRows / 2;
        int nextVisibleRows = (maxRows - 1) / 2;

        // Get selected stack values
        int selectedIndex = getSelectedIndex();
        ItemStack selectedStack;

        if (selectedIndex != -1) {
            selectedStack = getStacks().get(clampIndex(selectedIndex));
        } else {
            selectedStack = null;
            selectedIndex = 0;
        }

        int selectedRow = getRowForIndex(selectedIndex);

        // Clamp selected row to display the most rows possible when at the beginning or end of contents
        if (selectedRow < prevVisibleRows) {
            selectedRow = prevVisibleRows;
        } else if ((numRows - selectedRow - 1) < nextVisibleRows) {
            selectedRow = numRows - nextVisibleRows - 1;
        }

        // Create list to stores stacks that should be displayed
        List<ItemStack> displayedStacks = new ArrayList<>(maxRows * getRowWidth());
        int seedStart = 0;  // Count how many items come before displayedStacks to calculate seed
        int countPrev = 0;
        int countNext = 0;
        // Run through all stacks to either add their count or add them to be displayed
        for (int i = 0; i < getStacks().size(); i++) {
            int row = getRowForIndex(i);
            ItemStack stack = getStacks().get(i);

            if (row < selectedRow - prevVisibleRows) {
                seedStart++;
                countPrev += stack.getCount();
            } else if (row > selectedRow + nextVisibleRows) {
                countNext += stack.getCount();
            } else {
                displayedStacks.add(stack);
            }
        }

        // Get ends of display area
        int endX = x + getXMargin(width) + getTooltipWidth();
        int endY = y + Math.min(numRows, maxRows) * getSlotLength();

        // Calculate padding surrounding items
        int itemPadding = (getSlotLength() - 16) / 2;

        for (int i = 0; i < displayedStacks.size(); i++) {
            // iterate through list backwards to account for empty space at the beginning
            ItemStack stack = displayedStacks.get(displayedStacks.size() - 1 - i);
            int slotX = endX - (1 + i % getRowWidth()) * getSlotLength();
            int slotY = endY - (1 + i / getRowWidth()) * getSlotLength();

            if (i == displayedStacks.size() - 1 && countPrev > 0) {
                // If there are previous items, display the count instead
                countPrev += stack.getCount();
                guiGraphics.drawCenteredString(
                        font,
                        "+" + countPrev,
                        slotX + getSlotLength() / 2,
                        slotY + itemPadding + 6,
                        EXTRA_ITEMS_TEXT_COLOR);
            } else if (i == 0 && countNext > 0) {
                // If there are next items, display the count instead
                countNext += stack.getCount();
                guiGraphics.drawCenteredString(
                        font,
                        "+" + countNext,
                        slotX + getSlotLength() / 2,
                        slotY + itemPadding + 6, EXTRA_ITEMS_TEXT_COLOR);
            } else {
                // Render background. If stack is selected, render a different background
                if (stack == selectedStack) {
                    blit(
                            guiGraphics,
                            getSlotHighlightBackTexture(),
                            slotX, slotY,
                            getSlotLength(), getSlotLength(),
                            getSlotLength(), getSlotLength());
                } else {
                    blit(
                            guiGraphics,
                            getSlotBackgroundTexture(),
                            slotX, slotY,
                            getSlotLength(), getSlotLength(),
                            getSlotLength(), getSlotLength());
                }

                guiGraphics.renderItem(
                        stack,
                        slotX + itemPadding, slotY + itemPadding,
                        seedStart + displayedStacks.size() - 1 - i);
                guiGraphics.renderItemDecorations(
                        font,
                        stack,
                        slotX + itemPadding, slotY + itemPadding);

                if (stack == selectedStack) {
                    // Render with transparency and overlaying the rendered item
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(0, 0, 300);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    blit(
                            guiGraphics,
                            getSlotHighlightFrontTexture(),
                            slotX, slotY,
                            getSlotLength(), getSlotLength(),
                            getSlotLength(), getSlotLength());
                    RenderSystem.disableBlend();
                    guiGraphics.pose().popPose();
                }
            }
        }

        // Add selected tooltip at the top if there is a selected stack
        if (selectedStack != null) {
            Component name = selectedStack.getHoverName();
            int nameX = x + (width - font.width(name.getVisualOrderText())) / 2 - 12;
            int nameY = top - 15;
            guiGraphics.renderTooltip(
                    font,
                    name,
                    nameX, nameY);
        }
    }

    /**
     * Gets the row index for a given index of an item in the contents.
     * Does account for the initial empty spaces at the start of the first row.
     *
     * @param index the index of the item in the contents
     * @return the row index of the item
     */
    default int getRowForIndex(int index) {
        int emptySpaces = getRowWidth() - (1 + (getStacks().size() - 1) % getRowWidth());
        return (index + emptySpaces) / getRowWidth();
    }

    /**
     * Gets the number of rows that the total contents use.
     *
     * @return the total number of rows in the contents
     */
    default int getNumRows() {
        return 1 + (getStacks().size() - 1) / getRowWidth();
    }

    /**
     * Calculates the length in pixels that the progress bar should take up.
     *
     * @return the length in pixels of the progress bar
     */
    default int getProgressBarFill() {
        int progressBarWidth = getTooltipWidth() - 2;
        Fraction fillFraction = getFillFraction();
        return 1 + Mth.clamp(fillFraction.getNumerator() * progressBarWidth / fillFraction.getDenominator(), 0, progressBarWidth);
    }

    /**
     * Draws the fullness progress bar. Does not account for the padding surrounding the progress bar.
     *
     * @param font the font helper
     * @param x the x position to draw progress bar at
     * @param y the y position to draw progress bar at
     * @param width the width of the entire tooltip; can be more than the default width
     * @param guiGraphics the graphics helper
     */
    default void drawProgressBar(
            Font font,
            int x,
            int y,
            int width,
            GuiGraphics guiGraphics) {
        x += getXMargin(width);
        // Draw bar and border
        blit(
                guiGraphics,
                getProgressBarFillTexture(),
                x, y,
                getProgressBarFill(), getProgressBarHeight(),
                width, getProgressBarHeight());
        blit(
                guiGraphics, getProgressBarBorderTexture(),
                x, y,
                getTooltipWidth(), getProgressBarHeight(),
                width, getProgressBarHeight());

        // If there is a label, draw it centered on the progress bar
        Component progressBarLabel = getProgressBarLabel();
        if (progressBarLabel != null) {
            guiGraphics.drawCenteredString(
                    font,
                    progressBarLabel.getString(),
                    x + getTooltipWidth() / 2,
                    y + (getProgressBarHeight() - (font.lineHeight - 2)) / 2,
                    PROGRESS_BAR_TEXT_COLOR);
        }
    }

    /**
     * Calculates the height of the contents.
     *
     * @return the height in pixels that the contents take up
     */
    default int getContentsHeight() {
        return Math.min(getNumRows(), Math.max(getMaxRows(), MIN_MAX_ROWS)) * getSlotLength();
    }

    /**
     * Calculates how much x margin is needed to center the tooltip, given that the width
     * of the entire tooltip is greater than the width of the container tooltip.
     *
     * @param width the width of the entire tooltip; can be more than the default width
     * @return the horizontal margin needed to center the tooltip
     */
    default int getXMargin(int width) {
        return (width - getTooltipWidth()) / 2;
    }
    /**
     * Calculates the height that the given text takes up.
     *
     * @param text the text to get the height for
     * @param font the font helper
     * @return height in pixels that the given text takes up
     */
    default int getHeight(@Nullable Component text, Font font) {
        if (text == null) return 0;

        return font.split(text, getTooltipWidth()).size() * font.lineHeight;
    }

    /**
     * Blits a given texture location on the screen.
     *
     * @param guiGraphics the graphics helper
     * @param texture the location of the texture
     * @param x the x position to render the texture at
     * @param y the y position to render the texture at
     * @param width the width of the texture
     * @param height the height of the texture
     * @param maxWidth the max width of the texture
     * @param maxHeight the max height of the texture
     */
    default void blit(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x,
            int y,
            int width,
            int height,
            int maxWidth,
            int maxHeight) {
        guiGraphics.blit(
                texture,
                x, y,
                0, 0, 0,
                width, height,
                maxWidth, maxHeight);
    }
}
