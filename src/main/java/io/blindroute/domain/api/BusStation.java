package io.blindroute.domain.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusStation {
    private String stId;
    private String stNm;
//    private Double tmX;
//    private Double tmY;
//    private Double posX;
//    private Double posY;
    private String arsId;

}
