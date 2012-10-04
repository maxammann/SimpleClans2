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
 *     Created: 04.10.12 13:26
 */

package com.p000ison.dev.simpleclans2.test;

import com.p000ison.dev.simpleclans2.util.ChatBlock;
import com.p000ison.dev.simpleclans2.util.chat.Align;

/**
 * Represents a Test
 */
public class Test {

    public static void main(String[] args)
    {
//        System.out.println("['s sdfg ]'s".replaceAll("[\\['s']|['\\]'s']", ""));
//        for (int i = 80; i < 103; i++) {
//            System.out.println((char)i);
//        }

//        long start = System.currentTimeMillis();
//
//        StringBuilder sb = new StringBuilder("123456789");
//        System.out.println(ChatBlock.msgLength(sb));
//
//        ChatBlock.center(sb, 70);
//        System.out.print('|');
//        System.out.print(sb.toString());
//        System.out.print('|');
//
//        System.out.println();
//
//
//        long finish = System.currentTimeMillis();
//        System.out.printf("Check took %s!", finish - start);

        long start = System.currentTimeMillis();

        com.p000ison.dev.simpleclans2.util.ChatBlock.ChatBlock impl = new ChatBlock.ChatBlock();
        impl.setAlignment(Align.LEFT);
        for (int y = 0; y < 10; y++) {
            impl.addRow("1");
        }
        impl.sendBlock(null);
        long finish = System.currentTimeMillis();
        System.out.printf("Check took %s!", finish - start);


    }
}
