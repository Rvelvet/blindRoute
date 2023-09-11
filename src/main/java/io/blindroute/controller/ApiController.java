package io.blindroute.controller;


import io.blindroute.domain.api.*;
import io.blindroute.service.ApiService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/search/station")
    public JsonBusStation SearchStation(String searchKeyword, Authentication authentication, HttpServletResponse response) throws IOException {
        log.info("authentication info = {}", authentication);
        log.info("searchKeyword = {}", searchKeyword);


        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication Failed");
//            throw new RuntimeException();

        }
        List<BusStation> busStation = apiService.getBusStationsByString(searchKeyword);

        return new JsonBusStation(busStation);
    }

    @PostMapping("/select/route")
    public JsonBusRoute SearchRoute(String arsId, Authentication authentication
    ) {
        if (authentication == null|| ObjectUtils.isEmpty(arsId)) {
            return new JsonBusRoute(null);
        }
        List<BusRoute> busRouteByString = apiService.getBusRouteByString(arsId);

        return new JsonBusRoute(busRouteByString);
    }


    @PostMapping("/select/bus")
    public String selectBusNm(String busRouteNm, Authentication authentication) {
        if (authentication == null|| ObjectUtils.isEmpty(busRouteNm)) {
            return "fail";
        }
        return "success";
    }

    @PostMapping("/search/destination")
    public JsonDestination SearchDestination(String busRouteId, Authentication authentication) {
        if (authentication == null|| ObjectUtils.isEmpty(busRouteId)) {
            return new JsonDestination(null);
        }
        List<Destination> destinationList = apiService.getDestinationsByString(busRouteId);

        return new JsonDestination(destinationList);
    }

    @GetMapping("/login/sessions")
    public String login() {
        return "login";
    }
}
