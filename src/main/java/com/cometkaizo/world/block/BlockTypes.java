package com.cometkaizo.world.block;

import java.util.Map;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: List of all blocks and their type-codes
 */
public class BlockTypes {

    public static final Map<String, Block.Reader> BLOCKS = Map.of(
            ".", AirBlock::new, // explicit air block (for bottom-left anchor point on Google Sheets)
            "", AirBlock::new,
            "g", GroundBlock::new,
            "w", WallBlock::new,
            "counter", CounterBlock::new,
            "b", BarrierBlock::new
    );

}
