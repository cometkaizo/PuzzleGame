package com.cometkaizo.world.entity;

import java.util.Map;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: List of all entity types and their type-codes
 */
public class EntityTypes {

    public static final Map<String, Entity.Reader> ENTITIES = Map.ofEntries(
            Map.entry("letter", Letter::new),
            Map.entry("sign", Sign::new),
            Map.entry("mayan calendar", MayanCalendar::new),
            Map.entry("anubis", AnubisSculpture::new),
            Map.entry("cupid and psyche", CupidAndPsycheSculpture::new),
            Map.entry("the thinker", TheThinkerSculpture::new),
            Map.entry("statue of ra", RaSculpture::new),
            Map.entry("statue of david", DavidSculpture::new),
            Map.entry("mephistopheles and margaretta", DoubleSidedSculpture::new),
            Map.entry("mirror", Mirror::new),
            Map.entry("rosetta stone", RosettaStone::new),
            Map.entry("antikythera", Antikythera::new),
            Map.entry("morse code", MorseCodeArtifact::new),
            Map.entry("hope diamond", HopeDiamond::new),
            Map.entry("organ", Organ::new),
            Map.entry("chess", Chess::new),
            Map.entry("pacman", Pacman::new),
            Map.entry("typewriter", Typewriter::new),
            Map.entry("p", Painting::new),
            Map.entry("z", CombinationPuzzleBox::new),
            Map.entry("d", Door::new),
            Map.entry("k", Key::new)
    );

}
