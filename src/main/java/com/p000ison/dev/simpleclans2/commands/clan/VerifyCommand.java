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
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a VerifyCommand
 */
public class VerifyCommand extends GenericPlayerCommand {

    public VerifyCommand(SimpleClans plugin)
    {
        super("Verify", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.verify"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("verify.command"));
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (!cp.getClan().isVerified() && plugin.getSettingsManager().requireVerification() && plugin.getSettingsManager().isPurchaseVerification()) {
                return MessageFormat.format(Language.getTranslation("menu.verify"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (plugin.getSettingsManager().requireVerification()) {
            if (!clan.isVerified()) {

                if (SimpleClans.hasEconomy() && plugin.getSettingsManager().isPurchaseVerification()) {
                    if (!SimpleClans.withdrawBalance(player.getName(), plugin.getSettingsManager().getPurchaseVerificationPrice())) {
                        player.sendMessage(ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
                        return;
                    }
                }

                clan.setVerified(true);
                clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("clan.0.has.been.verified"), clan.getName()));
                clan.update();
                player.sendMessage(ChatColor.AQUA + Language.getTranslation("the.clan.has.been.verified"));
            } else {
                player.sendMessage(ChatColor.GRAY + Language.getTranslation("your.clan.is.already.verified"));
            }
        } else {
            player.sendMessage(ChatColor.GRAY + Language.getTranslation("you.dont.need.to.verify"));
        }
    }
}