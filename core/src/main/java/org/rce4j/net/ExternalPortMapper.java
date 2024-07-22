package org.rce4j.net;

import org.rce4j.util.ConcurrentHashSet;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;

public final class ExternalPortMapper {
    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;
    
    private static final int DEFAULT_SEARCHER_THREAD_COUNT = 10;
    private static final int DEFAULT_TIMEOUT = 1_800_000; // 30 minutes
    
    private ExternalPortMapper() {
    }
    
    public static Set<Integer> scan(InetAddress address, TransmissionProtocol protocolType) {
        return scan(address, protocolType, DEFAULT_SEARCHER_THREAD_COUNT, DEFAULT_TIMEOUT, MIN_PORT, MAX_PORT);
    }
    
    public static Set<Integer> scan(InetAddress address, TransmissionProtocol protocolType, int start, int end) {
        return scan(address, protocolType, DEFAULT_SEARCHER_THREAD_COUNT, DEFAULT_TIMEOUT, start, end);
    }
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static Set<Integer> scan(InetAddress address, TransmissionProtocol protocolType, int threads, int timeout, int start, int end) {
        Set<Integer> ports = new ConcurrentHashSet<>();
        
        if (start < MIN_PORT || start > end)
            throw new IllegalArgumentException("Out of range start: " + start);
        
        if (end > MAX_PORT)
            throw new IllegalArgumentException("Out of range end: " + end);
        
        BiPredicate<Integer, Integer> portTimeoutPredicate;
        if (protocolType == TransmissionProtocol.TCP) {
            portTimeoutPredicate = (t, p) -> isOpenTcpPort(address, t, p);
        } else if (protocolType == TransmissionProtocol.UDP) {
            portTimeoutPredicate = (t, p) -> isOpenUdpPort(address, t, p);
        } else {
            throw new NullPointerException("Parameter: protocolType");
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        
        long startTime = System.currentTimeMillis();
        
        AtomicInteger port = new AtomicInteger(start);
        while (port.get() <= end) {
            int currentPort = port.getAndIncrement();
            executor.submit(() -> {
                int timeUntilEnd = timeout - (int) (System.currentTimeMillis() - startTime);
                
                if (portTimeoutPredicate.test(currentPort, timeUntilEnd))
                    ports.add(currentPort);
            });
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
        return Collections.unmodifiableSet(ports);
    }
    
    public static boolean isOpenTcpPort(InetAddress address, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(timeout);
            
            socket.connect(new InetSocketAddress(address, port));
            socket.close();
            
            System.out.println(port + " is open");
            return true;
        } catch (IOException ignored) {
            System.out.println(port + " is closed");
            return false;
        }
    }
    
    public static boolean isOpenUdpPort(InetAddress address, int port, int timeout) {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            
            socket.connect(address, port);
            socket.close();
            
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}
