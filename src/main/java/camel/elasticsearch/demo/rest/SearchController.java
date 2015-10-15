package camel.elasticsearch.demo.rest;

import camel.elasticsearch.demo.CamelRoutes;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.ProducerTemplate;
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
        LOG.info("Tweet search request received with query: {} and max: {}", query, maxSize);
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("queryField", "title");
        headers.put("maxSize", maxSize);
        String result = producerTemplate.requestBodyAndHeaders(CamelRoutes.RSS_SEARCH_URI, query, headers, String.class);
        return result;
    }
}
