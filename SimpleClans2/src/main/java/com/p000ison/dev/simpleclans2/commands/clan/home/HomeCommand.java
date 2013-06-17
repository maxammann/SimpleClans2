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

package com.p000ison.dev.simpleclans2.commands.clan.home;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class HomeCommand extends GenericPlayerCommand {


    public HomeCommand(SimpleClans plugin) {
        super("Home", plugin);
        setDescription(Language.getTranslation("description.home"));
        setIdentifiers(Language.getTranslation("home.command"));
        addPermission("simpleclans.member.home");

        setNeedsClan();
        setNeedsClanVerified();
    }


    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        if (SimpleClans.hasEconomy() && getPlugin().getSettingsManager().isPurchaseTeleport() && !cp.withdraw(getPlugin().getSettingsManager().getPurchaseTeleportPrice())) {
            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
            return;
        }

        Location loc = clan.getFlags().getHomeLocation();

        if (loc == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("hombase.not.set"));
            return;
        }

        getPlugin().getTeleportManager().addPlayer(player, loc, ChatColor.AQUA + MessageFormat.format(Language.getTranslation("now.at.homebase"), clan.getName()));
    }
}
