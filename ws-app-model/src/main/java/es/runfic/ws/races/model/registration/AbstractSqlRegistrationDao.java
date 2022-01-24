package es.runfic.ws.races.model.registration;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractSqlRegistrationDao implements SqlRegistrationDao {

    @Override
    public Registration find(Connection connection, Long id) throws InstanceNotFoundException {
        String queryString = "SELECT id, runningRaceId, userMail, creditCard, dorsal, dorsalPickedUp, " +
                "registrationDate" +
                " FROM Registration where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString)) {

            preparedStatement.setLong(1, id);


            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(id, Registration.class.getName());
            }

            Registration registration  = new Registration();
            registration.setId(resultSet.getLong(1));
            registration.setRunningRaceId(resultSet.getLong(2));
            registration.setUserMail(resultSet.getString(3));
            registration.setCreditCard(resultSet.getString(4));
            registration.setDorsal(resultSet.getInt(5));
            registration.setDorsalPickedUp(resultSet.getBoolean(6));
            registration.setRegistrationDate(resultSet.getTimestamp(7).toLocalDateTime());


            return registration;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Registration> findByUser(Connection connection, String userMail) {
        String queryString = "SELECT id, runningRaceId, creditCard, dorsal, dorsalPickedUp, registrationDate" +
                " FROM Registration where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString)) {

            preparedStatement.setString(0, userMail);


            ResultSet resultSet = preparedStatement.executeQuery();

            List<Registration> registrations = new ArrayList<>();

            while (resultSet.next()) {
                Registration registration = new Registration();

                registration.setId(resultSet.getLong(1));
                registration.setRunningRaceId(resultSet.getLong(2));
                registration.setUserMail(resultSet.getString(3));
                registration.setCreditCard(resultSet.getString(4));
                registration.setDorsal(resultSet.getInt(5));
                registration.setDorsalPickedUp(resultSet.getBoolean(6));

                registrations.add(registration);
            }

            return registrations;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Registration registration) throws InstanceNotFoundException {
        String queryString = "UPDATE Registration set userMail = ? and creditCard = ? and " +
                "dorsal = ? and dorsalPickedUp = ?  where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString)) {
            preparedStatement.setString(1, registration.getUserMail());
            preparedStatement.setString(2, registration.getCreditCard());
            preparedStatement.setLong(3, registration.getDorsal());
            preparedStatement.setBoolean(4, registration.getDorsalPickedUp());
            preparedStatement.setLong(5, registration.getId());

            int updatedFiles = preparedStatement.executeUpdate();
            if (updatedFiles == 0) {
                throw new InstanceNotFoundException(registration.getId(), Registration.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long id) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Registration where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString)) {
            preparedStatement.setLong(1, id);

            int deletedFiles = preparedStatement.executeUpdate();

            if (deletedFiles == 0) {
                throw new InstanceNotFoundException(id, Registration.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}