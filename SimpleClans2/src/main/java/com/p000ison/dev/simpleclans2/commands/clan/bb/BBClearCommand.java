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

package com.p000ison.dev.simpleclans2.commands.clan.bb;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a ViewBBCommand
 */
public class BBClearCommand extends GenericPlayerCommand {

    public BBClearCommand(SimpleClans plugin) {
        super("Clear BB", plugin);
        setDescription(MessageFormat.format(Language.getTranslation("description.bb.clear"), plugin.getSettingsManager().getBBCommand()));
        setIdentifiers(Language.getTranslation("bb.clear.command"));
        addPermission("simpleclans.member.bb-clear");

        setNeedsClan();
        setNeedsClanVerified();
        setNeedsTrusted();
        setAsynchronous(true);

        setRankPermission("manage.bb");
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {

        Clan clan = cp.getClan();

        DatabaseManager dataManager = getPlugin().getDataManager();

        dataManager.purgeBB(clan);
        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("cleared.bb"));
    }
}
