package net.Demonly.freeenchanter.listeners;

import net.Demonly.freeenchanter.FreeEnchanter;
import net.Demonly.freeenchanter.menu.EnchantmentMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class CustomEnchanterListener implements Listener
{
    private final FreeEnchanter plugin;

    public CustomEnchanterListener(FreeEnchanter plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent ev)
    {
        Player p = ev.getPlayer();
        final ItemStack item = ev.getPlayer().getInventory().getItemInHand();
        Block block = ev.getClickedBlock();

        if (ev.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && isArmorOrWeapon(item)
                    && block.getType().equals(Material.ENDER_PORTAL_FRAME)
                        && p.hasPermission("enchanter.use"))
        {
            EnchantmentMenu menu = new EnchantmentMenu(p);
            plugin.getServer().getPluginManager().registerEvents(menu, plugin);
            menu.openInventory(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWear(PlayerInteractEvent ev)
    {
        if (ev.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if (isArmorOrWeapon(ev.getPlayer().getItemInHand()))
            {
                if (ev.getPlayer().getTargetBlock((Set<Material>) null, 10).getType().equals(Material.ENDER_PORTAL_FRAME))
                {
                    ev.setCancelled(true);
                }
            }
        }
    }

    private Boolean isArmorOrWeapon(ItemStack item)
    {
        if (null != item)
        {
            String name = item.getType().name();
            return name.endsWith("_HELMET")
                    || name.endsWith("_CHESTPLATE")
                    || name.endsWith("_LEGGINGS")
                    || name.endsWith("_BOOTS")
                    || name.endsWith("SWORD")
                    || name.endsWith("AXE");
        }

        return false;
    }
}
