package com.cometkaizo.game.item;

import java.util.Map;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: List of all item types and their types
 */
public class ItemTypes {

    public static final Map<String, Item.Reader> ITEMS = Map.ofEntries(
            Map.entry("chess_key", _ -> new ChessKeyItem()),
            Map.entry("entrance_key", _ -> new EntranceKeyItem()),
            Map.entry("feather", _ -> new FeatherItem()),
            Map.entry("heavy_heart", _ -> new HeavyHeartItem()),
            Map.entry("light_heart", _ -> new LightHeartItem()),
            Map.entry("machine_piece", _ -> new MachinePieceItem()),
            Map.entry("note", NoteItem::new),
            Map.entry("organ_key", _ -> new OrganKeyItem())
    );
}
