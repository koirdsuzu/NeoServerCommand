package com.koirdsuzu.neoservercommand;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class NeoServerPlugin extends Plugin {

    private static NeoServerPlugin instance;
    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        getProxy().getPluginManager().registerCommand(this, new NeoServerCommand());
        getProxy().getPluginManager().registerCommand(this, new NeoSendCommand());
        getProxy().getPluginManager().registerListener(this, new NeoServerTabListener());
        getLogger().info("NeoServerCommand has been enabled.");
    }

    public void loadConfig() {
        try {
            if (!getDataFolder().exists()) getDataFolder().mkdir();
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                Files.copy(getResourceAsStream("config.yml"), configFile.toPath());
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public static NeoServerPlugin getInstance() {
        return instance;
    }

    /**
     * BungeeCordのconfig.ymlからサーバー順序を取得
     */
    public List<String> getOrderedServers() {
        List<String> orderedServers = new ArrayList<>();
        
        try {
            File bungeeConfigFile = new File("config.yml");
            if (bungeeConfigFile.exists()) {
                Configuration bungeeConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(bungeeConfigFile);
                Configuration serversSection = bungeeConfig.getSection("servers");
                
                if (serversSection != null) {
                    // BungeeCordのconfig.ymlのserversセクションの順序でサーバーを取得
                    for (String serverName : serversSection.getKeys()) {
                        if (getProxy().getServers().containsKey(serverName)) {
                            orderedServers.add(serverName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            getLogger().warning("BungeeCordのconfig.ymlを読み取れませんでした: " + e.getMessage());
        }
        
        // もしBungeeCordのconfig.ymlが読み取れない場合は、プロキシサーバーの順序を使用
        if (orderedServers.isEmpty()) {
            orderedServers.addAll(getProxy().getServers().keySet());
        }
        
        return orderedServers;
    }
}
