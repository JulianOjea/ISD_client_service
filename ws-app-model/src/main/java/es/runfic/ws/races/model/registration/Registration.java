package es.runfic.ws.races.model.registration;

import java.time.LocalDateTime;
import java.util.Objects;

public class Registration {
    private Long id;
    private Long runningRaceId;
    private String userMail;
    private String creditCard;
    private int dorsal;
    private Boolean dorsalPickedUp;
    private LocalDateTime registrationDate;

    public Registration() {
        this.registrationDate = LocalDateTime.now().withNano(0);
        this.dorsalPickedUp = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRunningRaceId() {
        return runningRaceId;
    }

    public void setRunningRaceId(Long runningRaceId) {
        this.runningRaceId = runningRaceId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }


    public Boolean getDorsalPickedUp() {
        return dorsalPickedUp;
    }

    public void setDorsalPickedUp(Boolean dorsalPickedUp) {
        this.dorsalPickedUp = dorsalPickedUp;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(runningRaceId, that.runningRaceId) &&
                Objects.equals(userMail, that.userMail) &&
                Objects.equals(creditCard, that.creditCard) &&
                Objects.equals(dorsal, that.dorsal) &&
                Objects.equals(dorsalPickedUp, that.dorsalPickedUp) &&
                Objects.equals(registrationDate, that.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, runningRaceId, userMail, creditCard, dorsal, dorsalPickedUp, registrationDate);
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", runningRaceId=" + runningRaceId +
                ", userMail='" + userMail + '\'' +
                ", creditCard='" + creditCard + '\'' +
                ", dorsal=" + dorsal +
                ", dorsalPickedUp=" + dorsalPickedUp +
                ", registrationDate=" + registrationDate +
                '}';
    }
}



