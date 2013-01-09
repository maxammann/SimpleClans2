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
 *     Last modified: 04.11.12 17:01
 */

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

/**
 * Represents a VerifyModCommand
 */
public class VerifyModCommand extends GenericConsoleCommand {

    public VerifyModCommand(SimpleClans plugin)
    {
        super("VerifyMod", plugin);
        setArgumentRange(1, 1);
        setUsages(Language.getTranslation("usage.verifyclan", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("verifyclan.command"));
        setPermission("simpleclans.mod.verify");
    }

    @Override
    public String getMenu()
    {
        if (plugin.getSettingsManager().requireVerification()) {
            return ChatColor.DARK_RED + MessageFormat.format(Language.getTranslation("menu.verifyclan"), plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        Clan clan = plugin.getClanManager().getClan(args[0]);

        if (clan != null) {
            if (!clan.isVerified()) {
                clan.setVerified(true);
                clan.addBBMessage(Language.getTranslation("clan.0.has.been.verified", clan.getName()));
                clan.update();
                ChatBlock.sendMessage(sender, ChatColor.AQUA + Language.getTranslation("the.clan.has.been.verified"));
            } else {
                ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("the.clan.is.already.verified"));
            }
        } else {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("the.clan.does.not.exist"));
        }
    }
}