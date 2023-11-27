package io.blindroute.service;

import io.blindroute.domain.api.*;
import io.blindroute.repository.BookmarkRepository;
import io.blindroute.repository.BusInfoRepository;
import io.blindroute.repository.UrlRepository;
import io.blindroute.repository.WaitingGuestRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService{

    private final Environment environment;
    private final BusInfoRepository busInfoRepository;
    private final WaitingGuestRepository guestRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UrlRepository urlRepository;


    public ApiServiceImpl(Environment environment, BusInfoRepository busInfoRepository, WaitingGuestRepository guestRepository, BookmarkRepository bookmarkRepository, UrlRepository urlRepository) {
        this.environment = environment;
        this.busInfoRepository = busInfoRepository;
        this.guestRepository = guestRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.urlRepository = urlRepository;
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
        busInfoRepository.add(arsId, busInfo);
    }

    public void removeBusInfo(String arsId, BusInfo busInfo) {
        busInfoRepository.remove(arsId, busInfo);
    }

    public List<BusInfo> getList(String arsId ) {
        return busInfoRepository.getBusInfoList(arsId);
    }


    public boolean isArrived(String Key, String Name) {
        return guestRepository.isArrived(Key, Name);
    }

    public void addGuest(String Key, String Name) {
        guestRepository.add(Key, Name);
    }

    public void test(String arsId, BusInfo busInfo) {
        busInfoRepository.remove(arsId, busInfo);
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

    public Boolean urlUpdate(String url) {

        return urlRepository.update(url);
    }

    public boolean ImageProcess(byte[] image, String arsId){
        String url = urlRepository.getURL();
        if(url==null){
            log.info("no url in the repository");
            return false;
        }

        String  uri = UriComponentsBuilder.fromUriString(url).path("/upload").build().toString();
        log.info("uri path = {}", uri);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Resource resource = new ByteArrayResource(image){
            @Override
            public String getFilename() {
                return "image.jpg";
            }
        };
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);


        guestRepository.Arrived(arsId + "|" + response.getBody());


        return true;
    }

}
