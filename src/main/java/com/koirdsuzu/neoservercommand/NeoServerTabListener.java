package com.koirdsuzu.neoservercommand;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NeoServerTabListener implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer player)) return;

        String cursor = event.getCursor();
        
        // /server コマンドのTab補完
        if (cursor.startsWith("/server ")) {
            handleServerTabComplete(event, player, cursor);
        }
        // /send コマンドのTab補完
        else if (cursor.startsWith("/send ")) {
            handleSendTabComplete(event, player, cursor);
        }
    }

    private void handleServerTabComplete(TabCompleteEvent event, ProxiedPlayer player, String cursor) {
        String arg = cursor.replace("/server ", "").toLowerCase();
        List<String> suggestions = new ArrayList<>();

        NeoServerPlugin plugin = NeoServerPlugin.getInstance();
        for (String name : plugin.getOrderedServers()) {
            if ((name.toLowerCase().startsWith(arg))
                    && (player.hasPermission("bungeecord.command.neoserver.*")
                    || player.hasPermission("bungeecord.command.neoserver." + name))) {
                suggestions.add(name);
            }
        }

        event.getSuggestions().clear();
        event.getSuggestions().addAll(suggestions);
    }

    private void handleSendTabComplete(TabCompleteEvent event, ProxiedPlayer player, String cursor) {
        String commandArgs = cursor.replace("/send ", "").trim();
        String[] parts = commandArgs.split(" ");
        List<String> suggestions = new ArrayList<>();

        // 引数の数を正確に判定
        if (parts.length == 1 && !parts[0].isEmpty()) {
            // 第1引数: all, current, プレイヤー名
            String input = parts[0].toLowerCase();
            
            // all, current を追加
            if ("all".startsWith(input)) {
                suggestions.add("all");
            }
            if ("current".startsWith(input)) {
                suggestions.add("current");
            }
            
            // オンラインプレイヤー名を追加
            suggestions.addAll(ProxyServer.getInstance().getPlayers().stream()
                    .map(ProxiedPlayer::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList()));
        }
        else if (parts.length == 2 && !parts[0].isEmpty()) {
            // 第2引数: サーバー名のみ
            String input = parts[1].toLowerCase();
            NeoServerPlugin plugin = NeoServerPlugin.getInstance();
            
            suggestions.addAll(plugin.getOrderedServers().stream()
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .filter(name -> canAccessSend(player, name))
                    .collect(Collectors.toList()));
        }
        // 第3引数以降の場合は何も表示しない（コマンドは2引数まで）

        event.getSuggestions().clear();
        event.getSuggestions().addAll(suggestions);
    }

    private boolean canAccessSend(ProxiedPlayer player, String serverName) {
        return player.hasPermission("bungeecord.command.neosend.*")
                || player.hasPermission("bungeecord.command.neosend." + serverName);
    }
}
