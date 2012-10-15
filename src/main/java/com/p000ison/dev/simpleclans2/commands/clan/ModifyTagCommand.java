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
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
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
        setUsages(MessageFormat.format(Language.getTranslation("usage.modtag"), plugin.getSettingsManager().getClanCommand()), ChatColor.RED + Language.getTranslation("example.clan.modtag.4kfo.4l"));
        setIdentifiers(Language.getTranslation("modtag.command"));
        setPermission("simpleclans.leader.modtag");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.isLeader() && cp.getClan().isVerified()) {
            return MessageFormat.format(Language.getTranslation("0.modtag.tag.1.modify.the.clan.s.tag"), plugin.getSettingsManager().getClanCommand());
        }

        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {
//        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);
//
//        if (cp != null) {
//            Clan clan = cp.getClan();
//
//            if (clan.isVerified()) {
//                if (clan.isLeader(cp)) {
//
//                    String newTag = args[0];
//                    String cleanTag = Helper.cleanTag(newtag);
//
//                    if (Helper.stripColors(newtag).length() <= plugin.getSettingsManager().getTagMaxLength()) {
//                        if (!plugin.getSettingsManager().hasDisallowedColor(newtag)) {
//                            if (Helper.stripColors(newtag).matches("[0-9a-zA-Z]*")) {
//                                if (cleantag.equals(clan.getTag())) {
//                                    clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(Language.getTranslation("tag.changed.to.0"), Helper.parseColors(newtag)));
//                                    clan.changeClanTag(newtag);
//                                    plugin.getClanManager().updateDisplayName(player.getPlayer());
//                                } else {
//                                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.can.only.modify.the.color.and.case.of.the.tag"));
//                                }
//                            } else {
//                                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clan.tag.can.only.contain.letters.numbers.and.color.codes"));
//                            }
//                        } else {
//                            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(Language.getTranslation("your.tag.cannot.contain.the.following.colors"), plugin.getSettingsManager().getDisallowedColorString()));
//                        }
//
//                    } else {
//                        ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.tag.cannot.be.longer.than.characters"), plugin.getSettingsManager().getTagMaxLength()));
//                    }
//
//                } else {
//                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
//                }
//            } else {
//                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
//            }
//        } else {
//            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
//        }
    }
}