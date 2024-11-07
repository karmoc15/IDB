package com.mycompany.idb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

public class PlayerStatsInsert {

    private final CqlSession session;
    private final PreparedStatement insertIntoStats1;
    private final PreparedStatement insertIntoStats2;

    public PlayerStatsInsert(CqlSession session) {
        this.session = session;

        // Prepare statements for both tables
        this.insertIntoStats1 = session.prepare(
            "INSERT INTO player_data.player_stats_a_m (stats_type, sabonis, valanciunas, jokic, doncic, porzingis, james, durant, curry) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
        );

        this.insertIntoStats2 = session.prepare(
            "INSERT INTO player_data.player_stats_n_z (stats_type, sabonis, valanciunas, jokic, doncic, porzingis, james, durant, curry) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
        );
    }

    public void insertPlayerStats(String statsType, float sabonis, float valanciunas, float jokic,
                                  float doncic, float porzingis, float james, float durant, float curry) {

        // Convert statsType to uppercase for consistent storage
        String normalizedStatsType = statsType.toUpperCase();
        
        // Validate the first character of statsType
        char firstChar = normalizedStatsType.charAt(0);
        if ((firstChar < 'A' || (firstChar > 'M' && firstChar < 'N') || firstChar > 'Z')) {
            System.out.println("Invalid stats_type: " + normalizedStatsType + ". Must start with a letter A-M or N-Z.");
            return; // Skip insertion for invalid stats_type
        }

        // Determine the table based on the first character of the uppercase statsType
        PreparedStatement prepared = (firstChar <= 'M') ? insertIntoStats1 : insertIntoStats2;

        // Bind the values and execute the statement
        session.execute(prepared.bind(normalizedStatsType, sabonis, valanciunas, jokic, doncic, porzingis, james, durant, curry));
        System.out.println("Data inserted into " + ((prepared == insertIntoStats1) ? "player_stats_a_m" : "player_stats_n_z") 
                           + " with stats_type: " + normalizedStatsType);
    }
}