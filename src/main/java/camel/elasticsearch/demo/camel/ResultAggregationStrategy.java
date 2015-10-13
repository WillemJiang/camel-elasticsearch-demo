package camel.elasticsearch.demo.camel;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class ResultAggregationStrategy implements AggregationStrategy {
    @Override public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        } else {
            String result = oldExchange.getIn().getBody(String.class);
            String newLine = newExchange.getIn().getBody(String.class);
            result = result + newLine;
            oldExchange.getIn().setBody(result);
            return oldExchange;
        }
    }
}
