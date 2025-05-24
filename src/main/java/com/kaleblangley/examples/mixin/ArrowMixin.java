package com.kaleblangley.examples.mixin;

import com.kaleblangley.examples.impl.TrailSaver;
import com.kaleblangley.examples.util.TrailRender;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

@Mixin(Arrow.class)
public class ArrowMixin extends AbstractArrow implements TrailSaver {
    @Unique
    public List<Vec3> trails = new LinkedList<>();
    private double distanceAccumulator = 0.0;

    protected ArrowMixin(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void savePos(CallbackInfo ci) {
        if (!this.level().isClientSide) return;
        distanceAccumulator = TrailRender.saveTrailPos(trails, this.position(), distanceAccumulator);
    }

    @Override
    public List<Vec3> getPastPositions() {
        return this.trails;
    }
}
