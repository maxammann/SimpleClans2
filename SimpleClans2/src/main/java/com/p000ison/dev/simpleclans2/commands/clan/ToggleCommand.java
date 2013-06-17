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
 *     Last modified: 03.11.12 12:02
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.*;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.support.SpoutSupport;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a ToggleCommand
 */
public class ToggleCommand extends GenericPlayerCommand {

    public ToggleCommand(SimpleClans plugin) {
        super("Toggle", plugin);
        setDescription(Language.getTranslation("description.toggle"));
        setIdentifiers(Language.getTranslation("toggle.command"));

        CommandExecutor executor = plugin.getCommandManager();
        AnnotatedCommand.ExecutionRestriction restriction = new AnnotatedCommand.ExecutionRestriction() {

            @Override
            public boolean allowExecution(CommandSender commandSender, Command command) {
                ClanPlayer cp = getClanPlayer(commandSender);
                return cp != null && cp.getClan().isVerified();
            }
        };

        Command bb = executor.buildByMethod(this, "bb").setExecutionRestriction(restriction)
                .setIdentifiers(Language.getTranslation("bb.command"))
                .setDescription(Language.getTranslation("description.toggle.bb"))
                .addPermission("simpleclans.member.toggle.bb");
        this.addSubCommand(bb);
        Command cape = executor.buildByMethod(this, "cape").setExecutionRestriction(restriction)
                .setIdentifiers(Language.getTranslation("cape.command"))
                .setDescription(Language.getTranslation("description.toggle.cape"))
                .addPermission("simpleclans.member.toggle.cape");
        this.addSubCommand(cape);
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
    }

    @Override
    protected boolean displayHelpEntry(ClanPlayer cp, org.bukkit.command.CommandSender sender) {
        return false;
    }

    @CommandHandler(name = "Toggle bb")
    public void bb(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        Clan clan = cp.getClan();

        if (cp.isBBEnabled()) {
            sender.sendMessage(ChatColor.AQUA + Language.getTranslation("bboff"));
            cp.setBBEnabled(false);
        } else {
            sender.sendMessage(ChatColor.AQUA + Language.getTranslation("bbon"));
            cp.setBBEnabled(true);
        }
        clan.update();
    }

    @CommandHandler(name = "Toggle cape")
    public void cape(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        Clan clan = cp.getClan();

        if (cp.isCapeEnabled()) {
            sender.sendMessage(ChatColor.AQUA + Language.getTranslation("capeoff"));
            cp.setCapeEnabled(false);
            SpoutSupport spout = getPlugin().getSpoutSupport();
            if (spout.isEnabled()) {
                spout.clearCape(cp);
            }
        } else {
            sender.sendMessage(ChatColor.AQUA + Language.getTranslation("capeon"));
            cp.setCapeEnabled(true);
        }
        clan.update();
    }
}