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
 *     Created: 06.09.12 22:07
 */

package com.p000ison.dev.simpleclans2.commands.voting;

import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.requests.Request;
import com.p000ison.dev.simpleclans2.requests.VoteResult;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a AcceptCommand
 */
public class AbstainCommand extends GenericPlayerCommand {

    public AbstainCommand(SimpleClans plugin)
    {
        super("Abstain", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.abstain"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("abstain.command"));
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer)
    {
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getCreateClanPlayerExact(player);
        Request request = plugin.getRequestManager().vote(clanPlayer, VoteResult.ABSTAINED);

        if (request != null) {
            request.announceMessage(Language.getTranslation("voted.to.abstain", player.getDisplayName()));
        } else {
            player.sendMessage(Language.getTranslation("nothing.to.abstain"));
        }
    }
}
