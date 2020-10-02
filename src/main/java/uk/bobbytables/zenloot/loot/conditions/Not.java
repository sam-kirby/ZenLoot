package uk.bobbytables.zenloot.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import uk.bobbytables.zenloot.Reference;

import java.util.Random;

public class Not implements LootCondition {
    private final LootCondition inner;

    public Not(LootCondition lootCondition) {
        this.inner = lootCondition;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        return !this.inner.testCondition(rand, context);
    }

    public static class Serializer extends LootCondition.Serializer<Not> {
        public Serializer() {
            super(new ResourceLocation(Reference.MOD_ID, "not"), Not.class);
        }

        @Override
        public void serialize(JsonObject json, Not value, JsonSerializationContext context) {
            json.add("inner", context.serialize(value.inner));
        }

        @Override
        public Not deserialize(JsonObject json, JsonDeserializationContext context) {
            return new Not(
                    context.deserialize(JsonUtils.getJsonObject(json, "inner"), LootCondition.class)
            );
        }
    }
}
