package org.rce4j.module;

import org.rce4j.exception.HttpException;
import org.rce4j.module.event.BackdoorEvent;

import java.util.*;

public abstract class ModuleSuite<T extends BackdoorModule<E>, E extends BackdoorEvent> implements Iterable<T> { // generic arguments: the module, then the event type
    public ModuleSuite() {
        this.modules = Map.of();
    }
    
    @SafeVarargs
    public ModuleSuite(T... modules) {
        Map<ModulePath, T> $fieldModules = new HashMap<>();
        
        for (T module : modules) {
            if ($fieldModules.put(module.getPath(), module) != null) {
                throw new IllegalStateException("Module with path " + module.getPath() + " already exists");
            }
            
            for (Map.Entry<ModulePath, T> moduleEntry : $fieldModules.entrySet()) {
                T entryModule = moduleEntry.getValue();
                if (entryModule.getName().equals(module.getName()) && module != entryModule) {
                    throw new IllegalArgumentException("Duplicate module names: " + module.getName());
                }
            }
        }
        
        this.modules = Collections.unmodifiableMap($fieldModules);
    }
    
    public ModuleSuite(Collection<? extends T> modules) {
        Map<ModulePath, T> $fieldModules = new HashMap<>();
        
        for (T module : modules) {
            if ($fieldModules.put(module.getPath(), module) != null) {
                throw new IllegalStateException("Module with path " + module.getPath() + " already exists");
            }
            
            for (Map.Entry<ModulePath, T> moduleEntry : $fieldModules.entrySet()) {
                T entryModule = moduleEntry.getValue();
                if (entryModule.getName().equals(module.getName()) && module != entryModule) {
                    throw new IllegalArgumentException("Duplicate module names: " + module.getName());
                }
            }
        }
        
        this.modules = Collections.unmodifiableMap($fieldModules);
    }
    
    private final Map<ModulePath, T> modules;
    
    public Collection<ModulePath> getPaths() {
        return modules.keySet();
    }
    
    void fire(E event) throws HttpException {
        for (Map.Entry<ModulePath, T> module : modules.entrySet()) {
            if (module.getKey().equals(event.getPath())) {
                module.getValue().on(event);
                return;
            }
        }
        
        throw new HttpException(404);
    }
    
    public int size() {
        return modules.size();
    }
    
    @Override
    public Iterator<T> iterator() {
        return modules.values().iterator();
    }
}
