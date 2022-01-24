package es.runfic.ws.races.model.runningrace;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.runfic.ws.races.model.runningrace.RunningRace;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlRunningRaceDao {

    public RunningRace create(Connection connection, RunningRace runningRace);

    public RunningRace find(Connection connection, Long id)
        throws InstanceNotFoundException;

    public void update(Connection connection, RunningRace runningRace)
        throws InstanceNotFoundException;

    public void remove(Connection connection, Long id)
        throws InstanceNotFoundException;

    public List <RunningRace> find(Connection connection, LocalDateTime dateRace, String city);



}
