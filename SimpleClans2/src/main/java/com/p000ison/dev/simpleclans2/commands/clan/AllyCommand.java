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

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.*;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.AllyCreateRequest;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a AllyCommand
 */
public class AllyCommand extends GenericPlayerCommand {

    public AllyCommand(SimpleClans plugin) {
        super("Manage allies", plugin);
        setDescription(Language.getTranslation("description.ally"));
        setIdentifiers(Language.getTranslation("ally.command"));

        CommandExecutor executor = plugin.getCommandManager();
        AnnotatedCommand.ExecutionRestriction restriction = new AnnotatedCommand.ExecutionRestriction() {

            @Override
            public boolean allowExecution(CommandSender commandSender, Command command) {
                ClanPlayer cp = getClanPlayer(commandSender);
                return cp != null && cp.getClan().isVerified() && cp.hasRankPermission("manage.ally");
            }
        };

        Command add = executor.buildByMethod(this, "add").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.ally.add"))
                .setIdentifiers(Language.getTranslation("add.command"))
                .addArgument(Language.getTranslation("argument.tag"))
                .addPermission("simpleclans.leader.ally");
        this.addSubCommand(add);
        Command remove = executor.buildByMethod(this, "remove").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.ally.remove"))
                .setIdentifiers(Language.getTranslation("remove.command"))
                .addArgument(Language.getTranslation("argument.tag"))
                .addPermission("simpleclans.leader.ally");
        this.addSubCommand(remove);
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
    }

    @Override
    public boolean allowExecution(com.p000ison.dev.commandlib.CommandSender sender) {
        return false;
    }

    @CommandHandler(name = "Add allies")
    public void add(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        String[] arguments = info.getArguments();

        Clan clan = cp.getClan();

        if (clan.getSize() < getPlugin().getSettingsManager().getMinimalSizeToAlly()) {
            sender.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("minimum.to.make.alliance"), getPlugin().getSettingsManager().getMinimalSizeToAlly()));
            return;
        }

        Clan ally = getPlugin().getClanManager().getClan(arguments[0]);

        if (ally == null) {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (!clan.isAlly(ally)) {

            Set<ClanPlayer> leaders = GeneralHelper.stripOfflinePlayers(ally.getLeaders());

            if (!leaders.isEmpty()) {
                getPlugin().getRequestManager().createRequest(new AllyCreateRequest(getPlugin(), leaders, cp, ally));
                sender.sendMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("leaders.have.been.asked.for.an.alliance"), ally.getName()));
            } else {
                sender.sendMessage(ChatColor.RED + Language.getTranslation("at.least.one.leader.accept.the.alliance"));
            }
        } else {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("your.clans.are.already.allies"));
        }
    }

    @CommandHandler(name = "Remove allies")
    public void remove(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        String[] arguments = info.getArguments();

        Clan clan = cp.getClan();

        if (clan.getSize() < getPlugin().getSettingsManager().getMinimalSizeToAlly()) {
            sender.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("minimum.to.make.alliance"), getPlugin().getSettingsManager().getMinimalSizeToAlly()));
            return;
        }

        Clan ally = getPlugin().getClanManager().getClan(arguments[0]);

        if (ally == null) {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (clan.isAlly(ally)) {
            clan.removeAlly(ally);
            ally.removeAlly(clan);
            ally.addBBMessage(cp, MessageFormat.format(Language.getTranslation("has.broken.the.alliance"), clan.getName(), ally.getName()));
            clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("has.broken.the.alliance"), clan.getName(), ally.getName()));
            clan.update();
            ally.update();
        } else {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("your.clans.are.not.allies"));
        }
    }
}
