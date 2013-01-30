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
 *     Last modified: 1/9/13 9:44 PM
 */


package com.p000ison.dev.simpleclans2.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.Balance;
import com.p000ison.dev.simpleclans2.api.RelationType;
import com.p000ison.dev.simpleclans2.api.UpdateAble;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clan.ClanFlags;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.clanplayer.OnlineClanPlayer;
import com.p000ison.dev.simpleclans2.api.events.ClanRelationBreakEvent;
import com.p000ison.dev.simpleclans2.api.events.ClanRelationCreateEvent;
import com.p000ison.dev.simpleclans2.api.rank.Rank;
import com.p000ison.dev.simpleclans2.clan.ranks.CraftRank;
import com.p000ison.dev.simpleclans2.clanplayer.CraftClanPlayer;
import com.p000ison.dev.simpleclans2.database.response.responses.BBAddResponse;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.JSONUtil;
import com.p000ison.dev.sqlapi.TableObject;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumn;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumnGetter;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumnSetter;
import com.p000ison.dev.sqlapi.annotation.DatabaseTable;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a Clan
 */
@DatabaseTable(name = "sc2_clans")
public class CraftClan implements Clan, TableObject, UpdateAble {

    private transient SimpleClans plugin;

    private CraftClanFlags flags;
    private BankAccount bank;

    @DatabaseColumn(position = 0, databaseName = "id", id = true)
    private long id = -1;

    private String tag;
    private String name;

    private AtomicLong foundedDate = new AtomicLong(-1L);
    private AtomicLong lastActionDate = new AtomicLong(0L);
    private AtomicBoolean verified = new AtomicBoolean(false);

    private Set<Clan> allies;
    private Set<Clan> rivals;
    private Set<Clan> warring;
    private Set<ClanPlayer> allMembers;
    private Set<Rank> ranks;

    private AtomicBoolean update = new AtomicBoolean(false);

    //Locks
    private final static Object nameLock = new Object();

    public static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    public static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("MMM dd, yyyy h:mm a");
    public static final int MAX_NAME_LENGTH = 100, MAX_TAG_LENGTH = 26;
    private static final long serialVersionUID = 2276260953605541164L;

    /**
     * This is called by the SQLDatabaseAPI to build a clan
     *
     * @param plugin The plugin
     */
    public CraftClan(SimpleClans plugin) {
        flags = new CraftClanFlags();
        this.plugin = plugin;
    }

    /**
     * Creates a new clan
     *
     * @param plugin The plugin
     * @param tag    The tag of this clan
     * @param name   The full name of this clan
     */
    public CraftClan(SimpleClans plugin, String tag, String name) {
        this(plugin);
        flags = new CraftClanFlags();
        setTag(tag);
        setName(name);
    }

    public CraftClan() {
    }

    @Override
    public String getFoundedDateFormatted() {

        return DATE_FORMAT.format(this.foundedDate.get());
    }

    @Override
    @DatabaseColumnGetter(databaseName = "tag")
    public String getTag() {
        synchronized (nameLock) {
            return tag;
        }
    }

