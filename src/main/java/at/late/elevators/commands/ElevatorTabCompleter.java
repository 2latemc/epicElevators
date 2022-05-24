package at.late.elevators.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ElevatorTabCompleter implements TabCompleter {
    List<String> arguments = new ArrayList<String>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(arguments.isEmpty()) {
            arguments.add("info");
            arguments.add("give");
            arguments.add("reload");
        }
        List<String> results = new ArrayList<String>();
        if(args.length == 1) {
            for(String a : arguments) {
                if(a.toLowerCase().startsWith(args[0].toLowerCase())) results.add(a);
            }
            return results;
        }
        return null;
    }
}
