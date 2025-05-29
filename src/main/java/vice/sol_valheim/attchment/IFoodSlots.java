package vice.sol_valheim.attchment;

import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface IFoodSlots {
    List<FoodInstance> getSlots();
    boolean addFood(FoodData.FoodInfo info, Player player);
    void tick();
    void clear(Player player);
    boolean isFull();
}