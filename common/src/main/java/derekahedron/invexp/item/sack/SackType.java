package derekahedron.invexp.item.sack;

import com.mojang.serialization.Codec;

/**
 * Defines a sack type. Has no data.
 */
public record SackType() {

    public static final Codec<SackType> CODEC =
            Codec.unit(SackType::new);
}
