package com.kaleblangley.examples.mixin;

import com.kaleblangley.examples.impl.TrailSaver;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
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
    @Unique
    private Vec3 posO = Vec3.ZERO;
    protected ArrowMixin(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void savePos(CallbackInfo ci) {
        if (this.level().isClientSide) {
            if (trails.isEmpty()) {
                trails.add(this.position());
            }
            if (this.life % 4 == 0) {
                if (trails.get(trails.size() - 1).distanceTo(this.position()) > 1) {
                    trails.add(this.position());
                }
            }
            if (trails.size() > 10 || posO == this.position()) {
                trails.remove(0);
            }
            posO = this.position();
        }
    }


    @Override
    public List<Vec3> getPastPositions() {
        return this.trails;
    }
}
