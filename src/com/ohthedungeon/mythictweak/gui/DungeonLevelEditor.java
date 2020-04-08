package com.ohthedungeon.mythictweak.gui;

import com.ohthedungeon.mythictweak.config.SpawnerConfig;
import com.ohthedungeon.mythictweak.config.SpawnerSetting;
import com.ohthedungeon.mythictweak.util.MythicSpawnerType;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author shadow_wind
 */
public class DungeonLevelEditor extends javax.swing.JFrame {
    
    private final static String RADIUS = 
            "This is the radius around the spawner at which the mob can spawn. setting to"
            +" 0 will cause the mob to spawn exactly on its spawner. Setting it to 5 will "
            +"allow the mob to spawn anywhere within a 5 block radius of the spawner.";
    private final static String MAXMOBS = 
            "This is the max number of mobs that can be spawned and existing in the world "
            +"for this spawner. Should be set equal to or greater than the mobsperspawn settings.";
    private final static String MOBLEVEL = 
            "This is the level of the mob that should spawn from this spawner. Mob must "
            +"have level configuration for this to work. Can only be a single level.";
    private final static String MOBSPERSPAWN = 
            "This is the number of mobs spawned each time the spawner spawns a mob. "
            +"This is limited by the maxmobs settings.";
    private final static String COOLDOWN =
            "This the amount of time in seconds that the spawner waits after a mob "
            +"has been spawned before another mob is spawned.";
    private final static String WARMUP =
            "The amount of time in seconds before the spawner start cooldown. Warmup"
            +" start on activation and if maxmobs is reached and a mob dies.";
    private final static String ACT = 
            "What radius must players be within for the spawner to activate.";
    private final static String LEASHRANGE = 
            "This is the max distance that a mob can move from its spawn "
            +"location before it is teleported back to where it came from.";

    private final int level;
    private static void initTextHint(javax.swing.JTextArea textarea, String text) {
        textarea.setEditable(false);
        textarea.setText(text);
        textarea.setWrapStyleWord(true);
        textarea.setLineWrap(true);
        textarea.setCaretPosition(0);
    }
    
    private Map<String, Component> componentMap;
    
    private void createComponentMap() {
        componentMap = new HashMap<>();
        Component[] components = this.getContentPane().getComponents();
        for (Component component : components) {
            componentMap.put(component.getName(), component);
        }
    }

