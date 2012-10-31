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
 *     Last modified: 29.10.12 23:52
 */

package com.p000ison.dev.simpleclans2.clan.bank;

/**
 * Represents a DepositCommand
 */
public class BankAccount implements Balance {
    private double balance;

    public static final double MAX_BALANCE = 10E20;

    public BankAccount(double balance)
    {
        this.balance = balance;
    }

    @Override
    public double getBalance()
    {
        return balance;
    }

    public void setBalance(double balance)
    {
        if (balance < 0.0D) {
            throw new IllegalArgumentException("The balance can not be negative!");
        } else if (balance > MAX_BALANCE) {
            throw new IllegalArgumentException(String.format("The balance passed the maximum of %s", MAX_BALANCE));
        }

        this.balance = balance;
    }

    @Override
    public boolean withdraw(double amount)
    {
        if (amount < 0.0D) {
            throw new IllegalArgumentException("The amount can not be negative if you withdraw something!");
        }

        if (amount > balance) {
            return false;
        }

        balance -= amount;
        return true;
    }

    @Override
    public void deposit(double amount)
    {
        if (amount < 0.0D) {
            throw new IllegalArgumentException("The amount can not be negative if you withdraw something!");
        }

        double test = balance + amount;

        if (test > MAX_BALANCE) {
            throw new IllegalArgumentException(String.format("The balance passed the maximum of %s", MAX_BALANCE));
        }

        balance = test;
    }

    @Override
    public boolean transfer(double amount, Balance account)
    {
        if (amount > 0.0D) {
            amount = Math.abs(amount);

            if (!this.withdraw(amount)) {
                return false;
            }

            account.deposit(amount);
        } else {
            amount = Math.abs(amount);
            if (!account.withdraw(amount)) {
                return false;
            }

            this.deposit(amount);
        }

        return true;
    }
}
