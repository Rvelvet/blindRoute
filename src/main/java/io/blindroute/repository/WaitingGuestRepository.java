package io.blindroute.repository;

import io.blindroute.domain.api.BusInfo;
import io.blindroute.domain.repository.Guest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class WaitingGuestRepository {
    private final static ConcurrentHashMap<String, List<Guest>> LIST = new ConcurrentHashMap<>();


    //Key = ArsId+"|"+busRouteNm
    public boolean add(String key, String Name) {
        Guest guest= new Guest(Name, false);
        if(LIST.containsKey(key)){
            if(!LIST.get(key).contains(guest)){
                LIST.get(key).add(guest);
            }else return false;
        }else {
            LIST.put(key, new ArrayList<>());
            LIST.get(key).add(guest);
        }
        return true;
    }

    public boolean remove(String key, String Name) {
        if (LIST.containsKey(key)) {
            LIST.get(key).remove(new Guest(Name, false));
            return true;
        }else return false;
    }

    public boolean isArrived(String key, String Name) {
        Guest find = new Guest(Name, false);
        if (LIST.containsKey(key)) {
            Guest guest = LIST.get(key).stream().filter(gst -> gst.equals(find)).findAny().orElse(null);
            if(guest != null && guest.getIsArrived()){
                LIST.get(key).remove(guest);
                return true;
            }
        }
        return false;
    }

    public boolean clear(String Key) {
        if (LIST.containsKey(Key)) {
            LIST.get(Key).clear();
        }
        return true;

    }

    public boolean Arrived(String key) {
        if(LIST.containsKey(key)){
            LIST.get(key).stream().iterator().forEachRemaining(guest -> guest.setIsArrived(true));
            return true;
        }
        else return false;

    }
}
