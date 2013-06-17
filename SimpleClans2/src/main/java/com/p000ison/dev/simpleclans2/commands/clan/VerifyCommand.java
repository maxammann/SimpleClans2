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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a VerifyCommand
 */
public class VerifyCommand extends GenericPlayerCommand {

    public VerifyCommand(SimpleClans plugin) {
        super("Verify", plugin);
        setDescription(Language.getTranslation("description.verify"));
        addPermission("simpleclans.member.verify");
        setIdentifiers(Language.getTranslation("verify.command"));

        setNeedsClan();
        setNeedsClanNotVerified();
    }

    @Override
    protected boolean displayHelpEntry(ClanPlayer cp, CommandSender sender) {
        return getPlugin().getSettingsManager().requireVerification() && getPlugin().getSettingsManager().isPurchaseVerification();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        if (getPlugin().getSettingsManager().requireVerification()) {
            if (!clan.isVerified()) {

                if (SimpleClans.hasEconomy() && getPlugin().getSettingsManager().isPurchaseVerification() && !cp.withdraw(getPlugin().getSettingsManager().getPurchaseVerificationPrice())) {
                    ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
                    return;
                }

                clan.setVerified(true);
                clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("clan.0.has.been.verified"), clan.getName()));
                clan.update();
                ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("the.clan.has.been.verified"));
            } else {
                ChatBlock.sendMessage(player, ChatColor.GRAY + Language.getTranslation("you.dont.need.to.verify"));
            }
        }
    }
}