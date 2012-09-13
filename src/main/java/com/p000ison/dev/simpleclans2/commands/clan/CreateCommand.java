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
 *     Created: 02.09.12 23:40
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a CreateCommand
 */
public class CreateCommand extends GenericPlayerCommand {

    public CreateCommand(SimpleClans plugin)
    {
        super("CreateCommand", plugin);
        setArgumentRange(2, 20);
        setUsages(MessageFormat.format(Language.getTranslation("usage.create"), plugin.getSettingsManager().getClanCommand()), Language.getTranslation("example.clan.create"));
        setIdentifiers(Language.getTranslation("create.command"));
        setPermission("simpleclans.leader.create");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp == null) {
            if (plugin.getSettingsManager().isPurchaseCreation()) {
                return MessageFormat.format(Language.getTranslation("menu.create.purchase"), plugin.getSettingsManager().getClanCommand());
            } else {
                return MessageFormat.format(Language.getTranslation("menu.create.default"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {

        String tag = args[0];
        String cleanTag = ChatColor.stripColor(args[0]);

        String name = GeneralHelper.arrayBoundsToString(1, args);

        if (player.hasPermission("simpleclans.mod.bypass")) {
            if (cleanTag.length() >= plugin.getSettingsManager().getMaxTagLenght()) {
                player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.tag.cannot.be.longer.than.characters"), plugin.getSettingsManager().getMaxTagLenght()));
                return;
            }

            if (cleanTag.length() < plugin.getSettingsManager().getMinTagLenght()) {
                player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.tag.must.be.longer.than.characters"), plugin.getSettingsManager().getMinTagLenght()));
                return;
            }

            if (GeneralHelper.containsColor(tag, '&', plugin.getSettingsManager().getDisallowedColors())) {
                player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.tag.cannot.contain.the.following.colors"), GeneralHelper.arrayToString(plugin.getSettingsManager().getDisallowedColors())));
                return;
            }

            if (ChatColor.stripColor(name).length() >= plugin.getSettingsManager().getMaxTagLenght()) {
                player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.name.cannot.be.longer.than.characters"), plugin.getSettingsManager().getMaxTagLenght()));
                return;
            }

            if (ChatColor.stripColor(name).length() < plugin.getSettingsManager().getMinTagLenght()) {
                player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.name.must.be.longer.than.characters"), plugin.getSettingsManager().getMinTagLenght()));
                return;
            }

            if (plugin.getSettingsManager().isTagDisallowed(cleanTag.toLowerCase())) {
                player.sendMessage(ChatColor.RED + Language.getTranslation("that.tag.name.is.disallowed"));
                return;
            }
        }

        if (!cleanTag.matches("[0-9a-zA-Z]*")) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("your.clan.tag.can.only.contain.letters.numbers.and.color.codes"));
            return;
        }

        if (name.contains("&")) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("your.clan.name.cannot.contain.color.codes"));
            return;
        }

        ClanPlayer cp = plugin.getClanPlayerManager().getCreateClanPlayerExact(player);

        if (cp.getClan() != null) {
            player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("you.must.first.resign"), cp.getClan().getName()));
            return;
        }

        if (plugin.getClanManager().existsClan(cleanTag)) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("clan.with.this.tag.already.exists"));
            return;
        }

        if (SimpleClans.hasEconomy() && plugin.getSettingsManager().isPurchaseCreation()) {
            if (!SimpleClans.withdrawBalance(player.getName(), plugin.getSettingsManager().getPurchaseCreationPrice())) {
                player.sendMessage(ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
                return;
            }
        }

        Clan clan = plugin.getClanManager().createClan(args[0], name);
        cp.setLeader(true);
        cp.setTrusted(true);
        cp.setClan(clan);
        cp.update();
        clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("clan.created"), name));

        if (plugin.getSettingsManager().requireVerification()) {
            boolean verified = !plugin.getSettingsManager().requireVerification() || player.hasPermission("simpleclans.mod.verify");

            if (!verified) {
                player.sendMessage(ChatColor.AQUA + Language.getTranslation("get.your.clan.verified.to.access.advanced.features"));
            }

        } else {
            clan.setVerified(true);
        }

        clan.update();

    }
}