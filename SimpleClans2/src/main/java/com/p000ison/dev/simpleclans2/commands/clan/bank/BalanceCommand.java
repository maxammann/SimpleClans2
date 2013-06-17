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

import com.p000ison.dev.commandlib.CallInformation;
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

    public BalanceCommand(SimpleClans plugin) {
        super("Bank Balance", plugin);
        setDescription(MessageFormat.format(Language.getTranslation("description.bank.balance"), plugin.getSettingsManager().getBankCommand()));
        setIdentifiers(Language.getTranslation("bank.balance.command"));
        addPermission("simpleclans.member.bank.balance");

        setNeedsClanVerified();
        setNeedsTrusted();
        setNeedsClan();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("current.clan.balance", NumberFormat.getNumberInstance().format(clan.getBalance())));
    }
}
