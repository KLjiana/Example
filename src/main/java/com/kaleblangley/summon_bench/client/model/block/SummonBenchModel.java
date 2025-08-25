package com.kaleblangley.summon_bench.client.model.block;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SummonBenchModel extends GeoModel<SummonBenchBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SummonBenchBlockEntity animatable) {
        return new ResourceLocation(SummonBench.MODID, "geo/summon_bench.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SummonBenchBlockEntity animatable) {
        return new ResourceLocation(SummonBench.MODID, "textures/block/summon_bench.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SummonBenchBlockEntity animatable) {
        return new ResourceLocation(SummonBench.MODID, "animations/summon_bench.animation.json");
    }
}
