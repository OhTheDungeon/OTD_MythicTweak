package com.ohthedungeon.mythictweak.util;

import com.ohthedungeon.mythictweak.config.SpawnerConfig;
import com.ohthedungeon.mythictweak.config.SpawnerSetting;
import forge_sandbox.greymerk.roguelike.worldgen.Coord;
import forge_sandbox.greymerk.roguelike.worldgen.IWorldEditor;
import forge_sandbox.greymerk.roguelike.worldgen.MetaBlock;
import forge_sandbox.greymerk.roguelike.worldgen.spawners.Spawnable;
import forge_sandbox.greymerk.roguelike.worldgen.spawners.Spawner;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderInt;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import io.lumine.xikage.mythicmobs.spawning.spawners.SpawnerManager;
import io.lumine.xikage.mythicmobs.utils.numbers.RandomInt;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import shadow_lib.api.SpawnerApi;

/**
 *
 * @author shadow_wind
 */
public class MythicSpawnerHandler extends SpawnerApi.SpawnerHandler {
    @Override
    public void generate(Spawnable s, Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level) {
        if(level < 0 || level > 4) {
            return;
        }
        
        boolean b = SpawnerConfig.getRoguelikeStatus().get(level);
        if(!b) {
            s.generate_later_orign(pos, editor, rand, cursor, level);
            return;
        }
        
        String mob = Spawner.getRawName(s.type);
        
        List<String> mobs = SpawnerConfig.getRoguelikeMobList().get(level).get(mob);
        if(mobs == null || mobs.isEmpty()) {
            s.generate_later_orign(pos, editor, rand, cursor, level);
            return;
        }
        
        SpawnerSetting ex = SpawnerConfig.getRoguelikeSetting().get(level);
        if(ex == null) {
            editor.setBlock(pos, new MetaBlock(Material.SPAWNER), true, true);
            s.generate_later_orign(pos, editor, rand, cursor, level);
            return;
        }
        
        String choose = mobs.get(rand.nextInt(mobs.size()));
        SpawnerManager sm = MythicMobs.inst().getSpawnerManager();
        
        editor.getBlock(pos); //flush
        editor.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()).setType(Material.OAK_WOOD, false);
        
        String spawnerName = "otd_mt_" + editor.getWorldName() + 
                "_x" + pos.getX() + 
                "_y" + pos.getY() + 
                "_z" + pos.getZ() + "_" + choose;
        spawnerName = spawnerName.replace('-', 'n');
        
        MythicSpawner spawner = sm.createSpawner(spawnerName, new Location(editor.getWorld(), pos.getX(), pos.getY(), pos.getZ()), choose);
        if(spawner == null) {
            return;
        }
        
                
        spawner.setSpawnRadius(ex.getRadius());
        spawner.setMaxMobs(new PlaceholderInt(Integer.toString(ex.getMaxMobs())));
        spawner.setMobLevel(new RandomInt(ex.getMobLevel()));
        spawner.setMobsPerSpawn(ex.getMobsPerSpawn());
        spawner.setCooldownSeconds(ex.getCoolDown());
        spawner.setWarmupSeconds(ex.getWarmup());
        spawner.setActivationRange(ex.getActivationRange());
        spawner.setLeashRange(ex.getLeashRange());
        spawner.setHealOnLeash(ex.getHealOnLeash());
        spawner.setLeashResetsThreat(ex.getResetThreatOnLeash());
        spawner.setShowFlames(ex.getShowFlames());
        spawner.setBreakable(ex.getBreakable());
    }
}
