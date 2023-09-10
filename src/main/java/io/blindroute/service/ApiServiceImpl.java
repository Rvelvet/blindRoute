package io.blindroute.service;

import io.blindroute.domain.api.BusRoute;
import io.blindroute.domain.api.BusRouteBody;
import io.blindroute.domain.api.BusStationBody;
import io.blindroute.domain.api.BusStation;
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

    public ApiServiceImpl(Environment environment) {
        this.environment = environment;
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
}
