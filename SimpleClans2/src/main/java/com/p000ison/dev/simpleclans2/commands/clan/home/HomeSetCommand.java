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
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
//todo add rank permissions
public class HomeSetCommand extends GenericPlayerCommand {

    public HomeSetCommand(SimpleClans plugin) {
        super("Set home", plugin);
        setDescription(Language.getTranslation("description.home.set"));
        setIdentifiers(Language.getTranslation("home.set.command"));
        addPermission("simpleclans.leader.home-set");

        setNeedsClan();
        setNeedsClanVerified();
        setNeedsTrusted();

        setNeedsLeader();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        Location loc = player.getLocation();


        if (getPlugin().getPreciousStonesSupport().isTeleportAllowed(player, loc)) {
            if (getPlugin().getSettingsManager().isSetHomeOnlyOnce() && clan.getFlags().getHomeLocation() != null && !player.hasPermission("simpleclans.mod.home")) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("home.base.only.once"));
                return;
            }

            if (SimpleClans.hasEconomy() && getPlugin().getSettingsManager().isPurchaseSetTeleport() && !cp.withdraw(getPlugin().getSettingsManager().getPurchaseTeleportSetPrice())) {
                ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
                return;
            }

            clan.getFlags().setHomeLocation(loc);
            clan.update();
            ChatBlock.sendMessage(player, ChatColor.AQUA + MessageFormat.format(Language.getTranslation("hombase.set"), ChatColor.YELLOW + GeneralHelper.locationToString(loc)));
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.teleport"));
        }
    }
}
