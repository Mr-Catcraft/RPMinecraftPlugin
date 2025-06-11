package org.mr_catcraft.rpplugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class RPPlugins extends JavaPlugin {
    private String language;
    private double messageDistance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        language = config.getString("language", "en");
        messageDistance = config.getDouble("settings.message_distance", 100.0);

        getLogger().info("RPPlugin load!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RPPlugin unload!");
    }

    private String getMessage(String key) {
        return getConfig().getString("messages." + language + "." + key, "[Missing message: " + key + "]");
    }

    private void sendMessageNearby(Player player, String message, ChatColor color) {
        Location playerLocation = player.getLocation();
        for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
            if (nearbyPlayer.getWorld().equals(player.getWorld()) &&
                    nearbyPlayer.getLocation().distance(playerLocation) <= messageDistance) {
                nearbyPlayer.sendMessage(color + message);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage("player_only"));
            return true;
        }

        Player player = (Player) sender;

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
        String message = "*" + player.getName() + " " + String.join(" ", args) + "*";
        sendMessageNearby(player, message, ChatColor.LIGHT_PURPLE);
    }

    private void handleDoCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("do_usage"));
            return;
        }
        String message = "*" + String.join(" ", args) + " (" + player.getName() + ")*";
        sendMessageNearby(player, message, ChatColor.YELLOW);
    }

    private void handleRollCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("roll_usage"));
            return;
        }
        try {
            int max = Integer.parseInt(args[0]);
            if (max <= 0) {
                player.sendMessage(ChatColor.RED + getMessage("positive_number"));
                return;
            }
            int roll = new Random().nextInt(max) + 1;
            String message = player.getName() + " " + getMessage("rolled") + " " + roll + " (1-" + max + ")";
            sendMessageNearby(player, message, ChatColor.GREEN);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + getMessage("roll_usage"));
        }
    }

    private void handleTryCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + getMessage("try_usage"));
            return;
        }
        boolean success = new Random().nextBoolean();
        String message = player.getName() + " " + getMessage("tries") + " " + String.join(" ", args) + " " + (success ? getMessage("succeeds") : getMessage("fails"));
        sendMessageNearby(player, message, ChatColor.BLUE);
    }

    private void handleRpCommand(Player player, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            player.sendMessage(ChatColor.GOLD + getMessage("help_header"));
            player.sendMessage(ChatColor.AQUA + "/me <action>" + ChatColor.WHITE + " - " + getMessage("me_description"));
            player.sendMessage(ChatColor.AQUA + "/do <action>" + ChatColor.WHITE + " - " + getMessage("do_description"));
            player.sendMessage(ChatColor.AQUA + "/roll <number>" + ChatColor.WHITE + " - " + getMessage("roll_description"));
            player.sendMessage(ChatColor.AQUA + "/try <action>" + ChatColor.WHITE + " - " + getMessage("try_description"));
            player.sendMessage(ChatColor.AQUA + "/rp help" + ChatColor.WHITE + " - " + getMessage("help_description"));
        } else {
            player.sendMessage(ChatColor.RED + getMessage("rp_help_usage"));
        }
    }
}
