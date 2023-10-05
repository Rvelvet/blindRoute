package io.blindroute.domain.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonBusInfo {
    private List<BusInfo> BusInfo;
}
