package me.alvin.duck;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDuck implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("duck.command")) {
                player.sendMessage(DuckPlugin.getInstance().getMessage("command.noPermission"));
                return true;
            }

            if (args.length >= 1) {
                if ("reload".equalsIgnoreCase(args[0])) {
                    if (!player.hasPermission("duck.command.reload")) {
                        player.sendMessage(DuckPlugin.getInstance().getMessage("command.noPermission"));
                        return true;
                    }

                    // DuckPlugin.getInstance().reloadConfig();
                    DuckPlugin.getInstance().reloadMessages();

                    player.sendMessage(DuckPlugin.getInstance().getMessage("command.reload"));

                } else if ("spawn".equalsIgnoreCase(args[0])) {
                    if (!player.hasPermission("duck.command.spawn")) {
                        player.sendMessage(DuckPlugin.getInstance().getMessage("command.noPermission"));
                        return true;
                    }
                    if (args.length >= 2) {
                        if ("hostile".equalsIgnoreCase(args[1])) {
                                if (!player.hasPermission("duck.command.spawn.hostile")) {
                                player.sendMessage(DuckPlugin.getInstance().getMessage("command.noPermission"));
                                return true;
                            }

                            DuckPlugin.getInstance().spawnDuck(player.getLocation(), DuckType.HOSTILE);
                            player.sendMessage(DuckPlugin.getInstance().getMessage("command.duck.spawn.hostile"));
                        } else if ("scared".equalsIgnoreCase(args[1])) {
                            if (!player.hasPermission("duck.command.spawn.scared")) {
                                player.sendMessage(DuckPlugin.getInstance().getMessage("command.noPermission"));
                                return true;
                            }

                            DuckPlugin.getInstance().spawnDuck(player.getLocation(), DuckType.SCARED);
                            player.sendMessage(DuckPlugin.getInstance().getMessage("command.duck.spawn.scared"));
                        } else if ("neutral".equalsIgnoreCase(args[1])) {
                            if (!player.hasPermission("duck.command.spawn.neutral")) {
                                player.sendMessage(DuckPlugin.getInstance().getMessage("command.noPermission"));
                                return true;
                            }

                            DuckPlugin.getInstance().spawnDuck(player.getLocation(), DuckType.NEUTRAL);
                            player.sendMessage(DuckPlugin.getInstance().getMessage("command.duck.spawn.neutral"));
                        } else {
                            player.sendMessage(DuckPlugin.getInstance().getMessage("command.duck.spawn.unknown"));
                        }
                    } else {
                        if (!player.hasPermission("duck.command.spawn.neutral")) {
                            player.sendMessage(DuckPlugin.getInstance().getMessage("command.noPermission"));
                            return true;
                        }

                        DuckPlugin.getInstance().spawnDuck(player.getLocation(), DuckType.NEUTRAL);
                        player.sendMessage(DuckPlugin.getInstance().getMessage("command.duck.spawn.neutral"));
                    }
                }
            }
        }
        return true;
    }
}
