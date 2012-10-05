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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.clan;

import com.p000ison.dev.simpleclans2.KDR;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a Clan
 */
public class Clan implements KDR {

    private SimpleClans plugin;

    private long id = -1;
    private String tag, name;
    private ClanFlags flags;
    private long foundedDate;
    private long lastActionDate;
    private boolean verified, friendlyFire;
    private LinkedList<String> bb = new LinkedList<String>();
    private Set<Clan> allies = new HashSet<Clan>();
    private Set<Clan> rivals = new HashSet<Clan>();
    private Set<Clan> warring = new HashSet<Clan>();
    private Set<ClanPlayer> allMembers = new HashSet<ClanPlayer>();
    private Set<Rank> ranks = new HashSet<Rank>();
    private boolean update;

    /**
     * Creates a new clan
     *
     * @param plugin The plugin
     */
    public Clan(SimpleClans plugin)
    {
        flags = new ClanFlags();
        this.plugin = plugin;
    }

    public Clan(SimpleClans plugin, String tag, String name)
    {
        this(plugin);
        this.tag = tag;
        this.name = name;
    }

    public Clan(SimpleClans plugin, long id, String tag, String name)
    {
        this(plugin, tag, name);
        this.id = id;
    }

    public static final SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a");

    /**
     * Returns a formated string of the date this clan was founded
     *
     * @return Returns a formatted date
     */
    public String getFounded()
    {
        return DATE_FORMAT.format(new Date(this.foundedDate));
    }

    /**
     * Gets the tag of a clan. This is unique and can contain colors.
     *
     * @return The tag.
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * Sets the tag of this clan
     *
     * @param tag The tag
     */
    public void setTag(String tag)
    {
        this.tag = tag;
    }

    /**
     * Gets the unique id of this clan.
     *
     * @return The id.
     */
    public long getId()
    {
        return id;
    }

    /**
     * Sets the id of this clan.
     *
     * @param id The id.
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Returns the Flags of this clan. {@link ClanFlags} contains the flags of this clan.
     *
     * @return The flags of this clan.
     * @see ClanFlags
     */
    public ClanFlags getFlags()
    {
        return flags;
    }

    /**
     * Sets the flags of this clan to a new object.
     *
     * @param flags The flag to set
     */
    public void setFlags(ClanFlags flags)
    {
        this.flags = flags;
    }

    /**
     * Gets the name of this clan.
     *
     * @return The name of this clan.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this clan
     *
     * @param name The name of this clan
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the time in milliseconds in the unix time
     *
     * @return The time
     */
    public long getLastActionDate()
    {
        return lastActionDate;
    }

    /**
     * Updates the last action to NOW!
     */
    public void updateLastAction()
    {
        this.setLastActionDate(System.currentTimeMillis());
    }

    /**
     * Returns the date then this clan was founded in milliseconds in the unix time.
     *
     * @return The found date,
     */
    public long getFoundedDate()
    {
        return foundedDate;
    }

    /**
     * Sets when the clan was founded.
     *
     * @param foundedDate The time when it was founded.
     */
    public void setFoundedDate(long foundedDate)
    {
        this.foundedDate = foundedDate;
    }

    /**
     * Checks if this clan is verified.
     *
     * @return Weather this clan is verified.
     */
    public boolean isVerified()
    {
        return verified;
    }

    /**
     * Sets this clan verified.
     *
     * @param verified Weather this clan should be verified
     */
    public void setVerified(boolean verified)
    {
        this.verified = verified;
    }

    /**
     * Checks if friendly fire is on.
     *
     * @return Weather friendly fire is on of of.
     */
    public boolean isFriendlyFireOn()
    {
        return friendlyFire;
    }

    /**
     * Sets friendly fire for this clan.
     *
     * @param friendlyFire Weather friendly fire is on or off.
     */
    public void setFriendlyFire(boolean friendlyFire)
    {
        this.friendlyFire = friendlyFire;
    }

    /**
     * Returns a set of allies
     *
     * @return The allies.
     */
    public Set<Clan> getAllies()
    {
        return Collections.unmodifiableSet(allies);
    }

