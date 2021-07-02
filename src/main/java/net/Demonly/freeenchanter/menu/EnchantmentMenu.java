package net.Demonly.freeenchanter.menu;

import org.bukkit.Bukkit;
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

public class EnchantmentMenu implements Listener {

    private final Inventory inventory;
    private final ItemStack itemToBeEnchanted;

    public EnchantmentMenu(Player p)
    {

        inventory = Bukkit.createInventory(null, 54, "Enchanter");

        this.itemToBeEnchanted = p.getItemInHand();

        initItems();

    }

    public void initItems()
    {
        inventory.setItem(13, new ItemStack(Material.ENDER_PORTAL_FRAME));
        ArrayList<ItemStack> enchants = getEnchants(itemToBeEnchanted);
        int index = 37;
        for (ItemStack book : enchants){
            inventory.setItem(index, book);
            index +=2;
        }
    }

    public ArrayList<ItemStack> getEnchants(ItemStack item){
        ArrayList<ItemStack> enchants = new ArrayList<>();
        String name = item.getType().name();
        enchants.add(createItem("Unbreaking III"));
        if (name.endsWith("SWORD")){
            enchants.add(createItem("Sharpness V"));
            enchants.add(createItem("Knockback II"));
            enchants.add(createItem("Fire_Aspect II"));
        } else if (name.endsWith("HELMET")){
            enchants.add(createItem("Protection IV"));
            enchants.add(createItem("Respiration III"));
            enchants.add(createItem("Aqua_Affinity I"));
        } else if (name.endsWith("CHESTPLATE")){
            enchants.add(createItem("Protection IV"));
            enchants.add(createItem("Thorns III"));
        } else if (name.endsWith("LEGGINGS")){
            enchants.add(createItem("Protection IV"));
        } else if (name.endsWith("BOOTS")){
            enchants.add(createItem("Protection IV"));
            enchants.add(createItem("Feather_Falling IV"));
            enchants.add(createItem("Depth_Strider III"));
        }
        return enchants;
    }

    protected ItemStack createItem(String name)
    {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(name);
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

        ItemStack EnchantBook = ev.getCurrentItem();
        if (EnchantBook.getType().equals(Material.ENCHANTED_BOOK)) {
            closeInventory(p);
            try {
                if (expLevel >= 3) {
                    String[] enchLvl = EnchantBook.getItemMeta().getDisplayName().split(" ");
                    Enchantment ench = null;
                    int lvl = -1;
                    switch (enchLvl[0]) {
                        case "Sharpness" -> ench = Enchantment.DAMAGE_ALL;
                        case "Unbreaking" -> ench = Enchantment.DURABILITY;
                        case "Fire_Aspect" -> ench = Enchantment.FIRE_ASPECT;
                        case "Knockback" -> ench = Enchantment.KNOCKBACK;
                        case "Thorns" -> ench = Enchantment.THORNS;
                        case "Respiration" -> ench = Enchantment.OXYGEN;
                        case "Aqua Affinity" -> ench = Enchantment.WATER_WORKER;
                        case "Protection" -> ench = Enchantment.PROTECTION_ENVIRONMENTAL;
                        case "Feather_Falling" -> ench = Enchantment.PROTECTION_FALL;
                        case "Depth_Strider" -> ench = Enchantment.DEPTH_STRIDER;
                    }

                    switch (enchLvl[1]){
                        case "I" -> lvl = 1;
                        case "II" -> lvl = 2;
                        case "III" -> lvl = 3;
                        case "IV" -> lvl = 4;
                        case "V" -> lvl = 5;
                    }

                    p.getItemInHand().addEnchantment(ench, lvl);

                    p.sendMessage("Item successfully enchanted!");
                    p.setLevel(expLevel - 3);
                } else {
                    p.sendMessage("You do not have 3 levels to enchant this item");
                }
            } catch (IllegalArgumentException e) {
                p.sendMessage("Enchantment can't be applied to this item");
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
