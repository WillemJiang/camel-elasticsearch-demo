package camel.elasticsearch.demo.rest;

import camel.elasticsearch.demo.CamelRoutes;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.ProducerTemplate;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ProducerTemplate producerTemplate;

    @RequestMapping(value = "/rss/search", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String rssSearch(@RequestParam("q") String query,
                              @RequestParam(value = "max") int maxSize) {
        LOG.info("Rss search request received with query: {} and max: {}", query, maxSize);

        //Build up the SearchRequest
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        
        sourceBuilder.query(QueryBuilders.matchQuery("title", query));
        sourceBuilder.from(0).size(maxSize).explain(true);
        searchRequest.source(sourceBuilder);

        String result = producerTemplate.requestBody(CamelRoutes.RSS_SEARCH_URI, searchRequest, String.class);
        return result;
    }
}
