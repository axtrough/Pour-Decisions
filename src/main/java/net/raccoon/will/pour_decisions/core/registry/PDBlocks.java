package net.raccoon.will.pour_decisions.core.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raccoon.will.pour_decisions.core.PourDecisions;

import java.util.function.Supplier;

public class PDBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PourDecisions.MODID);

    public static final DeferredBlock<Block> CHROMSTONE = registerBlock("chromstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));

    //---Other stuff---\\
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, boolean registerItem) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        if (registerItem) {
            PDItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        }
        return toReturn;
    }
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, true);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
