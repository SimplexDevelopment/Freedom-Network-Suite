package fns.fossil.trail.types;

import fns.patchwork.particle.TrailType;
import fns.patchwork.utils.InterpolationUtils;
import java.util.Iterator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public final class RainbowTrail extends SimpleTrail
{
    private Iterator<Color> currentColor;

    protected RainbowTrail(final Player player)
    {
        super(player, TrailType.DEFAULT);
        setColors(InterpolationUtils.rainbow(40 % 7));
        this.currentColor = getColors().iterator();
    }

    @Override
    public void spawnParticle()
    {
        // Exit immediately if either case is false.
        if (!isActive() || !getAssociatedPlayer().isOnline()) return;

        // Re-initialize the color iterator if the iterator has previously reached the end of its index.
        if (!currentColor.hasNext())
        {
            this.currentColor = getColors().iterator();
        }

        final Color color = currentColor.next();
        final Player player = (Player) getAssociatedPlayer();
        final Particle particle = getTrailType().getType();
        final Particle.DustOptions options = new Particle.DustOptions(color, 3F);
        final Location location = player.getLocation()
                                        .clone()
                                        .subtract(0, 1, 0);

        location.getWorld()
                .spawnParticle(particle, location, 1, 0.0, 0.5, 0.0, options);
    }
}