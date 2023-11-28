package io.blindroute.controller;


import io.blindroute.domain.api.*;
import io.blindroute.repository.BusInfoRepository;
import io.blindroute.service.ApiService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService, BusInfoRepository repository) {
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
    public JsonBusRoute SearchRoute(String arsId, Authentication authentication)
    {
        if (authentication == null|| ObjectUtils.isEmpty(arsId)) {
            return new JsonBusRoute(null);
        }
        List<BusRoute> busRouteByString = apiService.getBusRouteByString(arsId);

        return new JsonBusRoute(busRouteByString);
    }


    @PostMapping("/select/bus")
    public String selectBusNm(String arsId, String busRouteId, String busRouteNm, String busRouteAbrv, Authentication authentication) {
        if (authentication == null|| ObjectUtils.isEmpty(busRouteNm)) {
            return "fail";
        }
        apiService.addBusInfo(arsId, new BusInfo(arsId, busRouteId, busRouteNm, busRouteAbrv));
        OidcUser user = (OidcUser) authentication.getPrincipal();
        apiService.addGuest(arsId+"|"+busRouteNm,(String) user.getClaims().get("sub"));

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

//    @PostMapping("/image/test/byte")
    public String handleFileUpload(@RequestParam("image") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            // 여기서 bytes를 사용하여 이미지를 처리하거나 저장합니다.

            file.transferTo(new File("C:\\Users\\NDH\\Spring\\blindroute\\"+file.getOriginalFilename()));
            log.info("file received");
            return "File uploaded successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }

    @PostMapping("/image/test/byte")
    public String returnFile(@RequestParam("image") MultipartFile file, String arsId) //String arsId
    {
        try {
            byte[] bytes = file.getBytes();
            // 여기서 bytes를 사용하여 이미지를 처리하거나 저장합니다.
//            apiService.ImageProcess(file);

            return apiService.ImageProcess(bytes, arsId);
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    @PostMapping("/select/wishroute")
    public JsonBusInfo getBusInfoList(String arsId) {
        return new JsonBusInfo(apiService.getList(arsId));
    }

    @PostMapping("/delete/bus")
    public String deleteBusInfo(String arsId, String busRouteId, String busRouteNm, String busRouteAbrv) {

        apiService.removeBusInfo(arsId, new BusInfo(arsId, busRouteId, busRouteNm, busRouteAbrv));
        apiService.clearGuestInfo(arsId, new BusInfo(arsId, busRouteId, busRouteNm, busRouteAbrv));
        return "success";
    }

    @PostMapping("/select/arrivalCheck")
    public Boolean arrivalCheck(String arsId, String busRouteId, String busRouteNm, String busRouteAbrv, Authentication authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        String name = (String) user.getClaims().get("sub");
        return apiService.isArrived(arsId+"|"+busRouteNm,name);
    }

    @PostMapping("/test")
    public void test(String arsId, String busRouteId, String busRouteNm, String busRouteAbrv) {
        apiService.test(arsId, new BusInfo(arsId, busRouteId, busRouteNm, busRouteAbrv));
    }

    @GetMapping("/authentication")
    public Boolean validation(Authentication authentication) {
        return authentication != null;
    }


    @PostMapping("/bookmark/register")
    public Boolean addBookmark(Authentication authentication, String arsId, String stNm, String busRouteId, String busRouteNm, String busRouteAbrv) {
        if (authentication == null) {
            return false;
        }
        OidcUser user = (OidcUser) authentication.getPrincipal();
        String sub = (String) user.getClaims().get("sub");
        Bookmark bookmark = new Bookmark(arsId, stNm, busRouteId, busRouteNm, busRouteAbrv);

        return apiService.addBookmark(sub, bookmark);
    }

    @PostMapping("/bookmark/list")
    public JsonBookmark getBookmarks(Authentication authentication) {
        if (authentication == null) {
            return new JsonBookmark(null);
        }
        OidcUser user = (OidcUser) authentication.getPrincipal();
        String sub = (String)user.getClaims().get("sub");

        return new JsonBookmark(apiService.getBookmarks(sub));
    }

    @PostMapping("/bookmark/remove")
    public Boolean removeBookmark(Authentication authentication, String arsId, String stNm, String busRouteId, String busRouteNm, String busRouteAbrv) {
        if (authentication == null) {
            return false;
        }
        OidcUser user = (OidcUser) authentication.getPrincipal();
        String sub = (String) user.getClaims().get("sub");
        Bookmark bookmark = new Bookmark(arsId, stNm, busRouteId, busRouteNm, busRouteAbrv);

        return apiService.removeBookmark(sub, bookmark);
    }

    @PostMapping("/bookmark/removeall")
    public Boolean removeAllBookmark(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        OidcUser user = (OidcUser) authentication.getPrincipal();
        String sub = (String) user.getClaims().get("sub");
        return apiService.removeAllBookmark(sub);
    }

    @PostMapping("/url")
    public String UrlUpdate(String url) {
        apiService.urlUpdate(url);
        return "success";
    }


    @GetMapping("/login/sessions")
    public String login() {

        return "login";
    }
}
