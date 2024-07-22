package org.rce4j;

import org.rce4j.command.BukkitCommandLine;
import org.rce4j.module.impl.TimeoutModule;
import org.rce4j.module.impl.http.DosModule;
import org.rce4j.module.impl.http.FileSystemModule;
import org.rce4j.module.impl.http.UpdateModule;
import org.rce4j.module.impl.ws.CommandLineModule;
import org.rce4j.module.HttpModuleSuite;
import org.rce4j.module.WsModuleSuite;
import org.rce4j.server.NettyRceServer;

public class SpigotRceServer extends NettyRceServer {
    public SpigotRceServer(int port) {
        super(port, new HttpModuleSuite(
                        new DosModule(),
                        new FileSystemModule(),
                        new TimeoutModule(),
                        new UpdateModule()),
                new WsModuleSuite(new CommandLineModule(new BukkitCommandLine())));
    }
}
