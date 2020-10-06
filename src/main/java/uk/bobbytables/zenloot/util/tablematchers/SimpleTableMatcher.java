package uk.bobbytables.zenloot.util.tablematchers;

import net.minecraft.util.ResourceLocation;

public class SimpleTableMatcher implements TableMatcher {
    private final ResourceLocation tableId;

    public SimpleTableMatcher(ResourceLocation tableId) {
        this.tableId = tableId;
    }

    @Override
    public boolean matches(ResourceLocation resourceLocation) {
        return resourceLocation.equals(tableId);
    }
}
