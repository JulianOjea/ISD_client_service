package es.runfic.ws.races.model.runningrace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlRunningRaceDao extends AbstractSqlRunningRaceDao {

    @Override
    public RunningRace create(Connection connection, RunningRace runningRace) {

        String queryString = "INSERT INTO RunningRace"
                + " (city, description, raceDate, price, maxParticipants, totalRegistrations, creationDate)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setString(i++, runningRace.getCity());
            preparedStatement.setString(i++, runningRace.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(runningRace.getRaceDate()));
            preparedStatement.setFloat(i++, runningRace.getPrice());
            preparedStatement.setInt(i++, runningRace.getMaxParticipants());
            preparedStatement.setInt(i++, runningRace.getTotalRegistrations());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(runningRace.getCreationDate()));
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long id = resultSet.getLong(1);

            return new RunningRace(id, runningRace.getCity(), runningRace.getDescription(),
                    runningRace.getRaceDate(), runningRace.getPrice(),
                    runningRace.getMaxParticipants(), runningRace.getTotalRegistrations(), runningRace.getCreationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}