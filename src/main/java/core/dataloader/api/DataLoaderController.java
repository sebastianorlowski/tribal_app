package core.dataloader.api;

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

    @Autowired
    public DataLoaderController(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    @PostMapping("/unit")
    private ResponseEntity<?> uploadUnitInfo() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    private ResponseEntity<?> uploadTribalWarsData() {
        dataLoaderService.uploadTribalWarsData();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove")
    private ResponseEntity<?> removeAllData() {
        dataLoaderService.removeTribalWarsData();
        return ResponseEntity.noContent().build();
    }
}
