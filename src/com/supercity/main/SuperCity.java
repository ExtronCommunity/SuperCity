package com.supercity.main;

import com.supercity.main.commands.CommandCityCoords;
import com.supercity.main.commands.CommandGetBackpack;
import com.supercity.main.commands.*;
import com.supercity.main.crafting.*;
import com.supercity.main.item.ItemBackPack;
import com.supercity.main.config.ConfigManager;
import com.supercity.main.enchants.*;
import com.supercity.main.event.*;
import com.supercity.main.creepers.CommandCreeperCheck;
import com.supercity.main.creepers.CreeperChecker;
import com.supercity.main.creepers.CreeperEvents;
import com.supercity.main.enchants.CurseOfBreaking;
import com.supercity.main.enchants.HeatWalker;
import com.supercity.main.event.PlayerCloseInventoryEvent;
import com.supercity.main.event.PlayerCraftItemEvent;
import com.supercity.main.event.PlayerDieEvent;
import com.supercity.main.event.PlayerInteractWithInventoryEvent;
import com.supercity.main.event.PlayerInteractionEvent;
import com.supercity.main.event.PlayerJoinGameEvent;
import com.supercity.main.event.PlayerToggleShiftEvent;
import com.supercity.main.event.custom.CustomEventListener;
import com.supercity.main.jetpack.JetpackHandler;
import com.supercity.main.recording.RecordingManager;
import com.supercity.main.sleep.OnePlayerSleepHandler;
import com.supercity.main.spawner.SpawnerMovingHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SuperCity extends JavaPlugin implements Listener {

    public static SuperCity INSTANCE;
    private JetpackHandler jetpackHandler;
    private OnePlayerSleepHandler onePlayerSleepHandler;

    public List<CustomEnchant> customEnchants = new ArrayList<>();

    public void onEnable() {
        INSTANCE = this;
        registerCommands();
        onePlayerSleepHandler = new OnePlayerSleepHandler();
        ConfigManager.init();
        RecordingManager.init();
        registerEvents();
        registerHandlers();

        initiatePlayers();
        getLogger().info(ChatColor.GREEN.toString() + "Super City has been successfully loaded!");
        //registerEnchants();
        getLogger().info(ChatColor.GREEN.toString() + "Extron Plus has been successfully loaded!");
        ItemBackPack.loadAllBackpacks();

        registerRecipes();
        CreeperChecker.load();
    }

    public void onDisable() {
        RecordingManager.setAllNotAFK();
        CreeperChecker.save();
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerInteractionEvent(), this);
        pm.registerEvents(new PlayerJoinGameEvent(), this);
        pm.registerEvents(new PlayerDieEvent(), this);
        pm.registerEvents(new PlayerToggleShiftEvent(), this);
        //pm.registerEvents(new ItemEnchantEvent(), this);
        pm.registerEvents(new CustomEventListener(), this);
        pm.registerEvents(new SpawnerMovingHandler(), this);
        pm.registerEvents(new MobSpawnEvent(),this);
        pm.registerEvents(new PlaceBlockEvent(),this);
        pm.registerEvents(new ChatMessageEvent(),this);
        pm.registerEvents(new PlayerMovedEvent(),this);
        pm.registerEvents(new PlayerBreakBlock(),this);
        //pm.registerEvents(new ChatMessageEvent(),this);
        //pm.registerEvents(new PlayerMovedEvent(),this);
        pm.registerEvents(this,this);
        pm.registerEvents(onePlayerSleepHandler, this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SuperCity.INSTANCE, RecordingManager::tick, 0, 1);
        pm.registerEvents(new PlayerCloseInventoryEvent(), this);
        pm.registerEvents(new PlayerCraftItemEvent(), this);
        pm.registerEvents(new PlayerInteractWithInventoryEvent(), this);
        pm.registerEvents(new CreeperEvents(),this);
    }

    private void registerCommands() {
        getCommand("getCustomItem").setExecutor(new CommandGetCustomItem());
        getCommand("dontSkipNight").setExecutor(new CommandDontSkipNight());
        getCommand("enableOnePlayerSleep").setExecutor(new CommandReEnableOnePlayerSleep());
        //getCommand("customEnchant").setExecutor(new CommandCustomEnchant());
        getCommand("recording").setExecutor(new CommandRecording());
        getCommand("togglescoreboard").setExecutor(new CommandToggleSB());
        getCommand("togglechat").setExecutor(new CommandToggleChat());
        getCommand("getBackpack").setExecutor(new CommandGetBackpack());
        getCommand("citycoords").setExecutor(new CommandCityCoords());
        //getCommand("recording").setExecutor(new CommandRecording());
        //getCommand("togglescoreboard").setExecutor(new CommandToggleSB());
        //getCommand("togglechat").setExecutor(new CommandToggleChat());
        getCommand("creeper").setExecutor(new CommandCreeperCheck());
        getCommand("money").setExecutor(new CommandMoney());
        getCommand("money").setTabCompleter(new CommandMoney());
    }

    private void registerHandlers() {
        jetpackHandler = new JetpackHandler();
    }

    private void initiatePlayers() {
        for(Player p : getServer().getOnlinePlayers()) {
            jetpackHandler.initiatePlayer(p);
        }
        RecordingManager.copyHealthFromMain();
    }

    private void registerRecipes() {
        getServer().addRecipe(new RecipeJetpack());
        getServer().addRecipe(new RecipeCraftingStick());
        getServer().addRecipe(new RecipeBackpack());
        getServer().addRecipe(new RecipeUncraftQuartz());
        getServer().addRecipe(new SmeltingIncSack());
        SmeltingConcrete.addAll();
    }

    public JetpackHandler getJetpackHandler() {
        return jetpackHandler;
    }

    private void registerEnchants() {
        customEnchants.add(new CurseOfBreaking());
        customEnchants.add(new HeatWalker());
        customEnchants.add(new Lifesteal());
        customEnchants.add(new IceAspect());
        customEnchants.add(new AutoSmelt());
        customEnchants.add(new Sniper());
        customEnchants.add(new FancyTrail());

        customEnchants.forEach(n -> Bukkit.getPluginManager().registerEvents(n,SuperCity.this));
    }
    public OnePlayerSleepHandler getOnePlayerSleepHandler() {
        return onePlayerSleepHandler;
    }

    public CustomEnchant getCustomEnchant(String id) {
        for (CustomEnchant e : customEnchants) {
            if (e.getId().equalsIgnoreCase(id)) {
                return e;
            }
        }
        return null;
    }
}
