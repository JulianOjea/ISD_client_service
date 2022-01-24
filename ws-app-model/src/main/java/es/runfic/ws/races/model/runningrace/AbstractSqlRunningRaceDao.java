package es.runfic.ws.races.model.runningrace;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Statement;
import es.runfic.ws.races.model.runningrace.RunningRace;

public abstract class AbstractSqlRunningRaceDao implements SqlRunningRaceDao {

    @Override
    public RunningRace find(Connection connection, Long id)
        throws InstanceNotFoundException {

            String queryString = "SELECT city, "
                    + " description, raceDate, price, maxParticipants, totalRegistrations, creationDate " +
                    "FROM RunningRace WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

                int i = 1;
                preparedStatement.setLong(i++, id.longValue());

                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    throw new InstanceNotFoundException(id,
                            RunningRace.class.getName());
                }

                i = 1;
                String city = resultSet.getString(i++);
                String description = resultSet.getString(i++);
                Timestamp raceDateStamp = resultSet.getTimestamp(i++);
                LocalDateTime raceDate = raceDateStamp.toLocalDateTime();
                float price = resultSet.getFloat(i++);
                int maxParticipants = resultSet.getInt(i++);
                int totalRegistrations = resultSet.getInt(i++);
                Timestamp creationDateStamp = resultSet.getTimestamp(i++);
                LocalDateTime creationDate = creationDateStamp.toLocalDateTime();

                return new RunningRace(id, city, description, raceDate, price, maxParticipants, totalRegistrations,
                        creationDate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    @Override
    public void update(Connection connection, RunningRace runningRace) throws InstanceNotFoundException {

        String queryString = "UPDATE RunningRace"
                + " SET city = ?, description = ?, raceDate = ?, "
                + "price = ?, maxParticipants = ?, totalRegistrations = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, runningRace.getCity());
            preparedStatement.setString(i++, runningRace.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(runningRace.getRaceDate()));
            preparedStatement.setFloat(i++, runningRace.getPrice());
            preparedStatement.setInt(i++, runningRace.getMaxParticipants());
            preparedStatement.setInt(i++, runningRace.getTotalRegistrations());
            preparedStatement.setLong(i++, runningRace.getId());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(runningRace.getId(),
                        RunningRace.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void remove(Connection connection, Long id)
        throws InstanceNotFoundException {

        String queryString = "DELETE FROM RunningRace WHERE" + " id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, id);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(id,
                RunningRace.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public List <RunningRace> find(Connection connection, LocalDateTime raceDate, String city) {
        String queryString = "SELECT id, ";
        if (city == null)
            queryString += "city, ";

        queryString += " description, price, maxParticipants, totalRegistrations, creationDate " +
                "FROM RunningRace WHERE id = ? " + "AND dateRace >= ? ";

        if (city != null)
            queryString += "AND LOWER(city) = LOWER(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(raceDate));
            if(city != null)
                preparedStatement.setString(i++,city);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<RunningRace> races = new ArrayList<>();

            while (resultSet.next()) {
                i = 1;
                Long id = Long.valueOf(resultSet.getLong(i++));
                if(city == null)
                    city = resultSet.getString(i++);
                String description = resultSet.getString(i++);
                float price = resultSet.getFloat(i++);
                int maxParticipants = resultSet.getInt(i++);
                int totalRegistrations = resultSet.getInt(i++);
                Timestamp creationDateStamp = resultSet.getTimestamp(i++);
                LocalDateTime creationDate = creationDateStamp.toLocalDateTime();

                races.add(new RunningRace(id, city, description, raceDate, price, maxParticipants, totalRegistrations, creationDate));
            }

            return races;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
