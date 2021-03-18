package net.Demonly.freeenchanter;

import net.Demonly.freeenchanter.listeners.CustomEnchanterListener;
import org.bukkit.plugin.java.JavaPlugin;

public class FreeEnchanter extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        System.out.println("[FreeEnchanter] FreeEnchantments for everyone!");

        getServer().getPluginManager().registerEvents(new CustomEnchanterListener(this), this);
    }

    @Override
    public void onDisable()
    {
        System.out.println("[FreeEnchanter] No more enchantment for you!!");
    }
}
