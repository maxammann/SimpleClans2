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
 *     Created: 10/2/12 10:20 PM
 */

package com.p000ison.dev.simpleclans2.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a PlayerState
 */
public class PlayerState {
    private Player player;
    private static Map<Material, Integer> FOOD = new HashMap<Material, Integer>();
    private static final DecimalFormat FOOD_FORMAT = new DecimalFormat("#.#");

    private static Map<Material, Integer> WEAPONS = new HashMap<Material, Integer>();
//    private static Map<Material, Integer> GOLD_TOOLS = new HashMap<Material, Integer>();
//    private static Map<Material, Integer> STONE_TOOLS = new HashMap<Material, Integer>();
//    private static Map<Material, Integer> IRON_TOOLS = new HashMap<Material, Integer>();
//    private static Map<Material, Integer> DIAMOND_TOOLS = new HashMap<Material, Integer>();
//    private static Map<Material, Integer> OTHER_TOOLS = new HashMap<Material, Integer>();


    private static Map<Material, Integer> ARMOR = new HashMap<Material, Integer>();

    static {
        FOOD.put(Material.GRILLED_PORK, 8);
        FOOD.put(Material.COOKED_FISH, 5);
        FOOD.put(Material.COOKIE, 1);
        FOOD.put(Material.CAKE, 12);
        FOOD.put(Material.MUSHROOM_SOUP, 10);
        FOOD.put(Material.BREAD, 5);
        FOOD.put(Material.APPLE, 4);
        FOOD.put(Material.GOLDEN_APPLE, 8);
        FOOD.put(Material.RAW_BEEF, 3);
        FOOD.put(Material.COOKED_BEEF, 8);
        FOOD.put(Material.PORK, 3);
        FOOD.put(Material.RAW_CHICKEN, 2);
        FOOD.put(Material.COOKED_CHICKEN, 6);
        FOOD.put(Material.ROTTEN_FLESH, 4);
        FOOD.put(Material.MELON_STEM, 2);
        //1.4
        //potato;carrot;baked potato; golden carrot; carrot; pumpkin pie

        WEAPONS.put(Material.WOOD_SWORD, 4);
        WEAPONS.put(Material.GOLD_SWORD, 4);
        WEAPONS.put(Material.STONE_SWORD, 6);
        WEAPONS.put(Material.IRON_SWORD, 8);
        WEAPONS.put(Material.DIAMOND_SWORD, 10);

        ARMOR.put(Material.LEATHER_HELMET, 1);
        ARMOR.put(Material.LEATHER_CHESTPLATE, 3);
        ARMOR.put(Material.LEATHER_LEGGINGS, 2);
        ARMOR.put(Material.LEATHER_BOOTS, 1);

        ARMOR.put(Material.GOLD_HELMET, 2);
        ARMOR.put(Material.GOLD_CHESTPLATE, 5);
        ARMOR.put(Material.GOLD_LEGGINGS, 3);
        ARMOR.put(Material.GOLD_BOOTS, 1);

        ARMOR.put(Material.CHAINMAIL_HELMET, 2);
        ARMOR.put(Material.CHAINMAIL_CHESTPLATE, 5);
        ARMOR.put(Material.CHAINMAIL_LEGGINGS, 4);
        ARMOR.put(Material.CHAINMAIL_BOOTS, 1);

        ARMOR.put(Material.IRON_HELMET, 2);
        ARMOR.put(Material.IRON_CHESTPLATE, 6);
        ARMOR.put(Material.IRON_LEGGINGS, 5);
        ARMOR.put(Material.IRON_BOOTS, 2);

        ARMOR.put(Material.DIAMOND_HELMET, 3);
        ARMOR.put(Material.DIAMOND_CHESTPLATE, 8);
        ARMOR.put(Material.DIAMOND_LEGGINGS, 6);
        ARMOR.put(Material.DIAMOND_BOOTS, 3);
    }


    public PlayerState(Player player)
    {
        this.player = player;
    }

    public String getFood(String format)
    {
        ItemStack[] contents = player.getInventory().getContents();

        double food = 0;

        for (ItemStack itemStack : contents) {
            if (itemStack == null) {
                continue;
            }

            Integer value = FOOD.get(itemStack.getType());

            if (value == null) {
                continue;
            }

            food += value;
        }

        food /= 2;

        if (food == 0) {
            return null;
        } else {
            return String.format(format, FOOD_FORMAT.format(food));
        }
    }

    public String getHealth()
    {
        int health = player.getHealth();
        return getBar(health);
    }

    public String getHunger()
    {
        int hunger = player.getFoodLevel();
        return getBar(hunger);
    }

    private static String getBar(int amount)
    {
        StringBuilder bar = new StringBuilder();

        if (amount > 0.80 * amount) {
            bar.append(ChatColor.GREEN);
        } else if (amount >= 0.45 * amount) {
            bar.append(ChatColor.GOLD);
        } else {
            bar.append(ChatColor.RED);
        }

        for (int i = 0; i < amount; i++) {
            bar.append('|');
        }

        return bar.toString();
    }
}
