package Enumerations;

import Models.Entity;

import java.util.EnumSet;

public enum EntityState {
    IDLE,
    MOVING,
    DEAD,
    STAGGERED, // controls disabled
    CASTINGINIT, // controls disabled, can be interrupted
    CASTING,
    DAMAGED, // immune, don't forget to toggle off!
    DESTROYED; // release instance

    // This state prevents the entity from changing it's state voluntarily
    public static final EnumSet<EntityState> BUSY = EnumSet.of(
            DEAD,
            STAGGERED,
            CASTINGINIT,
            DESTROYED);

    // This state is the inverse of BUSY, it means the entity can change it's state at will
    public static final EnumSet<EntityState> READY = EnumSet.complementOf(BUSY);
}
