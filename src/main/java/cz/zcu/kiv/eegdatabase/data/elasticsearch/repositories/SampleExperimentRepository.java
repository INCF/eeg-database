package cz.zcu.kiv.eegdatabase.data.elasticsearch.repositories;


import cz.zcu.kiv.eegdatabase.data.elasticsearch.entities.ExperimentElastic;
import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.Repository;

public interface SampleExperimentRepository extends ElasticsearchRepository<ExperimentElastic, String> {
	
}
