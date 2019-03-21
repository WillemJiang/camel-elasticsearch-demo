# camel-elasticsearch-demo
The demo is based on [the camel-twitter-demo of davidkiss](https://github.com/davidkiss/twitter-camel-ingester) to show how to use the camel-elasticsearch component.

The camel route pulls the RSS feed and send the RSS enteris into Elasticsearch. Users can query the RSS title with a key word from a RESTful service which can be accessed from "http://localhost:8080/rss/search" to search the RSS title.

## Installing ElasticSearch

1. Download Elasticsearch from https://www.elastic.co/downloads/elasticsearch.

2. Install it to a local folder ($ES_HOME)Edit $ES_HOME/config/elasticsearch.yml and add this line:
 
		cluster.name: rss-indexer


3. Install the [analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik) plugin to support the full text search of Chinese.
4. Run Elasticsearch: $ES_HOME/bin/elasticsearch.sh or $ES_HOME/bin/elasticsearch.bat

## Running the Demo
You just need to run the Application.main() from your favourite IDE or execute below line from the command line:

		mvn compile && mvn spring-boot:run





