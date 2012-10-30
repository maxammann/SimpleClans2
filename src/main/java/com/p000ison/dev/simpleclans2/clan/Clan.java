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


package com.p000ison.dev.simpleclans2.clan;

import com.p000ison.dev.simpleclans2.KDR;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.bank.Balance;
import com.p000ison.dev.simpleclans2.clan.bank.BankAccount;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.clanplayer.OnlineClanPlayer;
import com.p000ison.dev.simpleclans2.database.data.response.responses.BBAddResponse;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a Clan
 */
public class Clan implements KDR, Comparable<Clan>, Balance {

    private SimpleClans plugin;
    private ClanFlags flags;
    private BankAccount bank = new BankAccount(0.0D);

    private long id = -1;
    private String tag, name;
    private long foundedDate;
    private long lastActionDate;
    private boolean verified;

    private Set<Clan> allies = new HashSet<Clan>();
    private Set<Clan> rivals = new HashSet<Clan>();
    private Set<Clan> warring = new HashSet<Clan>();
    private Set<ClanPlayer> allMembers = new HashSet<ClanPlayer>();
    private Set<Rank> ranks = new HashSet<Rank>();

    private boolean update;

    public static final SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a");
    public static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

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

    /**
     * Creates a new clan
     *
     * @param plugin The plugin
     * @param tag    The tag of this clan
     * @param name   The full name of this clan
     */
    public Clan(SimpleClans plugin, String tag, String name)
    {
        this(plugin);
        this.tag = tag;
        this.name = name;
    }

