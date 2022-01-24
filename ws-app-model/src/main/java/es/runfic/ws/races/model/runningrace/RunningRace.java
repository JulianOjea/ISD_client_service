package es.runfic.ws.races.model.runningrace;

import java.time.LocalDateTime;
import java.util.Objects;

public class RunningRace {
    private Long id;
    private String city;
    private String description;
    private LocalDateTime raceDate;
    private float price;
    private int maxParticipants;
    private int totalRegistrations;
    private LocalDateTime creationDate;

    /* He asumido que, al crearse la carrera, no hay nunca nadie registrado */


    public RunningRace(String city, String description, LocalDateTime raceDate,
                       float price, int maxParticipants) {
        this.city = city;
        this.description = description;
        this.raceDate = raceDate.withNano(0);
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.totalRegistrations = 0;
        this.creationDate = LocalDateTime.now().withNano(0);
    }

    public RunningRace(Long id, String city, String description, LocalDateTime raceDate,
                 float price, int maxParticipants) {
        this(city, description, raceDate, price, maxParticipants);
        this.id = id;
        this.raceDate = raceDate.withNano(0);
        this.totalRegistrations = 0;
        this.creationDate = LocalDateTime.now().withNano(0);
    }

    public RunningRace(Long id, String city, String description, LocalDateTime raceDate,
                       float price, int maxParticipants, int totalRegistrations, LocalDateTime creationDate) {
        this(id, city, description, raceDate, price, maxParticipants);
        this.raceDate = raceDate.withNano(0);
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
        this.totalRegistrations = totalRegistrations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(LocalDateTime raceDate) {
        this.raceDate = raceDate;
    }

    public int getTotalRegistrations() {
        return totalRegistrations;
    }

    public void setTotalRegistrations(int totalRegistrations) {
        this.totalRegistrations = totalRegistrations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunningRace that = (RunningRace) o;
        return Float.compare(that.price, price) == 0 &&
                maxParticipants == that.maxParticipants &&
                totalRegistrations == that.totalRegistrations &&
                Objects.equals(id, that.id) &&
                Objects.equals(city, that.city) &&
                Objects.equals(description, that.description) &&
                Objects.equals(raceDate, that.raceDate) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, description, raceDate, price, maxParticipants, totalRegistrations, creationDate);
    }

    @Override
    public String toString() {
        return "RunningRace{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", raceDate=" + raceDate +
                ", price=" + price +
                ", maxParticipants=" + maxParticipants +
                ", totalRegistrations=" + totalRegistrations +
                ", creationDate=" + creationDate +
                '}';
    }
}
