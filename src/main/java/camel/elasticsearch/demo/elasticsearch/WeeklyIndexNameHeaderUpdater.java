/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

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
