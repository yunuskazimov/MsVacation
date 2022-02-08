package az.xazar.msvacation.controller;

import az.xazar.msvacation.client.MinioClient;
import az.xazar.msvacation.model.PageDto;
import az.xazar.msvacation.model.VacationDto;
import az.xazar.msvacation.model.VacationGetDto;
import az.xazar.msvacation.service.VacationService;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RestController
@RequestMapping("/api/vacation")
@RequiredArgsConstructor
@CrossOrigin
public class VacationController {

    private final VacationService service;
    private final MinioClient minioClient;

    @PreAuthorize(value = "@permissionService.checkRole(#userId, {'USER'})")
    @PostMapping(consumes = {"multipart/form-data"})
    @ApiOperation(value = "Add Vacation with File")
    public VacationDto create(
            @RequestHeader(name = "User-Id") Long userId,
            @ModelAttribute VacationDto vacationDto) {
        log.info("createWithFile started controller with {}",
                kv("fileDto", vacationDto));
        vacationDto.setUserId(userId);
        return service.create(vacationDto);
    }

    @PreAuthorize(value = "@permissionService.checkRole(#userId, {'ADMIN'})")
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @ApiOperation(value = "Edit Vacation with File")
    public VacationDto edit(@RequestHeader(name = "User-Id") Long userId,
                            @PathVariable Long id,
                            @ModelAttribute VacationDto vacationDto) {
        log.info("editWithFile started controller with {}",
                kv("businessTrip", vacationDto));
        return service.edit(id, vacationDto);
    }

    @PreAuthorize(value = "@permissionService.checkRole(#userId, {'ADMIN'})")
    @PutMapping("/result")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public VacationDto editResult(@RequestHeader(name = "User-Id") Long userId,
                                  @RequestBody ResultForum resultForum) {
        return service.editResult(resultForum.getId(), resultForum.getResult());
    }

    @PreAuthorize(value = "@permissionService.checkRole(#userId, {'USER'})")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@RequestHeader(name = "User-Id") Long userId,
                                             @PathVariable Long id) {
        log.info("deleteById started controller with {}",
                kv("id", id));
        return ResponseEntity.ok().body(service.delete(id));
    }

    @PreAuthorize(value = "@permissionService.checkRole(#userId, {'USER'})")
    @GetMapping()
    public Page<VacationGetDto> getList(@RequestHeader(name = "User-Id") Long userId,
                                        PageDto page) {
        return service.getList(page);
    }

    @PreAuthorize(value = "@permissionService.checkRole(#userId, {'USER'})")
    @GetMapping("/id/{id}")
    public VacationDto getById(@RequestHeader(name = "User-Id") String userId,
                               @PathVariable Long id) {
        return service.getById(id);
    }

    @PreAuthorize(value = "@permissionService.checkRole(#userId, {'USER'})")
    @GetMapping("/uid")
    public Page<VacationGetDto> getByUserId(@RequestHeader(name = "User-Id") Long userId,
                                            PageDto page) {
        return service.getByUserId(userId, page);
    }

    @PreAuthorize(value = "@permissionService.checkRole(#userId, {'ADMIN'})")
    @GetMapping("/file/{id}")
    public String getFileUrlById(@RequestHeader(name = "User-Id") Long userId,
                                 @PathVariable Long id) {
        return service.getFileUrlById(id);
    }

}

@Data
class ResultForum {
    private Long id;
    private String result;
}
