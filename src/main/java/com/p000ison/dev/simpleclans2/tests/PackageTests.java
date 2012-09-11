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
 *     Created: 10.09.12 19:30
 */

package com.p000ison.dev.simpleclans2.tests;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.commands.Command;
import com.p000ison.dev.simpleclans2.util.ClassesHelper;

import java.lang.reflect.Constructor;

/**
 * Represents a PackageTests
 */
public class PackageTests {

    public static void main(String[] args)
    {
        try {
            for (Class clazz : ClassesHelper.getClasses("com.p000ison.dev.simpleclans2.commands.commands")) {
                System.out.println(clazz.getName());
                Class<? extends Command> runClass = clazz.asSubclass(Command.class);
                Constructor<? extends Command> ctor = runClass.getConstructor(SimpleClans.class);
                SimpleClans d = new SimpleClans();
                System.out.println(ctor.newInstance(d).getName());
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
