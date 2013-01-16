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
 *     Last modified: 16.10.12 20:02
 */

package com.p000ison.dev.simpleclans2.clanplayer;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * Represents a CraftOnlineClanPlayer
 */
public class CraftOnlineClanPlayer implements com.p000ison.dev.simpleclans2.api.clanplayer.OnlineClanPlayer {

    private SimpleClans plugin;
    private ClanPlayer clanPlayer;
    private PermissionAttachment attachment;

    public CraftOnlineClanPlayer(SimpleClans plugin, ClanPlayer clanPlayer) {
        this.plugin = plugin;
        this.clanPlayer = clanPlayer;
    }

    @Override
    public Player toPlayer() {
        return plugin.getServer().getPlayerExact(clanPlayer.getName());
    }

    @Override
    public void setupPermissions() {
        if (clanPlayer.isTrusted()) {
            registerPermission("SCTrusted");
        } else {
            registerPermission("SCUntrusted");
        }

        if (clanPlayer.isLeader()) {
            registerPermission("SCLeader");
        }

        Clan clan = clanPlayer.getClan();
        if (clan != null) {
            registerPermission("SC" + clan.getID());
        }
    }

    private void registerPermission(String perm) {
        if (plugin.getServer().getPluginManager().getPermission(perm) != null) {
            if (attachment == null) {
                attachment = toPlayer().addAttachment(plugin);
            }

            attachment.setPermission(perm, true);
        }
    }

    @Override
    public void removePermissions() {
        if (attachment != null) {
            attachment.remove();
            attachment = null;
        }
    }
}
