package com.gmail.Xeiotos.HabitatInfo;

import com.gmail.Xeiotos.HabitatAPI.Habitat;
import com.gmail.Xeiotos.HabitatAPI.HabitatAPI;
import com.gmail.Xeiotos.HabitatAPI.HabitatPlayer;
import com.gmail.Xeiotos.HabitatAPI.HabitatPlugin;
import com.gmail.Xeiotos.HabitatAPI.Managers.HabitatPlayerManager;
import com.gmail.Xeiotos.HabitatAPI.Managers.HabitatPluginManager;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Xeiotos
 */
public class HabitatInfo extends JavaPlugin {

    @Override
    public void onEnable() {
        HabitatAPI.getHook(this);
    }

    @Override
    public void onDisable() {
        HabitatAPI.unHook(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "HabitatInfo - " + ChatColor.ITALIC + ChatColor.AQUA + "Available commands:");
            sender.sendMessage(ChatColor.GOLD + "Flags - " + ChatColor.ITALIC + ChatColor.AQUA + "Prints the active flags in this Habitat");
            sender.sendMessage(ChatColor.GOLD + "Description - " + ChatColor.ITALIC + ChatColor.AQUA + "Prints info about this Habitat in human language");
            sender.sendMessage(ChatColor.GOLD + "Me - " + ChatColor.ITALIC + ChatColor.AQUA + "Prints info about you");
        } else if (args.length != 0 && args[0].equals("flags")) {
            LinkedList<String> flags = HabitatPlayerManager.getManager().getHabitatPlayer(player).getHabitat().getFlags();
            if (flags != null) {
                sender.sendMessage(flags.toString());
            } else {
                sender.sendMessage("This Habitat does not have any flags.");
            }
        } else if (args.length != 0 && args[0].equals("flagsinfo")) {
            sender.sendMessage(HabitatPlayerManager.getManager().getHabitatPlayer(player).getHabitat().getFlags().toString());
        } else if (args.length != 0 && args[0].equals("description")) {
            Habitat habitat = HabitatPlayerManager.getManager().getHabitatPlayer(player).getHabitat();
            if (habitat == null) {
                sender.sendMessage("You're not in a habitat!");
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + "Habitat " + ChatColor.ITALIC
                    + ChatColor.AQUA + habitat.getTypeName()
                    + " " + (int) habitat.getRelativeCenter().getX()
                    + "," + (int) habitat.getRelativeCenter().getY());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("This habitat is of type ").append(habitat.getTypeName()).append(", which means that ");
            switch (habitat.getTypeName()) {
                case "Alpha":
                    stringBuilder.append("it isn't altered in any way. Enjoy vanilla gameplay! ");
                    break;
                case "Beta":
                    stringBuilder.append("it may have hidden dungeons, or special terrain.");
                    break;
                case "Gamma":
                    stringBuilder.append("it has certain modifiers. ");
                    break;
                case "Delta":
                    stringBuilder.append("it has certain recurring events. ");
                    break;
                case "Epsilon":
                    stringBuilder.append("it has a combination of Bèta, Gamma or Delta traits. This Habitat has the following: ");
                    break;
                case "Zeta":
                    stringBuilder.append("this is a minigames Habitat; This one is a(n) ");
                    break;
                case "Eta":
                    stringBuilder.append("this habitat belongs to ");
                    break;
                default:
                    break;
            }
            
            if (habitat.getFlags() != null) {
                int i = habitat.getFlags().size();

                LinkedList<String> flags = habitat.getFlags();
                for (String flag : flags) {
                    HabitatPlugin habitatPlugin = HabitatPluginManager.getManager().getPlugin(flag);
                    if (habitatPlugin == null) {
                        continue;
                    }

                    if (i == 0) {
                        stringBuilder.append(habitatPlugin.getDescription().getDescription());
                        stringBuilder.append(".");
                    } else if (i == flags.size()) {
                        stringBuilder.append("You ");
                        stringBuilder.append(habitatPlugin.getDescription().getDescription());
                    } else {
                        stringBuilder.append(", also, you ");
                        stringBuilder.append(habitatPlugin.getDescription().getDescription());
                    }
                    i--;
                }
            } else {
                stringBuilder.append("This Habitat does not have any flags.");
            }
            sender.sendMessage(stringBuilder.toString());
        } else if (args.length != 0 && args[0].equals("me")) {
            HabitatPlayer habitatPlayer = HabitatPlayerManager.getManager().getHabitatPlayer(player);
            StringBuilder voiceLevel = new StringBuilder();
            voiceLevel.append(ChatColor.GOLD).append("Voice - ").append(ChatColor.AQUA).append("[");
            int length = Integer.parseInt(habitatPlayer.getLevel("voice"));
            for (int i = 0; i != length; i++) {
                voiceLevel.append(ChatColor.GREEN).append("#");
            }

            for (int i = 0; i < 10 - length; i++) {
                voiceLevel.append(ChatColor.GRAY).append("#");
            }
            voiceLevel.append(ChatColor.AQUA).append("] - ").append(length).append("/10");
            sender.sendMessage(voiceLevel.toString());
            //TODO
        }

        return true;
    }
}
