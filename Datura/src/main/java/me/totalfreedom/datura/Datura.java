package me.totalfreedom.datura;

import me.totalfreedom.base.Patchwork;
import me.totalfreedom.datura.features.Fuckoff;
import me.totalfreedom.datura.punishment.Cager;
import me.totalfreedom.datura.punishment.Halter;
import me.totalfreedom.datura.punishment.Locker;
import me.totalfreedom.datura.sql.MySQL;
import me.totalfreedom.service.SubscriptionProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Datura extends JavaPlugin
{
    private final MySQL sql = new MySQL("localhost", 3011, "master");
    private final Halter halter = new Halter();
    private final Locker locker = new Locker();
    private final Cager cager = new Cager();
    private final Fuckoff fuckoff = new Fuckoff();

    @Override
    public void onEnable()
    {
        Patchwork.getInstance()
                 .getRegistrations()
                 .getModuleRegistry()
                 .addModule(this);

        Patchwork.getInstance()
                 .getRegistrations()
                 .getServiceTaskRegistry()
                 .registerService(SubscriptionProvider.syncService(this, locker));
        Patchwork.getInstance()
                 .getRegistrations()
                 .getServiceTaskRegistry()
                 .registerService(SubscriptionProvider.syncService(this, cager));
        Patchwork.getInstance()
                .getRegistrations()
                .getServiceTaskRegistry()
                .registerService(SubscriptionProvider.syncService(this, fuckoff));

        Bukkit.getPluginManager()
              .registerEvents(halter, this);
    }

    public MySQL getSQL()
    {
        return sql;
    }

    public Halter getHalter()
    {
        return halter;
    }

    public Locker getLocker()
    {
        return locker;
    }

    public Cager getCager()
    {
        return cager;
    }

    public Fuckoff getFuckoff() {
        return fuckoff;
    }
}
