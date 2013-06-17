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
import com.p000ison.dev.simpleclans2.database.response.responses.BBRetrieveResponse;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a ViewBBCommand
 */
public class ViewBBCommand extends GenericPlayerCommand {

    public ViewBBCommand(SimpleClans plugin) {
        super("View BB", plugin);
        addArgument(Language.getTranslation("argument.page"), true, true);
        setDescription(MessageFormat.format(Language.getTranslation("description.bb"), plugin.getSettingsManager().getBBCommand()));
        setIdentifiers(Language.getTranslation("view.command"));
        addPermission("simpleclans.member.bb");

        setNeedsClan();
        setNeedsClanVerified();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        int page = 0;
        if (arguments.length > 0) {
            try {
                page = Integer.parseInt(arguments[0]);
            } catch (NumberFormatException e) {
                ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("number.format"));
                return;
            }
        }

        getPlugin().getDataManager().addResponse(new BBRetrieveResponse(getPlugin(), player, clan, page, -1, true));
    }
}
