package es.runfic.ws.races.model.runningraceservice;

import es.runfic.ws.races.model.registration.Registration;
import es.runfic.ws.races.model.registration.SqlRegistrationDaoFactory;
import es.runfic.ws.races.model.runningrace.RunningRace;
import es.runfic.ws.races.model.runningrace.SqlRunningRaceDao;
import es.runfic.ws.races.model.registration.SqlRegistrationDao;
import es.runfic.ws.races.model.runningrace.SqlRunningRaceDaoFactory;
import es.runfic.ws.races.model.runningraceservice.exceptions.DorsalAlreadyPickedUpException;
import es.runfic.ws.races.model.runningraceservice.exceptions.MaxParticipantsReachedException;
import es.runfic.ws.races.model.runningraceservice.exceptions.PickUpDorsalDataMissMatchException;
import es.runfic.ws.races.model.runningraceservice.exceptions.RegistrationClosedException;
import es.runfic.ws.races.model.util.Constants;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.SQLException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

public class RunningRaceServiceImpl implements RunningRaceService{

    private final DataSource dataSource;
    private SqlRunningRaceDao raceDao = null;
    private SqlRegistrationDao registrationDao = null;

    public RunningRaceServiceImpl(){
        dataSource = DataSourceLocator.getDataSource(Constants.RACES_DATA_SOURCE);
        raceDao = SqlRunningRaceDaoFactory.getDao();
        registrationDao = SqlRegistrationDaoFactory.getDao();
    }


    @Override
    public RunningRace addRunningRace(RunningRace runningRace) throws InputValidationException {

        LocalDateTime now = LocalDateTime.now();
        now = now.withNano(0);
        if(runningRace.getRaceDate().isBefore(now) || runningRace.getRaceDate().isEqual(now))
            throw new InputValidationException("Invalid raceDate value (it must be " +
                    "at least one day after today)");

        if(runningRace.getTotalRegistrations() > 0)
            throw new InputValidationException("Invalid totalRegistrations value" +
                    "(there can´t be any participants before the race has been added)");

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                RunningRace createRunningRace = raceDao.create(connection, runningRace);

                connection.commit();

                return createRunningRace;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    public RunningRace findRunningRace(Long runningRaceId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return raceDao.find(connection, runningRaceId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RunningRace> findRunningRaces(LocalDateTime futureDate, String city) {
        return null;
    }

    @Override
    public Registration addRegistration(Long runningRaceId, String userMail, String creditCard)
            throws RegistrationClosedException, InputValidationException,
            MaxParticipantsReachedException, InstanceNotFoundException {

        PropertyValidator.validateCreditCard(creditCard);
        PropertyValidator.validateMandatoryString("userMail", userMail);

        Registration registration = null;

        try(Connection connection = this.dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                RunningRace runningRace = this.raceDao.find(connection, runningRaceId);

                if (runningRace.getTotalRegistrations() == runningRace.getMaxParticipants()) {
                    throw new MaxParticipantsReachedException("La carrera ya tiene inscritos el nº máximo de " +
                            "participantes");
                }
                runningRace.setTotalRegistrations(runningRace.getTotalRegistrations()+1);

                this.raceDao.update(connection, runningRace);

                validateRegistrationInTime(runningRace.getRaceDate());
                registration = new Registration();
                registration.setRunningRaceId(runningRaceId);
                registration.setUserMail(userMail);
                registration.setCreditCard(creditCard);
                registration.setDorsal(runningRace.getTotalRegistrations());
                registration = this.registrationDao.create(connection, registration);

                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return registration;
    }

    @Override
    public List<Registration> findRegistrationsByUserMail(String userMail) {
        return null;
    }

    @Override
    public Registration pickUpDorsal(Long runningRaceId, Long dorsalPickUpCode, String creditCard) throws InstanceNotFoundException, InputValidationException, PickUpDorsalDataMissMatchException, DorsalAlreadyPickedUpException {
        return null;
    }

    private void validateRegistrationInTime(LocalDateTime raceDate) throws RegistrationClosedException{
        if (raceDate.isBefore(LocalDateTime.now().plusDays(1))) {
            throw new RegistrationClosedException("No se puede realizar la inscripción debido a que quedan menos de 24 " +
                    "horas para la carrera");
        }
    }
}
