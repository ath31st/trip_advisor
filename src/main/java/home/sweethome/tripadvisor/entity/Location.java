package home.sweethome.tripadvisor.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private float latitude;
    private float longitude;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Weather> weather;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Location location = (Location) o;
        return id != null && Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "location: " + name + System.lineSeparator();
    }

}