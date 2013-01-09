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
 *     Last modified: 31.10.12 00:56
 */

package com.p000ison.dev.simpleclans2.commands.clan.bank;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.text.NumberFormat;

/**
 * Represents a DepositCommand
 */
public class BalanceCommand extends GenericPlayerCommand {

    public BalanceCommand(SimpleClans plugin)
    {
        super("BalanceCommand", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.bank.balance"), plugin.getSettingsManager().getBankCommand()));
        setIdentifiers(Language.getTranslation("bank.balance.command"));
        setPermission("simpleclans.member.bank.balance");
        setType(Type.BANK);
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.getClan().isVerified() && cp.isTrusted()) {
            return MessageFormat.format(Language.getTranslation("menu.bank.balance"), plugin.getSettingsManager().getBankCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isVerified()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            return;
        }

        if (!cp.isTrusted()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.bank"));
            return;
        }

        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("current.clan.balance", NumberFormat.getNumberInstance().format(clan.getBalance())));
    }
}
