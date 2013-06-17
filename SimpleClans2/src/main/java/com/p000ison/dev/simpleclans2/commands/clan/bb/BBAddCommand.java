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
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a ViewBBCommand
 */
public class BBAddCommand extends GenericPlayerCommand {

    public BBAddCommand(SimpleClans plugin) {
        super("Add message to BB", plugin);
        setInfinite(true);
        addArgument(Language.getTranslation("argument.message"));
        setDescription(MessageFormat.format(Language.getTranslation("description.bb.add"), plugin.getSettingsManager().getBBCommand()));
        setIdentifiers(Language.getTranslation("bb.add.command"));
        addPermission("simpleclans.member.bb-add");

        setNeedsClan();
        setNeedsClanVerified();
        setNeedsTrusted();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        String msg = GeneralHelper.arrayToString(arguments);
        if (msg == null) {
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("please.enter.message"));
            return;
        }

        clan.addBBMessage(cp, msg);
        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("bb.added"));
    }
}
