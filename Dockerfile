FROM docker.elastic.co/elasticsearch/elasticsearch:8.3.2
RUN elasticsearch-plugin install analysis-nori
RUN elasticsearch-plugin install https://github.com/netcrazy/elasticsearch-jaso-analyzer/releases/download/v8.3.2/jaso-analyzer-plugin-8.3.2-plugin.zip