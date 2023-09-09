package io.blindroute.controller;


import io.blindroute.domain.api.BusStation;
import io.blindroute.domain.api.JsonBusStation;
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
    public String SearchRoute(String stId, Authentication authentication
    ) {
        if (authentication == null|| ObjectUtils.isEmpty(stId)) {
            return "fail";
        }


        return "success";
    }

    @GetMapping("/login/session")
    public String login() {
        return "login";
    }
}
