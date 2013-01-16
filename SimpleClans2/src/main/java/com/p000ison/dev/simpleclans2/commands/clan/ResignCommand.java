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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;


public class ResignCommand extends GenericPlayerCommand {

    public ResignCommand(SimpleClans plugin) {
        super("Resign", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.resign"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("resign.command"));
        setPermission("simpleclans.member.resign");
    }

    @Override
    public String getMenu(ClanPlayer cp) {
        if (cp != null) {
            return MessageFormat.format(Language.getTranslation("menu.resign"), plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (!clan.isLeader(cp) || clan.getLeaders().size() > 1) {
                clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("0.has.resigned"), player.getName()));
                clan.removeMember(cp);
            } else if (clan.isLeader(cp) && clan.getLeaders().size() == 1) {
                plugin.serverAnnounce(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("clan.has.been.disbanded"), clan.getName()));
                clan.disband();
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("last.leader.cannot.resign.you.must.appoint.another.leader.or.disband.the.clan"));
            }

        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}
