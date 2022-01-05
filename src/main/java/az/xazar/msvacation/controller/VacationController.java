package az.xazar.msvacation.controller;

import az.xazar.msvacation.client.MinioClient;
import az.xazar.msvacation.model.VacationDto;
import az.xazar.msvacation.service.VacationService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RestController
@RequestMapping("/vacation")
public class VacationController {

    private final VacationService service;
    private final MinioClient minioClient;

    public VacationController(VacationService service, MinioClient minioClient) {
        this.service = service;
        this.minioClient = minioClient;
    }

    @PostMapping(value = "/file", consumes = {"multipart/form-data"})
    @ApiOperation(value = "Add User File to MsMinio")
    public VacationDto createWithFile(
            @ModelAttribute VacationDto vacationDto) {
        log.info("create With File started controller with {}",
                kv("fileDto", vacationDto));
        return service.create(vacationDto);
    }

    @PutMapping(value = "/file/{id}", consumes = {"multipart/form-data"})
    @ApiOperation(value = "Edit User File to MsMinio")
    public VacationDto editWithFile(@PathVariable Long id,
                                        @ModelAttribute VacationDto vacationDto) {
        log.info("edit With File started controller with {}",
                kv("vacation", vacationDto));
        return service.edit(id, vacationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.delete(id));
    }

    @GetMapping()
    public List<VacationDto> getVacationList() {
        return service.getList();
    }

    @GetMapping("id/{id}")
    public VacationDto getVacationById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("file/{id}")
    public String getFileUrlById(@PathVariable Long id) {
        return service.getFileUrlById(id);
    }

    @GetMapping("/uid/{uId}")
    public List<VacationDto> getByUserId(@PathVariable Long uId) {
        return service.getByUserId(uId);
    }

}
