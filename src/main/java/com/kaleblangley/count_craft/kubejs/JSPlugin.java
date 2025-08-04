package com.kaleblangley.count_craft.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;


public class JSPlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(new ResourceLocation("count_craft:shapeless"), CountShapelessRecipeSchema.SCHEMA);
        event.register(new ResourceLocation("count_craft:shaped"), CountShapedRecipeSchema.SCHEMA);
        event.register(new ResourceLocation("count_craft:smithing_transform"), CountSmithingTransformRecipeSchema.SCHEMA);
    }
}
