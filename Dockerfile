FROM docker.elastic.co/elasticsearch/elasticsearch:8.1.0
RUN elasticsearch-plugin install analysis-kuromoji

#ARG ELK_VERSION
#FROM docker.elastic.co/elasticsearch/elasticsearch:${ELK_VERSION}
#RUN elasticsearch-plugin install analysis-nori