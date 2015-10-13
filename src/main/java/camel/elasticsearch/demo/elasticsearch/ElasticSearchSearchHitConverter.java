package camel.elasticsearch.demo.elasticsearch;

import java.util.HashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.elasticsearch.search.SearchHit;


public class ElasticSearchSearchHitConverter implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        SearchHit hit = exchange.getIn().getBody(SearchHit.class);

        // Convert Elasticsearch documents to Maps before serializing to JSON:
        Map<String, Object> map = new HashMap<String, Object>(hit.sourceAsMap());
        map.put("score", hit.score());
        exchange.getIn().setBody(map);
    }
}
