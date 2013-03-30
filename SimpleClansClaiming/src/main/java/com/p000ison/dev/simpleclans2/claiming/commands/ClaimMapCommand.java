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
 *     Last modified: 25.03.13 17:01
 */

package com.p000ison.dev.simpleclans2.claiming.commands;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.command.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.claiming.ClaimLocation;
import com.p000ison.dev.simpleclans2.claiming.SCClaimingLanguage;
import com.p000ison.dev.simpleclans2.claiming.data.ClaimingManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * Represents a ClaimMapCommand
 */
public class ClaimMapCommand extends GenericPlayerCommand {

    private final SCCore sccore;
    private final ClaimingManager manager;

    private static final char NORTH_WEST = '\\';
    private static final char NORTH = 'N';
    private static final char NORTH_EAST = '/';
    private static final char WEST = 'W';
    private static final char CENTER = '+';
    private static final char EAST = 'E';
    private static final char SOUTH_WEST = '/';
    private static final char SOUTH_EAST = '\\';
    private static final char SOUTH = 'S';

    private static final int MAX_ROWS = 8;
    private static final int MAX_COLUMNS = 41;

    public ClaimMapCommand(SCCore sccore, ClaimingManager manager) {
        super("ClaimMap");
        this.sccore = sccore;
        this.manager = manager;
        setArgumentRange(1, 1);
        setUsages(SCClaimingLanguage.getTranslation("usage.claim.map"));
        setIdentifiers(SCClaimingLanguage.getTranslation("claim.map.command"));
        setPermission("simpleclans.member.claim.map");
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanPlayer clanPlayer = sccore.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + sccore.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = clanPlayer.getClan();
        Location location = player.getLocation();

        sendMap(player, location, clan);

    }

    @Override
    public String getMenu(ClanPlayer cp) {
        if (cp != null) {
            return SCClaimingLanguage.getTranslation("menu.claim.map");
        }
        return null;
    }

    public void sendMap(Player player, Location loc, Clan playerClan) {

        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int world = manager.getIDByWorld(loc.getWorld());
        ClaimLocation playerChunk = new ClaimLocation(world, x, z);
        StringBuilder[] out = new StringBuilder[MAX_ROWS + 1];

        Clan clanHere = manager.getClanAt(loc);

        //header above the map
        String header = ChatColor.GOLD + " __________________[ " + playerChunk.getX() + " " + playerChunk.getZ() + " " + (clanHere != null ? clanHere.getName() : "") + " ]__________________ \n";

        out[0] = new StringBuilder(ChatColor.GOLD + header + ChatColor.GRAY);

        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        ClaimLocation homeChunk = manager.getHomeChunk(playerClan);

        for (int xMap = 0; xMap < MAX_ROWS; xMap++) {
            StringBuilder row = new StringBuilder().append(' ');
            for (int yMap = MAX_COLUMNS; yMap > 0; yMap--) {
                //calculate map coordinates to real ones
                ClaimLocation chunk = new ClaimLocation(world, xMap + chunkX - (MAX_ROWS >> 1), yMap + chunkZ - (MAX_COLUMNS >> 1));
                Clan clan = manager.getClanAt(chunk);

                if (chunk.equals(playerChunk)) {
                    //- Nothing
                    row.append('P');
                } else if (clan == null) {
                    //is player location
                    row.append("-");
                } else {
                    if (playerClan == clan) {
                        //is own
                        if (homeChunk == chunk) {
                            //is homeblock
                            row.append(ChatColor.GREEN).append("+").append(ChatColor.GRAY);
                        } else {
                            row.append(ChatColor.GREEN).append("/").append(ChatColor.GRAY);
                        }
                    } else if (clan.getWarringClans().contains(playerClan)) {
                        //is enemy
                        if (homeChunk.equals(chunk)) {
                            //is homeblock
                            row.append(ChatColor.DARK_RED).append("+").append(ChatColor.GRAY);
                        } else {
                            row.append(ChatColor.DARK_RED).append("/").append(ChatColor.GRAY);
                        }
                    } else {
                        row.append('X');
                    }
                }
            }
            out[xMap + 1] = row;
        }

        out[1] = out[1].delete(0, 4).insert(0, getCompass(loc, 0));
        out[2] = out[2].delete(0, 4).insert(0, getCompass(loc, 1));
        out[3] = out[3].delete(0, 4).insert(0, getCompass(loc, 2));

        for (StringBuilder string : out) {
            player.sendMessage(string.insert(0, ChatColor.GRAY).toString());
        }
    }

    public static String getCompass(Location loc, int index) {
        BlockFace dir = getDirection(loc);

        StringBuilder sb = new StringBuilder();

        sb.append(ChatColor.GOLD);
        if (index == 0) {
            sb.append(' ');
            sb.append(colorize(dir == BlockFace.NORTH_WEST, NORTH_WEST));
            sb.append(colorize(dir == BlockFace.NORTH, NORTH));
            sb.append(colorize(dir == BlockFace.NORTH_EAST, NORTH_EAST));
        } else if (index == 1) {
            sb.append(' ');
            sb.append(colorize(dir == BlockFace.WEST, WEST)).append(CENTER);
            sb.append(colorize(dir == BlockFace.EAST, EAST));
        } else if (index == 2) {
            sb.append(' ');
            sb.append(colorize(dir == BlockFace.SOUTH_WEST, SOUTH_WEST));
            sb.append(colorize(dir == BlockFace.SOUTH, SOUTH));
            sb.append(colorize(dir == BlockFace.SOUTH_EAST, SOUTH_EAST));
        }
        sb.append(ChatColor.GRAY);

        return sb.toString();
    }

    public static BlockFace getDirection(Location loc) {
        int degrees = (Math.round(loc.getYaw()) + 270) % 360;
        if (degrees <= 22) {
            return BlockFace.NORTH;
        }
        if (degrees <= 67) {
            return BlockFace.NORTH_EAST;
        }
        if (degrees <= 112) {
            return BlockFace.EAST;
        }
        if (degrees <= 157) {
            return BlockFace.SOUTH_EAST;
        }
        if (degrees <= 202) {
            return BlockFace.SOUTH;
        }
        if (degrees <= 247) {
            return BlockFace.SOUTH_WEST;
        }
        if (degrees <= 292) {
            return BlockFace.WEST;
        }
        if (degrees <= 337) {
            return BlockFace.NORTH_WEST;
        }
        if (degrees <= 359) {
            return BlockFace.NORTH;
        }
        return null;
    }


    public static String colorize(boolean active, char c) {
        return (active ? ChatColor.RED.toString() + c + ChatColor.GOLD.toString() : String.valueOf(c));
    }
}
