package com.kaleblangley.summon_bench.client.model.block;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class SummonBenchModel extends DefaultedBlockGeoModel<SummonBenchBlockEntity> {
    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "workbench/sawmill")
     * }</pre>
     *
     */
    public SummonBenchModel() {
        super(new ResourceLocation(SummonBench.MODID, "summon_bench"));
    }
}
