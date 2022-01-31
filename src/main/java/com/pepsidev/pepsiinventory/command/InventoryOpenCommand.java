package com.pepsidev.pepsiinventory.command;

import com.pepsidev.pepsiinventory.listener.InventoryListener;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class InventoryOpenCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;

        if(args[0].equalsIgnoreCase("open")){
            new InventoryListener().getInventory(p);
        }

        return false;
    }

}