    /**
     * Creates a new clan
     *
     * @param plugin The plugin
     * @param id     The id
     * @param tag    The tag of this clan
     * @param name   The full name of this clan
     */
    public Clan(SimpleClans plugin, long id, String tag, String name)
    {
        this(plugin, tag, name);
        this.id = id;
    }

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
        return getFlags().isFriendlyFireEnabled();
    }

    /**
     * Sets friendly fire for this clan.
     *
     * @param friendlyFire Weather friendly fire is on or off.
     */
    public void setFriendlyFire(boolean friendlyFire)
    {
        getFlags().setFriendlyFire(friendlyFire);
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

            if (!comparePlayer.equals(cp)) {
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

        if (previous != null && isVerified()) {
            String pastClan = previous.getTag();

            if (clanPlayer.isLeader()) {
                pastClan += '*';
            }

            clanPlayer.addPastClan(pastClan);
        }

        if (plugin.getSettingsManager().isTrustMembersByDefault()) {
            clanPlayer.setTrusted(true);
        }

        allMembers.add(clanPlayer);
        clanPlayer.setClan(this);
        clanPlayer.updatePermissions();
        clanPlayer.update();
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
            announce(announcer, msg);
            addBBRawMessage(ChatBlock.parseColors(plugin.getSettingsManager().getClanPlayerBB().replace("+player", announcer.getName()).replace("+message", msg)));
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
            msg = ChatBlock.parseColors(plugin.getSettingsManager().getClanBB().replace("+clan", announcer.getTag()).replace("+message", msg));
            announce(msg);
            addBBRawMessage(msg);
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
        return ChatColor.stripColor(tag.toLowerCase(Locale.US));
    }

    public void addBBMessage(String msg)
    {
        addBBRawMessage(ChatBlock.parseColors(plugin.getSettingsManager().getDefaultBB().replace("+message", msg)));
    }

    private void addBBRawMessage(String message)
    {
        if (isVerified()) {
//            if (bb.size() > plugin.getSettingsManager().getMaxBBLenght()) {
//                bb.pollFirst();
//            }

            plugin.getDataManager().addResponse(new BBAddResponse(plugin, message, this));
        }
    }

    public void clearBB()
    {
        plugin.getDataManager().purgeBB(this);
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

    /**
     * Removes a member from this clan
     *
     * @param clanPlayer The member to remove
     */
    public void removeMember(ClanPlayer clanPlayer)
    {
        if (allMembers.remove(clanPlayer)) {
            clanPlayer.unset();
            clanPlayer.update();
            if (clanPlayer.isLeader()) {
                disband();
            }
            plugin.getRequestManager().clearRequests(clanPlayer);
        }
    }

    /**
     * Disbands this (performs all necessary steps)
     */
    public void disband()
    {

        for (ClanPlayer clanPlayer : allMembers) {
            clanPlayer.unset();

            String pastClan = getTag();

            if (clanPlayer.isLeader()) {
                pastClan += '*';
            }

            clanPlayer.addPastClan(pastClan);
            clanPlayer.update();
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


        for (Clan rival : rivals) {
            rival.removeRival(this);
            rival.addBBMessage(this, Language.getTranslation("has.been.disbanded.rivalry.ended", this.getTag()));
        }

        for (Clan ally : allies) {
            ally.removeAlly(this);
            ally.addBBMessage(this, Language.getTranslation("has.been.disbanded.alliance.ended", this.getTag()));
        }

        for (Clan warringClan : warring) {
            warringClan.removeWarringClan(this);
            warringClan.addBBMessage(this, Language.getTranslation("you.are.no.longer.at.war", this.getTag(), warringClan.getTag()));
        }

        plugin.getRequestManager().clearRequests(this);
        plugin.getClanManager().removeClan(this);
        plugin.getDataManager().deleteClan(this);
    }

    /**
     * Checks if this clan has allies
     *
     * @return Weather this clan has allies
     */
    public boolean hasAllies()
    {
        return allies != null && !allies.isEmpty();
    }

    /**
     * Checks if this clan has rivals
     *
     * @return Weather this clan has rivals
     */
    public boolean hasRivals()
    {
        return rivals != null && !rivals.isEmpty();
    }

    /**
     * Checks if this clan has warring clans
     *
     * @return Weather this clan has warring clans
     */
    public boolean hasWarringClans()
    {
        return warring != null && !warring.isEmpty();
    }

    /**
     * Checks if all leaders are online
     *
     * @return Weather all players are online
     */
    public boolean allLeadersOnline()
    {
        return allLeadersOnline(null);
    }

    /**
     * Checks if all leaders are online
     *
     * @param ignore Null or a player to ignore
     * @return Weather all players are online
     */
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

    /**
     * Checks if this clan needs a update
     *
     * @return Weather this clan needs a update
     */
    public boolean needsUpdate()
    {
        return update;
    }

    /**
     * Marks this clan to update
     */
    public void update()
    {
        this.update = true;
    }

    /**
     * Marks this clan to update
     *
     * @param update Weather to update
     */
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

    /**
     * Adds a rank to this clan
     *
     * @param rank The rank to add
     */
    public void addRank(Rank rank)
    {
        if (rank == null) {
            return;
        }

        ranks.add(rank);
    }


    /**
     * Removes a rank and removes them also from the players
     *
     * @param rank The rank to look for
     * @return Weather is was successfully
     */
    public long deleteRank(Rank rank)
    {
        if (rank == null) {
            return -1;
        }

        long id = rank.getId();

        if (ranks.remove(rank)) {
            for (ClanPlayer member : allMembers) {
                if (member.getRank().equals(rank)) {
                    member.setRank(null);
                    member.update();
                }
            }
            return id;
        }

        return -1;
    }

    /**
     * Searches for a rank and removes it.
     *
     * @param tag The search query
     * @return Weather it was sucessfully
     */
    public long deleteRank(String tag)
    {
        Iterator<Rank> it = ranks.iterator();

        while (it.hasNext()) {
            Rank rank = it.next();
            if (rank.getTag().startsWith(tag)) {
                long id = rank.getId();
                for (ClanPlayer member : allMembers) {
                    if (member.getRank().equals(rank)) {
                        member.setRank(null);
                        member.update();
                    }
                }
                it.remove();
                return id;
            }
        }
        return -1;
    }

    /**
     * Gets a set of all ranks of this clan
     *
     * @return All ranks of this clan
     */
    public Set<Rank> getRanks()
    {
        return Collections.unmodifiableSet(ranks);
    }


    /**
     * Loads the ranks for this clan
     *
     * @param ranks The ranks to load
     */
    public void loadRanks(Set<Rank> ranks)
    {
        this.ranks = ranks;
    }

    /**
     * Returns a rank of this clan
     *
     * @param id The id to look for
     * @return The rank
     */
    public Rank getRank(long id)
    {
        for (Rank rank : ranks) {
            if (rank.getId() == id) {
                return rank;
            }
        }

        return null;
    }

    /**
     * Searches for a rank saved in this clan
     *
     * @param query The search query
     * @return The rank
     */
    public Rank getRank(String query)
    {
        String cleanQuery = query.toLowerCase(Locale.US);
        for (Rank rank : ranks) {
            if (rank.getTag().toLowerCase(Locale.US).startsWith(cleanQuery)) {
                return rank;
            }
        }

        return null;
    }


    /**
     * Turns the most important information about this clan into a string
     *
     * @return A string with information about this clan
     */
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

    /**
     * Compares this clan to another clan based on the inactive days.
     *
     * @param anotherClan Another clan
     */
    @Override
    public int compareTo(Clan anotherClan)
    {
        int thisInactiveDate = this.getInactiveDays();
        int anotherInactiveDate = anotherClan.getInactiveDays();
        return (thisInactiveDate < anotherInactiveDate ? -1 : (thisInactiveDate == anotherInactiveDate ? 0 : 1));
    }

    /**
     * Returns all members, including the leaders
     *
     * @return All members of this clan
     */
    public Set<ClanPlayer> getAllAllyMembers()
    {
        Set<ClanPlayer> allyMembers = new HashSet<ClanPlayer>();

        for (Clan ally : getAllies()) {
            allyMembers.addAll(ally.getAllMembers());
        }

        return Collections.unmodifiableSet(allyMembers);
    }

    /**
     * Check whether the clan has crossed the rival limit
     * <p/>
     * <strong>limit = AllRivalAbleClans * rivalPercentLimit</strong>
     *
     * @return Weather the clan has reached the maximum rivalries
     */
    public boolean reachedRivalLimit()
    {
        int rivalCount = rivals.size();
        //minus 1 because this clan is rivable
        double clanCount = plugin.getClanManager().getRivalAbleClanCount() - 1;
        double rivalPercent = plugin.getSettingsManager().getRivalLimitPercent();

        double limit = clanCount * rivalPercent / 100.0D;

        return rivalCount > limit;
    }

    /**
     * Deletes the permission sets
     */
    public void removePermissions()
    {
        plugin.getServer().getPluginManager().removePermission(String.valueOf(id));
        plugin.getServer().getPluginManager().removePermission("^" + id);
    }

    /**
     * Creates a permission set for this clan and registers it
     *
     * @param permissions The set of permissions
     */
    public void setupPermissions(Map<String, Boolean> permissions)
    {
        removePermissions();

        Map<String, Boolean> positiveSet = new HashMap<String, Boolean>();
        Map<String, Boolean> negativeSet = new HashMap<String, Boolean>();

        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            if (entry.getValue()) {
                positiveSet.put(entry.getKey(), true);
            } else {
                negativeSet.put(entry.getKey(), false);
            }
        }

        Permission positive = new Permission(String.valueOf(id), PermissionDefault.FALSE, positiveSet);
        Permission negative = new Permission("^" + id, PermissionDefault.FALSE, negativeSet);

        plugin.getServer().getPluginManager().addPermission(positive);
        plugin.getServer().getPluginManager().addPermission(negative);
    }

    /**
     * Updates the permissions for every clanplayer of this clan
     */
    public void updatePermissions()
    {
        for (ClanPlayer clanPlayer : getAllMembers()) {
            OnlineClanPlayer online = clanPlayer.getOnlineVersion();
            if (online == null) {
                continue;
            }

            online.removePermissions();
            online.setupPermissions();
        }
    }

    /**
     * Announces a message to the server
     *
     * @param message The message
     */
    public void serverAnnounce(String message)
    {
        SimpleClans.serverAnnounceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getClanAnnounce().replace("+clan", this.getTag()).replace("+message", message)));
    }

    /**
     * Displays a profile of the clan the CommandSender
     *
     * @param sender The retriever
     */
    public void showClanProfile(CommandSender sender)
    {
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();
        ChatColor headColor = plugin.getSettingsManager().getHeaderPageColor();

        ChatBlock.sendBlank(sender);
        ChatBlock.sendHead(sender, Language.getTranslation("profile", plugin.getSettingsManager().getClanColor() + this.getName()), null);
        ChatBlock.sendBlank(sender);

        String name = this.getName();
        String leaders = plugin.getSettingsManager().getLeaderColor() + GeneralHelper.clansPlayersToString(this.getLeaders(), ",");
        String onlineCount = ChatColor.WHITE.toString() + GeneralHelper.stripOfflinePlayers(this.getAllMembers()).size();
        String membersOnline = onlineCount + subColor + "/" + ChatColor.WHITE + this.getSize();
        String inactive = ChatColor.WHITE.toString() + this.getInactiveDays() + subColor + "/" + ChatColor.WHITE.toString() + (this.isVerified() ? plugin.getSettingsManager().getPurgeInactiveClansDays() : plugin.getSettingsManager().getPurgeUnverifiedClansDays()) + " " + Language.getTranslation("days");
        String founded = ChatColor.WHITE + this.getFounded();

        String rawAllies = GeneralHelper.clansToString(this.getAllies(), ",");
        String allies = ChatColor.WHITE + (rawAllies == null ? Language.getTranslation("none") : rawAllies);

        String rawRivals = GeneralHelper.clansToString(this.getRivals(), ",");
        String rivals = ChatColor.WHITE + (rawRivals == null ? Language.getTranslation("none") : rawRivals);
        String kdr = ChatColor.YELLOW + DECIMAL_FORMAT.format(this.getKDR());

        int[] kills = this.getTotalKills();

        String deaths = ChatColor.WHITE.toString() + kills[0];
        String rival = ChatColor.WHITE.toString() + kills[1];
        String civ = ChatColor.WHITE.toString() + kills[2];
        String neutral = ChatColor.WHITE.toString() + kills[3];

        String status = ChatColor.WHITE + (this.isVerified() ? plugin.getSettingsManager().getTrustedColor() + Language.getTranslation("verified") : plugin.getSettingsManager().getUntrustedColor() + Language.getTranslation("unverified"));

        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("id"), this.getId()));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("name.0"), name));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("status.0"), status));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("leaders.0"), leaders));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("members.online.0"), membersOnline));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("kdr.0"), kdr));
        sender.sendMessage("  " + subColor + Language.getTranslation("kill.totals") + " " + headColor + "[" + Language.getTranslation("rival") + ":" + rival + " " + headColor + Language.getTranslation("neutral") + ":" + neutral + " " + headColor + Language.getTranslation("civilian") + ":" + civ + headColor + "]");
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("deaths.0"), deaths));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("allies.0"), allies));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("rivals.0"), rivals));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("founded.0"), founded));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("inactive.0"), inactive));

        ChatBlock.sendBlank(sender);
    }

    @Override
    public boolean withdraw(double amount)
    {
        return bank.withdraw(amount);
    }

    @Override
    public void deposit(double amount)
    {
        bank.deposit(amount);
    }

    @Override
    public boolean transfer(double amount, Balance account)
    {
        return bank.transfer(amount, account);
    }

    @Override
    public double getBalance()
    {
        return bank.getBalance();
    }

    public void setBalance(double balance)
    {
        this.bank.setBalance(balance);
    }
}
