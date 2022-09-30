package home.sweethome.fussdb.entity;


import home.sweethome.fussdb.util.Location;
import lombok.Data;

import java.util.List;

@Data
public class Trip {
    private User user;
    private int duration;
    private Location startRoutePoint;
    private Location endRoutePoint;
    private List<Weather> weather;
}
