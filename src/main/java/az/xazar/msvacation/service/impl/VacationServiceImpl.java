package az.xazar.msvacation.service.impl;

import az.xazar.msvacation.client.MinioClient;
import az.xazar.msvacation.client.UserClientRest;
import az.xazar.msvacation.dao.entity.VacationEntity;
import az.xazar.msvacation.dao.repository.VacationRepo;
import az.xazar.msvacation.exception.FileNotFoundException;
import az.xazar.msvacation.exception.VacationNotFoundException;
import az.xazar.msvacation.mapper.VacationMapper;
import az.xazar.msvacation.model.*;
import az.xazar.msvacation.model.client.user.UserDto;
import az.xazar.msvacation.service.VacationService;
import az.xazar.msvacation.util.VacationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final UserClientRest userClient;
    private final VacationRepo vacationRepo;
    private final VacationUtil vacationUtil;
    private final VacationMapper vacationMapper;
    private final MinioClient minioClient;


    @Override
    public VacationDto create(VacationDto vacationDto) {
        log.info("create service started with {}", kv("vacationDto", vacationDto));
        userClient.getById(vacationDto.getUserId());
        uploadFile(vacationDto);
        vacationDto.setResult(Result.PENDING);
        vacationDto.setDeleted(false);
        VacationEntity entity =
                vacationRepo.save(vacationMapper.toVacationEntity(vacationDto));
        return vacationMapper.toVacationDto(entity);
    }

    @Override
    public VacationDto edit(Long id, VacationDto vacationDto) {
        log.info("edit service started with {}", kv("vacation", vacationDto));
        userClient.getById(vacationDto.getUserId());
        VacationEntity entity = vacationUtil.findVacationById(id);
        checkIsDeleted(entity);
        //checkResult(vacationDto, entity);
        vacationDto.setResult(entity.getResult());
        updateFile(vacationDto, entity);
        vacationDto.setUserId(entity.getUserId());
        log.info("edit With File completed Service with {}", kv("vacation", vacationDto));

        return vacationMapper.toVacationDto(
                vacationRepo.save(vacationMapper.toVacationEntity(vacationDto)));
    }

    @Override
    public VacationDto editResult(Long id, String result) {
        log.info("service editResult started with id: {}, Result: {}", id, result);
        VacationDto vacationDto = getById(id);
        userClient.getById(vacationDto.getUserId());
        checkResult(result, vacationDto);
        vacationRepo.save(vacationMapper.toVacationEntity(vacationDto));
        log.info("service editResult completed with id: {}, Result: {}", id, result);
        return vacationDto;
    }

    @Override
    public String delete(Long id) {
        log.info("delete service already started. {}", kv("ID ", id));
        VacationEntity entity = vacationUtil.findVacationById(id);
        userClient.getById(entity.getUserId());
        if (!entity.isDeleted()) {
            entity.setDeleted(true);
            deleteFileWithCheck(entity);
            vacationRepo.save(entity);
            log.info("delete completed. {}", kv("Vacation ID", entity.getId()));
            return "Vacation Deleted.";
        }
        log.info("delete already completed. {}", kv("Deleted Time", entity.getUpdatedAt()));
        throw new VacationNotFoundException(
                "Vacation Already Deleted. ID: " + entity.getId()
                        + ", Delete Time: " + entity.getUpdatedAt());
    }

    @Override
    public VacationDto getById(Long id) {
        log.info("getById service already started. {}", kv("ID ", id));
        VacationEntity vacationEntity = vacationUtil.findVacationById(id);
        userClient.getById(vacationEntity.getUserId());
        return vacationMapper.toVacationDto(vacationEntity);

    }

    @Override
    public String getFileUrlById(Long id) {
        log.info("getFileUrlById service already started. {}", kv("ID ", id));
        VacationEntity vacationEntity = vacationUtil.findVacationById(id);
        userClient.getById(vacationEntity.getUserId());
        if (vacationEntity.getFileName() != null) {
            return minioClient.getFromMinio(vacationEntity.getFileName());
        }
        return null;
    }


    @Override
    public Page<VacationGetDto> getList(PageDto page) {
        log.info("getList service already started.");
        Page<VacationGetDto> getDto = vacationRepo.findByDeletedFalse(getPageable(page))
                .map(entity -> {
                    UserDto user = userClient.getById(entity.getUserId());
                    VacationGetDto dto = vacationMapper.toVacationGetDto(entity);
                    dto.setUserName(user.getName() + " " + user.getSurname());
                    return dto;
                });
        if (!getDto.isEmpty()) {
            log.info("getList service completed ");
            return getDto;
        } else {
            log.info("getList service run exception ");
            throw new VacationNotFoundException("Vacation Not Found");
        }
    }

    @Override
    public Page<VacationGetDto> getByUserId(Long userid, PageDto page) {
        log.info("getByUserId service already started. {}", kv("User ID ", userid));
        UserDto user = userClient.getById(userid);
        Page<VacationGetDto> getDto = vacationUtil
                .findVacationListByUserId(userid, getPageable(page))
                .map(entity -> {
                    VacationGetDto dto = vacationMapper.toVacationGetDto(entity);
                    dto.setUserName(user.getName() + " " + user.getSurname());
                    return dto;
                });
        if (!getDto.isEmpty()) {
            log.info("getByUserId service completed ");
            return getDto;
        } else {
            log.info("getByUserId service run exception ");
            throw new VacationNotFoundException("Vacation Not Found");
        }
    }

    private void deleteFileWithCheck(VacationEntity entity) {
        if (entity.getFileId() != null) {
            minioClient.deleteToMinio(entity.getFileId());
        }
    }

    private Pageable getPageable(PageDto page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
    }

    private void uploadFile(VacationDto vacationDto) {
        log.info("uploadFile service started Service with {}", kv("vacationDto", vacationDto));
        if (!vacationDto.getFile().isEmpty() && vacationDto.getFile() != null) {
            FileDto fileDto = minioClient.postToMinio(FileDto.builder()
                    .file(vacationDto.getFile())
                    .userId(vacationDto.getUserId())
                    .type("Vacation")                                     //TODO make that ENUM
                    .build()).getBody();
            assert fileDto != null;                                         //TODO bunu maraqlan
            vacationDto.setFileId(fileDto.getFileId());
            vacationDto.setFileName(fileDto.getFileName());
        }
    }

    private void updateFile(VacationDto vacationDto, VacationEntity entity) {
        log.info("updateFile service started Service with {}", kv("vacationDto", vacationDto));
        if (!vacationDto.getFile().isEmpty()) {
            FileDto fileDto = minioClient.putToMinio(FileDto.builder()
                    .fileId(entity.getFileId())
                    .file(vacationDto.getFile())
                    .userId(entity.getUserId())
                    .type("Vacation")                                     //TODO make that ENUM
                    .build()).getBody();

            vacationDto.setFileId(fileDto != null ? fileDto.getFileId() : entity.getFileId());
            vacationDto
                    .setFileName(fileDto != null ? fileDto.getFileName() : entity.getFileName());
        } else {
            vacationDto.setFileId(entity.getFileId());
            vacationDto.setFileName(entity.getFileName());
        }
    }

    private void checkIsDeleted(VacationEntity entity) {
        if (entity.isDeleted()) {
            throw new FileNotFoundException("File Already Deleted. ID:"
                    + entity.getId() + ", Delete Time: " + entity.getUpdatedAt());
        }
    }

//    private void checkResult(VacationDto vacationDto, VacationEntity entity) {
//        if (!entity.getResult().equals(vacationDto.getResult()) && !vacationDto.getResult().name().isEmpty()) {
//            editResult(vacationDto.getId(), entity.getResult().name());
//        } else if (vacationDto.getResult().name().isEmpty()) {
//            vacationDto.setResult(entity.getResult());
//        }
//    }

    private void checkResult(String result, VacationDto dto) {
        if (result.equalsIgnoreCase(Result.APPROVED.toLower())
                && dto.getResult().equals(Result.PENDING)) {
            dto.setResult(Result.APPROVED);
        } else if (result.equalsIgnoreCase(Result.COMPLETED.toLower())
                && dto.getResult().equals(Result.APPROVED)) {
            dto.setResult(Result.COMPLETED);
        } else if (result.equalsIgnoreCase(Result.DECLINED.toLower())) {
            dto.setResult(Result.DECLINED);
        } else if (dto.getResult().equals(Result.valueOf(result))) {
            log.info("Result is same as DB");
        } else {
            throw new RuntimeException("Wrong Result Name");
        }
    }
}
