package com.kaleblangley.summon_bench.common.item;

import com.kaleblangley.summon_bench.client.model.block.SummonBenchModel;
import com.kaleblangley.summon_bench.common.init.BlockInit;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class SummonBenchItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

    public SummonBenchItem() {
        super(BlockInit.SUMMON_BENCH.get(), new Item.Properties().fireResistant());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoItemRenderer<SummonBenchItem> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    this.renderer = new GeoItemRenderer<>(new GeoModel<>() {
                        @Override
                        public ResourceLocation getModelResource(SummonBenchItem animatable) {
                            return SummonBenchModel.MODEL;
                        }

                        @Override
                        public ResourceLocation getTextureResource(SummonBenchItem animatable) {
                            return SummonBenchModel.TEXTURE;
                        }

                        @Override
                        public ResourceLocation getAnimationResource(SummonBenchItem animatable) {
                            return SummonBenchModel.ANIMATION;
                        }
                    });
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }
}
