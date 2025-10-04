package com.kaleblangley.examples.client.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.List;

@SuppressWarnings({"unused"})
public interface ICustomBlockRenderer {
    /**
     * @param checkSides if {@code true}, only renders each side if {@link
     *                   #shouldRenderFace(
     *net.minecraft.world.level.block.state.BlockState,
     *                   net.minecraft.world.level.BlockGetter,
     *                   net.minecraft.core.BlockPos, net.minecraft.core.Direction,
     *                   net.minecraft.core.BlockPos)} returns {@code true}
     */
    default void tesselateBlock(RenderContent renderContent, BakedModel model, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType) {
        boolean flag = Minecraft.useAmbientOcclusion() && renderContent.state.getLightEmission(renderContent.level, renderContent.pos) == 0 && model.useAmbientOcclusion(renderContent.state, renderType);
        Vec3 vec3 = renderContent.state.getOffset(renderContent.level, renderContent.pos);
        renderContent.poseStack.translate(vec3.x, vec3.y, vec3.z);

        try {
            if (flag) {
                this.tesselateWithAO(renderContent, model, checkSides, random, seed, packedOverlay, modelData, renderType);
            } else {
                this.tesselateWithoutAO(renderContent, model, checkSides, random, seed, packedOverlay, modelData, renderType);
            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Block model being tesselated");
            CrashReportCategory.populateBlockDetails(crashreportcategory, renderContent.level, renderContent.pos, renderContent.state);
            crashreportcategory.setDetail("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * @param checkSides if {@code true}, only renders each side if {@link
     *                   #shouldRenderFace(
     *net.minecraft.world.level.block.state.BlockState,
     *                   net.minecraft.world.level.BlockGetter,
     *                   net.minecraft.core.BlockPos, net.minecraft.core.Direction,
     *                   net.minecraft.core.BlockPos)} returns {@code true}
     */
    default void tesselateWithAO(RenderContent renderContent, BakedModel model, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType) {
        float[] afloat = new float[ModelBlockRenderer.DIRECTIONS.length * 2];
        BitSet bitset = new BitSet(3);
        ModelBlockRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new ModelBlockRenderer.AmbientOcclusionFace();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = renderContent.pos.mutable();

        for (Direction direction : ModelBlockRenderer.DIRECTIONS) {
            random.setSeed(seed);
            List<BakedQuad> list = model.getQuads(renderContent.state, direction, random, modelData, renderType);
            if (!list.isEmpty()) {
                blockpos$mutableblockpos.setWithOffset(renderContent.pos, direction);
                if (!checkSides || shouldRenderFace(renderContent.state, renderContent.level, renderContent.pos, direction, blockpos$mutableblockpos)) {
                    this.renderModelFaceAO(renderContent, list, afloat, bitset, modelblockrenderer$ambientocclusionface, packedOverlay);
                }
            }
        }

        random.setSeed(seed);
        List<BakedQuad> list1 = model.getQuads(renderContent.state, null, random, modelData, renderType);
        if (!list1.isEmpty()) {
            this.renderModelFaceAO(renderContent, list1, afloat, bitset, modelblockrenderer$ambientocclusionface, packedOverlay);
        }
    }

    /**
     * @param checkSides if {@code true}, only renders each side if {@link
     *                   #shouldRenderFace(
     *net.minecraft.world.level.block.state.BlockState,
     *                   net.minecraft.world.level.BlockGetter,
     *                   net.minecraft.core.BlockPos, net.minecraft.core.Direction,
     *                   net.minecraft.core.BlockPos)} returns {@code true}
     */
    default void tesselateWithoutAO(RenderContent renderContent, BakedModel model, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType) {
        BitSet bitset = new BitSet(3);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = renderContent.pos.mutable();

        for (Direction direction : ModelBlockRenderer.DIRECTIONS) {
            random.setSeed(seed);
            List<BakedQuad> list = model.getQuads(renderContent.state, direction, random, modelData, renderType);
            if (!list.isEmpty()) {
                blockpos$mutableblockpos.setWithOffset(renderContent.pos, direction);
                if (!checkSides || shouldRenderFace(renderContent.state, renderContent.level, renderContent.pos, direction, blockpos$mutableblockpos)) {
                    int i = LevelRenderer.getLightColor(renderContent.level, renderContent.state, blockpos$mutableblockpos);
                    this.renderModelFaceFlat(renderContent, i, packedOverlay, false, list, bitset);
                }
            }
        }

        random.setSeed(seed);
        List<BakedQuad> list1 = model.getQuads(renderContent.state, null, random, modelData, renderType);
        if (!list1.isEmpty()) {
            this.renderModelFaceFlat(renderContent, -1, packedOverlay, true, list1, bitset);
        }
    }

    /**
     * @param shape      the array, of length 12, to store the shape bounds in
     * @param shapeFlags the bit set to store the shape flags in. The first bit will
     *                   be {@code true} if the face should be offset, and the second
     *                   if the face is less than a block in width and height.
     */
    default void renderModelFaceAO(RenderContent renderContent, List<BakedQuad> quads, float[] shape, BitSet shapeFlags, ModelBlockRenderer.AmbientOcclusionFace aoFace, int packedOverlay) {
        for (BakedQuad bakedquad : quads) {
            this.calculateShape(renderContent.modelBlockRenderer, renderContent.level, renderContent.state, renderContent.pos, bakedquad.getVertices(), bakedquad.getDirection(), shape, shapeFlags);
            if (!net.minecraftforge.client.ForgeHooksClient.calculateFaceWithoutAO(renderContent.level, renderContent.state, renderContent.pos, bakedquad, shapeFlags.get(0), aoFace.brightness, aoFace.lightmap))
                aoFace.calculate(renderContent.level, renderContent.state, renderContent.pos, bakedquad.getDirection(), shape, shapeFlags, bakedquad.isShade());
            this.putQuadData(renderContent, bakedquad, aoFace.brightness[0], aoFace.brightness[1], aoFace.brightness[2], aoFace.brightness[3], aoFace.lightmap[0], aoFace.lightmap[1], aoFace.lightmap[2], aoFace.lightmap[3], packedOverlay);
        }

    }

    default void putQuadData(RenderContent renderContent, BakedQuad quad, float brightness0, float brightness1, float brightness2, float brightness3, int lightmap0, int lightmap1, int lightmap2, int lightmap3, int packedOverlay) {
        float f;
        float f1;
        float f2;
        if (quad.isTinted()) {
            int i = renderContent.modelBlockRenderer.blockColors.getColor(renderContent.state, renderContent.level, renderContent.pos, quad.getTintIndex());
            f = (float) (i >> 16 & 255) / 255.0F;
            f1 = (float) (i >> 8 & 255) / 255.0F;
            f2 = (float) (i & 255) / 255.0F;
        } else {
            f = 1.0F;
            f1 = 1.0F;
            f2 = 1.0F;
        }

        renderContent.vertexConsumer.putBulkData(renderContent.poseStack.last(), quad, new float[]{brightness0, brightness1, brightness2, brightness3}, f, f1, f2, new int[]{lightmap0, lightmap1, lightmap2, lightmap3}, packedOverlay, true);
    }

    /**
     * Calculates the shape and corresponding flags for the specified {@code direction} and {@code vertices}, storing the resulting shape in the specified {@code shape} array and the shape flags in {@code shapeFlags}.
     *
     * @param shape      the array, of length 12, to store the shape bounds in, or {@code null} to only calculate shape flags
     * @param shapeFlags the bit set to store the shape flags in. The first bit will
     *                   be {@code true} if the face should be offset, and the second
     *                   if the face is less than a block in width and height.
     */
    default void calculateShape(ModelBlockRenderer modelBlockRenderer, BlockAndTintGetter level, BlockState state, BlockPos pos, int[] vertices, Direction direction, @Nullable float[] shape, BitSet shapeFlags) {
        float f = 32.0F;
        float f1 = 32.0F;
        float f2 = 32.0F;
        float f3 = -32.0F;
        float f4 = -32.0F;
        float f5 = -32.0F;

        for (int i = 0; i < 4; ++i) {
            float f6 = Float.intBitsToFloat(vertices[i * 8]);
            float f7 = Float.intBitsToFloat(vertices[i * 8 + 1]);
            float f8 = Float.intBitsToFloat(vertices[i * 8 + 2]);
            f = Math.min(f, f6);
            f1 = Math.min(f1, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.max(f3, f6);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
        }

        if (shape != null) {
            shape[Direction.WEST.get3DDataValue()] = f;
            shape[Direction.EAST.get3DDataValue()] = f3;
            shape[Direction.DOWN.get3DDataValue()] = f1;
            shape[Direction.UP.get3DDataValue()] = f4;
            shape[Direction.NORTH.get3DDataValue()] = f2;
            shape[Direction.SOUTH.get3DDataValue()] = f5;
            int j = ModelBlockRenderer.DIRECTIONS.length;
            shape[Direction.WEST.get3DDataValue() + j] = 1.0F - f;
            shape[Direction.EAST.get3DDataValue() + j] = 1.0F - f3;
            shape[Direction.DOWN.get3DDataValue() + j] = 1.0F - f1;
            shape[Direction.UP.get3DDataValue() + j] = 1.0F - f4;
            shape[Direction.NORTH.get3DDataValue() + j] = 1.0F - f2;
            shape[Direction.SOUTH.get3DDataValue() + j] = 1.0F - f5;
        }

        float f9 = 1.0E-4F;
        float f10 = 0.9999F;
        switch (direction) {
            case DOWN:
                shapeFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
                shapeFlags.set(0, f1 == f4 && (f1 < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case UP:
                shapeFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
                shapeFlags.set(0, f1 == f4 && (f4 > 0.9999F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case NORTH:
                shapeFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
                shapeFlags.set(0, f2 == f5 && (f2 < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case SOUTH:
                shapeFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
                shapeFlags.set(0, f2 == f5 && (f5 > 0.9999F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case WEST:
                shapeFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
                shapeFlags.set(0, f == f3 && (f < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case EAST:
                shapeFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
                shapeFlags.set(0, f == f3 && (f3 > 0.9999F || state.isCollisionShapeFullBlock(level, pos)));
        }

    }

    /**
     * @param repackLight {@code true} if packed light should be re-calculated
     * @param shapeFlags  the bit set to store the shape flags in. The first bit will
     *                    be {@code true} if the face should be offset, and the second
     *                    if the face is less than a block in width and height.
     */
    default void renderModelFaceFlat(RenderContent renderContent, int packedLight, int packedOverlay, boolean repackLight, List<BakedQuad> quads, BitSet shapeFlags) {
        for (BakedQuad bakedquad : quads) {
            if (repackLight) {
                this.calculateShape(renderContent.modelBlockRenderer, renderContent.level, renderContent.state, renderContent.pos, bakedquad.getVertices(), bakedquad.getDirection(), null, shapeFlags);
                BlockPos blockpos = shapeFlags.get(0) ? renderContent.pos.relative(bakedquad.getDirection()) : renderContent.pos;
                packedLight = LevelRenderer.getLightColor(renderContent.level, renderContent.state, blockpos);
            }

            float f = renderContent.level.getShade(bakedquad.getDirection(), bakedquad.isShade());
            this.putQuadData(renderContent, bakedquad, f, f, f, f, packedLight, packedLight, packedLight, packedLight, packedOverlay);
        }

    }

    default void renderModel(ModelBlockRenderer modelBlockRenderer, PoseStack.Pose pose, VertexConsumer consumer, @Nullable BlockState state, BakedModel model, float red, float green, float blue, int packedLight, int packedOverlay, ModelData modelData, RenderType renderType) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;

        for (Direction direction : ModelBlockRenderer.DIRECTIONS) {
            randomsource.setSeed(42L);
            renderQuadList(modelBlockRenderer, pose, consumer, red, green, blue, model.getQuads(state, direction, randomsource, modelData, renderType), packedLight, packedOverlay);
        }

        randomsource.setSeed(42L);
        renderQuadList(modelBlockRenderer, pose, consumer, red, green, blue, model.getQuads(state, null, randomsource, modelData, renderType), packedLight, packedOverlay);
    }

    default void renderQuadList(ModelBlockRenderer modelBlockRenderer, PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, List<BakedQuad> quads, int packedLight, int packedOverlay) {
        for (BakedQuad bakedquad : quads) {
            float f;
            float f1;
            float f2;
            if (bakedquad.isTinted()) {
                f = Mth.clamp(red, 0.0F, 1.0F);
                f1 = Mth.clamp(green, 0.0F, 1.0F);
                f2 = Mth.clamp(blue, 0.0F, 1.0F);
            } else {
                f = 1.0F;
                f1 = 1.0F;
                f2 = 1.0F;
            }

            consumer.putBulkData(pose, bakedquad, f, f1, f2, packedLight, packedOverlay);
        }
    }

    default boolean shouldRenderFace(BlockState state, BlockGetter level, BlockPos offset, Direction face, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        if (state.skipRendering(blockstate, face)) {
            return false;
        } else if (state.supportsExternalFaceHiding() && blockstate.hidesNeighborFace(level, pos, state, face.getOpposite())) {
            return false;
        } else if (blockstate.canOcclude()) {
            Block.BlockStatePairKey block$blockstatepairkey = new Block.BlockStatePairKey(state, blockstate, face);
            Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> object2bytelinkedopenhashmap = Block.OCCLUSION_CACHE.get();
            byte b0 = object2bytelinkedopenhashmap.getAndMoveToFirst(block$blockstatepairkey);
            if (b0 != 127) {
                return b0 != 0;
            } else {
                VoxelShape voxelshape = state.getFaceOcclusionShape(level, offset, face);
                if (voxelshape.isEmpty()) {
                    return true;
                } else {
                    VoxelShape voxelshape1 = blockstate.getFaceOcclusionShape(level, pos, face.getOpposite());
                    boolean flag = Shapes.joinIsNotEmpty(voxelshape, voxelshape1, BooleanOp.ONLY_FIRST);
                    if (object2bytelinkedopenhashmap.size() == 2048) {
                        object2bytelinkedopenhashmap.removeLastByte();
                    }

                    object2bytelinkedopenhashmap.putAndMoveToFirst(block$blockstatepairkey, (byte) (flag ? 1 : 0));
                    return flag;
                }
            }
        } else {
            return true;
        }
    }

    default UpdateLevel getUpdateLevel() {
        return UpdateLevel.DEFAULT;
    }

    /**
     * Only call when {@link #getUpdateLevel()} is EVERY_TIME
     */
    default void renderBlock(RenderContent renderContent, BakedModel model, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType, LevelRenderer levelRenderer, Matrix4f projectionMatrix, float partialTick, int renderTick, Camera camera, Frustum frustum) {
        tesselateBlock(renderContent, model, checkSides, random, seed, packedOverlay, modelData, renderType);
    }

    enum UpdateLevel {
        EVERY_TIME,
        DEFAULT
    }

    class RenderContent {
        public final ModelBlockRenderer modelBlockRenderer;
        public final BlockAndTintGetter level;
        public final BlockState state;
        public final BlockPos pos;
        public final PoseStack poseStack;
        public final VertexConsumer vertexConsumer;

        public RenderContent(ModelBlockRenderer modelBlockRenderer, BlockAndTintGetter level, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer vertexConsumer) {
            this.modelBlockRenderer = modelBlockRenderer;
            this.level = level;
            this.state = state;
            this.pos = pos;
            this.poseStack = poseStack;
            this.vertexConsumer = vertexConsumer;
        }
    }
}
