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

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class CapeCommand extends GenericPlayerCommand {

    public CapeCommand(SimpleClans plugin) {
        super("Cape", plugin);
        addArgument(Language.getTranslation("argument.url"));
        setDescription(Language.getTranslation("description.cape"));
        setIdentifiers(Language.getTranslation("cape.command"));
        addPermission("simpleclans.leader.cape");

        setNeedsClan();
        setNeedsClanVerified();
        setNeedsLeader();
    }

    @Override
    protected boolean displayHelpEntry(ClanPlayer cp, CommandSender sender) {
        return getPlugin().getSpoutSupport().isEnabled() && getPlugin().getSettingsManager().isCapesEnabled();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        if (!getPlugin().getSettingsManager().isCapesEnabled()) {
            return;
        }

        Clan clan = cp.getClan();

        String url = arguments[0];

        if (url.length() > 5 && url.length() < 255 && url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".png")) {
            if (GeneralHelper.checkConnection(url)) {
                clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("changed.the.clan.cape"), player.getName()));
                clan.getFlags().setClanCapeURL(url);
                clan.update();
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("url.error"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("cape.must.be.png"));
        }
    }
}
