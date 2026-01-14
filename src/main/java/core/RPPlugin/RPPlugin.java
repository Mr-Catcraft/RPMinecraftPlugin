package core.RPPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RPPlugin extends JavaPlugin implements TabCompleter {
    private String language;
    private double messageDistance;
    private boolean useDistance;
    private double trySuccessChance;
    private Map<String, String> emoteMap;
    private Map<String, String> formatMap;
    private Random random;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadPluginConfig();

        Objects.requireNonNull(getCommand("me")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("do")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("roll")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("try")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("whisper")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("yell")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("emote")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("rp")).setTabCompleter(this);

        getLogger().info("RPPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RPPlugin disabled!");
    }

    private void reloadPluginConfig() {
        reloadConfig();
        FileConfiguration config = getConfig();
        language = config.getString("language", "en");
        messageDistance = config.getDouble("settings.message_distance", 100.0);
        useDistance = config.getBoolean("settings.use_distance", true);
        trySuccessChance = config.getDouble("settings.try_success_chance", 0.5);

        emoteMap = new HashMap<>();
        if (config.contains("emotes")) {
            for (String key : Objects.requireNonNull(config.getConfigurationSection("emotes")).getKeys(false)) {
                emoteMap.put(key.toLowerCase(), config.getString("emotes." + key));
            }
        }

        formatMap = new HashMap<>();
        if (config.contains("formats")) {
            for (String key : Objects.requireNonNull(config.getConfigurationSection("formats")).getKeys(false)) {
                formatMap.put(key, config.getString("formats." + key));
            }
        }

        random = new Random();
    }

    private String getMessage(String key) {
        return getConfig().getString("messages." + language + "." + key, "[Missing message: " + key + "]");
    }

    private String formatMessage(String key, Map<String, String> placeholders) {
        String format = formatMap.get(key);
        if (format == null) {
            return null;
        }

        String result = format;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("%" + entry.getKey() + "%", entry.getValue());
        }

        return ChatColor.translateAlternateColorCodes('&', result);
    }

    private void sendFormattedMessage(Player player, String formatKey, Map<String, String> placeholders, ChatColor defaultColor) {
        String formatted = formatMessage(formatKey, placeholders);

        if (formatted != null) {
            sendMessageNearby(player, formatted);
        } else {
            String message = defaultColor + placeholders.get("raw_message");
            sendMessageNearby(player, message);
        }
    }

    private void sendMessageNearby(Player player, String message) {
        Location playerLocation = player.getLocation();

        for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
            if (nearbyPlayer.getWorld().equals(player.getWorld())) {
                if (!useDistance || nearbyPlayer.getLocation().distance(playerLocation) <= messageDistance) {
                    nearbyPlayer.sendMessage(message);
                }
            }
        }
    }

    private int[] parseDiceExpression(String expression) {
        Pattern pattern = Pattern.compile("(\\d+)?d(\\d+)([+-]\\d+)?");
        Matcher matcher = pattern.matcher(expression.toLowerCase());

        if (matcher.matches()) {
            int count = matcher.group(1) != null ? Integer.parseInt(matcher.group(1)) : 1;
            int sides = Integer.parseInt(matcher.group(2));
            int modifier = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;

            return new int[]{count, sides, modifier};
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getMessage("player_only"));
            return true;
        }

        switch (label.toLowerCase()) {
            case "me":
                handleMeCommand(player, args);
                break;
            case "do":
                handleDoCommand(player, args);
                break;
            case "roll":
                handleRollCommand(player, args);
                break;
            case "try":
                handleTryCommand(player, args);
                break;
            case "whisper":
                handleWhisperCommand(player, args);
                break;
            case "yell":
                handleYellCommand(player, args);
                break;
            case "emote":
                handleEmoteCommand(player, args);
                break;
            case "rp":
                handleRpCommand(player, args);
                break;
            default:
                return false;
        }

        return true;
    }

    private void handleMeCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("me_usage"));
            return;
        }

        String action = String.join(" ", args);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        placeholders.put("action", action);
        placeholders.put("raw_message", "*" + player.getName() + " " + action + "*");

        sendFormattedMessage(player, "me", placeholders, ChatColor.LIGHT_PURPLE);
    }

    private void handleDoCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("do_usage"));
            return;
        }

        String action = String.join(" ", args);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        placeholders.put("action", action);
        placeholders.put("raw_message", "*" + action + " (" + player.getName() + ")*");

        sendFormattedMessage(player, "do", placeholders, ChatColor.YELLOW);
    }

    private void handleRollCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("roll_usage"));
            return;
        }

        String input = args[0];
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());

        try {
            if (input.contains("-")) {
                String[] range = input.split("-");
                if (range.length != 2) {
                    player.sendMessage(ChatColor.RED + getMessage("roll_usage"));
                    return;
                }

                int min = Integer.parseInt(range[0]);
                int max = Integer.parseInt(range[1]);

                if (min > max) {
                    int temp = min;
                    min = max;
                    max = temp;
                }

                int roll = random.nextInt(max - min + 1) + min;

                placeholders.put("roll", String.valueOf(roll));
                placeholders.put("min", String.valueOf(min));
                placeholders.put("max", String.valueOf(max));
                placeholders.put("raw_message", player.getName() + " " + getMessage("rolled") + " " + roll + " (" + min + "-" + max + ")");

                sendFormattedMessage(player, "roll_range", placeholders, ChatColor.GREEN);

            } else if (input.toLowerCase().contains("d")) {
                int[] dice = parseDiceExpression(input);

                if (dice == null) {
                    player.sendMessage(ChatColor.RED + getMessage("roll_usage"));
                    return;
                }

                int count = dice[0];
                int sides = dice[1];
                int modifier = dice[2];

                if (count <= 0 || sides <= 0) {
                    player.sendMessage(ChatColor.RED + getMessage("positive_number"));
                    return;
                }

                int[] rolls = new int[count];
                int total = 0;
                StringBuilder details = new StringBuilder("[");

                for (int i = 0; i < count; i++) {
                    rolls[i] = random.nextInt(sides) + 1;
                    total += rolls[i];
                    details.append(rolls[i]);
                    if (i < count - 1) details.append(", ");
                }
                details.append("]");

                total += modifier;

                placeholders.put("roll", String.valueOf(total));
                placeholders.put("expression", input);
                placeholders.put("details", details.toString());
                placeholders.put("modifier", String.valueOf(modifier));
                placeholders.put("raw_message", player.getName() + " " + getMessage("rolled") + " " + total + " (" + input + ")");

                sendFormattedMessage(player, "roll_dice", placeholders, ChatColor.GREEN);

            } else {
                int max = Integer.parseInt(input);

                if (max <= 0) {
                    player.sendMessage(ChatColor.RED + getMessage("positive_number"));
                    return;
                }

                int roll = random.nextInt(max) + 1;

                placeholders.put("roll", String.valueOf(roll));
                placeholders.put("max", String.valueOf(max));
                placeholders.put("raw_message", player.getName() + " " + getMessage("rolled") + " " + roll + " (1-" + max + ")");

                sendFormattedMessage(player, "roll_simple", placeholders, ChatColor.GREEN);
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + getMessage("roll_usage"));
        }
    }

    private void handleTryCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("try_usage"));
            return;
        }

        boolean success = random.nextDouble() < trySuccessChance;
        String action = String.join(" ", args);

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        placeholders.put("action", action);

        if (success) {
            placeholders.put("raw_message", player.getName() + " " + getMessage("tries") + " " + action + " " + getMessage("succeeds"));
            sendFormattedMessage(player, "try_success", placeholders, ChatColor.BLUE);
        } else {
            placeholders.put("raw_message", player.getName() + " " + getMessage("tries") + " " + action + " " + getMessage("fails"));
            sendFormattedMessage(player, "try_fail", placeholders, ChatColor.BLUE);
        }
    }

    private void handleWhisperCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("whisper_usage"));
            return;
        }

        String message = String.join(" ", args);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        placeholders.put("message", message);
        placeholders.put("raw_message", ChatColor.GRAY + "[Whisper] " + player.getName() + ": " + message);

        sendFormattedMessage(player, "whisper", placeholders, ChatColor.GRAY);
    }

    private void handleYellCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("yell_usage"));
            return;
        }

        String message = String.join(" ", args);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        placeholders.put("message", message);
        placeholders.put("raw_message", ChatColor.RED + "[Yell] " + player.getName() + ": " + message);

        double yellDistance = messageDistance * 2;
        Location playerLocation = player.getLocation();

        for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
            if (nearbyPlayer.getWorld().equals(player.getWorld()) &&
                    nearbyPlayer.getLocation().distance(playerLocation) <= yellDistance) {

                String formatted = formatMessage("yell", placeholders);
                nearbyPlayer.sendMessage(Objects.requireNonNullElseGet(formatted, () -> ChatColor.RED + "[Yell] " + player.getName() + ": " + message));
            }
        }
    }

    private void handleEmoteCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("emote_usage"));
            return;
        }

        String emoteKey = args[0].toLowerCase();
        String emoteText = emoteMap.get(emoteKey);

        if (emoteText == null) {
            player.sendMessage(ChatColor.RED + getMessage("emote_not_found"));
            return;
        }

        StringBuilder target = new StringBuilder();
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                target.append(args[i]).append(" ");
            }
        }

        String targetStr = target.toString().trim();
        String message = "*" + player.getName() + " " + emoteText + (targetStr.isEmpty() ? "" : " " + targetStr) + "*";

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        placeholders.put("emote", emoteText);
        placeholders.put("target", targetStr);
        placeholders.put("raw_message", message);

        sendFormattedMessage(player, "emote", placeholders, ChatColor.GOLD);
    }

    private void handleRpCommand(Player player, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            player.sendMessage(ChatColor.GOLD + getMessage("help_header"));
            player.sendMessage(ChatColor.AQUA + "/me <action>" + ChatColor.WHITE + " - " + getMessage("me_description"));
            player.sendMessage(ChatColor.AQUA + "/do <action>" + ChatColor.WHITE + " - " + getMessage("do_description"));
            player.sendMessage(ChatColor.AQUA + "/roll <number>" + ChatColor.WHITE + " - " + getMessage("roll_description"));
            player.sendMessage(ChatColor.AQUA + "/try <action>" + ChatColor.WHITE + " - " + getMessage("try_description"));
            player.sendMessage(ChatColor.AQUA + "/whisper <message>" + ChatColor.WHITE + " - " + getMessage("whisper_description"));
            player.sendMessage(ChatColor.AQUA + "/yell <message>" + ChatColor.WHITE + " - " + getMessage("yell_description"));
            player.sendMessage(ChatColor.AQUA + "/emote <emote> [target]" + ChatColor.WHITE + " - " + getMessage("emote_description"));
            player.sendMessage(ChatColor.AQUA + "/rp help" + ChatColor.WHITE + " - " + getMessage("help_description"));
        } else {
            player.sendMessage(ChatColor.RED + getMessage("rp_help_usage"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        String commandName = command.getName().toLowerCase();

        switch (commandName) {
            case "me":
            case "do":
            case "try":
            case "whisper":
            case "yell":
                if (args.length == 1) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                            completions.add(player.getName());
                        }
                    }
                }
                break;

            case "roll":
                if (args.length == 1) {
                    List<String> examples = new ArrayList<>();
                    examples.add("10");
                    examples.add("1-20");
                    examples.add("d6");
                    examples.add("2d10");
                    examples.add("1d20+5");
                    examples.add("3d6+2");

                    for (String example : examples) {
                        if (example.toLowerCase().startsWith(args[0].toLowerCase())) {
                            completions.add(example);
                        }
                    }

                    for (int i = 1; i <= 20; i++) {
                        String num = String.valueOf(i);
                        if (num.startsWith(args[0])) {
                            completions.add(num);
                        }
                    }
                }
                break;

            case "emote":
                if (args.length == 1) {
                    for (String emote : emoteMap.keySet()) {
                        if (emote.toLowerCase().startsWith(args[0].toLowerCase())) {
                            completions.add(emote);
                        }
                    }
                } else if (args.length == 2) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completions.add(player.getName());
                        }
                    }
                }
                break;

            case "rp":
                if (args.length == 1) {
                    if ("help".startsWith(args[0].toLowerCase())) {
                        completions.add("help");
                    }
                }
                break;
        }

        Collections.sort(completions);
        return completions;
    }
}