    @Override
    @DatabaseColumnSetter(position = 1, databaseName = "tag", notNull = true, lenght = CraftClan.MAX_TAG_LENGTH, unique = true)
    public void setTag(String tag) {
        Validate.notNull(tag, "The clan tag must not be null!");
        synchronized (nameLock) {
            Validate.isTrue(tag.length() <= MAX_TAG_LENGTH, "The clan tag is too long!");
            this.tag = tag;
        }
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public ClanFlags getFlags() {
        return flags;
    }

    @Override
    @DatabaseColumnGetter(databaseName = "name")
    public String getName() {
        synchronized (nameLock) {
            return name;
        }
    }

    @Override
    @DatabaseColumnSetter(position = 2, databaseName = "name", notNull = true, lenght = CraftClan.MAX_NAME_LENGTH, unique = true)
    public void setName(String name) {
        Validate.notNull(tag, "The clan name must not be null!");
        synchronized (nameLock) {
            Validate.isTrue(name.length() <= MAX_NAME_LENGTH, "The clan name is too long!");
            this.name = name;
        }
    }

    @Override
    @DatabaseColumnGetter(databaseName = "last_action")
    public Date getLastActionDate() {
        return new Date(lastActionDate.get());
    }

    @DatabaseColumnSetter(position = 5, databaseName = "last_action")
    public void setLastActionDate(Date lastActionDate) {
        if (lastActionDate == null) {
            this.lastActionDate.set(System.currentTimeMillis());
            return;
        }
        this.lastActionDate.set(lastActionDate.getTime());
    }

    public void setLastAction(long lastActionDate) {
        this.lastActionDate.set(lastActionDate);
    }

    @Override
    public void updateLastAction() {
        this.lastActionDate.set(System.currentTimeMillis());
    }

    @Override
    @DatabaseColumnGetter(databaseName = "founded")
    public Date getFoundedDate() {
        return new Date(foundedDate.get());
    }

    @DatabaseColumnSetter(position = 4, databaseName = "founded")
    public void setFoundedDate(Date foundedDate) {
        if (foundedDate == null) {
            this.foundedDate.set(System.currentTimeMillis());
            return;
        }
        this.foundedDate.set(foundedDate.getTime());
    }

    public void setFoundedTime(long foundedDate) {
        this.foundedDate.set(foundedDate);
    }

    public long getFoundedTime() {
        return this.foundedDate.get();
    }

    @Override
    @DatabaseColumnGetter(databaseName = "verified")
    public boolean isVerified() {
        return verified.get();
    }

    @Override
    @DatabaseColumnSetter(position = 3, databaseName = "verified", defaultValue = "0")
    public void setVerified(boolean verified) {
        this.verified.set(verified);
    }

    @Override
    public boolean isFriendlyFireOn() {
        return getFlags().isFriendlyFireEnabled();
    }

    @Override
    public void setFriendlyFire(boolean friendlyFire) {
        getFlags().setFriendlyFire(friendlyFire);
    }

    @Override
    public Set<Clan> getAllies() {
        return allies == null ? new HashSet<Clan>() : Collections.unmodifiableSet(allies);
    }

    @Override
    public Set<Clan> getRivals() {
        return rivals == null ? new HashSet<Clan>() : Collections.unmodifiableSet(rivals);
    }

    @Override
    public Set<Clan> getWarringClans() {
        return warring == null ? new HashSet<Clan>() : Collections.unmodifiableSet(warring);
    }

    @Override
    public boolean isAlly(long id) {
        if (this.getID() == id) {
            return true;
        }

        if (allies == null) {
            return false;
        }

        for (Clan clan : allies) {
            if (clan.getID() == id) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isAlly(Clan clan) {
        return clan != null && (this.equals(clan) || (allies != null && allies.contains(clan)));
    }

    @Override
    public boolean isRival(long id) {
        if (this.getID() == id) {
            return false;
        }

        if (rivals == null) {
            return false;
        }

        for (Clan clan : rivals) {
            if (clan.getID() == id) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isRival(Clan clan) {
        return clan != null && (this.equals(clan) || (rivals != null && rivals.contains(clan)));
    }

    @Override
    public boolean isWarring(long id) {
        if (this.getID() == id) {
            return false;
        }

        if (warring == null) {
            return false;
        }

        for (Clan clan : warring) {
            if (clan.getID() == id) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isWarring(Clan clan) {
        return clan != null && (this.equals(clan) || (warring != null && warring.contains(clan)));
    }

    @Override
    public int getInactiveDays() {
        return (int) Math.round(DateHelper.differenceInDays(lastActionDate.get(), System.currentTimeMillis()));
    }

    @Override
    public boolean setLeader(ClanPlayer clanPlayer) {
        if (clanPlayer.getClanID() != id) {
            return false;
        }

        clanPlayer.setLeader(true);
        return true;
    }

    @Override
    public boolean demote(ClanPlayer clanPlayer) {

        if (clanPlayer.getClanID() != id) {
            return false;
        }

        clanPlayer.setLeader(false);
        return true;
    }

    @Override
    public boolean isMember(ClanPlayer cp) {
        if (allMembers == null) {
            return false;
        }

        for (ClanPlayer comparePlayer : allMembers) {

            if (!comparePlayer.equals(cp)) {
                continue;
            }

            if (cp.getClanID() == id && !cp.isLeader()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isLeader(ClanPlayer cp) {
        return cp.getClanID() == id && cp.isLeader();
    }

    @Override
    public boolean isAnyMember(ClanPlayer cp) {
        return allMembers != null && allMembers.contains(cp);
    }

    @Override
    public Set<ClanPlayer> getMembers() {
        Set<ClanPlayer> members = new HashSet<ClanPlayer>();

        for (ClanPlayer cp : getAllMembers()) {
            if (cp.getClanID() == id && !cp.isLeader()) {
                members.add(cp);
            }
        }

        return Collections.unmodifiableSet(members);
    }

    @Override
    public Set<ClanPlayer> getAllMembers() {
        return allMembers == null ? Collections.unmodifiableSet(new HashSet<ClanPlayer>()) : Collections.unmodifiableSet(allMembers);
    }

    @Override
    public Set<ClanPlayer> getLeaders() {
        Set<ClanPlayer> leaders = new HashSet<ClanPlayer>();

        for (ClanPlayer cp : getAllMembers()) {
            if (cp.isLeader()) {
                leaders.add(cp);
            }
        }

        return Collections.unmodifiableSet(leaders);
    }

    @Override
    public void addMember(ClanPlayer clanPlayer) {
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

        if (allMembers == null) {
            allMembers = new HashSet<ClanPlayer>();
        }

        allMembers.add(clanPlayer);
        clanPlayer.setClan(this);
        ((CraftClanPlayer) clanPlayer).updatePermissions();
        clanPlayer.update();
    }

    @Override
    public float getKDR() {
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

    @Override
    public int getSize() {
        return allMembers == null ? 0 : allMembers.size();
    }

    @Override
    public void addBBMessage(ClanPlayer announcer, String msg) {
        announce(announcer, msg);
        addBBRawMessage(ChatBlock.parseColors(plugin.getSettingsManager().getClanPlayerBB().replace("+player", announcer.getName()).replace("+message", msg)));
    }

    @Override
    public void addBBMessage(Clan announcer, String msg) {
        msg = ChatBlock.parseColors(plugin.getSettingsManager().getClanBB().replace("+clan", announcer.getTag()).replace("+message", msg));
        announce(msg);
        addBBRawMessage(msg);
    }

    @Override
    public void announce(ClanPlayer announcer, String msg) {
        announceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getClanPlayerAnnounce().replace("+player", announcer.getName()).replace("+message", msg)));
    }

    @Override
    public void announce(Clan announcer, String msg) {
        announceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getClanAnnounce().replace("+clan", announcer.getTag()).replace("+message", msg)));
    }

    @Override
    public void announce(String msg) {
        announceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getDefaultAnnounce().replace("+message", msg)));
    }

    /**
     * announces a message to all clan players raw
     * <p/>
     * <p><strong>Internally used!</strong></p>
     *
     * @param message The message
     */
    private void announceRaw(String message) {
        if (message == null) {
            return;
        }

        for (ClanPlayer clanPlayer : getAllMembers()) {
            Player player = clanPlayer.toPlayer();
            if (player != null) {
                ChatBlock.sendMessage(player, message);
            }
        }
    }

    @Override
    public String getCleanTag() {
        return ChatColor.stripColor(tag.toLowerCase(Locale.US));
    }

    @Override
    public void addBBMessage(String msg) {
        addBBRawMessage(ChatBlock.parseColors(plugin.getSettingsManager().getDefaultBB().replace("+message", msg)));
    }

    private void addBBRawMessage(String message) {
        plugin.getDataManager().addResponse(new BBAddResponse(plugin, message, this));
    }

    @Override
    public void clearBB() {
        plugin.getDataManager().purgeBB(this);
    }

    @Override
    public boolean equals(Object otherClan) {
        if (this == otherClan) {
            return true;
        }

        if (otherClan == null || !(otherClan instanceof Clan)) {
            return false;
        }

        Clan clan = (Clan) otherClan;

        return id == clan.getID();
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public void addRival(Clan rival) {
        if (rivals == null) {
            rivals = new HashSet<Clan>();
        }
        addRelation(RelationType.RIVAL, rivals, rival);
    }

    @Override
    public void addAlly(Clan ally) {
        if (allies == null) {
            allies = new HashSet<Clan>();
        }
        addRelation(RelationType.ALLY, allies, ally);
    }

    @Override
    public void addWarringClan(Clan warringClan) {
        if (warring == null) {
            warring = new HashSet<Clan>();
        }
        addRelation(RelationType.WAR, warring, warringClan);
    }

    private void addRelation(RelationType relationType, Set<Clan> relationSet, Clan clanToAdd) {
        ClanRelationCreateEvent relationEvent = new ClanRelationCreateEvent(this, clanToAdd, relationType);

        plugin.getServer().getPluginManager().callEvent(relationEvent);

        if (relationEvent.isCancelled()) {
            return;
        }

        relationSet.add(clanToAdd);
    }

    @Override
    public void removeRival(Clan rival) {
        removeRelation(RelationType.RIVAL, rivals, rival);
    }

    @Override
    public void removeAlly(Clan ally) {
        removeRelation(RelationType.ALLY, allies, ally);
    }

    @Override
    public void removeWarringClan(Clan warringClan) {
        removeRelation(RelationType.WAR, warring, warringClan);
    }

    private void removeRelation(RelationType relationType, Set<Clan> relationSet, Clan clanToRemove) {
        if (relationSet == null) {
            return;
        }

        ClanRelationBreakEvent relationEvent = new ClanRelationBreakEvent(this, clanToRemove, relationType);

        plugin.getServer().getPluginManager().callEvent(relationEvent);

        if (relationEvent.isCancelled()) {
            return;
        }

        relationSet.remove(clanToRemove);
    }

    @Override
    public void removeMember(ClanPlayer clanPlayer) {
        if (allMembers == null) {
            return;
        }

        if (allMembers.remove(clanPlayer)) {
            clanPlayer.unset();
            clanPlayer.update();
            if (clanPlayer.isLeader()) {
                disband();
            }
            plugin.getRequestManager().clearRequests(clanPlayer);
        }
    }

    @Override
    public void disband() {
        if (allMembers != null) {
            for (ClanPlayer clanPlayer : allMembers) {
                clanPlayer.unset();

                String pastClan = getTag();

                if (clanPlayer.isLeader()) {
                    pastClan += '*';
                }

                clanPlayer.addPastClan(pastClan);
                clanPlayer.update();
            }
        }

        if (warring != null) {
            for (Clan warringClan : warring) {
                warringClan.removeWarringClan(this);
                warringClan.addBBMessage(this, Language.getTranslation("you.are.no.longer.at.war", this.getTag(), warringClan.getTag()));
            }
        }

        if (allies != null) {
            for (Clan allyClan : allies) {
                allyClan.removeAlly(this);
                allyClan.addBBMessage(this, Language.getTranslation("has.been.disbanded.alliance.ended", this.getTag()));
            }
        }

        if (rivals != null) {
            for (Clan rivalClan : rivals) {
                rivalClan.removeRival(this);
                rivalClan.addBBMessage(this, Language.getTranslation("has.been.disbanded.rivalry.ended", this.getTag()));
            }
        }

        plugin.getRequestManager().clearRequests(this);
        plugin.getClanManager().removeClan(this);

        plugin.getDataManager().getDatabase().delete(this);
    }

    @Override
    public boolean hasAllies() {
        return allies != null && !allies.isEmpty();
    }

    @Override
    public boolean hasRivals() {
        return rivals != null && !rivals.isEmpty();
    }

    @Override
    public boolean hasWarringClans() {
        return warring != null && !warring.isEmpty();
    }

    @Override
    public boolean allLeadersOnline() {
        return allLeadersOnline(null);
    }

    @Override
    public boolean allLeadersOnline(ClanPlayer ignore) {
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

    public boolean needsUpdate() {
        return update.get();
    }

    @Override
    public void update() {
        this.update.set(true);
    }

    @Override
    public void update(boolean update) {
        this.update.set(update);
    }

    @Override
    public long getLastUpdated() {
        return lastActionDate.get();
    }

    @Override
    public int[] getTotalKills() {
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

    @Override
    public void addRank(Rank rank) {
        if (rank == null) {
            return;
        }
        if (ranks == null) {
            ranks = new HashSet<Rank>();
        }

        ranks.add(rank);
    }

    @Override
    public long deleteRank(Rank rank) {
        if (rank == null || ranks == null) {
            return -1;
        }

        long id = rank.getID();

        if (ranks.remove(rank)) {
            for (ClanPlayer member : allMembers) {
                if (member.getRank().equals(rank)) {
                    member.assignRank(null);
                    member.update();
                }
            }
            return id;
        }

        return -1;
    }

    @Override
    public long deleteRank(String tag) {
        if (ranks == null) {
            return -1;
        }

        Iterator<Rank> it = ranks.iterator();

        while (it.hasNext()) {
            Rank rank = it.next();
            if (rank.getTag().startsWith(tag)) {
                long id = rank.getID();
                for (ClanPlayer member : allMembers) {
                    Rank memberRank = member.getRank();
                    if (memberRank != null) {
                        if (memberRank.equals(rank)) {
                            member.assignRank(null);
                            member.update();
                        }
                    }
                }
                it.remove();
                return id;
            }
        }
        return -1;
    }

    @Override
    public Set<Rank> getRanks() {
        return ranks == null ? Collections.unmodifiableSet(new HashSet<Rank>()) : Collections.unmodifiableSet(ranks);
    }

    public void loadRanks(Set<CraftRank> ranks) {
        if (this.ranks == null) {
            this.ranks = new HashSet<Rank>();
        }
        this.ranks.addAll(ranks);
    }

    @Override
    public Rank getRank(long id) {
        if (ranks == null) {
            return null;
        }

        for (Rank rank : ranks) {
            if (rank.getID() == id) {
                return rank;
            }
        }

        return null;
    }

    @Override
    public Rank getRank(String query) {
        if (ranks == null) {
            return null;
        }

        String cleanQuery = query.toLowerCase(Locale.US);
        for (Rank rank : ranks) {
            if (rank.getTag().toLowerCase(Locale.US).startsWith(cleanQuery)) {
                return rank;
            }
        }

        return null;
    }

    @Override
    public Rank getRankByName(String name) {
        if (ranks == null) {
            return null;
        }

        String cleanQuery = name.toLowerCase(Locale.US);
        for (Rank rank : ranks) {
            if (rank.getName().toLowerCase(Locale.US).startsWith(cleanQuery)) {
                return rank;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Clan{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", lastActionDate=" + lastActionDate +
                ", verified=" + verified +
                ", update=" + update +
                '}';
    }

    @Override
    public int compareTo(Clan anotherClan) {
        int thisInactiveDate = this.getInactiveDays();
        int anotherInactiveDate = anotherClan.getInactiveDays();
        return (thisInactiveDate < anotherInactiveDate ? -1 : (thisInactiveDate == anotherInactiveDate ? 0 : 1));
    }

    @Override
    public Set<ClanPlayer> getAllAllyMembers() {
        Set<ClanPlayer> allyMembers = new HashSet<ClanPlayer>();

        for (Clan ally : getAllies()) {
            allyMembers.addAll(ally.getAllMembers());
        }

        return Collections.unmodifiableSet(allyMembers);
    }

    @Override
    public boolean reachedRivalLimit() {
        int rivalCount = rivals == null ? 0 : rivals.size();
        //minus 1 because this clan is rivable
        double clanCount = plugin.getClanManager().getRivalAbleClanCount() - 1;
        double rivalPercent = plugin.getSettingsManager().getRivalLimitPercent();

        double limit = clanCount * rivalPercent / 100.0D;

        return rivalCount > limit;
    }

    public void removePermissions() {
        plugin.getServer().getPluginManager().removePermission("SC" + String.valueOf(id));
    }

    public void setupPermissions(Map<String, Boolean> permissions) {
        removePermissions();

        plugin.registerSimpleClansPermission("SC" + String.valueOf(id), permissions);
    }

    public void updatePermissions() {
        for (ClanPlayer clanPlayer : getAllMembers()) {
            OnlineClanPlayer online = clanPlayer.getOnlineVersion();
            if (online == null) {
                continue;
            }

            online.removePermissions();
            online.setupPermissions();
        }
    }

    @Override
    public void serverAnnounce(String message) {
        SimpleClans.serverAnnounceRaw(ChatBlock.parseColors(plugin.getSettingsManager().getClanAnnounce().replace("+clan", this.getTag()).replace("+message", message)));
    }

    @Override
    public void showClanProfile(CommandSender sender) {
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
        String founded = ChatColor.WHITE + this.getFoundedDateFormatted();

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

        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("tag.0"), this.getTag()));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("name.0"), name));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("status.0"), status));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("leaders.0"), leaders));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("members.online.0"), membersOnline));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("kdr.0"), kdr));
        ChatBlock.sendMessage(sender, "  " + subColor + Language.getTranslation("kill.totals") + " " + headColor + "[" + Language.getTranslation("rival") + ":" + rival + " " + headColor + Language.getTranslation("neutral") + ":" + neutral + " " + headColor + Language.getTranslation("civilian") + ":" + civ + headColor + "]");
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("deaths.0"), deaths));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("allies.0"), allies));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("rivals.0"), rivals));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("founded.0"), founded));
        ChatBlock.sendMessage(sender, "  " + subColor + MessageFormat.format(Language.getTranslation("inactive.0"), inactive));

        ChatBlock.sendBlank(sender);
    }

    @Override
    public boolean withdraw(double amount) {
        return this.getBank().withdraw(amount);
    }

    @Override
    public void deposit(double amount) {
        this.getBank().deposit(amount);
    }

    @Override
    public boolean transfer(Balance account, double amount) {
        return this.getBank().transfer(account, amount);
    }

    @DatabaseColumnGetter(databaseName = "balance")
    @Override
    public double getBalance() {
        return this.getBank().getBalance();
    }

    @DatabaseColumnSetter(position = 10, databaseName = "balance", defaultValue = "0.0", lenght = {20, 2})
    private void setBalance(double balance) {
        this.getBank().setBalance(balance);
    }

    private BankAccount getBank() {
        if (bank == null) {
            bank = new BankAccount();
        }

        return bank;
    }

    @DatabaseColumnGetter(databaseName = "allies")
    private String getDatabaseAllies() {
        return allies == null || allies.isEmpty() ? null : JSONUtil.clansToJSON(allies);
    }

    @DatabaseColumnSetter(position = 6, databaseName = "allies", saveValueAfterLoading = true)
    private void setDatabaseAllies(String allies) {
        addDatabaseRelative(allies, this.allies = new HashSet<Clan>());
    }

    @DatabaseColumnGetter(databaseName = "rivals")
    private String getDatabaseRivals() {
        return rivals == null || rivals.isEmpty() ? null : JSONUtil.clansToJSON(rivals);
    }

    @DatabaseColumnSetter(position = 7, databaseName = "rivals", saveValueAfterLoading = true)
    private void setDatabaseRivals(String rivals) {
        addDatabaseRelative(rivals, this.rivals = new HashSet<Clan>());
    }

    @DatabaseColumnGetter(databaseName = "warring")
    private String getDatabaseWarring() {
        return warring == null || warring.isEmpty() ? null : JSONUtil.clansToJSON(warring);
    }

    @DatabaseColumnSetter(position = 8, databaseName = "warring", saveValueAfterLoading = true)
    private void setDatabaseWarring(String warring) {
        addDatabaseRelative(warring, this.warring = new HashSet<Clan>());
    }

    private void addDatabaseRelative(String json, Set<Clan> clans) {
        if (json == null) {
            return;
        }

        Set<Long> ids = JSONUtil.JSONToLongSet(json);

        if (ids == null) {
            return;
        }

        for (long clanId : ids) {
            Clan clan = plugin.getClanManager().getClan(clanId);
            if (clan != null) {
                clans.add(clan);
            }
        }
    }

    @DatabaseColumnSetter(position = 9, databaseName = "flags")
    public void setDatabaseFlags(String flags) {
        if (flags == null) {
            this.flags = new CraftClanFlags();
        }
        this.flags.deserialize(flags);
    }

    @DatabaseColumnGetter(databaseName = "flags")
    public String getDatabaseFlags() {
        if (getFlags() == null) {
            return null;
        }
        return flags.serialize();
    }

    public void addMemberInternally(ClanPlayer cp) {
        if (allMembers == null) {
            this.allMembers = new HashSet<ClanPlayer>();
        }
        this.allMembers.add(cp);
    }

    @Override
    public Iterator<ClanPlayer> iterator() {
        return allMembers.iterator();
    }
}
