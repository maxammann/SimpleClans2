/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.listeners;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Represents a SCEntityListener
 */
public class SCEntityListener implements Listener {

    private SimpleClans plugin;

    public SCEntityListener(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
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

            Clan attackerClan = attacker.getClan();
            Clan victimClan = victim.getClan();

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

            if (victimClan != null) {
                if (attackerClan != null) {
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

                    if (victim.equals(attackerClan)) {
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
            } else {
                // not part of a clan - check if safeCivilians is set
                // ignore setting if he has a specific permissions
                if (plugin.getSettingsManager().isSaveCivilians() && !victimPlayer.hasPermission("simpleclans.ignore-safe-civilians")) {
                    event.setCancelled(true);
                }
            }

        }
    }

    public void onPlayerDeath(PlayerDeathEvent event)
    {

        Player victimPlayer = event.getEntity();

        if (victimPlayer != null) {

            ClanPlayer victim = plugin.getClanPlayerManager().getCreateClanPlayerExact(victimPlayer);

            victim.addDeath();

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
                ClanPlayer attacker = plugin.getClanPlayerManager().getCreateClanPlayerExact(attackerPlayer);


            }
        }
    }
}
