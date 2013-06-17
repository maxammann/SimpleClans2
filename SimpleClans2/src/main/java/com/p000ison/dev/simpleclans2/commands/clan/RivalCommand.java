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
*     Last modified: 13.10.12 14:49
*/

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.*;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.RivalryBreakRequest;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Represents a RivalCommand
 */
public class RivalCommand extends GenericPlayerCommand {

    public RivalCommand(SimpleClans plugin) {
        super("Rival", plugin);
        setDescription(Language.getTranslation("description.rival"));
        setIdentifiers(Language.getTranslation("rival.command"));

        CommandExecutor executor = plugin.getCommandManager();
        AnnotatedCommand.ExecutionRestriction restriction = new AnnotatedCommand.ExecutionRestriction() {

            @Override
            public boolean allowExecution(CommandSender commandSender, Command command) {
                ClanPlayer cp = getClanPlayer(commandSender);
                return cp != null && cp.getClan().isVerified() && cp.hasRankPermission("manage.rival");
            }
        };

        Command add = executor.buildByMethod(this, "add").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.rival.add"))
                .setIdentifiers(Language.getTranslation("add.command"))
                .addPermission("simpleclans.leader.rival")
                .addArgument(Language.getTranslation("argument.tag"));
        this.addSubCommand(add);
        Command auto = executor.buildByMethod(this, "remove").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.rival.remove"))
                .setIdentifiers(Language.getTranslation("auto.command"))
                .addPermission("simpleclans.leader.rival")
                .addArgument(Language.getTranslation("argument.tag"));
        this.addSubCommand(auto);
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
    }

    @Override
    public boolean allowExecution(CommandSender sender) {
        return false;
    }

    @CommandHandler(name = "Add rivals")
    public void add(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        Player player = getPlayer(sender);
        String[] arguments = info.getArguments();

        Clan clan = cp.getClan();

        //todo duplicate code
        if (getPlugin().getSettingsManager().isUnRivalAble(clan)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clan.cannot.create.rivals"));
            return;
        }

        if (clan.getSize() < getPlugin().getSettingsManager().getMinimalSizeToRival()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("min.players.rivalries", getPlugin().getSettingsManager().getMinimalSizeToRival()));
            return;
        }

        Clan rival = getPlugin().getClanManager().getClan(arguments[0]);

        if (rival == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (getPlugin().getSettingsManager().isUnRivalAble(rival)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.clan.cannot.be.rivaled"));
            return;
        }

        if (!rival.isVerified()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("cannot.rival.an.unverified.clan"));
            return;
        }
        //

        if (clan.reachedRivalLimit()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("rival.limit.reached"));
            return;
        }

        if (!clan.isRival(rival)) {
            clan.addRival(rival);
            rival.addRival(clan);
            rival.addBBMessage(cp, ChatColor.AQUA + Language.getTranslation("has.initiated.a.rivalry", clan.getName(), rival.getName()));
            clan.addBBMessage(cp, ChatColor.AQUA + Language.getTranslation("has.initiated.a.rivalry", player.getName(), rival.getName()));
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clans.are.already.rivals"));
        }
    }

    @CommandHandler(name = "Remove rivals")
    public void remove(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        Player player = getPlayer(sender);
        String[] arguments = info.getArguments();

        Clan clan = cp.getClan();

        sender.sendMessage(Language.getTranslation("displaying.player", cp.getName()));

        if (getPlugin().getSettingsManager().isUnRivalAble(clan)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clan.cannot.create.rivals"));
            return;
        }

        if (clan.getSize() < getPlugin().getSettingsManager().getMinimalSizeToRival()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("min.players.rivalries", getPlugin().getSettingsManager().getMinimalSizeToRival()));
            return;
        }

        Clan rival = getPlugin().getClanManager().getClan(arguments[0]);

        if (rival == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (getPlugin().getSettingsManager().isUnRivalAble(rival)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.clan.cannot.be.rivaled"));
            return;
        }

        if (!rival.isVerified()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("cannot.rival.an.unverified.clan"));
            return;
        }
        //

        if (clan.isRival(rival)) {
            Set<ClanPlayer> leaders = GeneralHelper.stripOfflinePlayers(rival.getLeaders());

            if (!leaders.isEmpty()) {
                getPlugin().getRequestManager().createRequest(new RivalryBreakRequest(getPlugin(), leaders, cp, rival));
                ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("leaders.asked.to.end.rivalry", rival.getName()));
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("at.least.one.leader.break.the.rivalry"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clans.are.no.rivals"));
        }
    }
}