    private Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        }
        else return null;
    }
    
    private javax.swing.JTextArea registerMobList(MythicSpawnerType type, Map<String, List<String>> mobLists) {
        String name = type.toString();
        Component c = getComponentByName("jTextArea_" + name);
        if(c == null || !(c instanceof javax.swing.JTextArea)) return null;
        
        StringBuilder sb = new StringBuilder();
        List<String> mobs = mobLists.get(name);
        int size = mobs.size();
        for(int i = 0; i < size; i++) {
            sb.append(mobs.get(i));
            if(i != size - 1) sb.append(',');
        }
        javax.swing.JTextArea jTextArea = (javax.swing.JTextArea) c;
        jTextArea.setText(sb.toString());
        return jTextArea;
    }
    
    private List<String> getMobListFromComponent(MythicSpawnerType type) {
        String name = type.toString();
        Component c = getComponentByName("jTextArea_" + name);
        if(c == null || !(c instanceof javax.swing.JTextArea)) return new ArrayList<>();
        
        javax.swing.JTextArea jTextArea = (javax.swing.JTextArea) c; 
        String txtmobs = jTextArea.getText();
        String[] strs = txtmobs.split(",");
        List<String> mobs = new ArrayList<>();
        for(String str : strs) {
            mobs.add(str.trim());
        }
        
        return mobs;
    }

    
    /**
     * Creates new form DungeonLevelEditor
     * @param level
     */
    public DungeonLevelEditor(int level) {
        initComponents();
        createComponentMap();
        
        this.level = level;
        this.setTitle("Dungeon Spawner Editor for Level " + level);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        jCheckBox_Enable.setText("Enable on level " + level);
        
        initTextHint(jTextArea_radius, RADIUS);
        initTextHint(jTextArea_maxmob, MAXMOBS);
        initTextHint(jTextArea_moblevel, MOBLEVEL);
        initTextHint(jTextArea_mobsperspawn, MOBSPERSPAWN);
        initTextHint(jTextArea_cooldown, COOLDOWN);
        initTextHint(jTextArea_warmup, WARMUP);
        initTextHint(jTextArea_act, ACT);
        initTextHint(jTextArea_leashrange, LEASHRANGE);
        
        boolean isEnable = SpawnerConfig.getRoguelikeStatus().get(level);
        SpawnerSetting es = SpawnerConfig.getRoguelikeSetting().get(level);
        int radius = es.getRadius();
        int maxMobs = es.getMaxMobs();
        int mobLevel = es.getMobLevel();
        int mobsPerSpawn = es.getMobsPerSpawn();
        int cooldown = es.getCoolDown();
        int warmup = es.getWarmup();
        int activationRange = es.getActivationRange();
        int leashRange = es.getLeashRange();
        boolean healOnLeash = es.getHealOnLeash();
        boolean resetThreatOnLeash = es.getResetThreatOnLeash();
        boolean showFlames = es.getShowFlames();
        boolean breakable = es.getBreakable();
        
        jCheckBox_Enable.setSelected(isEnable);
        
        jSpinner_radius.setValue(radius);
        jSpinner_maxMobs.setValue(maxMobs);
        jSpinner_mobLevel.setValue(mobLevel);
        jSpinner_mobsPerSpawn.setValue(mobsPerSpawn);
        jSpinner_cooldown.setValue(cooldown);
        jSpinner_warmup.setValue(warmup);
        jSpinner_activationRange.setValue(activationRange);
        jSpinner_leashRange.setValue(leashRange);
        
        jCheckBox_healOnLeash.setSelected(healOnLeash);
        jCheckBox_resetThreatOnLeash.setSelected(resetThreatOnLeash);
        jCheckBox_showFlames.setSelected(showFlames);
        jCheckBox_breakable.setSelected(breakable);
        
        Map<String, List<String>> maps = SpawnerConfig.getRoguelikeMobList().get(level);
        
        for(MythicSpawnerType type : MythicSpawnerType.values()) {
            registerMobList(type, maps);
        }
        
        setStatus(isEnable);
    }
    
    private void setStatus(boolean b) {
        jSpinner_radius.setEnabled(b);
        jSpinner_maxMobs.setEnabled(b);
        jSpinner_mobLevel.setEnabled(b);
        jSpinner_mobsPerSpawn.setEnabled(b);
        jSpinner_cooldown.setEnabled(b);
        jSpinner_warmup.setEnabled(b);
        jSpinner_activationRange.setEnabled(b);
        jSpinner_leashRange.setEnabled(b);
        
        jCheckBox_healOnLeash.setEnabled(b);
        jCheckBox_resetThreatOnLeash.setEnabled(b);
        jCheckBox_showFlames.setEnabled(b);
        jCheckBox_breakable.setEnabled(b);
        
        jTextArea_creeper.setEnabled(b);
        jTextArea_cave_spider.setEnabled(b);
        jTextArea_spider.setEnabled(b);
        jTextArea_skeleton.setEnabled(b);
        jTextArea_zombie.setEnabled(b);
        jTextArea_silverfish.setEnabled(b);
        jTextArea_enderman.setEnabled(b);
        jTextArea_witch.setEnabled(b);
        jTextArea_wither.setEnabled(b);
        jTextArea_bat.setEnabled(b);
        jTextArea_magma_cube.setEnabled(b);
        jTextArea_blaze.setEnabled(b);
        jTextArea_slime.setEnabled(b);
        jTextArea_zombie_pigman.setEnabled(b);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox_Enable = new javax.swing.JCheckBox();
        jSpinner_radius = new javax.swing.JSpinner();
        jLabel_Radius = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_radius = new javax.swing.JTextArea();
        jLabel_MaxMobs = new javax.swing.JLabel();
        jSpinner_maxMobs = new javax.swing.JSpinner();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_maxmob = new javax.swing.JTextArea();
        jLabel_MobLevel = new javax.swing.JLabel();
        jSpinner_mobLevel = new javax.swing.JSpinner();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea_moblevel = new javax.swing.JTextArea();
        jLabel_mobsperspawn = new javax.swing.JLabel();
        jSpinner_mobsPerSpawn = new javax.swing.JSpinner();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea_mobsperspawn = new javax.swing.JTextArea();
        jLabel_cooldown = new javax.swing.JLabel();
        jSpinner_cooldown = new javax.swing.JSpinner();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea_cooldown = new javax.swing.JTextArea();
        jLabel_warmup = new javax.swing.JLabel();
        jSpinner_warmup = new javax.swing.JSpinner();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea_warmup = new javax.swing.JTextArea();
        jLabel_activation_range = new javax.swing.JLabel();
        jSpinner_activationRange = new javax.swing.JSpinner();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea_act = new javax.swing.JTextArea();
        jLabel_leash_range = new javax.swing.JLabel();
        jSpinner_leashRange = new javax.swing.JSpinner();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea_leashrange = new javax.swing.JTextArea();
        jCheckBox_healOnLeash = new javax.swing.JCheckBox();
        jCheckBox_resetThreatOnLeash = new javax.swing.JCheckBox();
        jCheckBox_showFlames = new javax.swing.JCheckBox();
        jCheckBox_breakable = new javax.swing.JCheckBox();
        jLabel_mobs_mapping = new javax.swing.JLabel();
        jLabel_maphint = new javax.swing.JLabel();
        jLabel_creeper = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTextArea_creeper = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTextArea_cave_spider = new javax.swing.JTextArea();
        jLabel_cave_spider = new javax.swing.JLabel();
        jLabel_spider = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextArea_spider = new javax.swing.JTextArea();
        jLabel_skeleton = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTextArea_skeleton = new javax.swing.JTextArea();
        jLabel_zombie = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTextArea_zombie = new javax.swing.JTextArea();
        jLabel_silverfish = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTextArea_silverfish = new javax.swing.JTextArea();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextArea_enderman = new javax.swing.JTextArea();
        jLabel_witch = new javax.swing.JLabel();
        jLabel_enderman = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTextArea_witch = new javax.swing.JTextArea();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTextArea_wither = new javax.swing.JTextArea();
        jLabel_bat = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTextArea_bat = new javax.swing.JTextArea();
        jLabel_wither = new javax.swing.JLabel();
        jLabel_blaze = new javax.swing.JLabel();
        jLabel_magma_cube = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTextArea_magma_cube = new javax.swing.JTextArea();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTextArea_blaze = new javax.swing.JTextArea();
        jLabel_zombie_pigman = new javax.swing.JLabel();
        jLabel_slime = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTextArea_slime = new javax.swing.JTextArea();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTextArea_zombie_pigman = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jCheckBox_Enable.setLabel("Enable");
        jCheckBox_Enable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_EnableActionPerformed(evt);
            }
        });

        jSpinner_radius.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel_Radius.setText("Radius                    ");
        jLabel_Radius.setToolTipText("");
        jLabel_Radius.setMaximumSize(new java.awt.Dimension(60, 17));
        jLabel_Radius.setMinimumSize(new java.awt.Dimension(60, 17));

        jTextArea_radius.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_radius.setColumns(20);
        jTextArea_radius.setRows(1);
        jScrollPane1.setViewportView(jTextArea_radius);

        jLabel_MaxMobs.setText("Max Mobs              ");
        jLabel_MaxMobs.setToolTipText("");

        jSpinner_maxMobs.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        jTextArea_maxmob.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_maxmob.setColumns(20);
        jTextArea_maxmob.setRows(1);
        jScrollPane2.setViewportView(jTextArea_maxmob);

        jLabel_MobLevel.setText("Mob Level              ");
        jLabel_MobLevel.setToolTipText("");

        jSpinner_mobLevel.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        jTextArea_moblevel.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_moblevel.setColumns(20);
        jTextArea_moblevel.setRows(1);
        jScrollPane3.setViewportView(jTextArea_moblevel);

        jLabel_mobsperspawn.setText("Mobs Per Spawn  ");
        jLabel_mobsperspawn.setToolTipText("");

        jSpinner_mobsPerSpawn.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        jTextArea_mobsperspawn.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_mobsperspawn.setColumns(20);
        jTextArea_mobsperspawn.setRows(1);
        jScrollPane4.setViewportView(jTextArea_mobsperspawn);

        jLabel_cooldown.setText("Cooldown               ");
        jLabel_cooldown.setToolTipText("");

        jSpinner_cooldown.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        jTextArea_cooldown.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_cooldown.setColumns(20);
        jTextArea_cooldown.setRows(1);
        jScrollPane5.setViewportView(jTextArea_cooldown);

        jLabel_warmup.setText("Warmup                 ");
        jLabel_warmup.setToolTipText("");

        jSpinner_warmup.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jTextArea_warmup.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_warmup.setColumns(20);
        jTextArea_warmup.setRows(1);
        jScrollPane6.setViewportView(jTextArea_warmup);

        jLabel_activation_range.setText("Activation Range  ");
        jLabel_activation_range.setToolTipText("");

        jSpinner_activationRange.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jTextArea_act.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_act.setColumns(20);
        jTextArea_act.setRows(1);
        jScrollPane7.setViewportView(jTextArea_act);

        jLabel_leash_range.setText("Leash Range          ");
        jLabel_leash_range.setToolTipText("");

        jSpinner_leashRange.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jTextArea_leashrange.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_leashrange.setColumns(20);
        jTextArea_leashrange.setRows(1);
        jScrollPane8.setViewportView(jTextArea_leashrange);

        jCheckBox_healOnLeash.setText("Heal On Leash");

        jCheckBox_resetThreatOnLeash.setText("Reset Threat On Leash");

        jCheckBox_showFlames.setText("Show Flames");

        jCheckBox_breakable.setText("Breakable");

        jLabel_mobs_mapping.setText("Mobs Mapping");

        jLabel_maphint.setText("<html>\n- Mapping a Normal Mob Spawner to Mythic Mob Spawner<br>\n- Please input your custom mythicmobs' names here<br>\n- Leave it empty if you want to use vanilla mobs<br>\n- These names can be found at plugins/MythicMobs/Mobs/*.yml<br>\n- Split with ','<br>\n<br>\nEX:<br>\nSkeletalKnight,SkeletonKing<br>\n</html>");

        jLabel_creeper.setText("creeper");

        jTextArea_creeper.setColumns(10);
        jTextArea_creeper.setRows(2);
        jScrollPane12.setViewportView(jTextArea_creeper);

        jTextArea_cave_spider.setColumns(10);
        jTextArea_cave_spider.setRows(2);
        jScrollPane13.setViewportView(jTextArea_cave_spider);

        jLabel_cave_spider.setText("<html>cave_<br>spider</html>");

        jLabel_spider.setText("spider");

        jTextArea_spider.setColumns(10);
        jTextArea_spider.setRows(2);
        jScrollPane14.setViewportView(jTextArea_spider);

        jLabel_skeleton.setText("skeleton");

        jTextArea_skeleton.setColumns(10);
        jTextArea_skeleton.setRows(2);
        jScrollPane15.setViewportView(jTextArea_skeleton);

        jLabel_zombie.setText("zombie");

        jTextArea_zombie.setColumns(10);
        jTextArea_zombie.setRows(2);
        jScrollPane16.setViewportView(jTextArea_zombie);

        jLabel_silverfish.setText("silverfish");

        jTextArea_silverfish.setColumns(10);
        jTextArea_silverfish.setRows(2);
        jScrollPane17.setViewportView(jTextArea_silverfish);

        jTextArea_enderman.setColumns(10);
        jTextArea_enderman.setRows(2);
        jScrollPane18.setViewportView(jTextArea_enderman);

        jLabel_witch.setText("witch");

        jLabel_enderman.setText("enderman");

        jTextArea_witch.setColumns(10);
        jTextArea_witch.setRows(2);
        jScrollPane19.setViewportView(jTextArea_witch);

        jTextArea_wither.setColumns(10);
        jTextArea_wither.setRows(2);
        jScrollPane20.setViewportView(jTextArea_wither);

        jLabel_bat.setText("bat");

        jTextArea_bat.setColumns(10);
        jTextArea_bat.setRows(2);
        jScrollPane21.setViewportView(jTextArea_bat);

        jLabel_wither.setText("wither");

        jLabel_blaze.setText("blaze");

        jLabel_magma_cube.setText("<html>magma_<br>cube</html>");

        jTextArea_magma_cube.setColumns(10);
        jTextArea_magma_cube.setRows(2);
        jScrollPane22.setViewportView(jTextArea_magma_cube);

        jTextArea_blaze.setColumns(10);
        jTextArea_blaze.setRows(2);
        jScrollPane23.setViewportView(jTextArea_blaze);

        jLabel_zombie_pigman.setText("<html>zombie_<br>pigman</html>");

        jLabel_slime.setText("slime");

        jTextArea_slime.setColumns(10);
        jTextArea_slime.setRows(2);
        jScrollPane24.setViewportView(jTextArea_slime);

        jTextArea_zombie_pigman.setColumns(10);
        jTextArea_zombie_pigman.setRows(2);
        jScrollPane25.setViewportView(jTextArea_zombie_pigman);

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox_Enable)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_MaxMobs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_maxMobs, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_MobLevel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_mobLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_mobsperspawn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_mobsPerSpawn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_cooldown)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_cooldown, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_warmup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_warmup, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_activation_range)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_activationRange, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBox_healOnLeash, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jCheckBox_resetThreatOnLeash, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_leash_range)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner_leashRange, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBox_showFlames, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox_breakable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_Radius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_radius, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel_creeper)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel_spider)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel_zombie)
                                        .addGap(30, 30, 30)
                                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_silverfish)
                                        .addComponent(jLabel_skeleton, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel_cave_spider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_wither)
                                    .addComponent(jLabel_enderman)
                                    .addComponent(jLabel_magma_cube, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel_blaze))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel_witch))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel_bat))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel_zombie_pigman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel_slime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel_mobs_mapping)
                    .addComponent(jLabel_maphint, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox_Enable)
                            .addComponent(jLabel_mobs_mapping))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner_radius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_Radius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner_maxMobs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_MaxMobs))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner_mobLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_MobLevel))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner_mobsPerSpawn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_mobsperspawn))
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner_cooldown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_cooldown))
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner_warmup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_warmup))
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner_activationRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_activation_range))
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner_leashRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_leash_range))
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(3, 3, 3)
                                .addComponent(jCheckBox_healOnLeash)
                                .addGap(3, 3, 3)
                                .addComponent(jCheckBox_resetThreatOnLeash)
                                .addGap(3, 3, 3)
                                .addComponent(jCheckBox_showFlames)
                                .addGap(3, 3, 3)
                                .addComponent(jCheckBox_breakable))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_maphint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_creeper)
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_cave_spider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_spider)
                                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_skeleton)
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_zombie)
                                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_silverfish)
                                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_enderman)
                                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_witch)
                                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_wither)
                                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_bat)
                                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_magma_cube, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_blaze)
                                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_slime)
                                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_zombie_pigman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox_EnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_EnableActionPerformed
        // TODO add your handling code here:
        if(jCheckBox_Enable.isSelected()) {
            setStatus(true);
        } else {
            setStatus(false);
        }
    }//GEN-LAST:event_jCheckBox_EnableActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        boolean isEnable = jCheckBox_Enable.isSelected();
        SpawnerConfig.setStatus(level, isEnable);
        
        SpawnerSetting ex = SpawnerConfig.getRoguelikeSetting().get(level);
        ex
                .setActivationRange((Integer) jSpinner_activationRange.getValue())
                .setBreakable(jCheckBox_breakable.isSelected())
                .setCooldown((Integer) jSpinner_cooldown.getValue())
                .setHealOnLeash(jCheckBox_healOnLeash.isSelected())
                .setLeashRange((Integer) jSpinner_leashRange.getValue())
                .setMaxMobs((Integer) jSpinner_maxMobs.getValue())
                .setMobLevel((Integer) jSpinner_mobLevel.getValue())
                .setMobsPerSpawn((Integer) jSpinner_mobsPerSpawn.getValue())
                .setRadius((Integer) jSpinner_radius.getValue())
                .setResetThreatOnLeash(jCheckBox_resetThreatOnLeash.isSelected())
                .setShowFlames(jCheckBox_showFlames.isSelected())
                .setWarmup((Integer) jSpinner_warmup.getValue());
                
        Map<String, List<String>> map = SpawnerConfig.getRoguelikeMobList().get(level);
        
        for(MythicSpawnerType type : MythicSpawnerType.values()) {
            List<String> mobs_tmp = getMobListFromComponent(type);
            List<String> mobs = map.get(type.toString());
            mobs.clear();
            for(String mob : mobs_tmp) mobs.add(mob);
        }
                
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DungeonLevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DungeonLevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DungeonLevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DungeonLevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DungeonLevelEditor(-1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox_Enable;
    private javax.swing.JCheckBox jCheckBox_breakable;
    private javax.swing.JCheckBox jCheckBox_healOnLeash;
    private javax.swing.JCheckBox jCheckBox_resetThreatOnLeash;
    private javax.swing.JCheckBox jCheckBox_showFlames;
    private javax.swing.JLabel jLabel_MaxMobs;
    private javax.swing.JLabel jLabel_MobLevel;
    private javax.swing.JLabel jLabel_Radius;
    private javax.swing.JLabel jLabel_activation_range;
    private javax.swing.JLabel jLabel_bat;
    private javax.swing.JLabel jLabel_blaze;
    private javax.swing.JLabel jLabel_cave_spider;
    private javax.swing.JLabel jLabel_cooldown;
    private javax.swing.JLabel jLabel_creeper;
    private javax.swing.JLabel jLabel_enderman;
    private javax.swing.JLabel jLabel_leash_range;
    private javax.swing.JLabel jLabel_magma_cube;
    private javax.swing.JLabel jLabel_maphint;
    private javax.swing.JLabel jLabel_mobs_mapping;
    private javax.swing.JLabel jLabel_mobsperspawn;
    private javax.swing.JLabel jLabel_silverfish;
    private javax.swing.JLabel jLabel_skeleton;
    private javax.swing.JLabel jLabel_slime;
    private javax.swing.JLabel jLabel_spider;
    private javax.swing.JLabel jLabel_warmup;
    private javax.swing.JLabel jLabel_witch;
    private javax.swing.JLabel jLabel_wither;
    private javax.swing.JLabel jLabel_zombie;
    private javax.swing.JLabel jLabel_zombie_pigman;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSpinner jSpinner_activationRange;
    private javax.swing.JSpinner jSpinner_cooldown;
    private javax.swing.JSpinner jSpinner_leashRange;
    private javax.swing.JSpinner jSpinner_maxMobs;
    private javax.swing.JSpinner jSpinner_mobLevel;
    private javax.swing.JSpinner jSpinner_mobsPerSpawn;
    private javax.swing.JSpinner jSpinner_radius;
    private javax.swing.JSpinner jSpinner_warmup;
    private javax.swing.JTextArea jTextArea_act;
    private javax.swing.JTextArea jTextArea_bat;
    private javax.swing.JTextArea jTextArea_blaze;
    private javax.swing.JTextArea jTextArea_cave_spider;
    private javax.swing.JTextArea jTextArea_cooldown;
    private javax.swing.JTextArea jTextArea_creeper;
    private javax.swing.JTextArea jTextArea_enderman;
    private javax.swing.JTextArea jTextArea_leashrange;
    private javax.swing.JTextArea jTextArea_magma_cube;
    private javax.swing.JTextArea jTextArea_maxmob;
    private javax.swing.JTextArea jTextArea_moblevel;
    private javax.swing.JTextArea jTextArea_mobsperspawn;
    private javax.swing.JTextArea jTextArea_radius;
    private javax.swing.JTextArea jTextArea_silverfish;
    private javax.swing.JTextArea jTextArea_skeleton;
    private javax.swing.JTextArea jTextArea_slime;
    private javax.swing.JTextArea jTextArea_spider;
    private javax.swing.JTextArea jTextArea_warmup;
    private javax.swing.JTextArea jTextArea_witch;
    private javax.swing.JTextArea jTextArea_wither;
    private javax.swing.JTextArea jTextArea_zombie;
    private javax.swing.JTextArea jTextArea_zombie_pigman;
    // End of variables declaration//GEN-END:variables
}
