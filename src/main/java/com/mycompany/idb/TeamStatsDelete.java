package com.mycompany.idb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatement;

public class TeamStatsDelete {

    private final CqlSession session;
    private final PreparedStatement deleteFromAM;
    private final PreparedStatement deleteFromNZ;

    // Constructor initializes the session and prepares delete statements for both tables
    public TeamStatsDelete(CqlSession session) {
        this.session = session;

        // Prepare delete statements for each table
        String deleteFromAMQuery = "DELETE FROM team_data.team_stats_a_m WHERE stats_type = ?";
        String deleteFromNZQuery = "DELETE FROM team_data.team_stats_n_z WHERE stats_type = ?";

        this.deleteFromAM = session.prepare(deleteFromAMQuery);
        this.deleteFromNZ = session.prepare(deleteFromNZQuery);
    }

    public void deleteByStatsType(String statsType) {
        // Convert stats_type to uppercase
        String upperStatsType = statsType.toUpperCase();

        // Determine the correct table based on the first letter of stats_type
        PreparedStatement deleteStatement;
        if (upperStatsType.charAt(0) >= 'A' && upperStatsType.charAt(0) <= 'M') {
            deleteStatement = deleteFromAM;
            System.out.println("Deleting from team_stats_a_m table");
        } else if (upperStatsType.charAt(0) >= 'N' && upperStatsType.charAt(0) <= 'Z'){
            deleteStatement = deleteFromNZ;
            System.out.println("Deleting from team_stats_n_z table");
        } else {
            throw new IllegalArgumentException("Invalid statsType: " + statsType);
        }

        // Bind the stats_type to the delete statement and execute it
        BoundStatement boundStatement = deleteStatement.bind(upperStatsType);
        session.execute(boundStatement);
        System.out.println("Deleted row with stats_type: " + upperStatsType);
    }
}
