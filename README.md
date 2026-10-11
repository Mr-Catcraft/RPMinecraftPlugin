# RPPlugin

A roleplay plugin for Minecraft servers running Paper, Spigot, or Bukkit 1.16 - 26.3. It adds in-character communication commands for roleplay servers, events, and survival communities.

[![License](https://img.shields.io/badge/license-GPL--3.0-blue)](LICENSE)
[![Version](https://img.shields.io/badge/version-1.3-green)]()
[![Platform](https://img.shields.io/badge/platform-Paper%20%7C%20Spigot%20%7C%20Bukkit-lightgrey)]()
[![Minecraft](https://img.shields.io/badge/Minecraft-1.16--26.3-green)]()

## What is it

RPPlugin provides commands for character actions, environmental descriptions, dice rolls, chance-based attempts, private whispers, loud yells, and emotes. It is built for server administrators who want to enable immersive roleplay without installing a full roleplay framework.

Messages are delivered to nearby players based on configurable distance, so local chat remains local. Formats and messages are configurable, with English and Russian translations included. Additional languages can be added by copying a message section in `config.yml`.

The plugin has no external dependencies and uses the standard Bukkit command system. Tab completion is available for player names, dice examples, emotes, and `/rp help`.

## Features

- Adds `/me`, `/do`, `/try`, `/roll`, `/whisper`, `/yell`, `/emote`, and `/rp help`.
- Supports distance-based message delivery. `use_distance` toggles the check, and `message_distance` sets the default range. `/yell` uses twice that distance.
- Rolls random numbers and dice with simple, range, and dice notation formats. Examples: `/roll 20`, `/roll 5-15`, `/roll d6`, `/roll 2d10`, `/roll 1d20+5`, `/roll 3d6+2`.
- Attempts actions with a configurable success chance through `settings.try_success_chance`. Default is `0.5`.
- Sends whispers and yells with distinct formats.
- Performs predefined emotes with an optional target. Emotes are defined under `emotes` in `config.yml`.
- Formats every command output through `formats` in `config.yml`, with color code support.
- Ships with English and Russian messages under `messages`. Set `language` to `en` or `ru`.
- Provides tab completion for player names, roll examples, emotes, and `/rp help`.
- Has no external dependencies.

## Installation

1. Download the RPPlugin `1.3` JAR.
2. Place the JAR in your server's `plugins` folder.
3. Restart the server or load the plugin with a plugin manager.
4. Edit `plugins/RPPlugin/config.yml` to match your server.
5. Run `/rp help` in game to see available commands.

No additional setup is required. The plugin uses the default command permissions, so all players can use the commands unless your permission plugin restricts them.

## Usage

| Command | Permission | Description |
|---|---|---|
| `/me <action>` | None | Describes your character's action in the third person. |
| `/do <action>` | None | Describes an event or environmental action nearby. |
| `/roll <number>` | None | Rolls a random number from 1 to the given value. |
| `/roll <min>-<max>` | None | Rolls a random number in the given range. |
| `/roll <amount>d<sides>[+/-<modifier>]` | None | Rolls dice with optional modifier. Examples: `2d6+3`, `1d20`. |
| `/try <action>` | None | Attempts an action with a configurable chance of success. |
| `/whisper <message>` | None | Sends a quiet message to nearby players. |
| `/yell <message>` | None | Sends a loud message to players within twice the default distance. |
| `/emote <emote> [target]` | None | Performs a predefined emote, optionally targeting a player. |
| `/rp help` | None | Shows command help. |

The plugin does not define permission nodes in `plugin.yml`. Access is controlled by the server's permission system only if you add nodes manually or use a command restriction plugin.

## Configuration

The plugin generates `plugins/RPPlugin/config.yml`. The most important settings are:

```yaml
language: "en"

settings:
  message_distance: 100.0
  use_distance: true
  try_success_chance: 0.5

formats:
  me: "&d* %player% %action% *"
  do: "&e* %action% (%player%) *"
  roll_simple: "&a%player% rolls %roll% (1-%max%)"
  roll_range: "&a%player% rolls %roll% (%min%-%max%)"
  roll_dice: "&a%player% rolls %roll% (%expression%)"
  try_success: "&9%player% tries to %action% and succeeds!"
  try_fail: "&9%player% tries to %action% and fails!"
  whisper: "&7[Whisper] %player%: %message%"
  yell: "&c[Yell] %player%: %message%"
  emote: "&6* %player% %emote% %target% *"

emotes:
  wave: "waves"
  cry: "cries"
  dance: "dances"
  laugh: "laughs"
  bow: "bows"
  nod: "nods"
  shake: "shakes head"
  shrug: "shrugs"
  smile: "smiles"
  wink: "winks"

messages:
  en:
    player_only: "This command can only be used by players."
    me_usage: "Usage: /me <action>"
    do_usage: "Usage: /do <action>"
    roll_usage: "Usage: /roll <number> or /roll <min>-<max> or /roll <amount>d<sides>[+/-<modifier>]"
    try_usage: "Usage: /try <action>"
    whisper_usage: "Usage: /whisper <message>"
    yell_usage: "Usage: /yell <message>"
    emote_usage: "Usage: /emote <emote> [target]"
    positive_number: "Please provide a positive number."
    rolled: "rolled"
    tries: "tries to"
    succeeds: "and succeeds!"
    fails: "and fails."
    help_header: "Roleplay Plugin Commands:"
    me_description: "Express an action in the first person."
    do_description: "Describe an action or event happening nearby."
    roll_description: "Roll a random number or dice."
    try_description: "Attempt an action with a configurable chance of success."
    whisper_description: "Send a message with a short distance."
    yell_description: "Send a message with a long distance."
    emote_description: "Perform an emote."
    help_description: "Show this help message."
    rp_help_usage: "Usage: /rp help"
    emote_not_found: "Emote not found. Use /rp help to see available emotes."

  ru:
    player_only: "Только игроки могут использовать эти команды."
    me_usage: "Использование: /me <действие>"
    do_usage: "Использование: /do <действие>"
    roll_usage: "Использование: /roll <число> или /roll <мин>-<макс> или /roll <кол-во>d<грани>[+/-<модификатор>]"
    try_usage: "Использование: /try <действие>"
    whisper_usage: "Использование: /whisper <сообщение>"
    yell_usage: "Использование: /yell <сообщение>"
    emote_usage: "Использование: /emote <эмоция> [цель]"
    positive_number: "Число должно быть больше нуля."
    rolled: " выбросил "
    tries: " пытается "
    succeeds: " и удается!"
    fails: " и не удается!"
    help_header: "Справка по RP командам:"
    me_description: "Описывает действие от третьего лица."
    do_description: "Описывает происходящее действие или событие."
    roll_description: "Бросает случайное число или кости."
    try_description: "Пытается выполнить действие с настраиваемым шансом."
    whisper_description: "Отправляет сообщение на короткое расстояние."
    yell_description: "Отправляет сообщение на большое расстояние."
    emote_description: "Выполняет эмоцию."
    help_description: "Показывает это сообщение."
    rp_help_usage: "Использование: /rp help"
    emote_not_found: "Эмоция не найдена. Используйте /rp help для списка эмоций."
```

`language` selects the message set. Built-in values are `en` and `ru`. `message_distance` controls the default range for `/me`, `/do`, `/try`, `/whisper`, and `/emote`. `/yell` uses twice this value. `use_distance` disables distance checks when set to `false`, making messages global. `try_success_chance` sets the success probability for `/try` and accepts values from `0.0` to `1.0`. `formats` defines output for each command and supports `&` color codes and placeholders. `emotes` maps emote keys to action text. Add new emotes by adding keys under `emotes`.

Messages are under `messages`. To add a language, copy the `en` or `ru` block, translate the values, and set `language` to the new key.

## Compatibility

- Minecraft: 1.16 - 26.3
- Platform: Paper, Spigot, Bukkit
- Java: any version supported by the server platform; no specific Java version is required

No known incompatibilities with other plugins or libraries.

## Technologies

- Language: Java
- Platform API: Bukkit, Spigot, Paper
- Build: standard plugin JAR
- Key dependencies: none

## License

This project is distributed under the [GNU General Public License v3.0](LICENSE).