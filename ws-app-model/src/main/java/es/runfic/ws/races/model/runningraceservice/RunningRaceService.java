package es.runfic.ws.races.model.runningraceservice;

import es.runfic.ws.races.model.registration.Registration;
import es.runfic.ws.races.model.runningrace.RunningRace;
import es.runfic.ws.races.model.runningraceservice.exceptions.DorsalAlreadyPickedUpException;
import es.runfic.ws.races.model.runningraceservice.exceptions.MaxParticipantsReachedException;
import es.runfic.ws.races.model.runningraceservice.exceptions.PickUpDorsalDataMissMatchException;
import es.runfic.ws.races.model.runningraceservice.exceptions.RegistrationClosedException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;


public interface RunningRaceService {
    public RunningRace addRunningRace(RunningRace runningRace) throws InputValidationException;

    public RunningRace findRunningRace(Long runningRaceId) throws InstanceNotFoundException;

    public List<RunningRace> findRunningRaces(LocalDateTime futureDate, String city);

    public Registration addRegistration(Long runningRaceId, String userMail, String creditCard)
            throws RegistrationClosedException, InputValidationException, InstanceNotFoundException, MaxParticipantsReachedException;

    public List<Registration> findRegistrationsByUserMail(String userMail);

    public Registration pickUpDorsal(Long runningRaceId, Long dorsalPickUpCode, String creditCard)
            throws InstanceNotFoundException, InputValidationException, PickUpDorsalDataMissMatchException,
            DorsalAlreadyPickedUpException;

}
