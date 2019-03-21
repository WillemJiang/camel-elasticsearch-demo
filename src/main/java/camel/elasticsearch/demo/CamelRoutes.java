package camel.elasticsearch.demo;

import camel.elasticsearch.demo.camel.ListAggregationStrategy;
import camel.elasticsearch.demo.camel.ResultAggregationStrategy;
import camel.elasticsearch.demo.elasticsearch.*;

import javax.annotation.PostConstruct;
import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.component.elasticsearch.ElasticsearchComponent;
import org.apache.camel.component.elasticsearch.ElasticsearchEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Setup the camel routes here
 */
@Component
public class CamelRoutes extends RouteBuilder {

    private static final String ES_RSS_INDEX_TYPE = "rss";

    public static final String RSS_SEARCH_URI = "vm:rssSearch";

    @Value("${elasticsearch.hostaddresses}")
    private String elasticsearchHostaddresses;

    @Value("${elasticsearch.scrollBatchSize}")
    private int searchBatchSize;

    @Value("${rss.url}")
    private String RssUrl;

    @PostConstruct
    public void initCamelContext() throws Exception {
        // shutdown the producer first that reads from the twitter sample feed:
        getContext().getShutdownStrategy().setShutdownRoutesInReverseOrder(false);
        // wait max 5 seconds for camel to stop:
        getContext().getShutdownStrategy().setTimeout(5L);

    }


    @Override public void configure() throws Exception {
        String rssEndpointUri = String.format("rss:%s?splitEntries=true&consumer.delay=1000", RssUrl);
        String esBlukIndexUri = String.format("elasticsearch-rest://rss-indexer?operation=BulkIndex&hostAddresses=%s", elasticsearchHostaddresses);
        String esSearchUri = String.format("elasticsearch-rest://rss-indexer?operation=Search&hostAddresses=%s", elasticsearchHostaddresses);
        SplitterBean splitterBean = new SplitterBean();

        // Pulling the rss feed every 1 second
        from(rssEndpointUri)
                .process(new WeeklyIndexNameHeaderUpdater(ES_RSS_INDEX_TYPE))
                .process(new ElasticSearchRSSConverter())
                // collects feeds into weekly batches based on index name:
                .aggregate(header("indexName"), new ListAggregationStrategy())
                    // creates new batches every 2 seconds
                    .completionInterval(2000)
                    // makes sure the last batch will be processed before application shuts down:
                    .forceCompletionOnStop()
                    .to("log:message")
                    // inserts a batch of feeds to elastic search:
                    .to(esBlukIndexUri)
                .log("Uploaded documents to ElasticSearch index ${headers.indexName}: ${body.length}");

        // Just search the message from the elastic search service
        from(RSS_SEARCH_URI)
                .to(esSearchUri)
                .split(method(splitterBean, "splitSearchHits"), new ResultAggregationStrategy())
                   .process(new ElasticSearchSearchHitConverter())
                   .to("freemarker:Response.ftl")
                .end()
           // Just add a return to the
        .to("freemarker:ResultPage.ftl");

    }

}
