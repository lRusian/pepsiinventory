package com.pepsidev.pepsiinventory;

import com.pepsidev.pepsiinventory.command.InventoryOpenCommand;
import com.pepsidev.pepsiinventory.listener.InventoryListener;
import com.pepsidev.pepsiinventory.util.FileConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Pepsi extends JavaPlugin {

    FileConfig defaultConfig;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getCommand("inventory").setExecutor(new InventoryOpenCommand());



        loadConfigs();
    }


    @Override
    public void onDisable() {
    }


    private void loadConfigs() {
        this.defaultConfig = new FileConfig(this, "config.yml");

    }


    public static Pepsi get() {
        return Pepsi.getPlugin(Pepsi.class);
    }

}
