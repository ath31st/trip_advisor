package home.sweethome.fussdb.entity;


import lombok.Data;

import java.util.List;

@Data
public class Trip {
    private User user;
    private int duration;
    private String startRoutePoint;
    private String endRoutePoint;
    private List<Weather> weather;
}
