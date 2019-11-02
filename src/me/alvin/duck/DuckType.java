package me.alvin.duck;

import org.bukkit.entity.EntityType;

public enum DuckType {
    /**
     * The duck will attack players and villagers
     */
    HOSTILE(EntityType.ZOMBIE),

    /**
     * The duck is scared of players and will run away from them.
     */
    SCARED(EntityType.OCELOT),

    /**
     * The duck has no reaction against meeting a player, or any other mob.
     */
    NEUTRAL(EntityType.CHICKEN);

    private EntityType entityType;
    private double teleportDistance;

    DuckType(EntityType type) {
        this.entityType = type;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }
}
