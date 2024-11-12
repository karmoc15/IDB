package com.mycompany.idb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ColumnDefinitions;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;

public class TeamStatsSelect {

    private final CqlSession session;

    public TeamStatsSelect(CqlSession session) {
        this.session = session;
    }
    
    private String determineTable(String statsType) {
        // Convert statsType to uppercase to ignore case
        char firstChar = Character.toUpperCase(statsType.charAt(0));
        return (firstChar >= 'A' && firstChar <= 'M') ? "team_data.team_stats_a_m" : "team_data.team_stats_n_z";
    }
    
    public Row getTeamStats(String statsType) {
        // Convert statsType to uppercase to match stored uppercase values
        String upperCaseStatsType = statsType.toUpperCase();
        String tableName = determineTable(upperCaseStatsType);
        String query = String.format("SELECT * FROM %s WHERE stats_type = ?", tableName);

        ResultSet resultSet = session.execute(session.prepare(query).bind(upperCaseStatsType));
        return resultSet.one();
    }

    public void printTeamStats(Row row) {
        if (row != null) {
            ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
            
            for (int i = 0; i < columnDefinitions.size(); i++) {
                String columnName = columnDefinitions.get(i).getName().asInternal(); // Get column name
                DataType columnType = columnDefinitions.get(i).getType(); // Get column data type
                
                // Retrieve the column value based on its type
                Object columnValue;
                if (columnType.equals(DataTypes.FLOAT)) {
                    columnValue = row.getFloat(columnName);
                } else if (columnType.equals(DataTypes.TEXT)) {
                    columnValue = row.getString(columnName);
                } else {
                    columnValue = row.getObject(columnName); // Fallback for other types
                }
                
                System.out.println(columnName + ": " + columnValue);
            }
        } else {
            System.out.println("No data found for the specified stats type.");
        }
    }
}
