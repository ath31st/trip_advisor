package home.sweethome.tripadvisor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import home.sweethome.tripadvisor.util.CustomDataDeserializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonDeserialize(using = CustomDataDeserializer.class)
    private String date;
    @JsonProperty("weather")
    private String weather;
    private int maxTemp;
    private int minTemp;
    @JsonProperty("wind10m_max")
    private int powerWind;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Weather weather = (Weather) o;
        return id != null && Objects.equals(id, weather.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "weather on " + date + System.lineSeparator()
                + "max temperature: " + maxTemp + System.lineSeparator()
                + "min temperature: " + minTemp + System.lineSeparator()
                + "power wind: " + powerWind + System.lineSeparator();
    }

}
