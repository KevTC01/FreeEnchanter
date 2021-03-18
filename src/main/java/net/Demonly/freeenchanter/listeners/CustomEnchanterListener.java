package net.Demonly.freeenchanter.listeners;

import net.Demonly.freeenchanter.FreeEnchanter;
import net.Demonly.freeenchanter.menu.EnchantmentMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class CustomEnchanterListener implements Listener
{
    private FreeEnchanter plugin;

    public CustomEnchanterListener(FreeEnchanter plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent ev)
    {
        Player p = ev.getPlayer();
        ItemStack item = ev.getPlayer().getInventory().getItemInMainHand();
        Block block = ev.getClickedBlock();

        if (ev.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && isArmorOrWeapon(item)
                    && block.getType().equals(Material.END_PORTAL_FRAME)
                        && p.hasPermission("enchanter.use"))
        {
            beginEnchantment(item, p);
        }
    }

    private Boolean isArmorOrWeapon(ItemStack item)
    {
        if (null != item)
        {
            String name = item.getType().name();
            if (name.endsWith("_HELMET")
            || name.endsWith("_CHESTPLATE")
            || name.endsWith("_LEGGINGS")
            || name.endsWith("_BOOTS")
            || name.endsWith("SWORD")
            || name.endsWith("AXE")) {
                return true;
            }
        }

        return false;
    }

    private void beginEnchantment(ItemStack item, Player p)
    {
        Enchantment randomEnchantment = Enchantment.values()[(int) (Math.random()*Enchantment.values().length)];

        int level;
        if (randomEnchantment.getMaxLevel() != 1) {
            level = ThreadLocalRandom.current().nextInt(1, randomEnchantment.getMaxLevel());
        } else {
            level = 1;
        }

        EnchantmentMenu menu = new EnchantmentMenu(randomEnchantment, item, level);
        plugin.getServer().getPluginManager().registerEvents(menu, plugin);
        menu.openInventory(p);

    }
}
