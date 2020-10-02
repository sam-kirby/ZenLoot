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

public class Or implements LootCondition {
    private final LootCondition lootCondition;
    private final LootCondition lootCondition1;

    public Or(LootCondition lootCondition, LootCondition lootCondition1) {
        this.lootCondition = lootCondition;
        this.lootCondition1 = lootCondition1;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        return lootCondition.testCondition(rand, context) || lootCondition1.testCondition(rand, context);
    }

    public static class Serializer extends LootCondition.Serializer<Or> {
        public Serializer() {
            super(new ResourceLocation(Reference.MOD_ID, "or"), Or.class);
        }

        @Override
        public void serialize(JsonObject json, Or value, JsonSerializationContext context) {
            json.add("loot_condition", context.serialize(value.lootCondition));
            json.add("loot_condition1", context.serialize(value.lootCondition1));
        }

        @Override
        public Or deserialize(JsonObject json, JsonDeserializationContext context) {
            return new Or(
                    context.deserialize(JsonUtils.getJsonObject(json, "loot_condition"), LootCondition.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "loot_condition1"), LootCondition.class)
            );
        }
    }
}
