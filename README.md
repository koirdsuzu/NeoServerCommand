# NeoServerCommand

[English](#english) | [日本語](#日本語)

---

## English

### Description

NeoServerCommand is a powerful BungeeCord plugin that enhances the `/server` command with interactive features and adds a new `/send` command for managing player transfers between servers. The plugin displays servers in the order they appear in BungeeCord's `config.yml` and provides a fully customizable permission system.

### Features

#### `/server` Command
- **Interactive Server List**: Displays available servers with hover text showing player count
- **Click-to-Connect**: Click on a server name to connect instantly
- **Tab Completion**: Smart tab completion based on permissions
- **Permission-Based Access**: Control server access with granular permissions
- **Customizable Messages**: All messages can be customized in `config.yml`

#### `/send` Command
- **Send Players**: Transfer players to other servers
- **Multiple Targets**: Support for `all`, `current`, or specific player names
- **Interactive Server List**: Hover and click functionality for server selection
- **Tab Completion**: Smart tab completion for both arguments
- **Permission-Based Access**: Control send permissions per server

### Installation

1. Download `NeoServerCommand-1.0.jar` from the releases page
2. Place it in your BungeeCord `plugins` folder
3. Restart your BungeeCord server
4. Configure permissions and messages in `plugins/NeoServerCommand/config.yml`

### Commands

#### `/server`
- **Usage**: `/server [server name]`
- **Description**: Display available servers or connect to a specific server
- **Permission**: `bungeecord.command.neoserver`
- **Examples**:
  - `/server` - Show interactive server list
  - `/server server01` - Connect to server01

#### `/send`
- **Usage**: `/send <player|all|current> <server name>`
- **Description**: Send players to another server
- **Permission**: `bungeecord.command.neosend`
- **Examples**:
  - `/send all server01` - Send all players to server01
  - `/send current server02` - Send all players from current server to server02
  - `/send PlayerName server03` - Send specific player to server03

### Permissions

#### Server Command Permissions
- `bungeecord.command.neoserver.*` - Access to all servers
- `bungeecord.command.neoserver.<server>` - Access to specific server (e.g., `bungeecord.command.neoserver.server01`)

#### Send Command Permissions
- `bungeecord.command.neosend.*` - Send to all servers
- `bungeecord.command.neosend.<server>` - Send to specific server (e.g., `bungeecord.command.neosend.server01`)

### Configuration

All messages can be customized in `plugins/NeoServerCommand/config.yml`. The plugin supports color codes using `&` symbol.

#### Example Configuration

```yaml
messages:
  # Server list messages
  available-servers: "&6&l=== &b&lAvailable Servers &6&l==="
  server-list-item: " &8▸ &a%server%"
  server-hover: "&e%server%\n&7%players% players online\n&aClick to connect"
  
  # Error messages
  invalid-server: "&c&l✗ &cServer &e%server% &cdoes not exist."
  no-permission: "&c&l✗ &cYou don't have permission to connect to &e%server%."
  
  # Connection messages
  connecting: "&a&l✓ &aConnecting to &e%server%..."
  
  # Send command messages
  send-usage: "&eUsage: /send <player|all|current> <server>\n&7Example: /send all server01"
  send-success: "&a&l✓ &aSent &e%player% &ato &e%server%."
```

### Server Order

The plugin reads server order from BungeeCord's `config.yml` file. Servers are displayed in the same order they appear in the `servers` section.

### Requirements

- BungeeCord 1.21 or higher
- Java 17 or higher

### Author

koirdsuzu

### License

This plugin is provided as-is. Use at your own risk.

---

## 日本語

### 説明

NeoServerCommandは、`/server`コマンドを強化し、新しい`/send`コマンドを追加する強力なBungeeCordプラグインです。BungeeCordの`config.yml`に登録された順序でサーバーを表示し、完全にカスタマイズ可能な権限システムを提供します。

### 機能

#### `/server`コマンド
- **インタラクティブなサーバー一覧**: プレイヤー数を表示するホバーテキスト付きでサーバー一覧を表示
- **クリックで接続**: サーバー名をクリックして即座に接続
- **Tab補完**: 権限に基づいたスマートなTab補完
- **権限ベースのアクセス**: 細かい権限でサーバーアクセスを制御
- **カスタマイズ可能なメッセージ**: すべてのメッセージを`config.yml`でカスタマイズ可能

#### `/send`コマンド
- **プレイヤー送信**: プレイヤーを他のサーバーに送信
- **複数のターゲット**: `all`、`current`、または特定のプレイヤー名に対応
- **インタラクティブなサーバー一覧**: サーバー選択のためのホバーとクリック機能
- **Tab補完**: 両方の引数に対するスマートなTab補完
- **権限ベースのアクセス**: サーバーごとの送信権限を制御

### インストール

1. リリースページから`NeoServerCommand-1.0.jar`をダウンロード
2. BungeeCordの`plugins`フォルダに配置
3. BungeeCordサーバーを再起動
4. `plugins/NeoServerCommand/config.yml`で権限とメッセージを設定

### コマンド

#### `/server`
- **使用方法**: `/server [サーバー名]`
- **説明**: 利用可能なサーバーを表示するか、特定のサーバーに接続
- **権限**: `bungeecord.command.neoserver`
- **例**:
  - `/server` - インタラクティブなサーバー一覧を表示
  - `/server server01` - server01に接続

#### `/send`
- **使用方法**: `/send <player|all|current> <サーバー名>`
- **説明**: プレイヤーを他のサーバーに送信
- **権限**: `bungeecord.command.neosend`
- **例**:
  - `/send all server01` - 全プレイヤーをserver01に送信
  - `/send current server02` - 現在のサーバーの全プレイヤーをserver02に送信
  - `/send PlayerName server03` - 特定のプレイヤーをserver03に送信

### 権限

#### Serverコマンドの権限
- `bungeecord.command.neoserver.*` - 全サーバーへのアクセス権限
- `bungeecord.command.neoserver.<サーバー名>` - 特定サーバーへのアクセス権限（例: `bungeecord.command.neoserver.server01`）

#### Sendコマンドの権限
- `bungeecord.command.neosend.*` - 全サーバーへの送信権限
- `bungeecord.command.neosend.<サーバー名>` - 特定サーバーへの送信権限（例: `bungeecord.command.neosend.server01`）

### 設定

すべてのメッセージは`plugins/NeoServerCommand/config.yml`でカスタマイズできます。プラグインは`&`記号を使用したカラーコードをサポートしています。

#### 設定例

```yaml
messages:
  # サーバー一覧表示メッセージ
  available-servers: "&6&l=== &b&l利用可能なサーバー &6&l==="
  server-list-item: " &8▸ &a%server%"
  server-hover: "&e%server%\n&7%players% プレイヤーが接続中\n&aクリックでサーバーに移動"
  
  # エラーメッセージ
  invalid-server: "&c&l✗ &cサーバー &e%server% &cは存在しません。"
  no-permission: "&c&l✗ &cサーバー &e%server% &cに接続する権限がありません。"
  
  # 接続メッセージ
  connecting: "&a&l✓ &aサーバー &e%server% &aに接続中..."
  
  # Sendコマンド関連メッセージ
  send-usage: "&e使用方法: /send <player|all|current> <サーバー名>\n&7例: /send all server01"
  send-success: "&a&l✓ &aプレイヤー &e%player% &aをサーバー &e%server% &aに送信しました。"
```

### サーバー順序

プラグインはBungeeCordの`config.yml`ファイルからサーバー順序を読み取ります。サーバーは`servers`セクションに表示される順序で表示されます。

### 要件

- BungeeCord 1.21以上
- Java 17以上

### 作者

koirdsuzu

### ライセンス

このプラグインは現状のまま提供されます。自己責任でご使用ください。

