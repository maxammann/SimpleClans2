/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 10.10.12 21:57
 */

package com.p000ison.dev.simpleclans2.support;

import com.p000ison.dev.simpleclans2.api.logging.Logging;
import net.sacredlabyrinth.Phaed.PreciousStones.FieldFlag;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import net.sacredlabyrinth.Phaed.PreciousStones.vectors.Field;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Represents a PreciousStonesSupport
 */
public class PreciousStonesSupport {

    private PreciousStones preciousStones;

    public PreciousStonesSupport() {
        Plugin preciousStonesRaw = Bukkit.getServer().getPluginManager().getPlugin("PreciousStones");

        if (!(preciousStonesRaw instanceof PreciousStones)) {
            Logging.debug("PreciousStones not found! Skipping!");
            return;
        }

        Logging.debug("Hooked PreciousStones!");

        preciousStones = (PreciousStones) preciousStonesRaw;
    }


    public boolean isTeleportAllowed(Player player, Location location) {
        if (preciousStones == null) {
            return true;
        }

        List<Field> fields = preciousStones.getForceFieldManager().getSourceFields(location, FieldFlag.PREVENT_TELEPORT);

        if (fields != null && !fields.isEmpty()) {
            for (Field field : fields) {
                boolean allowed = preciousStones.getForceFieldManager().isAllowed(field, player.getName());

                if (!allowed || field.hasFlag(FieldFlag.ALL)) {
                    return false;
                }
            }
        }

        return true;
    }
}
