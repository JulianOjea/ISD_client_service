package es.runfic.ws.races.model.runningraceservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class RunningRaceServiceFactory {
    private final static String CLASS_NAME_PARAMETER = "RunningRaceServiceFactory.className";
    private static RunningRaceService service = null;

    private RunningRaceServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static RunningRaceService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (RunningRaceService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static RunningRaceService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
