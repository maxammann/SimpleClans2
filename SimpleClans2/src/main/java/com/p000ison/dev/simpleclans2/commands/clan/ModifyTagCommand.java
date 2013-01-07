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
 *     Last modified: 13.10.12 19:03
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a ModifyTagCommand
 */
public class ModifyTagCommand extends GenericPlayerCommand {

    public ModifyTagCommand(SimpleClans plugin)
    {
        super("Modtag", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.modtag"), plugin.getSettingsManager().getClanCommand()), ChatColor.RED + Language.getTranslation("example.clan.modtag"));
        setIdentifiers(Language.getTranslation("modtag.command"));
        setPermission("simpleclans.leader.modtag");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.isLeader() && cp.getClan().isVerified()) {
            return MessageFormat.format(Language.getTranslation("menu.modtag"), plugin.getSettingsManager().getClanCommand());
        }

        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isVerified()) {
                if (clan.isLeader(cp)) {

                    String newTag = ChatBlock.parseColors(args[0]);
                    String tagBefore = clan.getTag();
                    boolean bypass = player.hasPermission("simpleclans.mod.bypass");

                    if (!plugin.getClanManager().verifyClanTag(player, newTag, tagBefore, bypass)) {
                        return;
                    }

                    if (plugin.getSettingsManager().isModifyTagCompletely()) {
                        if (plugin.getClanManager().existsClanByTag(newTag)) {
                            player.sendMessage(ChatColor.RED + Language.getTranslation("clan.with.this.tag.already.exists"));
                            return;
                        }
                    }

                    clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("tag.changed.to.0"), newTag));
                    clan.setTag(newTag);
                    clan.update();
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}