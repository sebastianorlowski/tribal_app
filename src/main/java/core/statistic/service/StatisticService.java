package core.statistic.service;

import core.dataloader.persistence.model.Ally;
import core.dataloader.persistence.repository.AllyRepository;
import core.dataloader.persistence.repository.VillageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Service
public class StatisticService {

    private final VillageRepository villageRepository;
    private final AllyRepository allyRepository;

    @Autowired
    public StatisticService(VillageRepository villageRepository,
                            AllyRepository allyRepository) {
        this.villageRepository = villageRepository;
        this.allyRepository = allyRepository;
    }

    public String generatedInformation() {
        return westInformation().concat(eastInformation());
    }

    public String generatedInformationAboutAllies(List<String> firstAlly, List<String> secondAlly) {
        return westInformation()
                .concat(eastInformation())
                .concat(firstAlliesAsWestSide(firstAlly))
                .concat(secondAlliesAsEastSide(secondAlly));
    }

    public String continentStatistics(String continent, String firstAlly, String secondAlly) {
        int x = Integer.parseInt(continent.substring(0, 1));
        int y = Integer.parseInt(continent.substring(1, 2));
        List<String> firstSide = List.of(firstAlly.split(";"));
        List<String> secondSide = List.of(secondAlly.split(";"));

        return null;
    }

    //Retrieve first ally and second with number of villages from continent of number

    private String firstAlliesAsWestSide(List<String> firstAlly) {
        Integer villageWithPlayerWest = villageRepository.westSideWithoutBarbarian();
        List<Ally> firstAllies = allyRepository.findByTagIn(firstAlly);
        int valueFirst = firstAllies.stream().mapToInt(Ally::getVillages).sum();
        int withoutPlayer = villageWithPlayerWest - valueFirst;

        return "WITAM: " + valueFirst + " wiosek \n" +
                "Ilość wiosek z graczem bez plemienia na zachodzie: " + withoutPlayer + " wiosek \n";

    }

    private String secondAlliesAsEastSide(List<String> secondAlly) {
        Integer villageWithPlayerEast = villageRepository.eastSideWithoutBarbarian();
        List<Ally> secondAllies = allyRepository.findByTagIn(secondAlly);
        int valueSecond = secondAllies.stream().mapToInt(Ally::getVillages).sum();
        int withoutPlayer = villageWithPlayerEast - valueSecond;

        return "PEWNI: " + valueSecond + " wiosek \n" +
                "Ilość wiosek z graczem bez plemienia na wschodzie: " + withoutPlayer + " wiosek \n";

    }

    private String westInformation() {
        Integer westTotal = villageRepository.westSideTotal();
        Integer villageWithPlayerWest = villageRepository.westSideWithoutBarbarian();
        int barbarianVillages = westTotal - villageWithPlayerWest;
        return "ZACHÓD \n" +
                "Wszystkie wioski na zachodzie: " + westTotal + "\n" +
                "Wioski z graczem: " + villageWithPlayerWest + "\n" +
                "Barbarzyńskie: " + barbarianVillages + "\n";
    }

    private String eastInformation() {
        Integer eastTotal = villageRepository.eastSideTotal();
        Integer villageWithPlayerEast = villageRepository.eastSideWithoutBarbarian();
        int barbarianVillages = eastTotal - villageWithPlayerEast;
        return "WSCHÓD \n" +
                "Wszystkie wioski na wschodzie: " + eastTotal + "\n" +
                "Wioski z graczem: " + villageWithPlayerEast + "\n" +
                "Barbarzyńskie: " + barbarianVillages + "\n";
    }
}
