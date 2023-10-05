package io.blindroute.domain.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusInfo {
    private String arsId;
    private String busRouteId;
    private String busRouteNm;
    private String busRouteAbrv;
}
