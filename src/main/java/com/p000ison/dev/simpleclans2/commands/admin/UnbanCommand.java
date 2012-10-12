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

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;


public class UnbanCommand extends GenericConsoleCommand {

    public UnbanCommand(SimpleClans plugin)
    {
        super("Unban", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.Command"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("Command.command"));
        setPermission("simpleclans.mod.unban");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format(Language.getTranslation("menu.unban"), plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String command, String[] args)
    {

        ClanPlayer banned = plugin.getClanPlayerManager().getAnyClanPlayerExact(args[0]);

        if (banned == null) {
            sender.sendMessage(Language.getTranslation("no.player.matched"));
            return;
        }

        if (banned.isBanned()) {
            Player bannedPlayer = banned.toPlayer();

            if (bannedPlayer != null) {
                bannedPlayer.sendMessage(ChatColor.AQUA + Language.getTranslation("you.have.been.unbanned.from.clan.commands"));
            }

            banned.setBanned(false);
            banned.update();
            sender.sendMessage(ChatColor.AQUA + Language.getTranslation("player.removed.from.the.banned.list"));
        } else {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("this.player.is.not.banned"));
        }
    }
}
