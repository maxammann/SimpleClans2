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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.PlayerState;
import com.p000ison.dev.simpleclans2.util.StringHelper;
import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a VitalsCommand
 */
public class VitalsCommand extends GenericPlayerCommand {

    public VitalsCommand(SimpleClans plugin)
    {
        super("Vitals", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.vitals"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("vitals.command"));
        setPermission("simpleclans.member.vitals");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.isTrusted() && cp.getClan().isVerified()) {
            return MessageFormat.format(Language.getTranslation("menu.vitals"), plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ChatColor headColor = plugin.getSettingsManager().getHeaderPageColor();


        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer != null) {
            Clan clan = clanPlayer.getClan();

            if (clan.isVerified()) {
                if (clanPlayer.isTrusted()) {

                    ChatBlock chatBlock = new ChatBlock();
                    ChatBlock.sendBlank(player);
                    ChatBlock.sendHead(player, plugin.getSettingsManager().getClanColor() + StringHelper.capitalize(clan.getName()), Language.getTranslation("vitals"));

                    ChatBlock.sendBlank(player);
                    ChatBlock.sendMessage(player, headColor + Language.getTranslation("weapons") + ": " + Language.getTranslation("weapon.legend", ChatColor.WHITE, ChatColor.DARK_GRAY, ChatColor.WHITE, ChatColor.DARK_GRAY, ChatColor.WHITE));
                    ChatBlock.sendMessage(player, headColor + Language.getTranslation("materials") + ": " + ChatColor.AQUA + Language.getTranslation("diamond") + ChatColor.DARK_GRAY + ", " + ChatColor.YELLOW + Language.getTranslation("gold") + ChatColor.DARK_GRAY + ", " + ChatColor.GRAY + Language.getTranslation("stone") + ChatColor.DARK_GRAY + ", " + ChatColor.WHITE + Language.getTranslation("iron") + ChatColor.DARK_GRAY + ", " + ChatColor.GOLD + Language.getTranslation("wood"));

                    ChatBlock.sendBlank(player);

                    chatBlock.setAlignment(Align.LEFT, Align.LEFT, Align.LEFT, Align.CENTER, Align.CENTER, Align.CENTER);

                    chatBlock.addRow(headColor + Language.getTranslation("name"), Language.getTranslation("health"), Language.getTranslation("hunger"), Language.getTranslation("food"), Language.getTranslation("armor"), Language.getTranslation("weapons"));

                    Set<ClanPlayer> members = GeneralHelper.stripOfflinePlayers(clan.getAllMembers());

                    for (ClanPlayer cp : members) {
                        Player iPlayer = cp.toPlayer();
                        PlayerState state = new PlayerState(iPlayer);

                        if (iPlayer != null) {
                            String name = cp.getColor() + cp.getName();
                            String health = state.getHealth();
                            String hunger = state.getHunger();
                            String armor = state.getArmor(Language.getTranslation("armor.h"), Language.getTranslation("armor.c"), Language.getTranslation("armor.l"), Language.getTranslation("armor.b"));
                            String weapons = state.getWeapons(Language.getTranslation("weapon.S"), Language.getTranslation("weapon.B"), Language.getTranslation("weapon.A"));
                            String food = state.getFood("%s§ch");

                            chatBlock.addRow(name, ChatColor.RED + health, hunger, ChatColor.WHITE + food, armor, weapons);
                        }
                    }

                    chatBlock.sendBlock(player);

                    Set<ClanPlayer> allAllyMembers = clan.getAllAllyMembers();

                    if (!allAllyMembers.isEmpty()) {
                        chatBlock.clear();

                        ChatBlock.sendMessage(player, ChatColor.GRAY + " -- Allies -- ");


                        for (ClanPlayer cp : allAllyMembers) {
                            Player iPlayer = cp.toPlayer();
                            PlayerState state = new PlayerState(iPlayer);

                            if (iPlayer != null) {
                                String name = cp.getColor() + cp.getName();
                                String health = state.getHealth();
                                String hunger = state.getHunger();
                                String armor = state.getArmor(Language.getTranslation("armor.h"), Language.getTranslation("armor.c"), Language.getTranslation("armor.l"), Language.getTranslation("armor.b"));
                                String weapons = state.getWeapons(Language.getTranslation("weapon.S"), Language.getTranslation("weapon.B"), Language.getTranslation("weapon.A"));
                                String food = state.getFood("%sh");

                                chatBlock.addRow("  " + name, ChatColor.RED + health, hunger, ChatColor.WHITE + food, armor, weapons);
                            }
                        }

                        chatBlock.sendBlock(player);
                    }


                    ChatBlock.sendBlank(player);

                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.vitals"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}