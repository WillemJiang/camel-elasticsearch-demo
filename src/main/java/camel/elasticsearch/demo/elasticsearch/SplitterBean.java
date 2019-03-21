package camel.elasticsearch.demo.elasticsearch;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class SplitterBean {
    public List<SearchHit> splitSearchHits (SearchHits searchHits) {
        List<SearchHit> result = new ArrayList<>();
        for(SearchHit hit : searchHits.getHits()) {
            result.add(hit);
        }
        return result;
    }
}
