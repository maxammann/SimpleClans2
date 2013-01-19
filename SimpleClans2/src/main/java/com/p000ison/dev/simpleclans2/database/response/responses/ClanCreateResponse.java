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
 *     Last modified: 18.01.13 20:52
 */

package com.p000ison.dev.simpleclans2.database.response.responses;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.database.response.Response;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a BBRetrieveResponse
 */
public class ClanCreateResponse extends Response {

    private final Clan clan;
    private final ClanPlayer clanPlayer;

    public ClanCreateResponse(SimpleClans plugin, Clan clan, Player player, ClanPlayer clanPlayer) {
        super(plugin, player);
        this.clan = clan;
        this.clanPlayer = clanPlayer;
    }

    @Override
    public boolean response() {
        if (plugin.getClanManager().createClan(clan) == null) {
            getRetriever().sendMessage(ChatColor.DARK_RED + Language.getTranslation("clan.creation.failed"));
            return true;
        }

        getRetriever().sendMessage(ChatColor.GREEN + Language.getTranslation("clan.created", clan.getName()));
        clan.addBBMessage(clanPlayer, MessageFormat.format(Language.getTranslation("clan.created"), clan.getName()));
        plugin.broadcast(ChatColor.AQUA + Language.getTranslation("broadcast.clan.created", clan.getName(), clan.getTag()));
        return true;
    }

    @Override
    public boolean needsRetriever() {
        return true;
    }
}
