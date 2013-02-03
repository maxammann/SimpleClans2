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


package com.p000ison.dev.simpleclans2.listeners;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.database.KillType;
import com.p000ison.dev.simpleclans2.database.statements.KillStatement;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Represents a SCEntityListener
 */
@SuppressWarnings("unused")
public class SCEntityListener implements Listener {

    private SimpleClans plugin;

    public SCEntityListener(SimpleClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity victimEntity = event.getEntity();

        if (victimEntity instanceof Player) {
            Player victimPlayer = (Player) victimEntity;
            Player attackerPlayer = null;
            Entity attackerEntity = event.getDamager();

            if (attackerEntity instanceof Player) {
                attackerPlayer = (Player) attackerEntity;
            } else if (attackerEntity instanceof Projectile) {
                LivingEntity shooter = ((Projectile) attackerEntity).getShooter();

                if (shooter instanceof Player) {
                    attackerPlayer = (Player) shooter;
                }
            }

            if (attackerPlayer == null) {
                return;
            }

            ClanPlayer attacker = plugin.getClanPlayerManager().getClanPlayer(attackerPlayer);
            ClanPlayer victim = plugin.getClanPlayerManager().getClanPlayer(victimPlayer);

            Clan attackerClan = attacker == null ? null : attacker.getClan();
            Clan victimClan = victim == null ? null : victim.getClan();

            if (plugin.getSettingsManager().isOnlyPvPinWar()) {

                // if one doesn't have clan then they cant be at war
                if (attackerClan == null || victimClan == null) {
                    event.setCancelled(true);
                    return;
                }

                if (victimPlayer.hasPermission("simpleclans.mod.nopvpinwar")) {
                    event.setCancelled(true);
                    return;
                }

                if (!attackerClan.isWarring(victimClan)) {
                    event.setCancelled(true);
                    return;
                }

                return;
            }

            if (victimClan != null && attackerClan != null) {
                    // personal ff enabled, allow damage
                    //skip if globalff is on
                    if (plugin.getSettingsManager().isGlobalFFForced() || victim.isFriendlyFireOn()) {
                        return;
                    }

                    // clan ff enabled, allow damage

                    if (plugin.getSettingsManager().isGlobalFFForced() || victimClan.isFriendlyFireOn()) {
                        return;
                    }

                    // same clan, deny damage

                    if (victimClan.equals(attackerClan)) {
                        event.setCancelled(true);
                        return;
                    }

                    // ally clan, deny damage

                    if (victimClan.isAlly(attackerClan)) {
                        event.setCancelled(true);
                    }
            } else {
                // not part of a clan - check if safeCivilians is set
                // ignore setting if he has a specific permissions
                if (plugin.getSettingsManager().isSaveCivilians() && !victimPlayer.hasPermission("simpleclans.ignore-safe-civilians")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victimPlayer = event.getEntity();

        if (victimPlayer != null) {

            ClanPlayer victim = plugin.getClanPlayerManager().getCreateClanPlayerExact(victimPlayer);

            EntityDamageEvent lastDamageCause = victimPlayer.getLastDamageCause();

            if (!(lastDamageCause instanceof EntityDamageByEntityEvent)) {
                return;
            }

            Entity attackerEntity = ((EntityDamageByEntityEvent) lastDamageCause).getDamager();
            Player attackerPlayer = null;

            if (attackerEntity instanceof Projectile) {
                LivingEntity shooter = ((Projectile) attackerEntity).getShooter();

                if (shooter instanceof Player) {
                    attackerPlayer = (Player) shooter;
                }

            } else if (attackerEntity instanceof Player) {
                attackerPlayer = (Player) attackerEntity;
            }

            if (attackerPlayer != null) {
                victim.addDeath();
                victim.update();

                ClanPlayer attacker = plugin.getClanPlayerManager().getCreateClanPlayerExact(attackerPlayer);
                Clan attackerClan = attacker.getClan();
                Clan victimClan = victim.getClan();

                KillType type;

                boolean war = false;

                if (victimClan == null || attackerClan == null || !victim.getClan().isVerified() || !attacker.getClan().isVerified()) {
                    attacker.addCivilianKill();
                    type = KillType.CIVILIAN;
                } else if (attackerClan.isRival(victimClan)) {

                    if (attackerClan.isWarring(victimClan)) {
                        war = true;
                    }

                    attacker.addRivalKill();
                    type = KillType.RIVAL;
                } else /*if (attackerClan.isAlly(victimClan))*/ {
                    attacker.addNeutralKill();
                    type = KillType.NEUTRAL;
                }

                attacker.update();

                plugin.getDataManager().addStatement(new KillStatement(attacker.getID(), attackerClan == null ? -1L : attackerClan.getID(),
                        victim.getID(), victimClan == null ? -1L : victimClan.getID(), war, type));
            }
        }
    }
}
