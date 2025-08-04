package com.kaleblangley.count_craft.init;

import com.kaleblangley.count_craft.CountCraft;
import com.kaleblangley.count_craft.kubejs.CountShapedRecipe;
import com.kaleblangley.count_craft.kubejs.CountShapelessRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class RecipeSerializerInit {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CountCraft.MODID);

    public static Supplier<RecipeSerializer<?>> SHAPED = REGISTER.register("shaped", CountShapedRecipe.Serializer::new);
    public static Supplier<RecipeSerializer<?>> SHAPELESS = REGISTER.register("shapeless", CountShapelessRecipe.Serializer::new);

    public static void init(IEventBus iEventBus) {
        REGISTER.register(iEventBus);
    }
}
