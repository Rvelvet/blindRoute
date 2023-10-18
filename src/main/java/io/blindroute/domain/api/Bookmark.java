package io.blindroute.domain.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    private String arsId;
    private String stNm;
    private String busRouteId;
    private String busRouteNm;
    private String busRouteAbrv;
}
