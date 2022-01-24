package es.runfic.ws.races.model.registration;


import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlRegistrationDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlRegistrationDaoFactory.className";
    private static SqlRegistrationDao dao = null;

    private SqlRegistrationDaoFactory() {}

    public static SqlRegistrationDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlRegistrationDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlRegistrationDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }


}
