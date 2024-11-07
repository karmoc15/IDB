/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.idb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DriverException;
import java.net.InetSocketAddress;

public class CassandraConnector {

    private CqlSession session;

    public void connect(String node, int port, String dataCenter) {
        try {
            session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(node, port))
                    .withLocalDatacenter(dataCenter)
                    .build();
            System.out.println("Connected to Cassandra!");
        } catch (DriverException e) {
            System.err.println("Unable to connect to Cassandra: " + e.getMessage());
        }
    }

    public CqlSession getSession() {
        return session;
    }

    public void close() {
        if (session != null) {
            session.close();
            System.out.println("Session closed.");
        }
    }
}

