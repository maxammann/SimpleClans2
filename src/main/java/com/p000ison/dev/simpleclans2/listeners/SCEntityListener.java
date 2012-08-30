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
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.entity.Entity;
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

            Entity damager = ((EntityDamageByEntityEvent) lastDamageCause).getDamager();
            Player attackerPlayer = null;

            if (damager instanceof Projectile) {
                Projectile projectile = (Projectile) damager;

                if (!(projectile.getShooter() instanceof Player)) {
                    return;
                }

                attackerPlayer = (Player) projectile.getShooter();

            } else if (damager instanceof Player) {
                attackerPlayer = (Player) damager;
            }

            if (attackerPlayer != null) {
                ClanPlayer attacker = plugin.getClanPlayerManager().getCreateClanPlayerExact(attackerPlayer);


            }
        }
    }
}
