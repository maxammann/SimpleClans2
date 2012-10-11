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

package com.p000ison.dev.simpleclans2.commands.members;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class FFCommand extends GenericPlayerCommand {

    public FFCommand(SimpleClans plugin)
    {
        super("FFCommand", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.ff"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("ff.command"));
        setPermission("simpleclans.member.ff");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            return MessageFormat.format(Language.getTranslation("menu.ff"), plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            String action = args[0];

            if (action.equalsIgnoreCase(Language.getTranslation("allow"))) {
                cp.setFriendlyFire(true);
                cp.update();
                player.sendMessage(ChatColor.AQUA + Language.getTranslation("personal.friendly.fire.is.set.to.allowed"));
            } else if (action.equalsIgnoreCase(Language.getTranslation("auto"))) {
                cp.setFriendlyFire(false);
                cp.update();
                player.sendMessage(ChatColor.AQUA + Language.getTranslation("friendy.fire.is.now.managed.by.your.clan"));
            } else {
                player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("usage.0.ff.allow.auto"), plugin.getSettingsManager().getClanCommand()));
            }
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}
