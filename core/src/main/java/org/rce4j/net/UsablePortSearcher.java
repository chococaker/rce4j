package org.rce4j.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.*;

/**
 * Finds a random unused port on the host machine.
 */
@SuppressWarnings("SpellCheckingInspection") // IntelliJ gets on my nerves sometimes.
public final class UsablePortSearcher {
    public static final Set<Integer> DEFAULT_EXCLUDED = Set.of( // ports acquired from https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers
            3659, // Battlefield 4
            3667, // Information Exchange
            4000, // Diablo II
            4018, // Protocol Information and Warnings
            4197, // Harman International's HControl protocol
            4200, // Angular app
            4444, // Metasploit listener port
            4730, // Gearman
            4739, // IP Flow Information Export
            5000, 5001, // Lots of shit
            5004, // RTP
            5005, // RTCP
            5022, // MSSQL Server Replication and Database mirroring endpoints
            5099, // SafeNet server-to-server
            5124, 5125, // Torga
            5172, // PC over IP Endpoint Management
            5201, // Iperf3
            5228, // Lots of shit
            5235, 5236, // Firebase
            5269, 5280, 5281, 5298, // XMPP
            5351, // NAT PMP & PCP
            5432, // Postgres
            5480, // VMware VAMI
            5550, // HP Data Protector
            5555, // Lots of shit
            5723, // SCOM
            5900, // RFBP
            6379, // Redis
            6432, // PGBouncer
            6463, 6464, 6465, 6466, 6467, 6468, 6469, 6470, 6471, 6472, // Discord RPC
            6622, // Multicast FTP
            7070, // RTSP
            7474, // Neo4j
            7777, // Lots of shit, including Terraria
            7946, // Docker Swarm
            8000, 8005, // Lots of shit
            8008, // HTTP Alternative
            8009, // ajp13
            8081, // Sun Proxy Admin Service
            8139, 8140, // Puppet
            8222, // VMware
            8332, 8333, // Bitcoin
            8629, // Tibero
            8887, // HyperVM HTTP
            8888, // Lots of shit
            9006, // Reserved
            9030, 9050, 9051, // TOOOOOOOOOOOOOOOOOOOOOOOOR
            9092, // H2
            9150, // TOOOOOOOOOOOOOOOOOOOOOOOOR
            9332, 9333, // LiteCoin
            9418, // Git
            9443, // VMware
            9735, // BitCoin
            9999, // Dash
            10823, // Farming Simulator 2011 ♪
            18333, // BitCoin testnet
            19132, // Minecraft Bedrock
            19133, // Minecraft Bedrock IPv6
            25565, 25575, // Minecraft Java
            27000, 27001, 27002, 27003, 27004, 27005, 27006, 27007, 27008, 27009, 27010, 27011, 27012, 27013, 27014, 27015, // Steam
            28015, // Rust
            28852, // Killing Floor
            33848, // Jenkins
            34197, // Factorio
            43594, 43595 // RuneScape (very important)
    );
    
    public static final int MINIMUM_PORT = 3_390;
    public static final int MAXIMUM_PORT = 49_150;
    
    private UsablePortSearcher() {}
    
    public static Integer get() {
        return get(DEFAULT_EXCLUDED);
    }
    
    public static Integer get(Collection<Integer> excluded) {
        int port;
        do {
            port = ((int) (Math.random() * MAXIMUM_PORT)) + 1;
        } while (excluded.contains(port) || !isAvailablePort(port));
        
        return port;
    }
    
    public static Collection<Integer> getAll() {
        return getAll(DEFAULT_EXCLUDED);
    }
    
    public static Collection<Integer> getAll(Collection<Integer> excluded) {
        List<Integer> ports = new ArrayList<>(MAXIMUM_PORT - MINIMUM_PORT - excluded.size());
        
        for (int i = MINIMUM_PORT; i < MAXIMUM_PORT; i++) {
            if (!excluded.contains(i) && isAvailablePort(i))
                ports.add(i);
        }
        
        return ports;
    }
    
    public static boolean isAvailablePort(int port) {
        try (ServerSocket ss = new ServerSocket(port);
             DatagramSocket ds = new DatagramSocket(port)) {
            ss.setReuseAddress(true);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}
