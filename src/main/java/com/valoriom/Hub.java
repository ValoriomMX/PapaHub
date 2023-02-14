package com.valoriom;

import com.valoriom.board.Assemble;
import com.valoriom.server.accessory.Selector;
import com.valoriom.utils.Bungee;
import com.valoriom.utils.Format;
import com.valoriom.board.AssembleStyle;
import com.valoriom.commands.SetSpawnCommand;
import com.valoriom.commands.SpawnCommand;
import com.valoriom.listeners.DoubleJumpListener;
import com.valoriom.listeners.WorldListener;
import com.valoriom.providers.ScoreboardProviderAPI;
import com.valoriom.providers.TablistProviderAPI;
import com.valoriom.server.queue.QueueHandler;
import com.valoriom.server.ItemsHandler;
import com.valoriom.server.accessory.Cosmetic;
import com.valoriom.server.accessory.EnderButt;
import com.valoriom.server.accessory.cosmetic.Hats;
import com.valoriom.server.accessory.cosmetic.outfits.Outfits;
import com.valoriom.server.accessory.cosmetic.particle.Particles;
import com.valoriom.server.queue.LeaveQueue;
import com.valoriom.tablist.Tablist;
import com.valoriom.utils.config.ConfigFile;
import com.valoriom.utils.config.ScoreboardConfig;
import com.valoriom.utils.config.TablistConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;

public final class Hub extends JavaPlugin {

    @Getter
    public static Hub inst;
    private Bungee serverHandler;
    private static Hub plugin;
    private QueueHandler queueHandler;
    public YamlConfiguration bfile;
    public boolean isPlaceholderAPI = false;

    public static Hub getPlugin() {
        return Hub.plugin;
    }


    @Override
    public void onEnable() {
        Hub.plugin = this;
        this.serverHandler = new Bungee(this);

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        Bukkit.getConsoleSender().sendMessage(Format.translate("&bEnabling Hub..."));
        Bukkit.getConsoleSender().sendMessage(Format.translate("&aLoading File Configs:     "));
        Bukkit.getConsoleSender().sendMessage(Format.translate(""));
        registerCommands();
        registerConfigs();
        list();

        queueHandler = new QueueHandler();

        if (TablistConfig.getConfig().getBoolean("tab.enabled")) {
            Tablist test = new Tablist(Hub.getPlugin(), new TablistProviderAPI() {
            });
        }

        if (ScoreboardConfig.getConfig().getBoolean("scoreboard.enabled")) {
            Assemble assemble = new Assemble(this, new ScoreboardProviderAPI());
            assemble.setTicks(2);
            assemble.setAssembleStyle(AssembleStyle.MODERN);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Format.translate("&cDisabling Hub..."));
        Bukkit.getConsoleSender().sendMessage(Format.translate("&cSaving File Configs:     "));
        Bukkit.getConsoleSender().sendMessage(Format.translate(""));

        ScoreboardConfig.getConfig().save();
        TablistConfig.getConfig().save();
        ConfigFile.getConfig().save();
    }

    private void registerConfigs() {
        Bukkit.getConsoleSender().sendMessage(Format.translate("&aTablist.yml config loaded."));
        Bukkit.getConsoleSender().sendMessage(Format.translate("&aScoreboard.yml config loaded."));
        Bukkit.getConsoleSender().sendMessage(Format.translate("&aConfig.yml config loaded."));
        Bukkit.getConsoleSender().sendMessage(Format.translate("&aitem.yml config loaded."));
        Bukkit.getConsoleSender().sendMessage(Format.translate("&aLanguage.yml config loaded."));
    }

    private void registerCommands() {
        new SetSpawnCommand();
        new SpawnCommand();
    }

    private void list() {
        PluginManager m = Bukkit.getPluginManager();
        m.registerEvents((Listener) new DoubleJumpListener(), (Plugin) this);
        m.registerEvents((Listener) new WorldListener(), (Plugin) this);
        m.registerEvents((Listener)new EnderButt(), (Plugin)this);
        m.registerEvents((Listener)new Cosmetic(), (Plugin)this);
        m.registerEvents((Listener)new Outfits(), (Plugin)this);
        m.registerEvents((Listener)new Selector(), (Plugin)this);
        m.registerEvents((Listener)new Particles(), (Plugin)this);
        m.registerEvents((Listener)new Hats(), (Plugin)this);
        m.registerEvents((Listener)new LeaveQueue(), (Plugin)this);
        m.registerEvents((Listener)new ItemsHandler(), (Plugin)this);
    }

    public static Hub getInstance() {
        return Hub.getPlugin(Hub.class);
    }

    public QueueHandler getQueueHandler() {
        return queueHandler;
    }

    public Collection<? extends Player> getOnlinePlayers() {
        Collection<Player> collection = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            collection.add(player);
        }
        return collection;
    }

    public static Hub getInst() {
        return Hub.inst;
    }

    public Bungee getServerHandler() {
        return this.serverHandler;
    }
}
