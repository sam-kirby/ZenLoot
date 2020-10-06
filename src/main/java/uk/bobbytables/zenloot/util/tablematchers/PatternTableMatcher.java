package uk.bobbytables.zenloot.util.tablematchers;

import net.minecraft.util.ResourceLocation;

public class PatternTableMatcher implements TableMatcher {
    private final String pattern;

    public PatternTableMatcher(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(ResourceLocation resourceLocation) {
        return resourceLocation.toString().matches(pattern);
    }
}
