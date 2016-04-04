package Factories;

import Interfaces.IEnemyProducible;
import Interfaces.IPlayerProducible;

public class CreatureFactory implements IEnemyProducible, IPlayerProducible {
    // TODO: Discuss necessity of a creature factory, since all creatures are the same class and the player is unique. A combined entity factory makes more sense?
}
