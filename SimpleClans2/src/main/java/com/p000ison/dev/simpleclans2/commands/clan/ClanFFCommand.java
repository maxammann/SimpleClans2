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
import org.bukkit.entity.Player;

/**
 * Represents a ClanFFCommand
 */
public class ClanFFCommand extends GenericPlayerCommand {

    public ClanFFCommand(SimpleClans plugin) {
        super("Clan FF", plugin);
        setDescription(Language.getTranslation("description.clanff"));
        setIdentifiers(Language.getTranslation("clanff.command"));

        setNeedsClan();
        setRankPermission("manage.clanff");

        CommandExecutor executor = plugin.getCommandManager();
        AnnotatedCommand.ExecutionRestriction restriction = new AnnotatedCommand.ExecutionRestriction() {

            @Override
            public boolean allowExecution(CommandSender commandSender, Command command) {
                ClanPlayer cp = getClanPlayer(commandSender);
                return cp != null && cp.hasRankPermission("manage.clanff");
            }
        };

        Command allow = executor.buildByMethod(this, "allow").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.clanff.allow"))
                .addPermission("simpleclans.leader.ff")
                .setIdentifiers(Language.getTranslation("allow.command"));
        this.addSubCommand(allow);
        Command block = executor.buildByMethod(this, "block").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.clanff.block"))
                .addPermission("simpleclans.leader.ff")
                .setIdentifiers(Language.getTranslation("block.command"));
        this.addSubCommand(block);
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
    }

    @CommandHandler(name = "Allow clan FF")
    public void allow(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        Clan clan = cp.getClan();

        clan.addBBMessage(cp, Language.getTranslation("clan.wide.friendly.fire.is.allowed"));
        clan.setFriendlyFire(true);
        clan.update();
    }

    @CommandHandler(name = "Block clan FF")
    public void block(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);
        Clan clan = cp.getClan();

        clan.addBBMessage(cp, Language.getTranslation("clan.wide.friendly.fire.blocked"));
        clan.setFriendlyFire(false);
        clan.update();
    }
}