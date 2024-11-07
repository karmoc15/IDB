package com.mycompany.idb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;

class Idb {

    public static void main(String[] args) {
        CassandraConnector connector = new CassandraConnector();
        
        // Connect to Cassandra at localhost on default port 9042, with local datacenter "datacenter1"
        connector.connect("127.0.0.1", 9042, "datacenter1");
        
        CqlSession session = connector.getSession();
        
        PlayerStatsInsert inserter = new PlayerStatsInsert(session);
        PlayerStatsSelect selecter = new PlayerStatsSelect(session);
        PlayerStatsUpdate updater = new PlayerStatsUpdate(session);
        PlayerStatsDelete deleter = new PlayerStatsDelete(session);

        //Insert sample data with different cases for statsType
        //inserter.insertPlayerStats("a_stat", 10.5f, 12.3f, 23.2f, 15.6f, 8.5f, 30.0f, 25.3f, 32.1f);
        //inserter.insertPlayerStats("N_STAT", 11.5f, 13.3f, 24.2f, 16.6f, 9.5f, 31.0f, 26.3f, 33.1f);
        //inserter.insertPlayerStats("Z_stat", 9.5f, 10.0f, 20.0f, 14.5f, 7.5f, 28.0f, 22.3f, 30.0f);        

        //Write searching statsType. Fetch and print stats
        //String statsType = "a_stat";
        //Row row = selecter.getPlayerStats(statsType);
        //selecter.printPlayerStats(row);

        // Update player stats
        //updater.updatePlayerStats("n_stat", 10.5f, 12.0f, 15.2f, 18.7f, 20.1f, 25.3f, 27.8f, 30.5f);

        // Delete entry with a specific stats_type
        //deleter.deleteByStatsType("2_stat");

        // Close connection
        connector.close();
    }
}