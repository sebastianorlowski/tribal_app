package core.villagegenerator.api;

import core.dataloader.persistence.model.Player;
import core.dataloader.persistence.model.Village;
import core.villagegenerator.api.dto.VillagePlayerCountDto;
import core.villagegenerator.api.dto.VillageDto;
import core.villagegenerator.api.mapper.VillageMapper;
import core.villagegenerator.service.VillageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/villages")
@Api(tags = "Villages")
public class VillageController {

    private final VillageService villageService;
    private final VillageMapper villageMapper;

    @Autowired
    public VillageController(VillageService villageService,
                             VillageMapper villageMapper) {
        this.villageService = villageService;
        this.villageMapper = villageMapper;
    }

    @GetMapping(value = "/list", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveVillagesAsTextFile(@RequestParam String playerName) {
        List<Village> villages = villageService.findPlayerVillages(playerName);
        List<VillageDto> villageDto = villageMapper.listVillageDto(villages);
        return villageDto.toString().replaceAll("[\\]\\[\\,\\ ]", "");
    }

    /*** Provided time is count as minutes. ***/
    @GetMapping(value = "/aroundVillage", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrievePlayersAroundVillage(@RequestParam Integer time,
                                                @RequestParam String coords,
                                                @RequestParam Integer unitTime) {
        Map<Player, Integer> villages = villageService.findVillagesAroundVillage(time, coords, unitTime);
        List<VillagePlayerCountDto> villagePlayerCountDtoList = villageMapper.asPlayerVillageCountDto(villages);

        return villagePlayerCountDtoList.toString().replaceAll("[\\]\\[\\,]", "");
    }

    @GetMapping(value = "/generate_attackers", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveAttackers(@RequestParam String playerName,
                                     @RequestParam Integer time,
                                     @RequestParam Integer unitTime,
                                     @RequestParam Integer countOfVillage) {
        Map<Village, List<Player>> playersToVillage = villageService.generatePlayersAroundVillages(
                playerName, time, unitTime, countOfVillage);
        List<VillagePlayerCountDto> villagePlayerCountDtoList = villageMapper.asVillagePlayerDto(playersToVillage);
        List<String> villagePlayerList = new ArrayList<>();
        villagePlayerCountDtoList.forEach(villagePlayerCountDto -> {
            villagePlayerList.add(villagePlayerCountDto.toStringVillagePlayers());
        });

        return villagePlayerList.toString().replace(",", "");
    }

    @GetMapping(value = "/generate_attackers/complete", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveCompleteAttackers(@RequestParam String playerName,
                                             @RequestParam Integer time,
                                             @RequestParam Integer unitTime,
                                             @RequestParam Integer countOfVillage) {
        Map<Village, Player> playerToVillage = villageService
                .generateAttackerToVillage(playerName, time, unitTime, countOfVillage);

    }
}
