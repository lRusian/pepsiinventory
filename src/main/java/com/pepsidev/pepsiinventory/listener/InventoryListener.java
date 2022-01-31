package com.pepsidev.pepsiinventory.listener;

import com.pepsidev.pepsiinventory.Pepsi;
import com.pepsidev.pepsiinventory.util.CC;
import com.pepsidev.pepsiinventory.util.ItemMaker;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import java.util.*;

public class InventoryListener implements Listener
{
    public static ItemStack BLANK;

    public void getInventory(final Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 9 * Pepsi.get().getConfig().getInt("menu.rows"), CC.translate(Pepsi.get().getConfig().getString("menu.name")));
        for (int i = 0; i < inventory.getSize(); ++i) {
            inventory.setItem(i, InventoryListener.BLANK);
        }

        for (final String serverName : Pepsi.get().getConfig().getConfigurationSection("menu.category").getKeys(false)) {
            inventory.setItem(Pepsi.get().getConfig().getInt("menu.category." + serverName + ".slot") - 1, this.getItem(player, serverName));
        }
        player.openInventory(inventory);
    }

    public ItemStack getItem(final Player player, final String serverName) {
        final List<String> lore = new ArrayList<String>();

        for (String string : Pepsi.get().getConfig().getStringList("menu.category." + serverName + ".lore")) {

            String product_name = Pepsi.get().getConfig().getString("menu.category." + serverName + ".name");

                lore.add(CC.translate(string)
                        .replace("<player_name>", player.getName())
                        .replace("<product_name>", String.valueOf(product_name))
                        .replace("|", "\u2503")
                );

        }
        Material material = Material.getMaterial(Pepsi.get().getConfig().getString("crystal.material"));
        int data = Pepsi.get().getConfig().getInt("crystal.data");
        if (((Pepsi.get().getConfig().getBoolean("crystal.enabled")) || !Pepsi.get().getConfig().getBoolean("crystal.enabled"))) {
            material = Material.matchMaterial(Pepsi.get().getConfig().getString("menu.category." + serverName + ".material"));
            data = Pepsi.get().getConfig().getInt("menu.category." + serverName + ".data");
        }
        final ItemMaker setTitle = new ItemMaker(material)
                .setData(data)
                .setLore(lore)
                .setTitle(Pepsi.get().getConfig().getString("menu.category." + serverName + ".name").replace("%player%", player.getName()));

        final ItemStack itemStack = setTitle.setAmount(Pepsi.get().getConfig().getInt("menu.category." + serverName + ".amount")).build();
        return itemStack;

    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getInventory() != event.getClickedInventory()) return;
        final Material material = Material.getMaterial(Pepsi.get().getConfig().getString("crystal.material"));
        if (event.getInventory().getTitle().equals(CC.translate(Pepsi.get().getConfig().getString("menu.name")))) {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);

            if (!(event.getCurrentItem().getType() != material)) {
                //player.closeInventory();
                return;
            }

            for (final String serverName : Pepsi.get().getConfig().getConfigurationSection("menu.category").getKeys(false)) {

                if (event.getSlot() == Pepsi.get().getConfig().getInt("menu.category." + serverName + ".slot") - 1 && event.getCurrentItem() != null) {
                    event.setCancelled(true);
                    List<String> commands = Pepsi.get().getConfig().getStringList("menu.category." + serverName + ".executor");
                    commands.forEach(action -> {
                        action = action.replace("<player>", player.getName());
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), action);
                        return;
                    });
                }


            }
        }
    }


    static {

        InventoryListener.BLANK = new ItemMaker(Material.STAINED_GLASS_PANE).setData(0).setTitle(" ").build();
    }
}
