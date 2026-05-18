package derekahedron.invexp.client.gui.tooltip;

import derekahedron.invexp.item.bundle.BundleContentsWriter;
import derekahedron.invexp.item.tooltip.BetterBundleTooltip;
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

public class BetterClientBundleTooltip implements ClientTooltipComponent, ContainerItemTooltipComponent {
    // Assets
    public static final ResourceLocation BUNDLE_PROGRESS_BAR_BORDER_TEXTURE =
            InvExpUtil.location("textures/gui/container/bundle/bundle_progressbar_border.png");
    public static final ResourceLocation BUNDLE_PROGRESS_BAR_FILL_TEXTURE =
            InvExpUtil.location("textures/gui/container/bundle/bundle_progressbar_fill.png");
    public static final ResourceLocation BUNDLE_PROGRESS_BAR_FULL_TEXTURE =
            InvExpUtil.location("textures/gui/container/bundle/bundle_progressbar_full.png");
    public static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE =
            InvExpUtil.location("textures/gui/container/bundle/slot_highlight_back.png");
    public static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE =
            InvExpUtil.location("textures/gui/container/bundle/slot_highlight_front.png");
    public static final ResourceLocation BUNDLE_SLOT_BACKGROUND_TEXTURE =
            InvExpUtil.location("textures/gui/container/bundle/slot_background.png");
    // Text Components
    public static final Component BUNDLE_FULL =
            Component.translatable("item.invexp.bundle.full");
    public static final Component BUNDLE_EMPTY =
            Component.translatable("item.invexp.bundle.empty");
    public static final Component BUNDLE_TOO_MANY_STACKS =
            Component.translatable("item.invexp.bundle.too_many_stacks");
    public static final Component BUNDLE_EMPTY_DESCRIPTION =
            Component.translatable("item.invexp.bundle.description.empty");
    public static final String BUNDLE_EMPTY_DESCRIPTION_PLURAL =
            "item.invexp.bundle.description.empty.plural";

    public final BundleContentsWriter contents;
    @Nullable
    public final Component description;
    @Nullable
    private Font lastFont;

    public BetterClientBundleTooltip(BetterBundleTooltip tooltip) {
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
                Component bundleEmptyDescription = getBundleEmptyDescription();
                drawContext.drawWordWrap(
                        textRenderer, bundleEmptyDescription, x + getXMargin(width), y,
                        getTooltipWidth(), DESCRIPTION_TEXT_COLOR
                );
                y += getHeight(bundleEmptyDescription, textRenderer);
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
                height += getHeight(getBundleEmptyDescription(), lastFont);
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
     * Generates description for an empty bundle.
     *
     * @return the empty bundle description text
     */
    public Component getBundleEmptyDescription() {
        if (contents.getMaxWeight().compareTo(Fraction.ONE) == 0) {
            return BUNDLE_EMPTY_DESCRIPTION;
        } else {
            return Component.translatable(
                    BUNDLE_EMPTY_DESCRIPTION_PLURAL,
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
            return BUNDLE_PROGRESS_BAR_FULL_TEXTURE;
        } else {
            return BUNDLE_PROGRESS_BAR_FILL_TEXTURE;
        }
    }

    @Override
    public ResourceLocation getProgressBarBorderTexture() {
        return BUNDLE_PROGRESS_BAR_BORDER_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotBackgroundTexture() {
        return BUNDLE_SLOT_BACKGROUND_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotHighlightBackTexture() {
        return BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotHighlightFrontTexture() {
        return BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE;
    }

    @Override
    @Nullable
    public Component getProgressBarLabel() {
        if (contents.getTotalWeight().compareTo(contents.getMaxWeight()) >= 0) {
            return BUNDLE_FULL;
        } else if (getStacks().size() >= contents.getMaxStacks()) {
            return BUNDLE_TOO_MANY_STACKS;
        } else if (contents.isEmpty()) {
            return BUNDLE_EMPTY;
        } else {
            return null;
        }
    }
}
