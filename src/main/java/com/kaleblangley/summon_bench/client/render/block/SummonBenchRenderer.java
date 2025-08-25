package com.kaleblangley.summon_bench.client.render.block;

import com.kaleblangley.summon_bench.client.model.block.SummonBenchModel;
import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SummonBenchRenderer extends GeoBlockRenderer<SummonBenchBlockEntity> {
    public SummonBenchRenderer(BlockEntityRendererProvider.Context context) {
        super(new SummonBenchModel());
    }
}
