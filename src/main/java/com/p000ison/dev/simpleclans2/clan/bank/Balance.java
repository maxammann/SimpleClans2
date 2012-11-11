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
 *     Last modified: 30.10.12 00:11
 */

package com.p000ison.dev.simpleclans2.clan.bank;

/**
 *
 */
public interface Balance {

    /**
     * Withdraws money from a account for example. Please use only positive values!
     * This withdraws only if it would be successfully.
     *
     * @param amount The amount to withdraw
     * @return Weather it was successfully.
     */
    boolean withdraw(double amount);

    /**
     * Deposits money to a account for example. Please use only positive values!
     *
     * @param amount The amount to deposit
     */
    void deposit(double amount);

    /**
     * Transferees money from this account for example to another one.
     * <strong>If amount is positive:</strong>
     * this -> the other account
     * <p/>
     * <strong>If amount is negative</strong>
     * the other account -> this
     *
     * @param amount The amount to withdraw
     * @return Weather it was successfully.
     */
    boolean transfer(Balance account, double amount);

    /**
     * Gets the balance of the account.
     *
     * @return The balance of the account.
     */
    double getBalance();
}
