package com.mycompany.idb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

public class TeamStatsUpdate {

    private final CqlSession session;

    public TeamStatsUpdate(CqlSession session) {
        this.session = session;
    }
    
    public void updateTeamStats(String statsType, float bucks, float mavericks, float lakers, float celtics,
                                float nets, float warriors, float grizzlies, float heat) {

        // Convert statsType to uppercase
        statsType = statsType.toUpperCase();

        // Determine the table based on the first letter of statsType
        char firstChar = statsType.charAt(0);
        String tableName;

        if (firstChar >= 'A' && firstChar <= 'M') {
            tableName = "team_data.team_stats_a_m";
        } else if (firstChar >= 'N' && firstChar <= 'Z') {
            tableName = "team_data.team_stats_n_z";
        } else {
            throw new IllegalArgumentException("Invalid statsType: " + statsType);
        }

        // Build the update query
        String query = "UPDATE " + tableName + " SET " +
                "bucks = ?, mavericks = ?, lakers = ?, celtics = ?, nets = ?, " +
                "warriors = ?, grizzlies = ?, heat = ? " +
                "WHERE stats_type = ?";

        // Execute the update statement
        session.execute(SimpleStatement.newInstance(query, bucks, mavericks, lakers, celtics,
                nets, warriors, grizzlies, heat, statsType));
        System.out.println("Data updated successfully in " + tableName);
    }
}
