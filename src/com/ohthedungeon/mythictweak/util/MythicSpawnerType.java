package com.ohthedungeon.mythictweak.util;

public enum MythicSpawnerType {
    
    CREEPER("creeper"),
    CAVE_SPIDER("cave_spider"),
    SPIDER("spider"),
    SKELETON("skeleton"),
    ZOMBIE("zombie"),
    SILVERFISH("silverfish"),
    ENDERMAN("enderman"),
    WITCH("witch"),
    WITCHER("wither"),
    BAT("bat"),
    MAGMA_CUBE("magma_cube"),
    BLAZE("blaze"),
    SLIME("slime"),
    ZOMBIE_PIGMAN("zombie_pigman");
    
    private String name;
    MythicSpawnerType(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}

