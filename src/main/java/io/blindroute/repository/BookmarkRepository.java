package io.blindroute.repository;

import io.blindroute.domain.api.Bookmark;
import io.blindroute.domain.api.BusInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookmarkRepository {
    private static final ConcurrentHashMap<String, List<Bookmark>> BOOKMARK = new ConcurrentHashMap<>();

    public boolean add(String key, Bookmark bookmark) {
        if(BOOKMARK.containsKey(key)){
            if(!BOOKMARK.get(key).contains(bookmark)){
                BOOKMARK.get(key).add(bookmark);
            }else return false;
        }
        else{
            BOOKMARK.put(key, new ArrayList<>());
            BOOKMARK.get(key).add(bookmark);
        }
        return true;
    }

    public boolean remove(String key, Bookmark bookmark) {
        return BOOKMARK.containsKey(key) && BOOKMARK.get(key).remove(bookmark);
    }

    public boolean removeAll(String key) {
        if (BOOKMARK.containsKey(key)) {
            BOOKMARK.get(key).clear();
            return true;
        } else return false;
    }

    public List<Bookmark> getBookmark(String key) {
        if (BOOKMARK.containsKey(key) && BOOKMARK.get(key).size() > 0) {
            return BOOKMARK.get(key);
        } else return null;
    }


}
