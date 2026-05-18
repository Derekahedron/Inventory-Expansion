package derekahedron.invexp.client.gui.tooltip;

import derekahedron.invexp.item.sack.SackContentsReader;
import derekahedron.invexp.item.tooltip.SackTooltip;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ClientSackTooltip implements ClientTooltipComponent, ContainerItemTooltipComponent {
    // Assets
    public static final ResourceLocation SACK_PROGRESS_BAR_BORDER_TEXTURE =
            InvExpUtil.location("textures/gui/container/sack/sack_progressbar_border.png");
    public static final ResourceLocation SACK_PROGRESS_BAR_FILL_TEXTURE =
            InvExpUtil.location("textures/gui/container/sack/sack_progressbar_fill.png");
    public static final ResourceLocation SACK_PROGRESS_BAR_FULL_TEXTURE =
            InvExpUtil.location("textures/gui/container/sack/sack_progressbar_full.png");
    public static final ResourceLocation SACK_SLOT_HIGHLIGHT_BACK_TEXTURE =
            InvExpUtil.location("textures/gui/container/sack/slot_highlight_back.png");
    public static final ResourceLocation SACK_SLOT_HIGHLIGHT_FRONT_TEXTURE =
            InvExpUtil.location("textures/gui/container/sack/slot_highlight_front.png");
    public static final ResourceLocation SACK_SLOT_BACKGROUND_TEXTURE =
            InvExpUtil.location("textures/gui/container/sack/slot_background.png");
    // Text Components
    public static final Component SACK_FULL =
            Component.translatable("item.invexp.sack.full");
    public static final Component SACK_EMPTY =
            Component.translatable("item.invexp.sack.empty");
    public static final Component SACK_TOO_MANY_STACKS =
            Component.translatable("item.invexp.sack.too_many_stacks");
    public static final String SACK_PARTIAL =
            "item.invexp.sack.partial";
    public static final String SACK_EMPTY_DESCRIPTION =
            "item.invexp.sack.description.empty";
    public static final String SACK_TYPE_DESCRIPTION =
            "item.invexp.sack.description.type";
    public static final String SACK_TYPE_DESCRIPTION_CONJUNCTION =
            "item.invexp.sack.description.type.conjunction";
    public static final String SACK_TYPE_DESCRIPTION_MANY_CONJUNCTION =
            "item.invexp.sack.description.type.many.conjunction";
    public static final String SACK_TYPE_DESCRIPTION_MANY_LAST_CONJUNCTION =
            "item.invexp.sack.description.type.many.last_conjunction";

    public final SackContentsReader contents;
    @Nullable
    public final Component description;
    @Nullable
    private Font lastFont;

    public ClientSackTooltip(SackTooltip tooltip) {
        contents = tooltip.contents();
        description = tooltip.description().orElse(null);
    }

    @Override
    public void renderImage(
            Font textRenderer,
            int x, int y,
            GuiGraphics drawContext) {
        int width = getTooltipWidth();
        int top = y;  // Store top

        if (contents.isEmpty()) {
            if (lastFont != null) {
                // Draw empty description
                Component sackEmptyDescription = getSackEmptyDescription();
                drawContext.drawWordWrap(
                        textRenderer, sackEmptyDescription, x + getXMargin(width), y,
                        getTooltipWidth(), DESCRIPTION_TEXT_COLOR
                );
                y += getHeight(sackEmptyDescription, textRenderer);
            }
        } else {
            // Draw sack type description and contents
            if (lastFont != null) {
                Component sackTypeDescription = getSackTypeDescription();
                if (sackTypeDescription != null) {
                    drawContext.drawWordWrap(
                            textRenderer, sackTypeDescription, x + getXMargin(width), y,
                            getTooltipWidth(), DESCRIPTION_TEXT_COLOR
                    );
                    y += getHeight(sackTypeDescription, textRenderer);
                }
            }
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

        if (lastFont != null) {
            if (contents.isEmpty()) {
                height += getHeight(getSackEmptyDescription(), lastFont);
            } else {
                height += getHeight(getSackTypeDescription(), lastFont);
                height += getContentsHeight();
            }
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
     * Generates description for an empty sack.
     *
     * @return the empty sack description text
     */
    public Component getSackEmptyDescription() {
        return Component.translatable(SACK_EMPTY_DESCRIPTION, formatWeight(contents.getMaxWeight()));
    }

    /**
     * Generates description to display all the sack types that are used.
     *
     * @return the sack type description text
     */
    @Nullable
    public Component getSackTypeDescription() {
        Component description = null;

        // Get list of all names
        List<Component> names = contents.getSackTypes().stream()
                .map(ResourceLocation::tryParse)
                .filter(Objects::nonNull)
                .map(resource -> Component.translatable(Util.makeDescriptionId("sack_type", resource)))
                .map(Component.class::cast)
                .toList();

        for (int i = 0; i < names.size(); i++) {
            Component name = names.get(i);

            if (description == null) {
                // If first entry, set description to name
                description = name;
            } else if (i != names.size() - 1) {
                // If not last entry, add conjunction "original_list, new_entry"
                description = Component.translatable(SACK_TYPE_DESCRIPTION_MANY_CONJUNCTION, description, name);
            } else if (names.size() == 2) {
                // If last entry for list of size 2, add conjunction "original_list and new_entry"
                description = Component.translatable(SACK_TYPE_DESCRIPTION_CONJUNCTION, description, name);
            } else {
                // If last entry for list of size > 2, add conjunction "original_list, and new_entry"
                description = Component.translatable(SACK_TYPE_DESCRIPTION_MANY_LAST_CONJUNCTION, description, name);
            }
        }

        // If a description was made, prepend "Sack of " to names
        if (description != null) {
            description = Component.translatable(SACK_TYPE_DESCRIPTION, description);
        }
        return description;
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
            return SACK_PROGRESS_BAR_FULL_TEXTURE;
        } else {
            return SACK_PROGRESS_BAR_FILL_TEXTURE;
        }
    }

    @Override
    public ResourceLocation getProgressBarBorderTexture() {
        return SACK_PROGRESS_BAR_BORDER_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotBackgroundTexture() {
        return SACK_SLOT_BACKGROUND_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotHighlightBackTexture() {
        return SACK_SLOT_HIGHLIGHT_BACK_TEXTURE;
    }

    @Override
    public ResourceLocation getSlotHighlightFrontTexture() {
        return SACK_SLOT_HIGHLIGHT_FRONT_TEXTURE;
    }

    @Override
    public Component getProgressBarLabel() {
        if (contents.getTotalWeight().compareTo(contents.getMaxWeight()) >= 0) {
            return SACK_FULL;
        } else if (getStacks().size() >= contents.getMaxStacks()) {
            return SACK_TOO_MANY_STACKS;
        } else if (contents.isEmpty()) {
            return SACK_EMPTY;
        } else {
            return Component.translatable(SACK_PARTIAL,
                    formatWeight(contents.getTotalWeight()),
                    formatWeight(contents.getMaxWeight()));
        }
    }

    /**
     * Formats sack weight for display. The weight is divided by the default sack weight.
     * If the resulting value is a fraction, format to two decimal places.
     *
     * @param weight the sack weight to format
     * @return the formatted text representing the sack weight
     */
    public static MutableComponent formatWeight(Fraction weight) {
        weight = weight.multiplyBy(Fraction.getFraction(64));
        if (weight.getNumerator() % weight.getDenominator() == 0) {
            return Component.literal(String.valueOf(weight.intValue()));
        } else {
            return Component.literal(String.format("%.2f", weight.floatValue()));
        }
    }
}
