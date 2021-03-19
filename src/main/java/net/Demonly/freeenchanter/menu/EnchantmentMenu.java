package net.Demonly.freeenchanter.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentMenu implements Listener {

    private Inventory inventory;
    private ItemStack itemToBeEnchanted;
    private ItemStack enchantmentItem;
    private Enchantment enchantment;
    private int level;

    public EnchantmentMenu(Enchantment enchantment, int level, Player p)
    {
        this.enchantment = enchantment;
        this.level = level;

        setEnchantmentItem();
        inventory = Bukkit.createInventory(null, 54, "Free Enchantment");

        this.itemToBeEnchanted = p.getItemInHand();

        initItems();

    }

    public void initItems()
    {
        inventory.setItem(13, this.itemToBeEnchanted);
        inventory.setItem(31, createItem(enchantmentItem.getType(), enchantmentItem.getItemMeta().getDisplayName(), enchantmentItem.getItemMeta().getLore()));
    }

    protected ItemStack createItem(Material material, String name, List<String> lore)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(HumanEntity player)
    {
        player.openInventory(inventory);
    }

    public void closeInventory(HumanEntity player)
    {
        player.closeInventory();
    }

    private void setEnchantmentItem()
    {
        ArrayList<String> lore = new ArrayList<String>() {{
            add(ChatColor.GRAY + enchantment.getName() + " " + level);
        }};

        ItemStack enchantmentItem = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = enchantmentItem.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Click to add enchantment:");
        meta.setLore(lore);
        enchantmentItem.setItemMeta(meta);
        this.enchantmentItem = enchantmentItem;

    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent ev)
    {
        if (!ev.getInventory().equals(inventory))
        {
            return;
        }
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent ev)
    {
        Player p = (Player) ev.getWhoClicked();
        int expLevel = p.getLevel();

        if (!ev.getInventory().equals(inventory))
        {
            return;
        }

        ev.setCancelled(true);

        if (ev.getSlot() == 31)
        {
            closeInventory(p);
            try {
                if (expLevel >= 3) {
                    p.getItemInHand().addEnchantment(enchantment, level);

                    p.sendMessage("[FreeEnchantment]: Item Enchantment Added: " + enchantment.getName() + " " + level);
                    p.setLevel(expLevel - 3);
                } else {
                    p.sendMessage("[FreeEnchantment]: You do not have 3 levels to enchant this item");
                }
            } catch (IllegalArgumentException e) {
                p.sendMessage("[FreeEnchantment]: Enchantment can't be applied to this item");
            }
            HandlerList.unregisterAll(this);
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent ev)
    {
        if (!ev.getInventory().equals(inventory))
        {
            return;
        }
        ev.setCancelled(true);
    }
}
