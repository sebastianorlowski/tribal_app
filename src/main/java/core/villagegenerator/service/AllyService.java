package core.villagegenerator.service;

import core.dataloader.persistence.model.Ally;
import core.dataloader.persistence.model.Village;
import core.dataloader.persistence.repository.AllyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllyService {

    private final AllyRepository allyRepository;

    @Autowired
    public AllyService(AllyRepository allyRepository) {
        this.allyRepository = allyRepository;
    }

    public Ally getAlly(String tag) {
        return allyRepository.findAllyByTag(tag).get();
    }
}
