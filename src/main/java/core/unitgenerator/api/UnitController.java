package core.unitgenerator.api;

import core.unitgenerator.service.UnitService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/unit")
@Api(tags = "Unit")
public class UnitController {

    private final UnitService unitService;

    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping(value = "/army/", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveInformationAboutArmy(@RequestParam Integer spear,
                                                @RequestParam Integer sword,
                                                @RequestParam Integer heavy,
                                                @RequestParam Integer coordX) {
        Map<String, Integer> info = unitService.retrieveInformationAboutArmy(spear, sword, heavy, coordX);
        List<UnitInformation> unitInformationList = new ArrayList<>();
        info.forEach((key, value) -> {
            UnitInformation unitInformation = new UnitInformation();
            unitInformation.setPlayerName(key);
            unitInformation.setPackages(value);
            unitInformationList.add(unitInformation);
        });
        return unitInformationList.toString().replaceAll(",", "");
    }

    @GetMapping(value = "/packages/")
    private ResponseEntity<Integer> retrieveInformationAboutPackages(@RequestParam Integer coordX,
                                                                     @RequestParam Integer coordY) {
        Integer packages = unitService.retrieveInformationAboutPackages(coordX, coordY);
        return ResponseEntity.ok(packages);
    }

    @GetMapping(value = "/army/left", produces = MediaType.TEXT_PLAIN_VALUE)
    private String retrieveInformationAboutArmyLeft(@RequestParam Integer spear,
                                                    @RequestParam Integer sword,
                                                    @RequestParam Integer heavy,
                                                    @RequestParam Integer coordX) {
        Map<String, Integer> info = unitService.retrievePlayersAbleToProduceArmy(spear, sword, heavy, coordX);
        List<UnitInformation> unitInformationList = new ArrayList<>();
        info.forEach((key, value) -> {
            UnitInformation unitInformation = new UnitInformation();
            unitInformation.setPlayerName(key);
            unitInformation.setPackages(value);
            unitInformationList.add(unitInformation);
        });
        return unitInformationList.toString().replaceAll(",", "");
    }
}
