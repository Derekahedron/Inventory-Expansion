package derekahedron.invexp.client.gui.tooltip;

import derekahedron.invexp.item.quiver.QuiverContentsWriter;
import derekahedron.invexp.item.tooltip.QuiverTooltip;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Special tooltip for displaying quivers and their contents.
 */
public class ClientQuiverTooltip implements ClientTooltipComponent, ContainerItemTooltipComponent {
    // Assets
    public static final ResourceLocation QUIVER_PROGRESS_BAR_BORDER_TEXTURE =
            InvExpUtil.location("textures/gui/container/quiver/quiver_progressbar_border.png");
    public static final ResourceLocation QUIVER_PROGRESS_BAR_FILL_TEXTURE =
            InvExpUtil.location("textures/gui/container/quiver/quiver_progressbar_fill.png");
    public static final ResourceLocation QUIVER_PROGRESS_BAR_FULL_TEXTURE =
            InvExpUtil.location("textures/gui/container/quiver/quiver_progressbar_full.png");
    public static final ResourceLocation QUIVER_SLOT_HIGHLIGHT_BACK_TEXTURE =
            InvExpUtil.location("textures/gui/container/quiver/slot_highlight_back.png");
    public static final ResourceLocation QUIVER_SLOT_HIGHLIGHT_FRONT_TEXTURE =
            InvExpUtil.location("textures/gui/container/quiver/slot_highlight_front.png");
    public static final ResourceLocation QUIVER_SLOT_BACKGROUND_TEXTURE =
            InvExpUtil.location("textures/gui/container/quiver/slot_background.png");
    // Text Components
    public static final Component QUIVER_FULL =
            Component.translatable("item.invexp.quiver.full");
    public static final Component QUIVER_EMPTY =
            Component.translatable("item.invexp.quiver.empty");
    public static final Component QUIVER_TOO_MANY_STACKS =
            Component.translatable("item.invexp.quiver.too_many_stacks");
    public static final Component QUIVER_EMPTY_DESCRIPTION =
            Component.translatable("item.invexp.quiver.description.empty");
    public static final String QUIVER_EMPTY_DESCRIPTION_PLURAL =
            "item.invexp.quiver.description.empty.plural";

    public final QuiverContentsWriter contents;
    @Nullable
    public final Component description;
    @Nullable
    private Font lastFont;

    public ClientQuiverTooltip(QuiverTooltip tooltip) {
        contents = tooltip.contents();
        description = tooltip.description().orElse(null);
    }

    @Override
    public void renderImage(
            Font textRenderer,
            int x, int y,
            GuiGraphics drawContext) {
        int width = getTooltipWidth();
        int top = y;

        if (contents.isEmpty()) {
            if (lastFont != null) {
                // Draw empty description
                Component quiverEmptyDescription = getQuiverEmptyDescription();
                drawContext.drawWordWrap(
                        textRenderer, quiverEmptyDescription, x + getXMargin(width), y,
                        getTooltipWidth(), DESCRIPTION_TEXT_COLOR
                );
                y += getHeight(quiverEmptyDescription, textRenderer);
            }
        } else {
            // Draw contents
            drawContents(textRenderer, x, y, width, top, drawContext);
            y += getContentsHeight();
        }
        // Finally draw progress bar
        y += getProgressBarPadding();
        drawProgressBar(textRenderer, x, y, width, drawContext);
        y += getProgressBarHeight();
        y += getProgressBarPadding();
        if (description != null) {
            drawContext.drawWordWrap(
                    textRenderer, description, x + getXMargin(width), y,
                    getTooltipWidth(), DESCRIPTION_TEXT_COLOR
            );
            // y += getDescriptionPadding();
        }
    }

    @Override
    public int getWidth(@Nullable Font textRenderer) {
        lastFont = textRenderer;
        return getTooltipWidth();
    }

    @Override
    public int getHeight() {
        int height = 0;

        if (contents.isEmpty()) {
            if (lastFont != null) {
                height += getHeight(getQuiverEmptyDescription(), lastFont);
            }
        } else {
            height += getContentsHeight();
        }

        height += getProgressBarPadding();
        height += getProgressBarHeight();
        height += getProgressBarPadding();

        if (description != null) {
            if (lastFont != null) {
                height += getHeight(description, lastFont);
                height += getDescriptionPadding();
            }
        }
        return height;
    }

    /**
     * Generates description for an empty quiver.
     *
     * @return the empty quiver description text
     */
    public Component getQuiverEmptyDescription() {
        if (contents.getMaxWeight().compareTo(Fraction.ONE) == 0) {
            return QUIVER_EMPTY_DESCRIPTION;
        } else {
            return Component.translatable(
                    QUIVER_EMPTY_DESCRIPTION_PLURAL,
                    TOOLTIP_FRACTION_FORMAT.format(contents.getMaxWeight()));
        }
    }

    @Override
    public List<ItemStack> getStacks() {
        return contents.getStacks();
    }

    @Override
    public int getSelectedIndex() {
        return contents.getSelectedIndex();
    }

    @Override
    public int clampIndex(int index) {
        return contents.clampIndex(index);
    }

    @Override
    public Fraction getFillFraction() {
        return contents.getFillFraction();
    }

    @Override
    public ResourceLocation getProgressBarFillTexture() {
        if (contents.isFull()) {
            return QUIVER_PROGRESS_BAR_FULL_TEXTURE;
        } else {
            return QUIVER_PROGRESS_BAR_FILL_TEXTURE;
        }
    }

    @Override
    public ResourceLocation getProgressBarBorderTexture() {
        return QUIVER_PROGRESS_BAR_BORDER_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotBackgroundTexture() {
        return QUIVER_SLOT_BACKGROUND_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotHighlightBackTexture() {
        return QUIVER_SLOT_HIGHLIGHT_BACK_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotHighlightFrontTexture() {
        return QUIVER_SLOT_HIGHLIGHT_FRONT_TEXTURE;
    }

    @Override
    @Nullable
    public Component getProgressBarLabel() {
        if (contents.getTotalWeight().compareTo(contents.getMaxWeight()) >= 0) {
            return QUIVER_FULL;
        } else if (getStacks().size() >= contents.getMaxStacks()) {
            return QUIVER_TOO_MANY_STACKS;
        } else if (contents.isEmpty()) {
            return QUIVER_EMPTY;
        } else {
            return null;
        }
    }
}
