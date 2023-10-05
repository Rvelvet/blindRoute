package io.blindroute.service;

import io.blindroute.domain.api.BusInfo;
import io.blindroute.domain.api.BusRoute;
import io.blindroute.domain.api.BusStation;
import io.blindroute.domain.api.Destination;

import java.util.List;

public interface ApiService {

    public List<BusStation> getBusStationsByString(String Search);

    public List<BusRoute> getBusRouteByString(String busRouteNm);

    public List<Destination> getDestinationsByString(String busRouteId);

    public void addBusInfo(String arsId, BusInfo busInfo);

    public void removeBusInfo(String arsId, BusInfo busInfo);

    public List<BusInfo> getList(String arsId);

}
