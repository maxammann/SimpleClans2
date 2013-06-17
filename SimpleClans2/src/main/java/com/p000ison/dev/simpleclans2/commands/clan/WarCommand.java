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
 *     Last modified: 02.11.12 16:54
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.*;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.WarStartRequest;
import com.p000ison.dev.simpleclans2.requests.requests.WarStopRequest;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Represents a WarCommand
 */
public class WarCommand extends GenericPlayerCommand {

    public WarCommand(SimpleClans plugin) {
        super("War", plugin);
        setDescription(Language.getTranslation("description.war"));
        setIdentifiers(Language.getTranslation("war.command"));

        setNeedsClan();
        setNeedsLeader();
        setNeedsClanVerified();

        CommandExecutor executor = plugin.getCommandManager();

        AnnotatedCommand.ExecutionRestriction restriction = new AnnotatedCommand.ExecutionRestriction() {
            @Override
            public boolean allowExecution(CommandSender sender, Command command) {
                ClanPlayer cp = getClanPlayer(sender);
                return cp != null && cp.getClan().isVerified();
            }
        };

        Command start = executor.buildByMethod(this, "start").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.war.start"))
                .addPermission("simpleclans.leader.war")
                .setIdentifiers(Language.getTranslation("start.command"))
                .addArgument(Language.getTranslation("argument.tag"));
        this.addSubCommand(start);
        Command end = executor.buildByMethod(this, "end").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.war.end"))
                .addPermission("simpleclans.leader.war")
                .setIdentifiers(Language.getTranslation("end.command"))
                .addArgument(Language.getTranslation("argument.tag"));
        this.addSubCommand(end);
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
    }

    @Override
    protected boolean displayHelpEntry(ClanPlayer cp, org.bukkit.command.CommandSender sender) {
        return false;
    }

    @CommandHandler(name = "Start war")
    public void start(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        Clan clan = cp.getClan();
        String[] arguments = info.getArguments();

        Clan toWarring = getPlugin().getClanManager().getClan(arguments[0]);

        if (toWarring == null) {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (!clan.isRival(toWarring)) {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("you.can.only.start.war.with.rivals"));
            return;
        }

        if (!clan.isWarring(toWarring)) {
            Set<ClanPlayer> onlineLeaders = GeneralHelper.stripOfflinePlayers(toWarring.getLeaders());

            if (!onlineLeaders.isEmpty()) {
                getPlugin().getRequestManager().createRequest(new WarStartRequest(getPlugin(), onlineLeaders, cp, toWarring));
                sender.sendMessage(ChatColor.AQUA + Language.getTranslation("leaders.have.been.asked.to.accept.the.war.request", toWarring.getName()));
            } else {
                sender.sendMessage(ChatColor.RED + Language.getTranslation("at.least.one.leader.accept.the.war.start"));
            }

        } else {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("clans.already.at.war"));
        }
    }

    @CommandHandler(name = "End war")
    public void end(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        Clan clan = cp.getClan();
        String[] arguments = info.getArguments();

        Clan toWarring = getPlugin().getClanManager().getClan(arguments[0]);

        if (toWarring == null) {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (!clan.isRival(toWarring)) {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("you.can.only.start.war.with.rivals"));
            return;
        }

        if (clan.isWarring(toWarring)) {

            Set<ClanPlayer> onlineLeaders = GeneralHelper.stripOfflinePlayers(toWarring.getLeaders());
            if (!onlineLeaders.isEmpty()) {
                getPlugin().getRequestManager().createRequest(new WarStopRequest(getPlugin(), onlineLeaders, cp, toWarring));
                sender.sendMessage(ChatColor.AQUA + Language.getTranslation("leaders.asked.to.end.rivalry", toWarring.getName()));
            } else {
                sender.sendMessage(ChatColor.RED + Language.getTranslation("at.least.one.leader.accept.the.war.stop"));
            }
        } else {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("clans.not.at.war"));
        }
    }
}