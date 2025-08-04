package com.kaleblangley.count_craft.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.special.ShapelessKubeJSRecipe;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public interface ICountSerializer {
    default Int2IntOpenHashMap serializer2Map(List<Integer> indexs, List<Integer> counts) {
        Int2IntOpenHashMap index2count = new Int2IntOpenHashMap();
        IntStream.range(0, indexs.size())
                .forEach(i -> index2count.put(
                        indexs.get(i).intValue(),
                        counts.get(Math.min(i, counts.size() - 1)).intValue()
                ));
        return index2count;
    }

    default void tooNetwork(FriendlyByteBuf buf, Recipe<?> recipe) {
        if (recipe instanceof ICountRecipe countRecipe) {
            IntArrayList indexList = new IntArrayList(countRecipe.getIndex2count().keySet());
            IntArrayList countList = new IntArrayList(countRecipe.getIndex2count().values());
            buf.writeIntIdList(indexList);
            buf.writeIntIdList(countList);
        }
    }

    default Int2IntOpenHashMap fromNetwork(FriendlyByteBuf buf) {
        return serializer2Map(buf.readIntIdList(), buf.readIntIdList());
    }

    default Int2IntOpenHashMap fromJson(JsonObject jsonObject) {
        JsonArray indexJsonArray = jsonObject.getAsJsonArray("indexs");
        JsonArray countJsonElements = jsonObject.getAsJsonArray("counts");

        List<Integer> indexs = StreamSupport.stream(indexJsonArray.spliterator(), false)
                .map(JsonElement::getAsInt)
                .toList();
        List<Integer> counts = StreamSupport.stream(countJsonElements.spliterator(), false)
                .map(JsonElement::getAsInt)
                .toList();
        return serializer2Map(indexs, counts);
    }
}
