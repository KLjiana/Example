package vice.sol_valheim;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vice.sol_valheim.attchment.FoodDataAttachment;

import java.util.function.Supplier;

@Mod(SOLValheim.MOD_ID)
public class SOLValheim {
    public static final String MOD_ID = "sol_valheim";
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);
    public static final Supplier<AttachmentType<FoodDataAttachment>> FOOD_DATA = ATTACHMENT_TYPE.register("food_data", () -> AttachmentType.serializable(() -> new FoodDataAttachment()).build());

    public SOLValheim(IEventBus iEventBus, ModContainer modContainer) {
        ATTACHMENT_TYPE.register(iEventBus);
    }
}
