package cz.zcu.kiv.eegdatabase.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SampleArticleRepository extends ElasticsearchRepository<Article,String> {
}
