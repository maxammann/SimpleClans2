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

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Represents a BanCommand
 */
public class BanCommand extends GenericConsoleCommand {

    public BanCommand(SimpleClans plugin) {
        super("Ban", plugin);
        addArgument(Language.getTranslation("argument.player"));
        setDescription(Language.getTranslation("description.ban"));
        setIdentifiers(Language.getTranslation("ban.command"));
        addPermission("simpleclans.mod.ban");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments, CallInformation info) {
        ClanPlayer clanPlayer = getPlugin().getClanPlayerManager().getCreateClanPlayerExact(arguments[0]);

        if (!clanPlayer.isBanned()) {
            Player player = getPlugin().getServer().getPlayerExact(clanPlayer.getName());

            if (player != null) {
                ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("you.banned"));
            }

            getPlugin().getClanPlayerManager().ban(clanPlayer);
            ChatBlock.sendMessage(sender, ChatColor.AQUA + Language.getTranslation("player.added.to.banned.list"));
        } else {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("this.player.is.already.banned"));
        }
    }
}
