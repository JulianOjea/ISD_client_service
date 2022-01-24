package es.runfic.ws.races.model.runningrace;

/* Hemos de hacer que vea en el Configuration Parameters el tipo de Dao a usar */

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlRunningRaceDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlRunningRaceDaoFactory.className";
    private static SqlRunningRaceDao dao = null;

    private SqlRunningRaceDaoFactory() {}

    public static SqlRunningRaceDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlRunningRaceDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlRunningRaceDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }


}
