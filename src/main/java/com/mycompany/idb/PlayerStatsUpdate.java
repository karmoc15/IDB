package com.mycompany.idb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

public class PlayerStatsUpdate {

    private final CqlSession session;

    public PlayerStatsUpdate(CqlSession session) {
        this.session = session;
    }
    
    public void updatePlayerStats(String statsType, float sabonis, float valanciunas, float jokic,
                                  float doncic, float porzingis, float james, float durant, float curry) {

        // Convert statsType to uppercase
        statsType = statsType.toUpperCase();

        // Determine the table based on the first letter of statsType
        char firstChar = statsType.charAt(0);
        String tableName;

        if (firstChar >= 'A' && firstChar <= 'M') {
            tableName = "player_data.player_stats_a_m";
        } else if (firstChar >= 'N' && firstChar <= 'Z') {
            tableName = "player_data.player_stats_n_z";
        } else {
            throw new IllegalArgumentException("Invalid statsType: " + statsType);
        }

        // Build the update query
        String query = "UPDATE " + tableName + " SET " +
                "sabonis = ?, valanciunas = ?, jokic = ?, doncic = ?, porzingis = ?, " +
                "james = ?, durant = ?, curry = ? " +
                "WHERE stats_type = ?";

        // Execute the update statement
        session.execute(SimpleStatement.newInstance(query, sabonis, valanciunas, jokic,
                doncic, porzingis, james, durant, curry, statsType));
        System.out.println("Data updated successfully in " + tableName);
    }
}