package es.runfic.ws.races.model.registration;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.runfic.ws.races.model.registration.Registration;
import java.sql.Connection;
import java.util.List;

public interface SqlRegistrationDao {

    public Registration create(Connection connection, Registration registration);

    public Registration find(Connection connection, Long id)
        throws InstanceNotFoundException;

    public List <Registration> findByUser(Connection connection, String userMail);

    public void update(Connection connection, Registration registration)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long id)
        throws InstanceNotFoundException;
}
