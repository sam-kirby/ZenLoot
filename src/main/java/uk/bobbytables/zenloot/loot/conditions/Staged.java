package uk.bobbytables.zenloot.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import uk.bobbytables.zenloot.Reference;

import java.util.Random;

public class Staged implements LootCondition {
    private final String stage;

    public Staged(String stage) {
        this.stage = stage;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        return context.getKillerPlayer() instanceof EntityPlayer && GameStageHelper.hasStage((EntityPlayer) context.getKillerPlayer(), this.stage);
    }

    public static class Serializer extends LootCondition.Serializer<Staged> {
        public Serializer() {
            super(new ResourceLocation(Reference.MOD_ID, "staged"), Staged.class);
        }

        @Override
        public void serialize(JsonObject json, Staged value, JsonSerializationContext context) {
            json.add("stage", context.serialize(value.stage));
        }

        @Override
        public Staged deserialize(JsonObject json, JsonDeserializationContext context) {
            return new Staged(JsonUtils.getString(json, "stage"));
        }
    }
}
