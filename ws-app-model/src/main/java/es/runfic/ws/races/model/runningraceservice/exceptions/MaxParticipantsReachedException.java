package es.runfic.ws.races.model.runningraceservice.exceptions;

public class MaxParticipantsReachedException extends Exception{
    public MaxParticipantsReachedException(String message) {
        super(message);
    }
}
