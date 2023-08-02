package fns.corvo;

import fns.patchwork.base.Patchwork;
import org.bukkit.plugin.java.JavaPlugin;

public class Corvo extends JavaPlugin
{
    @Override
    public void onDisable()
    {
        Patchwork.getInstance()
                 .getRegistrations()
                 .getModuleRegistry()
                 .removeModule(this);
    }

    @Override
    public void onEnable()
    {
        Patchwork.getInstance()
                 .getRegistrations()
                 .getModuleRegistry()
                 .addModule(this);
    }
}
