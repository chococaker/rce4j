package org.rce4j.net.dos;

import java.net.InetAddress;

public class DosAttack {
    public static final long DEFAULT_DURATION = 150000L; // 30 minutes (wow)
    public static final int DEFAULT_POWER = 100;
    
    public DosAttack(InetAddress address, int port, AttackMethod attackMethod) {
        this(address, port, attackMethod, DEFAULT_POWER);
    }
    
    public DosAttack(InetAddress address, int port, AttackMethod attackMethod, long duration) {
        this(address, port, attackMethod, duration, DEFAULT_POWER);
    }
    
    public DosAttack(InetAddress address, int port, AttackMethod attackMethod, int power) {
        this(address, port, attackMethod, DEFAULT_DURATION, power);
    }
    
    public DosAttack(InetAddress address, int port, AttackMethod attackMethod, long duration, int power) {
        this.address = address;
        this.attackMethod = attackMethod;
        this.port = port;
        this.duration = duration;
        this.power = power;
    }
    
    private final InetAddress address;
    private final AttackMethod attackMethod;
    private final int port;
    private final long duration;
    private final int power;
    
    public void execute() {
        new Thread(this::executeThreadBlocking).start();
    }
    
    public void executeThreadBlocking() {
        attackMethod.accept(this);
    }
    
    public InetAddress getAddress() {
        return address;
    }
    
    public AttackMethod getAttackMethod() {
        return attackMethod;
    }
    
    public int getPort() {
        return port;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public int getPower() {
        return power;
    }
}
