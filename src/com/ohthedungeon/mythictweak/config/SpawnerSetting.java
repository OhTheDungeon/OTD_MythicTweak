package com.ohthedungeon.mythictweak.config;

import com.ohthedungeon.mythictweak.com.google.gson.Gson;

/**
 *
 * @author shadow_wind
 */
public class SpawnerSetting {
    private int radius = 10;
    private int maxMobs = 1;
    private int mobLevel = 1;
    private int mobsPerSpawn = 1;
    private int cooldown = 5;
    private int warmup = 0;
    private int activationRange = 40;
    private int leashRange = 32;
    private boolean healOnLeash = false;
    private boolean resetThreatOnLeash = false;
    private boolean showFlames = false;
    private boolean breakable = true;
    
    @Override
    public String toString() {
        return (new Gson()).toJson(this);
    }
    
    public boolean getBreakable() {
        return this.breakable;
    }
    public SpawnerSetting setBreakable(boolean breakable) {
        this.breakable = breakable;
        return this;
    }
    
    public boolean getShowFlames() {
        return this.showFlames;
    }
    public SpawnerSetting setShowFlames(boolean showFlames) {
        this.showFlames = showFlames;
        return this;
    }
    
    public boolean getResetThreatOnLeash() {
        return this.resetThreatOnLeash;
    }
    public SpawnerSetting setResetThreatOnLeash(boolean resetThreatOnLeash) {
        this.resetThreatOnLeash = resetThreatOnLeash;
        return this;
    }
    
    public boolean getHealOnLeash() {
        return this.healOnLeash;
    }
    public SpawnerSetting setHealOnLeash(boolean healOnLeash) {
        this.healOnLeash = healOnLeash;
        return this;
    }
    
    public int getWarmup() {
        return this.warmup;
    }
    public SpawnerSetting setWarmup(int warmup) {
        this.warmup = warmup;
        return this;
    }
    
    public int getLeashRange() {
        return this.leashRange;
    }
    public SpawnerSetting setLeashRange(int leashRange) {
        this.leashRange = leashRange;
        return this;
    }
    
    public int getActivationRange() {
        return this.activationRange;
    }
    public SpawnerSetting setActivationRange(int activationRange) {
        this.activationRange = activationRange;
        return this;
    }
    
    public int getRadius() {
        return this.radius;
    }
    public SpawnerSetting setRadius(int radius) {
        this.radius = radius;
        return this;
    }
    
    public int getMaxMobs() {
        return this.maxMobs;
    }
    public SpawnerSetting setMaxMobs(int maxMobs) {
        this.maxMobs = maxMobs;
        return this;
    }
    
    public int getMobLevel() {
        return this.mobLevel;
    }
    public SpawnerSetting setMobLevel(int mobLevel) {
        this.mobLevel = mobLevel;
        return this;
    }
    
    public int getMobsPerSpawn() {
        return this.mobsPerSpawn;
    }
    public SpawnerSetting setMobsPerSpawn(int mobsPerSpawn) {
        this.mobsPerSpawn = mobsPerSpawn;
        return this;
    }
    
    public int getCoolDown() {
        return this.cooldown;
    }
    public SpawnerSetting setCooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }
}
