package com.cometkaizo.world.entity;

import java.util.Map;

public class EntityTypes {

    public static final Map<String, Entity.Reader> ENTITIES = Map.of(
            "b", Button::new,
            "letter", Letter::new,
            "p", Painting::new,
            "z", CombinationPuzzleBox::new,
            "d", Door::new,
            "k", Key::new
    );

}
