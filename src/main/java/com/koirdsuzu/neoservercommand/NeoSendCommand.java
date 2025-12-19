package com.koirdsuzu.neoservercommand;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.ProxyServer;

import java.util.*;

public class NeoSendCommand extends Command {

    public NeoSendCommand() {
        super("send", "bungeecord.command.neosend");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        NeoServerPlugin plugin = NeoServerPlugin.getInstance();
        var config = plugin.getConfig();

        if (args.length == 0) {
            // プレイヤーがコマンドを実行した場合
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                player.sendMessage(new TextComponent(color(config.getString("messages.send-available-servers", "&6&l=== &b&l送信可能なサーバー &6&l==="))));

                List<String> orderedServers = plugin.getOrderedServers();
                boolean hasAccessibleServers = false;
                
                for (String name : orderedServers) {
                    if (canAccess(player, name)) {
                        hasAccessibleServers = true;
                        player.sendMessage(createServerListComponent(name, config, player));
                    }
                }
                
                if (!hasAccessibleServers) {
                    player.sendMessage(new TextComponent(color(config.getString("messages.send-permission-info", "&7必要な権限: &ebungeecord.command.neosend.* &7または &ebungeecord.command.neosend.[サーバー名]"))));
                }
            } else {
                // コンソールから実行された場合
                sender.sendMessage(new TextComponent(color(config.getString("messages.send-usage", "&e使用方法: /send <プレイヤー> [サーバー名]"))));
            }
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(new TextComponent(color(config.getString("messages.send-usage", "&e使用方法: /send <プレイヤー> [サーバー名]"))));
            return;
        }

        String targetPlayer = args[0];
        String targetServer = args[1];

        // サーバーの存在確認
        if (!ProxyServer.getInstance().getServers().containsKey(targetServer)) {
            sender.sendMessage(new TextComponent(color(config.getString("messages.invalid-server", "&c&l✗ &cサーバー &e%server% &cは存在しません。").replace("%server%", targetServer))));
            return;
        }

        // 権限チェック
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer senderPlayer = (ProxiedPlayer) sender;
            if (!canAccess(senderPlayer, targetServer)) {
                sender.sendMessage(new TextComponent(color(config.getString("messages.send-no-permission", "&c&l✗ &cサーバー &e%server% &cに送信する権限がありません。").replace("%server%", targetServer))));
                return;
            }
        }

        // ターゲットプレイヤーの処理
        List<ProxiedPlayer> targetPlayers = getTargetPlayers(targetPlayer, sender);
        
        if (targetPlayers.isEmpty()) {
            sender.sendMessage(new TextComponent(color(config.getString("messages.player-not-found", "&c&l✗ &cプレイヤー &e%player% &cが見つかりません。").replace("%player%", targetPlayer))));
            return;
        }

        // プレイヤーをサーバーに送信
        int successCount = 0;
        for (ProxiedPlayer player : targetPlayers) {
            try {
                player.connect(ProxyServer.getInstance().getServerInfo(targetServer));
                successCount++;
                // 送信されたプレイヤーにメッセージ
                player.sendMessage(new TextComponent(color(config.getString("messages.send-received", "&a&l✓ &aサーバー &e%server% &aに移動しました。").replace("%server%", targetServer))));
            } catch (Exception e) {
                // 接続に失敗した場合はスキップ
                continue;
            }
        }
        
        // 送信者にメッセージ
        if (successCount > 0) {
            String message;
            if ("all".equals(targetPlayer)) {
                message = config.getString("messages.send-all-success", "&a&l✓ &a%count%人のプレイヤーをサーバー &e%server% &aに送信しました。")
                        .replace("%count%", String.valueOf(successCount))
                        .replace("%server%", targetServer);
            } else if ("current".equals(targetPlayer)) {
                message = config.getString("messages.send-current-success", "&a&l✓ &a現在のサーバーの%count%人のプレイヤーをサーバー &e%server% &aに送信しました。")
                        .replace("%count%", String.valueOf(successCount))
                        .replace("%server%", targetServer);
            } else {
                message = config.getString("messages.send-success", "&a&l✓ &aプレイヤー &e%player% &aをサーバー &e%server% &aに送信しました。")
                        .replace("%player%", targetPlayer)
                        .replace("%server%", targetServer);
            }
            sender.sendMessage(new TextComponent(color(message)));
        } else {
            sender.sendMessage(new TextComponent(color(config.getString("messages.send-failed", "&c&l✗ &cプレイヤーの送信に失敗しました。"))));
        }
    }


    private boolean canAccess(ProxiedPlayer player, String serverName) {
        return player.hasPermission("bungeecord.command.neosend.*")
                || player.hasPermission("bungeecord.command.neosend." + serverName);
    }

    private String color(String msg) {
        return msg == null ? "" : msg.replace("&", "§");
    }

    /**
     * ターゲットプレイヤーのリストを取得
     */
    private List<ProxiedPlayer> getTargetPlayers(String target, CommandSender sender) {
        List<ProxiedPlayer> players = new ArrayList<>();
        
        if ("all".equals(target)) {
            // 全プレイヤー
            players.addAll(ProxyServer.getInstance().getPlayers());
        } else if ("current".equals(target)) {
            // 現在のサーバーのプレイヤー
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer senderPlayer = (ProxiedPlayer) sender;
                String currentServer = senderPlayer.getServer().getInfo().getName();
                players.addAll(ProxyServer.getInstance().getServerInfo(currentServer).getPlayers());
            } else {
                // コンソールの場合は全プレイヤー
                players.addAll(ProxyServer.getInstance().getPlayers());
            }
        } else {
            // 特定のプレイヤー
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(target);
            if (player != null) {
                players.add(player);
            }
        }
        
        return players;
    }

    /**
     * ホバーテキストとクリック機能を持つサーバー一覧コンポーネントを作成（sendコマンド用）
     */
    private BaseComponent createServerListComponent(String serverName, net.md_5.bungee.config.Configuration config, ProxiedPlayer player) {
        NeoServerPlugin plugin = NeoServerPlugin.getInstance();
        
        // サーバーのプレイヤー数を取得
        int playerCount = plugin.getProxy().getServerInfo(serverName).getPlayers().size();
        
        // 基本テキスト
        String baseText = config.getString("messages.send-server-list-item", " &8▸ &a%server%").replace("%server%", serverName);
        TextComponent component = new TextComponent(color(baseText));
        
        // ホバーテキストを作成
        String hoverText = config.getString("messages.send-server-hover", "&e%server%\n&7%players% プレイヤーが接続中\n&aクリックでサーバーに送信")
                .replace("%server%", serverName)
                .replace("%players%", String.valueOf(playerCount));
        
        // ホバーイベントを設定
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(color(hoverText)).create()));
        
        // クリックイベントを設定（/send <プレイヤー名> <サーバー名>を実行）
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/send " + player.getName() + " " + serverName));
        
        return component;
    }
}
