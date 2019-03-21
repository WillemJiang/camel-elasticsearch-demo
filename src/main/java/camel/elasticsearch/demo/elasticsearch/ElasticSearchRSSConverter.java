package camel.elasticsearch.demo.elasticsearch;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Just put the entry into a map which can be processed by elastic search
 */
public class ElasticSearchRSSConverter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        SyndFeed feed = exchange.getIn().getBody(SyndFeed.class);
        SyndEntry entry = (SyndEntry)feed.getEntries().get(0);
        Map map = new HashMap();
        map.put("title", entry.getTitle());
        map.put("link", entry.getLink());
        map.put("description", stripHtmlTags(entry.getDescription().getValue()));
        map.put("publishedData", entry.getPublishedDate());
        exchange.getIn().setBody(map);
    }

    // Just remove the HtmlTags
    private String stripHtmlTags(String str) {
        if (str != null) {
            return str.replaceAll("\\<.*?\\>", "");
        } else {
            return null;
        }
    }
}
