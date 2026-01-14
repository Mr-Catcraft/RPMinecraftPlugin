![Background](https://cdn.modrinth.com/data/cached_images/8b3c6021b616d2a90fe3691f2833b436065010a7.png)

# RPPlugin

**RPPlugin** is a comprehensive roleplay-focused plugin for Minecraft servers Spigot/Paper 1.16.x - 1.21.1 that enhances immersive roleplay with advanced character interaction commands. Perfect for RP servers, events, or survival communities seeking deeper in-character communication.

## 🎭 Features

### Core Roleplay Commands
- **`/me <action>`**: Describe your character's actions in the third person
- **`/do <action>`**: Describe surrounding events and environmental actions
- **`/try <action>`**: Attempt an action with configurable success chance
- **`/roll <number>`**: Roll dice with advanced dice notation support

### New Enhanced Features
- **`/whisper <message>`**: Send messages with short distance for private conversations
- **`/yell <message>`**: Broadcast messages with extended range for shouting
- **`/emote <emote> [target]`**: Perform predefined emotes with optional targets
- **Advanced Dice System**: Support for complex dice expressions (2d6+3, 1d20, etc.)
- **Customizable Message Formats**: Fully customizable output messages with color codes
- **Distance Control**: Toggle distance-based message delivery on/off
- **Emote Library**: Built-in collection of common roleplay emotes

## 💬 Commands

| Command | Description | Usage Examples |
|---------|-------------|----------------|
| **`/me`** | Describe your character's actions | `/me smiles warmly` |
| **`/do`** | Describe events in the world | `/do A cold breeze passes by` |
| **`/roll`** | Roll dice with various formats | `/roll 20`, `/roll 5-15`, `/roll 2d6+3` |
| **`/try`** | Attempt an action with chance | `/try open the rusty door` |
| **`/whisper`** | Send a quiet message | `/whisper Hey, come here` |
| **`/yell`** | Shout a message loudly | `/yell Help! I'm over here!` |
| **`/emote`** | Perform an emote | `/emote wave`, `/emote smile John` |
| **`/rp help`** | Show command help | `/rp help` |

## 🎲 Dice System Examples

The plugin supports multiple dice notation formats:

```yaml
/roll 20           # Roll 1-20 (traditional)
/roll 5-15         # Roll between 5 and 15
/roll d6           # Roll one six-sided die
/roll 2d10         # Roll two ten-sided dice
/roll 1d20+5       # Roll d20 and add 5
/roll 3d6+2        # Roll three d6 and add 2
```

## 😄 Available Emotes

The plugin includes a library of common roleplay emotes:

- `wave` - Waves at someone
- `cry` - Cries
- `dance` - Dances happily
- `laugh` - Laughs out loud
- `bow` - Bows respectfully
- `nod` - Nods in agreement
- `shake` - Shakes head in disagreement
- `shrug` - Shrugs shoulders
- `smile` - Smiles warmly
- `wink` - Winks playfully

You can easily add more emotes in the configuration file!

## ⚙️ Configuration

The plugin features extensive configuration with multiple language support:

```yaml
# Language settings
language: "en"

# Plugin settings
settings:
  message_distance: 100.0    # Default message range
  use_distance: true         # Enable/disable distance check
  try_success_chance: 0.5    # Success chance for /try command

# Message formatting
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

# Emote definitions
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

# Multi-language support
messages:
  en:
    # English messages...
  ru:
    # Russian messages...
```

## 🔧 Installation

1. Download the latest `RPPlugin.jar`
2. Place it in your server's `plugins/` directory
3. Restart your server
4. Configure `plugins/RPPlugin/config.yml` to your preferences
5. Use `/rp help` in-game to see available commands

## 📋 Requirements

- **Minecraft Server**: Spigot or Paper 1.16.x - 1.21.1
- **Java**: Version 21 or higher recommended
