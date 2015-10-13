package camel.elasticsearch.demo.elasticsearch;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;


/**
 * Groups the entry into weekly indexes.
 *
 */
public class WeeklyIndexNameHeaderUpdater implements Processor {
    private final String indexType;

    public WeeklyIndexNameHeaderUpdater(String indexType) {
        this.indexType = indexType;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        SyndFeed feed = exchange.getIn().getBody(SyndFeed.class);
        SyndEntry entry = (SyndEntry)feed.getEntries().get(0);
        String indexName = new DateTime(entry.getPublishedDate()).withDayOfWeek(DateTimeConstants.MONDAY)
                .toString(String.format("'%s-'yyyy-MM-dd", indexType));

        exchange.getIn().setHeader("indexName", indexName);
        exchange.getIn().setHeader("indexType", indexType);
    }
}
