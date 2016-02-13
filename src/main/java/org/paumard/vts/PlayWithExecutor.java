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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author José Paumard
 */
public class PlayWithExecutor {

    public static void main(String... args) throws Exception {

        Supplier<Long> task = () -> {
            System.out.println("Current thread: " + Thread.currentThread().getName());
            System.out.println("Is it daemon? " + Thread.currentThread().isDaemon());
            try (Stream<String> lines = Files.lines(Paths.get("files/leipzig100k.txt"));) {
                return lines.count();
            } catch (IOException ex) {
                Logger.getLogger(PlayWithExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
            return -1L;
        };
        
        Executor executor = runnable -> new Thread(runnable).start();
        ExecutorService singleThreadES = Executors.newSingleThreadExecutor();
        
        CompletableFuture<Long> cf = CompletableFuture.supplyAsync(task, singleThreadES);
        cf.thenAccept(lines -> System.out.println("# lines : " + lines))
          .thenRun(() -> singleThreadES.shutdown());
        
        System.out.println("Done!");
        
        // Thread.sleep(1_000);
    }
}
