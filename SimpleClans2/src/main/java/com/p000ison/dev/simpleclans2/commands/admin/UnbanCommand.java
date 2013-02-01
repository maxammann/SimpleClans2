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
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class UnbanCommand extends GenericConsoleCommand {

    public UnbanCommand(SimpleClans plugin) {
        super("Unban", plugin);
        setArgumentRange(1, 1);
        setUsages(Language.getTranslation("usage.unban"));
        setIdentifiers(Language.getTranslation("unban.command"));
        setPermission("simpleclans.mod.unban");
    }

    @Override
    public String getMenu() {
        return Language.getTranslation("menu.unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        ClanPlayer banned = plugin.getClanPlayerManager().getAnyClanPlayerExact(args[0]);

        if (banned == null) {
            ChatBlock.sendMessage(sender, Language.getTranslation("no.player.matched"));
            return;
        }

        if (banned.isBanned()) {
            Player bannedPlayer = banned.toPlayer();

            if (bannedPlayer != null) {
                ChatBlock.sendMessage(bannedPlayer, ChatColor.AQUA + Language.getTranslation("you.have.been.unbanned.from.clan.commands"));
            }

            banned.setBanned(false);
            banned.update();
            ChatBlock.sendMessage(sender, ChatColor.AQUA + Language.getTranslation("player.removed.from.the.banned.list"));
        } else {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("this.player.is.not.banned"));
        }
    }
}
