package core.statistic.api;

import core.dataloader.persistence.repository.VillageRepository;
import core.statistic.service.StatisticService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@Api(tags = "Statistics")
public class StatisticController {

    private StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping(value = "/half_world", produces = MediaType.TEXT_PLAIN_VALUE)
    public String halfOfWorld() {
        return statisticService.generatedInformation().replaceAll("[\\]\\[\\,]", "");
    }

    @GetMapping(value = "/two_allies", produces = MediaType.TEXT_PLAIN_VALUE)
    public String twoAlliesStatistics(String first, String second) {
        List<String> firstSide = List.of(first.split(";"));
        List<String> secondSide = List.of(second.split(";"));
        return statisticService.generatedInformationAboutAllies(firstSide, secondSide)
                .replaceAll("[\\]\\[\\,]", "")
                .concat("Wszystkie wioski poza plemionami " + first + ";" + second + " są liczone jako bez plemienia");
    }

    @GetMapping(value = "/continents", produces = MediaType.TEXT_PLAIN_VALUE)
    public String continentStatistics(String continent, String firstAlly, String secondAlly) {
        return statisticService.continentStatistics(continent, firstAlly, secondAlly);
    }
}
