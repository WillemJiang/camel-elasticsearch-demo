package camel.elasticsearch.demo;

import camel.elasticsearch.demo.camel.ListAggregationStrategy;
import camel.elasticsearch.demo.camel.ResultAggregationStrategy;
import camel.elasticsearch.demo.elasticsearch.ElasticSearchRSSConverter;
import camel.elasticsearch.demo.elasticsearch.ElasticSearchSearchHitConverter;
import camel.elasticsearch.demo.elasticsearch.ElasticSearchService;
import camel.elasticsearch.demo.elasticsearch.WeeklyIndexNameHeaderUpdater;
import javax.annotation.PostConstruct;
import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.component.elasticsearch.ElasticsearchComponent;
import org.apache.camel.component.elasticsearch.ElasticsearchEndpoint;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Setup the camel routes here
 */
@Component
public class CamelRoutes extends RouteBuilder {

    private             String ES_TWEET_INDEXER_ENDPOINT = "direct:tweet-indexer-ES";
    private             String ES_RSS_INDEX_TYPE         = "rss";
    public static final String RSS_SEARCH_URI            = "vm:rssSearch";

    @Value("${elasticsearch.rss.uri}") private         String               elasticsearchRssUri;
    @Value("${elasticsearch.scrollBatchSize}") private int                  searchBatchSize;
    private                                            ElasticSearchService esRssService;

    @PostConstruct public void initCamelContext() throws Exception {
        // shutdown the producer first that reads from the twitter sample feed:
        getContext().getShutdownStrategy().setShutdownRoutesInReverseOrder(false);
        // wait max 5 seconds for camel to stop:
        getContext().getShutdownStrategy().setTimeout(5L);

        initESTweetService();
    }

    private void initESTweetService() {
        ElasticsearchComponent elasticsearchComponent = new ElasticsearchComponent(getContext());
        getContext().addComponent("elasticsearch", elasticsearchComponent);
        ElasticsearchEndpoint esTweetEndpoint = (ElasticsearchEndpoint) getContext().getEndpoint(elasticsearchRssUri);
        esRssService = new ElasticSearchService(esTweetEndpoint.getClient(), ES_RSS_INDEX_TYPE, searchBatchSize);
    }

    @Override
    public void configure() throws Exception {
        from("rss:http://cn.reuters.feedsportal.com/CNTechNews?splitEntries=true&consumer.delay=500")
                .process(new WeeklyIndexNameHeaderUpdater(ES_RSS_INDEX_TYPE))
                .process(new ElasticSearchRSSConverter())
                // collects feeds into weekly batches based on index name:
                .aggregate(header("indexName"), new ListAggregationStrategy())
                    // creates new batches every 2 seconds
                    .completionInterval(2000)
                    // makes sure the last batch will be processed before application shuts down:
                    .forceCompletionOnStop()
                    // inserts a batch of feeds to elastic search:
                    .to(elasticsearchRssUri)
                .log("Uploaded documents to ElasticSearch index ${headers.indexName}: ${body.size()}");

        // Just search the message from the elastic search service
        from(RSS_SEARCH_URI)
                // use an iterator to process search result instead of keeping results in memory:
                .split(method(esRssService, "search"), new ResultAggregationStrategy())
                .process(new ElasticSearchSearchHitConverter())
                .marshal(new JacksonDataFormat())
                // Just add a return to the
                .setBody(simple("${body}\n"))
                .end();

    }

}