    /**
     * Returns a set of rivals
     *
     * @return The rival clans.
     */
    public Set<Clan> getRivals()
    {
        return Collections.unmodifiableSet(rivals);
    }

    /**
     * Returns a set of warring clans
     *
     * @return The warring clans.
     */
    public Set<Clan> getWarringClans()
    {
        return Collections.unmodifiableSet(warring);
    }

    /**
     * Checks weather the clan is a ally.
     *
     * @param id The id of the other clan.
     * @return Weather they are allies or not.
     */
    public boolean isAlly(long id)
    {
        if (this.getId() == id) {
            return true;
        }

        for (Clan clan : allies) {
            if (clan.getId() == id) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks weather the clan  is a ally.
     *
     * @param clan The other clan
     * @return Weather they are allies or not.
     */
    public boolean isAlly(Clan clan)
    {
        return clan != null && (this.equals(clan) || allies.contains(clan));
    }

    /**
     * Checks weather the clan with the id is a rival.
     *
     * @param id The id of the other clan.
     * @return Weather they are rivals or not.
     */
    public boolean isRival(long id)
    {
        if (this.getId() == id) {
            return false;
        }

        for (Clan clan : rivals) {
            if (clan.getId() == id) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks weather the clan is a rival.
     *
     * @param clan The other clan.
     * @return Weather they are rivals or not.
     */
    public boolean isRival(Clan clan)
    {
        return clan != null && !this.equals(clan) && rivals.contains(clan);
    }

    /**
     * Checks weather the clan is warring with this one.
     *
     * @param id The id of the other clan.
     * @return Weather they are warring or not.
     */
    public boolean isWarring(long id)
    {
        if (this.getId() == id) {
            return false;
        }

        for (Clan clan : warring) {
            if (clan.getId() == id) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks weather the clan is warring with this one.
     *
     * @param clan The other clan.
     * @return Weather they are warring or not.
     */
    public boolean isWarring(Clan clan)
    {
        return clan != null && !this.equals(clan) && warring.contains(clan);
    }

    /**
     * Gets the days this clan is inactive
     *
     * @return The days this clan is inactive
     */
    public int getInactiveDays()
    {
        return (int) Math.round(DateHelper.differenceInDays(lastActionDate, System.currentTimeMillis()));
    }


    /**
     * Sets the date when the last action happened.
     *
     * @param lastActionDate The date when the last action happened.
     */
    public void setLastActionDate(long lastActionDate)
    {
        this.lastActionDate = lastActionDate;
    }

    /**
     * Sets the leader of this clan
     *
     * @param clanPlayer The player to set
     * @return Weather it was successfully
     */
    public boolean setLeader(ClanPlayer clanPlayer)
    {
        if (clanPlayer.getClanId() != id) {
            return false;
        }

        clanPlayer.setLeader(true);
        return true;
    }

    /**
     * Demotes a leader to a normal player
     *
     * @param clanPlayer The player to demote
     * @return Weather it was successfully
     */
    public boolean demote(ClanPlayer clanPlayer)
    {

        if (clanPlayer.getClanId() != id) {
            return false;
        }

        clanPlayer.setLeader(false);
        return true;
    }

    /**
     * Checks if the player is a member of this clan. This means no leader.
     *
     * @param cp The player
     * @return Checks if the player is a member
     */
    public boolean isMember(ClanPlayer cp)
    {
        for (ClanPlayer comparePlayer : allMembers) {

            if (!comparePlayer.getName().equals(cp.getName())) {
                continue;
            }

            if (cp.getClanId() == id && !cp.isLeader()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the player the leader of this clan.
     *
     * @param cp The player
     * @return Checks if the player is the leader
     */
    public boolean isLeader(ClanPlayer cp)
    {
        return cp.getClanId() == id && cp.isLeader();
    }

    /**
     * Checks if the player is any member of this clan. This means member/leader
     *
     * @param cp The player
     * @return Checks if the player is any member
     */
    public boolean isAnyMember(ClanPlayer cp)
    {
        return allMembers.contains(cp);
    }

    /**
     * Gets all the members excluded the leader
     *
     * @return A Set of the members
     */
    public Set<ClanPlayer> getMembers()
    {
        Set<ClanPlayer> members = new HashSet<ClanPlayer>();

        for (ClanPlayer cp : allMembers) {
            if (cp.getClanId() == id && !cp.isLeader()) {
                members.add(cp);
            }
        }

        return Collections.unmodifiableSet(members);
    }

    /**
     * Returns all members including the leader
     *
     * @return A set of all members of this clan
     */
    public Set<ClanPlayer> getAllMembers()
    {
        return allMembers;
    }

    /**
     * Returns a set of all leaders
     *
     * @return A Set of all leaders
     */
    public Set<ClanPlayer> getLeaders()
    {
        Set<ClanPlayer> leaders = new HashSet<ClanPlayer>();

        for (ClanPlayer cp : allMembers) {
            if (cp.isLeader()) {
                leaders.add(cp);
            }
        }

        return Collections.unmodifiableSet(leaders);
    }

    /**
     * Adds a player as member to this clan
     *
     * @param clanPlayer The player
     */
    public void addMember(ClanPlayer clanPlayer)
    {
        Clan previous = clanPlayer.getClan();

        if (previous != null) {
            if (isVerified()) {
                clanPlayer.addPastClan(getTag() + (clanPlayer.isLeader() ? "*" : ""));
            }
        }

        allMembers.add(clanPlayer);
        clanPlayer.setClan(this);
    }


    /**
     * Gets the total kdr of all members
     *
     * @return The total KDR
     */
    public float getKDR()
    {
        double totalWeightedKills = 0;
        int totalDeaths = 0;

        for (ClanPlayer member : getAllMembers()) {
            totalWeightedKills += member.getWeightedKills();
            totalDeaths += member.getDeaths();
        }

        if (totalDeaths == 0) {
            totalDeaths = 1;
        }

        return ((float) totalWeightedKills) / ((float) totalDeaths);
    }

    /**
     * The size of this clan
     *
     * @return The total size of this clan
     */
    public int getSize()
    {
        return getAllMembers().size();
    }

    /**
     * Adds a message to the bb
     *
     * @param announcer The announcer of this message
     * @param msg       The message which should be posted
     */
    public void addBBMessage(ClanPlayer announcer, String msg)
    {
        if (isVerified()) {
            addBBRawMessage(ChatBlock.parseColors(plugin.getSettingsManager().getClanPlayerBB().replace("+player", announcer.getName()).replace("+message", msg)));
//            addBBRawMessage(GeneralHelper.parseColors("+player +message".replace("+player", announcer.getName()).replace("+message", msg)));
        }
    }


    /**
     * Adds a message to the bb
     *
     * @param announcer The announcer clan of this message
     * @param msg       The message which should be posted
     */
    public void addBBMessage(Clan announcer, String msg)
    {
        if (isVerified()) {
            addBBRawMessage(ChatBlock.parseColors(plugin.getSettingsManager().getClanBB().replace("+clan", announcer.getTag()).replace("+message", msg)));
        }
    }


    /**
     * Announces a message to all clan members
     *
     * @param announcer The announcer of this message
     * @param msg       The message which should be announced
     */
    public void announce(ClanPlayer announcer, String msg)
    {
        announceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getClanPlayerAnnounce().replace("+player", announcer.getName()).replace("+message", msg)));
    }

    /**
     * Announces a message to all clan members
     *
     * @param announcer The announcer clan of this message
     * @param msg       The message which should be announced
     */
    public void announce(Clan announcer, String msg)
    {
        announceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getClanAnnounce().replace("+clan", announcer.getTag()).replace("+message", msg)));
    }

    /**
     * Announces a message to all clan members
     *
     * @param msg The message which should be announced
     */
    public void announce(String msg)
    {
        announceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getDefaultAnnounce().replace("+message", msg)));
    }

    /**
     * announces a message to all clan players raw
     * <p/>
     * <p><strong>Internally used!</strong></p>
     *
     * @param message The message
     */
    private void announceRaw(String message)
    {
        if (message == null) {
            return;
        }

        for (ClanPlayer clanPlayer : getAllMembers()) {
            Player player = clanPlayer.toPlayer();

            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Gets a clean comparable tag of this clan
     *
     * @return The clean tag
     */
    public String getCleanTag()
    {
        return ChatColor.stripColor(tag.toLowerCase());
    }

    public void addBBMessage(String msg)
    {
        addBBRawMessage(ChatBlock.parseColors(plugin.getSettingsManager().getDefaultBB().replace("+message", msg)));
    }

    private void addBBRawMessage(String message)
    {
        if (isVerified()) {
            if (bb.size() > plugin.getSettingsManager().getMaxBBLenght()) {
                bb.pollFirst();
            }

            bb.add(message);
        }
    }

    public void clearBB()
    {
        bb.clear();
    }

    public void loadBB(LinkedList<String> bb)
    {
        this.bb = bb;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Clan)) {
            return false;
        }

        Clan clan = (Clan) o;

        return id == clan.id;

    }

    @Override
    public int hashCode()
    {
        return (int) (id ^ (id >>> 32));
    }

    public void addRival(Clan rival)
    {
        rivals.add(rival);
    }

    public void addAlly(Clan ally)
    {
        allies.add(ally);
    }

    public void addWarringClan(Clan warringClan)
    {
        warring.add(warringClan);
    }

    public void removeRival(Clan rival)
    {
        rivals.remove(rival);
    }

    public void removeAlly(Clan ally)
    {
        allies.remove(ally);
    }

    public void removeWarringClan(Clan warringClan)
    {
        warring.remove(warringClan);
        addBBMessage(warringClan, MessageFormat.format(Language.getTranslation("you.are.no.longer.at.war"), warringClan.getName(), getTag()));
    }

    public void removeMember(ClanPlayer clanPlayer)
    {
        if (allMembers.contains(clanPlayer)) {

            allMembers.remove(clanPlayer);

            if (clanPlayer.isLeader()) {
                clanPlayer.setLeader(false);
                disband();
            }
        }
    }

    public void disband()
    {
        Iterator<ClanPlayer> clanPlayers = allMembers.iterator();

        while (clanPlayers.hasNext()) {

            ClanPlayer clanPlayer = clanPlayers.next();

            removeMember(clanPlayer);

            clanPlayer.update();
            clanPlayers.remove();
        }


        for (Clan warringClan : warring) {
            warringClan.removeWarringClan(this);
        }

        for (Clan allyClan : allies) {
            allyClan.removeAlly(this);
        }

        for (Clan rivalClan : rivals) {
            rivalClan.removeRival(this);
        }


//        for (Clan c : clans) {
//            String disbanded = plugin.getLang("clan.disbanded");
//
//            if (c.removeWarringClan(this)) {
//                c.addBb(disbanded, ChatColor.AQUA + MessageFormat.format(plugin.getLang("you.are.no.longer.at.war"), Helper.capitalize(c.getName()), getColorTag()));
//            }
//
//            if (c.removeRival(getTag())) {
//                c.addBb(disbanded, ChatColor.AQUA + MessageFormat.format(plugin.getLang("has.been.disbanded.rivalry.ended"), Helper.capitalize(getName())));
//            }
//
//            if (c.removeAlly(getTag())) {
//                c.addBb(disbanded, ChatColor.AQUA + MessageFormat.format(plugin.getLang("has.been.disbanded.alliance.ended"), Helper.capitalize(getName())));
//            }
//        }

        plugin.getClanManager().removeClan(this);
        plugin.getDataManager().deleteClan(this);
    }

    public void displayBb(CommandSender sender, int maxLines)
    {
        if (bb == null || bb.isEmpty()) {
            sender.sendMessage(Language.getTranslation("bb.is.empty"));
            return;
        }

        int start;

        if (bb.size() - maxLines < 0) {
            start = 0;
        } else {
            start = bb.size() - maxLines;
        }

        int end = bb.size();

        for (; start < end; start++) {
            sender.sendMessage(bb.get(start));
        }
    }

    public void displayBb(CommandSender sender)
    {
        if (bb == null || bb.isEmpty()) {
            sender.sendMessage(Language.getTranslation("bb.is.empty"));
            return;
        }

        for (String bbMessage : bb) {
            sender.sendMessage(bbMessage);
        }
    }

    public boolean hasAllies()
    {
        return allies != null && !allies.isEmpty();
    }

    public boolean hasRivals()
    {
        return rivals != null && !rivals.isEmpty();
    }

    public boolean hasWarringClans()
    {
        return warring != null && !warring.isEmpty();
    }

    public boolean hasBB()
    {
        return bb != null && !bb.isEmpty();
    }

    public LinkedList<String> getBB()
    {
        return bb;
    }

    public boolean allLeadersOnline()
    {
        return allLeadersOnline(null);
    }

    public boolean allLeadersOnline(ClanPlayer ignore)
    {
        for (ClanPlayer clanPlayer : getLeaders()) {
            if (ignore != null && clanPlayer.equals(ignore)) {
                continue;
            }

            if (clanPlayer.toPlayer() == null) {
                return false;
            }
        }

        return true;
    }

//    public void update()
//    {
//        plugin.getDataManager().UPDATE_CLAN(this);
//    }

    public boolean needsUpdate()
    {
        return update;
    }

    public void update()
    {
        this.update = true;
    }

    public void update(boolean update)
    {
        this.update = update;
    }

    /**
     * This return a array of kills this clan made. This needs only one iteration.
     * <p/>
     * <p><strong>Total Deaths: </strong><i>Index 0</i></p>
     * <p><strong>Total Rival Kills: </strong><i>Index 1</i></p>
     * <p><strong>Total Civilian Kills: </strong><i>Index 2</i></p>
     * <p><strong>Total Neutral Kills: </strong><i>Index 3</i></p>
     *
     * @return A array of the kills of this clan from index 0 - 3
     */
    public int[] getTotalKills()
    {
        int totalDeaths = 0;
        int totalRivalKills = 0;
        int totalCivilianKills = 0;
        int totalNeutralKills = 0;

        for (ClanPlayer clanPlayer : getAllMembers()) {
            totalDeaths += clanPlayer.getDeaths();
            totalRivalKills += clanPlayer.getRivalKills();
            totalCivilianKills += clanPlayer.getCivilianKills();
            totalNeutralKills += clanPlayer.getNeutralKills();
        }

        return new int[]{totalDeaths, totalRivalKills, totalCivilianKills, totalCivilianKills, totalNeutralKills};
    }

    public void addRank(Rank rank)
    {
        if (rank == null) {
            return;
        }

        ranks.add(rank);
    }

    public Set<Rank> getRanks()
    {
        return Collections.unmodifiableSet(ranks);
    }

    public boolean removeRank(String query)
    {
        for (ClanPlayer clanPlayer : allMembers) {
            Rank rank = clanPlayer.getRank();

            if (rank == null) {
                continue;
            }

            if (rank.getName().equals(query)) {
                clanPlayer.setRank(null);
            }
        }

        Iterator<Rank> rankIterator = ranks.iterator();

        while (rankIterator.hasNext()) {
            Rank currentRank = rankIterator.next();
            if (currentRank.getName().equals(query)) {
                plugin.getDataManager().deleteRank(currentRank.getId());
                rankIterator.remove();
            }
        }

        return true;
    }

    public boolean removeRank(Rank query)
    {
        for (ClanPlayer clanPlayer : allMembers) {
            Rank rank = clanPlayer.getRank();

            if (rank == null) {
                continue;
            }

            if (rank.equals(query)) {
                clanPlayer.setRank(null);
            }
        }

        return ranks.remove(query);
    }

    public void setRanks(Set<Rank> ranks)
    {
        this.ranks = ranks;
    }

    public Rank getRank(long id)
    {
        for (Rank rank : ranks) {
            if (rank.getId() == id) {
                return rank;
            }
        }

        return null;
    }

    public Rank getRank(String query)
    {
        String cleanQuery = query.toLowerCase();
        for (Rank rank : ranks) {
            if (rank.getName().toLowerCase().startsWith(cleanQuery)) {
                return rank;
            }
        }

        return null;
    }


    @Override
    public String toString()
    {
        return "Clan{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", lastActionDate=" + lastActionDate +
                ", verified=" + verified +
                ", update=" + update +
                '}';
    }
}
