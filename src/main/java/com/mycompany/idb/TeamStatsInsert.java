package com.mycompany.idb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

public class TeamStatsInsert {

    private final CqlSession session;
    private final PreparedStatement insertIntoTeamAM;
    private final PreparedStatement insertIntoTeamNZ;

    public TeamStatsInsert(CqlSession session) {
        this.session = session;

        // Prepare statements for both tables
        this.insertIntoTeamAM = session.prepare(
            "INSERT INTO team_data.team_stats_a_m (stats_type, bucks, mavericks, lakers, celtics, nets, warriors, grizzlies, heat) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
        );

        this.insertIntoTeamNZ = session.prepare(
            "INSERT INTO team_data.team_stats_n_z (stats_type, bucks, mavericks, lakers, celtics, nets, warriors, grizzlies, heat) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
        );
    }

    public void insertTeamStats(String statsType, float bucks, float mavericks, float lakers, float celtics,
                                float nets, float warriors, float grizzlies, float heat) {

        // Convert statsType to uppercase for consistent storage
        String normalizedStatsType = statsType.toUpperCase();
        
        // Validate the first character of statsType
        char firstChar = normalizedStatsType.charAt(0);
        if ((firstChar < 'A' || (firstChar > 'M' && firstChar < 'N') || firstChar > 'Z')) {
            System.out.println("Invalid stats_type: " + normalizedStatsType + ". Must start with a letter A-M or N-Z.");
            return; // Skip insertion for invalid stats_type
        }

        // Determine the table based on the first character of the uppercase statsType
        PreparedStatement prepared = (firstChar <= 'M') ? insertIntoTeamAM : insertIntoTeamNZ;

        // Bind the values and execute the statement
        session.execute(prepared.bind(normalizedStatsType, bucks, mavericks, lakers, celtics, nets, warriors, grizzlies, heat));
        System.out.println("Data inserted into " + ((prepared == insertIntoTeamAM) ? "team_stats_a_m" : "team_stats_n_z") 
                           + " with stats_type: " + normalizedStatsType);
    }
}
