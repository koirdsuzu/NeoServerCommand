package com.koirdsuzu.neoservercommand;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.ProxyServer;

import java.util.*;

public class NeoServerCommand extends Command {

    public NeoServerCommand() {
        super("server", "bungeecord.command.neoserver");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("This command can only be used by players."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        var plugin = NeoServerPlugin.getInstance();
        var config = plugin.getConfig();

        if (args.length == 0) {
            player.sendMessage(new TextComponent(color(config.getString("messages.available-servers", "&6&l=== &b&l利用可能なサーバー &6&l==="))));

            List<String> orderedServers = plugin.getOrderedServers();
            boolean hasAccessibleServers = false;
            
            for (String name : orderedServers) {
                if (canAccess(player, name)) {
                    hasAccessibleServers = true;
                    player.sendMessage(createServerListComponent(name, config));
                }
            }
            
            if (!hasAccessibleServers) {
                player.sendMessage(new TextComponent(color(config.getString("messages.permission-info", "&7必要な権限: &ebungeecord.command.neoserver.* &7または &ebungeecord.command.neoserver.[サーバー名]"))));
            }
            return;
        }

        String target = args[0];
        if (!ProxyServer.getInstance().getServers().containsKey(target)) {
            player.sendMessage(new TextComponent(color(config.getString("messages.invalid-server", "&c&l✗ &cサーバー &e%server% &cは存在しません。").replace("%server%", target))));
            return;
        }

        if (!canAccess(player, target)) {
            player.sendMessage(new TextComponent(color(config.getString("messages.no-permission", "&c&l✗ &cサーバー &e%server% &cに接続する権限がありません。").replace("%server%", target))));
            return;
        }

        player.connect(ProxyServer.getInstance().getServerInfo(target));
        player.sendMessage(new TextComponent(color(config.getString("messages.connecting", "&a&l✓ &aサーバー &e%server% &aに接続中...").replace("%server%", target))));
    }


    private boolean canAccess(ProxiedPlayer player, String serverName) {
        return player.hasPermission("bungeecord.command.neoserver.*")
                || player.hasPermission("bungeecord.command.neoserver." + serverName);
    }


    private String color(String msg) {
        return msg == null ? "" : msg.replace("&", "§");
    }

    /**
     * ホバーテキストとクリック機能を持つサーバー一覧コンポーネントを作成
     */
    private BaseComponent createServerListComponent(String serverName, net.md_5.bungee.config.Configuration config) {
        NeoServerPlugin plugin = NeoServerPlugin.getInstance();
        
        // サーバーのプレイヤー数を取得
        int playerCount = plugin.getProxy().getServerInfo(serverName).getPlayers().size();
        
        // 基本テキスト
        String baseText = config.getString("messages.server-list-item", " &8▸ &a%server%").replace("%server%", serverName);
        TextComponent component = new TextComponent(color(baseText));
        
        // ホバーテキストを作成
        String hoverText = config.getString("messages.server-hover", "&e%server%\n&7%players% プレイヤーが接続中\n&aクリックでサーバーに移動")
                .replace("%server%", serverName)
                .replace("%players%", String.valueOf(playerCount));
        
        // ホバーイベントを設定
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(color(hoverText)).create()));
        
        // クリックイベントを設定（/server <サーバー名>を実行）
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + serverName));
        
        return component;
    }
}
