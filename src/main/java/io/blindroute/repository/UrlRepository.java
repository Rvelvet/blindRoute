package io.blindroute.repository;

import io.blindroute.domain.api.BusInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UrlRepository {
    private final static ConcurrentHashMap<String, String> URLRepository = new ConcurrentHashMap<>();

    public boolean update(String url) {
        URLRepository.put("URL", url);
        return true;
    }

    public String getURL() {
        return URLRepository.getOrDefault("URL", null);
    }


}
