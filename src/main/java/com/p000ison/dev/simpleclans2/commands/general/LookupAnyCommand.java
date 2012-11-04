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
 *     Last modified: 29.10.12 13:15
 */

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents a LookupAnyCommand
 */
public class LookupAnyCommand extends GenericConsoleCommand {

    public LookupAnyCommand(SimpleClans plugin)
    {
        super("LookupAnyCommand", plugin);
        setArgumentRange(1, 1);
        setUsages(Language.getTranslation("usage.lookup.any", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("lookup.command"));
        setPermission("simpleclans.anyone.lookup");
    }

    @Override
    public String getMenu()
    {
        return Language.getTranslation("menu.lookup.any", plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getAnyClanPlayer(args[0]);

        if (clanPlayer != null) {
            Clan clan = clanPlayer.getClan();
            clanPlayer.showProfile(sender, clan);
        } else {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("no.player.data.found"));
        }
    }
}