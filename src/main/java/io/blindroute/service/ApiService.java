package io.blindroute.service;

import io.blindroute.domain.api.BusRoute;
import io.blindroute.domain.api.BusStation;

import java.util.List;

public interface ApiService {

    public List<BusStation> getBusStationsByString(String Search);

    public List<BusRoute> getBusRouteByString(String Search);

}
