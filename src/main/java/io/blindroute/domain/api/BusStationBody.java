package io.blindroute.domain.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusStationBody {
//    private comMsgHeader comMsgHeader;
//    private msgHeader msgHeader;
    private BusStationMsgBody msgBody;
}
