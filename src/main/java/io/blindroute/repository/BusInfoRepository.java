package io.blindroute.repository;

import io.blindroute.domain.api.BusInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BusInfoRepository {
    private final static ConcurrentHashMap<String, List<BusInfo>> busInfo = new ConcurrentHashMap<>();


    public boolean add(String arsId, BusInfo bus) {
        if(busInfo.containsKey(arsId)){
            if(!busInfo.get(arsId).contains(bus)){
                busInfo.get(arsId).add(bus);
            }
        }else {
            busInfo.put(arsId, new ArrayList<>());
            busInfo.get(arsId).add(bus);
        }
        return true;
    }

    public boolean remove(String arsId, BusInfo bus) {
        if (busInfo.containsKey(arsId)) {
            busInfo.get(arsId).remove(bus);
        }
        return true;
    }

    public List<BusInfo> getBusInfoList(String arsId) {
        if (busInfo.containsKey(arsId)) {
            return busInfo.get(arsId);
        } else return new ArrayList<BusInfo>();
    }

}
