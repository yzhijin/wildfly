/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.server.manager;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jboss.as.model.JvmElement;
import org.jboss.as.model.PropertiesElement;
import org.jboss.as.model.Standalone;
import org.jboss.as.process.ProcessManagerSlave;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ServerMaker {
    
    private final String workingDirectory;
    private final ProcessManagerSlave processManagerSlave;
    private final MessageHandler messageHandler;
    
    public ServerMaker(String workingDirectory, 
            ProcessManagerSlave processManagerSlave, MessageHandler messageHandler) {
        if (workingDirectory == null) {
            throw new IllegalArgumentException("workingDirectory is null");
        }
        this.workingDirectory = workingDirectory;
        if (processManagerSlave == null) {
            throw new IllegalArgumentException("processManagerSlave is null");
        }
        this.processManagerSlave = processManagerSlave;
        if (messageHandler == null) {
            throw new IllegalArgumentException("messageHandler is null");
        }
        this.messageHandler = messageHandler;
    }
    
    public Server makeServer(Standalone serverConfig) throws IOException {
//        final List<String> args = new ArrayList<String>();
//        if (false) {
//            // Example: run at high priority on *NIX
//            args.add("/usr/bin/nice");
//            args.add("-n");
//            args.add("-10");
//        }
//        if (false) {
//            // Example: run only on processors 1-4 on Linux
//            args.add("/usr/bin/taskset");
//            args.add("0x0000000F");
//        }
//        args.add("/home/david/local/jdk/home/bin/java");
//        args.add("-Djava.util.logging.manager=org.jboss.logmanager.LogManager");
//        args.add("-jar");
//        args.add("jboss-modules.jar");
//        args.add("-mp");
//        args.add("modules");
//        args.add("org.jboss.as:server");
//        ProcessBuilder builder = new ProcessBuilder(args);
//        builder.redirectErrorStream(false);
//        final Process process = builder.start();
//
//        // Read errors from here, pass to logger
//        final InputStream errorStream = process.getErrorStream();
//        // Read commands and responses from here
//        final InputStream inputStream = process.getInputStream();
//        // Write commands and responses to here
//        final OutputStream outputStream = process.getOutputStream();
        
        String serverName = serverConfig.getServerName();
        List<String> command = getServerLaunchCommand(serverConfig.getJvm());
        Map<String, String> env = getServerLaunchEnvironment(serverConfig.getJvm());
        processManagerSlave.addProcess(serverName, command, env, workingDirectory);
        
        ServerCommunicationHandler commHandler = new ProcessManagerServerCommunicationHandler(serverName, processManagerSlave);
        Server server = new Server(commHandler);
//        messageHandler.registerServer(serverConfig.getServerName(), server);
        return server;
    }

    private List<String> getServerLaunchCommand(JvmElement jvm) {
        // FIXME implement getServerLaunchCommand
        throw new UnsupportedOperationException("implement me");
    }

    private Map<String, String> getServerLaunchEnvironment(JvmElement jvm) {
        Map<String, String> env = null;
        PropertiesElement pe = jvm.getEnvironmentVariables();
        if (pe != null) {
            env = pe.getProperties();
        }
        else {
            env = Collections.emptyMap();
        }
        return env;
    }
    
}
