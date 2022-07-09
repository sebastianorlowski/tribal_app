package core.dataloader.api;

import core.dataloader.persistence.repository.UnitRepository;
import core.dataloader.service.DataLoaderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@RestController
@RequestMapping("/data")
@Api(tags = "Data Loader")
public class DataLoaderController {

    private final DataLoaderService dataLoaderService;
    private final UnitRepository unitRepository;

    @Autowired
    public DataLoaderController(DataLoaderService dataLoaderService, UnitRepository unitRepository) {
        this.dataLoaderService = dataLoaderService;
        this.unitRepository = unitRepository;
    }

    @PostMapping("/unit")
    private ResponseEntity<?> uploadUnitInfo(@RequestBody String data) {
        dataLoaderService.loadUnitData(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/info")
    private ResponseEntity<?> uploadTribalWarsData() {
        dataLoaderService.uploadTribalWarsData();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove")
    private ResponseEntity<?> removeAllData() {
        dataLoaderService.removeTribalWarsData();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/info/ally")
    private ResponseEntity<?> uploadAllyData() {
        dataLoaderService.uploadAllyData();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove/unit")
    private ResponseEntity<?> removeDataAboutUnit() {
        unitRepository.deleteAllInBatch();
        return ResponseEntity.noContent().build();
    }
}
