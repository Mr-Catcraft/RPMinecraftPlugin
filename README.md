![Background](https://cdn.modrinth.com/data/cached_images/8b3c6021b616d2a90fe3691f2833b436065010a7.png)

# RPPlugins

**RPPlugins** is a roleplay-focused plugin for Minecraft servers (Spigot/Paper) 1.16.x - 1.21.1 that adds immersive in-character commands such as `/me`, `/do`, `/try`, and `/roll`. Messages are localized and only visible to nearby players — perfect for RP servers, events, or survival communities seeking deeper interaction.

## 🎭 Features

- ✅ Roleplay chat commands:
  - `/me`: Describe your character’s actions.
  - `/do`: Describe surrounding events.
  - `/try`: Attempt an action with randomized success/failure.
  - `/roll`: Roll a number between 1 and N.
- 🧠 Nearby players only see RP messages (distance configurable).
- 🌐 Multi-language support via `config.yml`.
- 🛠️ Clean and simple design with customizable messages.
- 📜 Built-in `/rp help` command for users.

## 💬 Commands

| Command        | Description                                |
|----------------|--------------------------------------------|
| `/me <text>`   | Describes your character's actions.        |
| `/do <text>`   | Describes events in the world.             |
| `/try <text>`  | Attempts an action with success or failure.|
| `/roll <max>`  | Rolls a number between 1 and `<max>`.      |
| `/rp help`     | Shows command help.                        |

## 🔁 Example Usage

- `/me smiles warmly` → *Steve smiles warmly*
- `/do A cold breeze passes by (Steve)` → *A cold breeze passes by (Steve)*
- `/try open the rusty door` → Steve tries to open the rusty door and succeeds/fails.
- `/roll 100` → Steve rolled 57 (1-100)

## ⚙️ Configuration

The plugin uses a simple `config.yml` file with these options:

```yaml
language: "en"

settings:
  message_distance: 100.0

messages:
  en:
    player_only: "Only players can use this command."
    me_usage: "Usage: /me <action>"
    do_usage: "Usage: /do <description>"
    roll_usage: "Usage: /roll <max number>"
    try_usage: "Usage: /try <action>"
    positive_number: "You must enter a positive number."
    rolled: "rolled"
    tries: "tries to"
    succeeds: "and succeeds."
    fails: "but fails."
    rp_help_usage: "Use /rp help to view available commands."
    help_header: "Available RP commands:"
    me_description: "Describe your character's actions."
    do_description: "Describe the world around you."
    roll_description: "Roll a number between 1 and your chosen max."
    try_description: "Attempt an action with randomized success or failure."
    help_description: "Display this help menu."

  ru:
    player_only: "Только игроки могут использовать эти команды."
    me_usage: "Использование: /me <действие>"
    do_usage: "Использование: /do <действие>"
    positive_number: "Число должно быть больше нуля."
    roll_usage: "Использование: /roll <число>"
    rolled: " выбросил "
    tries: " пытается "
    succeeds: " и удается!"
    fails: " и не удается!"
    try_usage: "Использование: /try <действие>"
    help_header: "Справка по RP командам:"
    me_description: "Описывает действие от третьего лица."
    do_description: "Описывает происходящее действие или событие."
    roll_description: "Бросает случайное число от 1 до указанного числа."
    try_description: "Пытается выполнить действие с шансом 50/50."
    help_description: "Показывает это сообщение."
    rp_help_usage: "Использование: /rp help"
