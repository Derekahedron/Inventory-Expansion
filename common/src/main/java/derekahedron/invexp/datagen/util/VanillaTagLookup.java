package derekahedron.invexp.datagen.util;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

import java.util.Optional;

/**
 * Tag lookup provider that always approves vanilla tags.
 * Used to create tags that start with minecraft: so we don't have to add them optionally.
 *
 * @param <T> the type of content being tagged
 */
public class VanillaTagLookup<T> implements TagsProvider.TagLookup<T> {

    @Override
    public Optional<TagBuilder> apply(TagKey<T> tTagKey) {
        return Optional.empty();
    }

    @Override
    public boolean contains(TagKey<T> key) {
        return key.location().getNamespace().equals("minecraft");
    }
}
