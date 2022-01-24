package es.runfic.ws.races.model.registration;

import java.sql.*;

public class Jdbc3CcSqlRegistrationDao extends AbstractSqlRegistrationDao{

    @Override
    public Registration create(Connection connection, Registration registration) {
        String queryString = "INSERT INTO Registration"
                + " (runningRaceId, userMail, creditCard, dorsal, dorsalPickedUp, registrationDate)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, registration.getRunningRaceId());
            preparedStatement.setString(2, registration.getUserMail());
            preparedStatement.setString(3, registration.getCreditCard());
            preparedStatement.setLong(4, registration.getDorsal());
            preparedStatement.setBoolean(5, registration.getDorsalPickedUp());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(registration.getRegistrationDate()));
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long id = resultSet.getLong(1);

            registration.setId(id);

            return registration;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
