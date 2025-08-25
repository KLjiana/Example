package com.kaleblangley.summon_bench.client.render.block;

import com.kaleblangley.summon_bench.client.model.block.SummonBenchModel;
import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SummonBenchRenderer extends GeoBlockRenderer<SummonBenchBlockEntity> {
    public SummonBenchRenderer() {
        super(new SummonBenchModel());
    }
}
