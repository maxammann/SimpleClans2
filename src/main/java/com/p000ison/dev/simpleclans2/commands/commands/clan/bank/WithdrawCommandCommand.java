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
 *     Created: 11.09.12 19:46
 */

package com.p000ison.dev.simpleclans2.commands.commands.clan.bank;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import net.sacredlabyrinth.phaed.simpleclans.results.BankResult;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;


public class WithdrawCommandCommand extends GenericPlayerCommand {


    public WithdrawCommandCommand(SimpleClans plugin)
    {
        super("Bank", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.bank.withdraw"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("bank.withdraw.command"));
        setPermission("simpleclans.member.bank.withdraw");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.isTrusted() && cp.getClan().isVerified()) {
                return MessageFormat.format(Language.getTranslation("bank.withdraw.deposit.status"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {
        if (!SimpleClans.hasEconomy()) {
            return;
        }

        if (!SimpleClans.hasBankSupport()) {
            return;
        }

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {

            Clan clan = cp.getClan();

            if (!clan.isMember(cp)) {
                return;
            }
            if (!clan.isVerified()) {
                player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
                return;
            }

            if (cp.isTrusted()) {
                player.sendMessage(ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.stats"));
                return;
            }

            double amount;

            try {
                amount = Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                return;
            }



            BankResult result = null;


            if (cp.getClan().isLeader(cp) || clan.isGlobalAllowWithdraw()) {
                if (args[1].equalsIgnoreCase("all")) {
                    amount = clanbalance;
                    result = clan.withdraw(clanbalance, cp);
                } else {
                    amount = money;
                    result = clan.withdraw(money, cp);
                }
            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            }


            if (result != null) {

                switch (result) {
                    case BANK_NOT_ENOUGH_MONEY:
                        player.sendMessage(ChatColor.AQUA + Language.getTranslation("clan.bank.not.enough.money"));
                        break;
                    case PLAYER_NOT_ENOUGH_MONEY:
                        player.sendMessage(ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
                        break;
                    case SUCCESS_DEPOSIT:
                        player.sendMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("player.clan.deposit"), amount));
                        clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(Language.getTranslation("bb.clan.deposit"), amount));
                        break;
                    case SUCCESS_WITHDRAW:
                        player.sendMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("player.clan.withdraw"), amount));
                        clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(Language.getTranslation("bb.clan.withdraw"), amount));
                        break;
                    case FAILED:
                        player.sendMessage(ChatColor.DARK_RED + Language.getTranslation("transaction.failed"));
                        break;
                }
            }


        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}
