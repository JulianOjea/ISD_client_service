package es.runfic.ws.races.test.model.runningraceservice;


import es.runfic.ws.races.model.registration.Registration;
import es.runfic.ws.races.model.registration.SqlRegistrationDao;
import es.runfic.ws.races.model.registration.SqlRegistrationDaoFactory;
import es.runfic.ws.races.model.runningrace.RunningRace;
import es.runfic.ws.races.model.runningrace.SqlRunningRaceDao;
import es.runfic.ws.races.model.runningrace.SqlRunningRaceDaoFactory;
import es.runfic.ws.races.model.runningraceservice.RunningRaceService;
import es.runfic.ws.races.model.runningraceservice.RunningRaceServiceFactory;
import es.runfic.ws.races.model.runningraceservice.exceptions.MaxParticipantsReachedException;
import es.runfic.ws.races.model.runningraceservice.exceptions.RegistrationClosedException;
import es.runfic.ws.races.model.util.Constants;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RunningRaceServiceTest {

    private static final String USER_MAIL = "usermail@test.es";
    private static final String CREDIT_CARD = "1234567898765432";

    private static RunningRaceService runningRaceService = null;
    private static SqlRegistrationDao registrationDao = null;
    private static SqlRunningRaceDao runningRaceDao = null;

    @BeforeAll
    public static void init() {
        DataSource dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(Constants.RACES_DATA_SOURCE, dataSource);

        runningRaceService = RunningRaceServiceFactory.getService();
        registrationDao = SqlRegistrationDaoFactory.getDao();
        runningRaceDao = SqlRunningRaceDaoFactory.getDao();
    }

    private RunningRace createRunningRace(RunningRace runningRace) {
        DataSource dataSource = DataSourceLocator.getDataSource(Constants.RACES_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            return runningRaceService.addRunningRace(runningRace);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeRunningRace(Long runningRaceId) {
        DataSource dataSource = DataSourceLocator.getDataSource(Constants.RACES_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                runningRaceDao.remove(connection, runningRaceId);

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            }
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeRegistration(Long registrationId) {
        DataSource dataSource = DataSourceLocator.getDataSource(Constants.RACES_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                registrationDao.remove(connection, registrationId);

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            }
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Registration findRegistration(Long registrationId) {
        DataSource dataSource = DataSourceLocator.getDataSource(Constants.RACES_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {
                Registration registration = registrationDao.find(connection, registrationId);
                return registration;
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private RunningRace getValidRunningRace() {
        return new RunningRace("Acapulco", "carrera muy larga", LocalDateTime.now().plusMonths(1), 10.0f, 20);
    }

    @Test
    public void testAddAndFindRunningRace()
            throws InputValidationException, InstanceNotFoundException {

        RunningRace runningRace = getValidRunningRace();
        RunningRace addedRunningRace = null;

        try {

            // Create RunningRace
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedRunningRace = runningRaceService.addRunningRace(runningRace);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            // Find RunningRace
            RunningRace foundRunningRace = runningRaceService.findRunningRace(addedRunningRace.getId());

            assertEquals(addedRunningRace, foundRunningRace);
            assertEquals(addedRunningRace.getId(), foundRunningRace.getId());
            assertEquals(addedRunningRace.getCity(), foundRunningRace.getCity());
            assertEquals(addedRunningRace.getDescription(), foundRunningRace.getDescription());
            assertEquals(addedRunningRace.getRaceDate(), foundRunningRace.getRaceDate());
            assertEquals(addedRunningRace.getPrice(), foundRunningRace.getPrice());
            assertEquals(addedRunningRace.getMaxParticipants(), foundRunningRace.getMaxParticipants());
            assertEquals(addedRunningRace.getTotalRegistrations(), foundRunningRace.getTotalRegistrations());
            assertEquals(addedRunningRace.getTotalRegistrations(), 0);
            assertEquals(addedRunningRace.getCreationDate(), foundRunningRace.getCreationDate());

            assertTrue((foundRunningRace.getCreationDate().compareTo(beforeCreationDate) >= 0)
                    && (foundRunningRace.getCreationDate().compareTo(afterCreationDate) <= 0));

        } finally {
            //Clear database
            if (addedRunningRace!=null) {
                removeRunningRace(addedRunningRace.getId());
            }
        }
    }


    @Test
    public void testRemoveRunningRace() {

        RunningRace runningRace = getValidRunningRace();


        try {
            RunningRace createdRace = runningRaceService.addRunningRace(runningRace);

            removeRunningRace(createdRace.getId());

            assertThrows(InstanceNotFoundException.class, () -> runningRaceService.findRunningRace(runningRace.getId()));

        } catch (InputValidationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAddRegistrationAndIncrementParticipants()
            throws InputValidationException, InstanceNotFoundException, RegistrationClosedException, MaxParticipantsReachedException {

        RunningRace runningRace = new RunningRace("A Coruña", "Carrera de 10 km",
                LocalDateTime.now().plusMonths(1),20.0f, 1500);
        runningRace = createRunningRace(runningRace);
        assertEquals(runningRace.getTotalRegistrations(), 0);
        Registration registration = null;
        try {

            registration = runningRaceService.addRegistration(runningRace.getId(), USER_MAIL, CREDIT_CARD);
            Registration registrationToCompare = findRegistration(registration.getId());
            assertEquals(registration, registrationToCompare);
            RunningRace runningRaceFound = runningRaceService.findRunningRace(runningRace.getId());
            assertEquals(runningRaceFound.getTotalRegistrations(), 1);
        }finally {
            if (null != registration) {
                removeRegistration(registration.getId());
            }
            removeRunningRace(runningRace.getId());
        }
    }

    @Test
    public void testMaxRegistrationsReached() throws MaxParticipantsReachedException{
        assertThrows(MaxParticipantsReachedException.class,() -> {
            RunningRace runningRace = new RunningRace("A Coruña", "Carrera de 10 km",
                    LocalDateTime.now().plusMonths(1), 20.0f, 1);
            runningRace = createRunningRace(runningRace);
            assertEquals(runningRace.getTotalRegistrations(), 0);
            Registration registration = runningRaceService.addRegistration(runningRace.getId(), USER_MAIL,
                    CREDIT_CARD);
            RunningRace runningRaceFound = runningRaceService.findRunningRace(runningRace.getId());
            assertEquals(runningRaceFound.getTotalRegistrations(), 1);
            Registration registration2 = runningRaceService.addRegistration(runningRace.getId(), USER_MAIL,
                    CREDIT_CARD);
            removeRegistration(registration.getId());
            removeRegistration(registration2.getId());
            removeRunningRace(runningRace.getId());
        });
    }

    @Test
    public void testRegistrationClosed() throws RegistrationClosedException {
        assertThrows(RegistrationClosedException.class,() -> {
            RunningRace runningRace = new RunningRace("A Coruña", "Carrera de 10 km",
                    LocalDateTime.now().plusHours(23), 20.0f, 1500);
            runningRace = createRunningRace(runningRace);
            assertEquals(runningRace.getTotalRegistrations(), 0);
            Registration registration = runningRaceService.addRegistration(runningRace.getId(), USER_MAIL,
                    CREDIT_CARD);
            removeRegistration(registration.getId());
            removeRunningRace(runningRace.getId());
        });
    }
}

