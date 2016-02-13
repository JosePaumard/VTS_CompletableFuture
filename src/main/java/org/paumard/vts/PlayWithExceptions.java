/*
 * Copyright (C) 2016 José Paumard
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.paumard.vts;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author José Paumard
 */
public class PlayWithExceptions {

    public static void main(String... args) throws Exception {

        CompletableFuture<Void> cf1 = new CompletableFuture();
        
        CompletableFuture<Void> cf21 = cf1.thenRun(() -> System.out.println("Executing task 21"));
        
        CompletableFuture<Void> cf22 = cf1.thenRun(() -> System.out.println("Executing task 22"));
        
        /*
        CompletableFuture<Void> cf22 = cf1.thenRun(
                () -> {
                   throw new NullPointerException("CF 22 NPE"); 
                });*/
        
        CompletableFuture<Void> cf30 = cf22.exceptionally(
                throwable -> {
                    System.out.println(throwable.getMessage());
                    return null;
                }
        );
        
        CompletableFuture<Void> cf31 = cf30.thenRun(() -> System.out.println("Executing task 31"));
        CompletableFuture<Void> cf32 = new CompletableFuture();
        
        CompletableFuture<Void> cf41 = CompletableFuture.allOf(cf31, cf32);
        
        cf41.thenRun(() -> System.out.println("Task 41 excuted"));
        
        cf1.complete(null);
        cf32.complete(null);
        
        Thread.sleep(1_000);
        
        System.out.println("CF 22 completed exceptionnaly: " + cf22.isCompletedExceptionally());
        System.out.println("CF 31 completed exceptionnaly: " + cf31.isCompletedExceptionally());
        System.out.println("CF 22 exception: " + cf22.join());
        
        System.out.println("Exiting");
    }
}
