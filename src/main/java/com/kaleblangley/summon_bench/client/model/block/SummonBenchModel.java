package com.kaleblangley.summon_bench.client.model.block;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SummonBenchModel extends GeoModel<SummonBenchBlockEntity> {
    public static final ResourceLocation MODEL = new ResourceLocation(SummonBench.MODID, "geo/block/summon_bench.geo.json");
    public static final ResourceLocation TEXTURE = new ResourceLocation(SummonBench.MODID, "textures/block/summon_bench.png");
    public static final ResourceLocation ANIMATION = new ResourceLocation(SummonBench.MODID, "animations/block/summon_bench.animation.json");

    @Override
    public ResourceLocation getModelResource(SummonBenchBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SummonBenchBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SummonBenchBlockEntity animatable) {
        return ANIMATION;
    }
}
