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

import com.p000ison.dev.commandlib.*;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.commands.ClanPlayerCommand;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author phaed
 */
public class FFCommand extends GenericPlayerCommand {

    public FFCommand(SimpleClans plugin) {
        super("Personal FF", plugin);
        setDescription(Language.getTranslation("description.ff"));
        setIdentifiers(Language.getTranslation("ff.command"));

        setNeedsClan();

        CommandExecutor executor = plugin.getCommandManager();

        AnnotatedCommand.ExecutionRestriction restriction = new AnnotatedCommand.ExecutionRestriction() {
            @Override
            public boolean allowExecution(CommandSender sender, Command command) {
                return sender instanceof ClanPlayerCommand;
            }
        };

        Command allow = executor.buildByMethod(this, "allow").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.ff.allow"))
                .setIdentifiers(Language.getTranslation("allow.command"))
                .addPermission("simpleclans.member.ff");
        this.addSubCommand(allow);
        Command auto = executor.buildByMethod(this, "auto").setExecutionRestriction(restriction)
                .setDescription(Language.getTranslation("description.ff.auto"))
                .setIdentifiers(Language.getTranslation("auto.command"))
                .addPermission("simpleclans.member.ff");
        this.addSubCommand(auto);
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
    }

    @Override
    protected boolean displayHelpEntry(ClanPlayer cp, org.bukkit.command.CommandSender sender) {
        return false;
    }

    @CommandHandler(name = "Allow FF")
    public void allow(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);

        cp.setFriendlyFire(true);
        cp.update();
        ChatBlock.sendMessage(getPlayer(sender), ChatColor.AQUA + Language.getTranslation("personal.friendly.fire.is.set.to.allowed"));
    }

    @CommandHandler(name = "Auto FF")
    public void auto(CommandSender sender, CallInformation info) {
        ClanPlayer cp = getClanPlayer(sender);

        cp.setFriendlyFire(false);
        cp.update();
        ChatBlock.sendMessage(getPlayer(sender), ChatColor.AQUA + Language.getTranslation("friendy.fire.is.now.managed.by.your.clan"));
    }
}
