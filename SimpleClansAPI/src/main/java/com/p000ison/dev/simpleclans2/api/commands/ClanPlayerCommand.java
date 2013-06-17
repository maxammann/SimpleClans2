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
 *     Last modified: 31.05.13 12:23
 */

package com.p000ison.dev.simpleclans2.api.commands;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Represents a ClanPlayerCommand
 */
public abstract class ClanPlayerCommand extends BukkitCommand {

    protected boolean trusted, hasClan, noClan, clanVerified, clanNotVerified, leader;
    protected String rankPerm;

    public ClanPlayerCommand(String name) {
        super(name);
    }

    @Override
    public void execute(com.p000ison.dev.commandlib.CommandSender sender, CallInformation information) {
        if (sender instanceof ClanPlayerSender) {
            ClanPlayerSender cpSender = (ClanPlayerSender) sender;
            ClanPlayer cp = cpSender.getClanPlayer();
            Player player = cpSender.getSender();

            if (isAllowed(cp, sender)) {
                execute(player, cp, information.getArguments(), information);
            }
        } else {
            sender.sendMessage("Only players!");
        }
    }

    public abstract void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info);


    public abstract boolean isAllowed(ClanPlayer cp, com.p000ison.dev.commandlib.CommandSender sender);

    @Override
    public boolean allowExecution(com.p000ison.dev.commandlib.CommandSender sender) {
        if (sender instanceof ClanPlayerSender) {
            ClanPlayerSender clanPlayerSender = (ClanPlayerSender) sender;
            ClanPlayer cp = clanPlayerSender.getClanPlayer();

            return isAllowed(cp, null) && displayHelpEntry(cp, clanPlayerSender.getSender());
        }

        return false;
    }

    protected boolean displayHelpEntry(ClanPlayer cp, CommandSender sender) {
        return true;
    }

    public ClanPlayerCommand setNeedsTrusted() {
        this.trusted = true;
        return this;
    }

    public ClanPlayerCommand setNeedsClan() {
        this.hasClan = true;
        return this;
    }

    public ClanPlayerCommand setNeedsNoClan() {
        this.noClan = true;
        return this;
    }

    public ClanPlayerCommand setNeedsClanVerified() {
        this.clanVerified = true;
        return this;
    }

    public ClanPlayerCommand setNeedsLeader() {
        this.leader = true;
        return this;
    }

    public ClanPlayerCommand setRankPermission(String rankPerm) {
        this.rankPerm = rankPerm;
        return this;
    }

    public void setNeedsClanNotVerified() {
        this.clanNotVerified = true;
    }
}
