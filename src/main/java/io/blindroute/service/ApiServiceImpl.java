package io.blindroute.service;

import io.blindroute.domain.api.*;
import io.blindroute.repository.BookmarkRepository;
import io.blindroute.repository.BusInfoRepository;
import io.blindroute.repository.WaitingGuestRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService{

    private final Environment environment;
    private final BusInfoRepository repository;
    private final WaitingGuestRepository guestRepository;
    private final BookmarkRepository bookmarkRepository;


    public ApiServiceImpl(Environment environment, BusInfoRepository repository, WaitingGuestRepository guestRepository, BookmarkRepository bookmarkRepository) {
        this.environment = environment;
        this.repository = repository;
        this.guestRepository = guestRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    public List<BusStation> getBusStationsByString(String Search) {

        String serviceKey =  environment.getProperty("api.serviceKey");
        Search = URLEncoder.encode(Search, Charset.forName("UTF-8"));
        URI uri = UriComponentsBuilder.fromUriString("http://ws.bus.go.kr")
                .path("/api/rest/stationinfo/getStationByName")
//                .queryParam("stSrch", URLEncoder.encode(Search, Charset.forName("UTF-8")))
                .queryParam("ServiceKey",
                        serviceKey)
                .queryParam("stSrch", Search)
                .queryParam("resultType", "json")
                .build(true)
                .toUri();

//        String uri = "http://ws.bus.go.kr/api/rest/stationinfo/getLowStationByName?serviceKey=" +
//                serviceKey + "&stSrch=" + Search + "&resultType=json";


        RestTemplate restTemplate = new RestTemplate();

//        RequestEntity.get(uri).


        ResponseEntity<BusStationBody> busStations = restTemplate.getForEntity(uri, BusStationBody.class);
//        RequestEntity<Void> build = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
//        ResponseEntity<Body> busStations = restTemplate.exchange(build, Body.class);


        return busStations.getBody().getMsgBody().getItemList();
    }

    @Override
    public List<BusRoute> getBusRouteByString(String busRouteNm) {

        String serviceKey =  environment.getProperty("api.serviceKey");
        busRouteNm = URLEncoder.encode(busRouteNm, Charset.forName("UTF-8"));
        URI uri = UriComponentsBuilder.fromUriString("http://ws.bus.go.kr")
                .path("/api/rest/stationinfo/getRouteByStation")
//                .queryParam("arsId", URLEncoder.encode(Search, Charset.forName("UTF-8")))
                .queryParam("serviceKey",
                        serviceKey)
                .queryParam("arsId", busRouteNm)
                .queryParam("resultType", "json")
                .build(true)
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<BusRouteBody> busRoute = restTemplate.getForEntity(uri, BusRouteBody.class);


        return busRoute.getBody().getMsgBody().getItemList();
    }

    @Override
    public List<Destination> getDestinationsByString(String busRouteId) {


        String serviceKey =  environment.getProperty("api.serviceKey");
        busRouteId = URLEncoder.encode(busRouteId, Charset.forName("UTF-8"));
        URI uri = UriComponentsBuilder.fromUriString("http://ws.bus.go.kr")
                .path("/api/rest/busRouteInfo/getStaionByRoute")
//                .queryParam("arsId", URLEncoder.encode(Search, Charset.forName("UTF-8")))
                .queryParam("serviceKey",
                        serviceKey)
                .queryParam("busRouteId", busRouteId)
                .queryParam("resultType", "json")
                .build(true)
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<DestinationBody> busDestination = restTemplate.getForEntity(uri, DestinationBody.class);

        return busDestination.getBody().getMsgBody().getItemList();
    }

    public void addBusInfo(String arsId, BusInfo busInfo) {
        repository.add(arsId, busInfo);
    }

    public void removeBusInfo(String arsId, BusInfo busInfo) {
        repository.remove(arsId, busInfo);
    }

    public List<BusInfo> getList(String arsId ) {
        return repository.getBusInfoList(arsId);
    }


    public boolean isArrived(String Key, String Name) {
        return guestRepository.isArrived(Key, Name);
    }

    public void addGuest(String Key, String Name) {
        guestRepository.add(Key, Name);
    }

    public void test(String arsId, BusInfo busInfo) {
        repository.remove(arsId, busInfo);
        guestRepository.Arrived(arsId + "|" + busInfo.getBusRouteNm());
    }

    public void clearGuestInfo(String arsId, BusInfo busInfo) {
        guestRepository.clear(arsId + "|" + busInfo.getBusRouteNm());
    }

    public boolean addBookmark(String key, Bookmark bookmark) {
        return bookmarkRepository.add(key, bookmark);
    }

    public List<Bookmark> getBookmarks(String key) {
        return bookmarkRepository.getBookmark(key);
    }

    public boolean removeBookmark(String key, Bookmark bookmark) {
        return bookmarkRepository.remove(key, bookmark);
    }

    public boolean removeAllBookmark(String key) {
        return bookmarkRepository.removeAll(key);
    }

}
