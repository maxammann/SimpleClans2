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
 *     Created: 11.09.12 19:46
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class CapeCommand extends GenericPlayerCommand {

    public CapeCommand(SimpleClans plugin)
    {
        super("Cape", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.cape"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("cape.command"));
        setPermission("simpleclans.leader.cape");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.getClan().isVerified() && cp.isLeader() && plugin.getSpoutSupport().isEnabled() && plugin.getSettingsManager().isCapesEnabled()) {
                return MessageFormat.format(Language.getTranslation("menu.cape"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        if (!plugin.getSettingsManager().isCapesEnabled()) {
            return;
        }

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isVerified()) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            return;
        }

        if (!clan.isLeader(cp)) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            return;
        }

        String url = args[0];

        if (url.length() > 5 && url.length() < 255 && url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".png")) {
            if (GeneralHelper.checkConnection(url)) {
                clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("changed.the.clan.cape"), player.getName()));
                clan.getFlags().setClanCapeURL(url);
                clan.update();
            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("url.error"));
            }
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("cape.must.be.png"));
        }
    }
}
