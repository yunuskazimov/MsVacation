package az.xazar.msvacation.service;

import az.xazar.msvacation.client.MinioClient;
import az.xazar.msvacation.entity.VacationEntity;
import az.xazar.msvacation.exception.FileNotFoundException;
import az.xazar.msvacation.exception.VacationNotFoundException;
import az.xazar.msvacation.mapper.VacationMapper;
import az.xazar.msvacation.model.FileDto;
import az.xazar.msvacation.model.VacationDto;
import az.xazar.msvacation.repository.VacationRepo;
import az.xazar.msvacation.util.VacationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
public class VacationServiceImpl implements VacationService {

    private final VacationRepo vacationRepo;
    private final VacationUtil vacationUtil;
    private final VacationMapper vacationMapper;
    private final MinioClient minioClient;

    public VacationServiceImpl(VacationRepo vacationRepo,
                               VacationUtil vacationUtil,
                               VacationMapper vacationMapper,
                               MinioClient minioClient) {
        this.vacationRepo = vacationRepo;
        this.vacationUtil = vacationUtil;
        this.vacationMapper = vacationMapper;
        this.minioClient = minioClient;
    }

    @Override
    public VacationDto create(VacationDto vacationDto) {

        uploadFile(vacationDto);

        VacationEntity entity =
                vacationRepo.save(vacationMapper.toVacationEntity(vacationDto));
        vacationDto.setId(entity.getId());

        return vacationDto;
    }

    private void uploadFile(VacationDto vacationDto) {
        if (vacationDto.getFile() != null) {
            FileDto fileDto = minioClient.postToMinio(FileDto.builder()
                    .file(vacationDto.getFile())
                    .userId(vacationDto.getUserId())
                    .type("Vacation")                                     //TODO make that ENUM
                    .build()).getBody();
            vacationDto.setFileId(fileDto.getFileId());
            vacationDto.setFileName(fileDto.getFileName());
        }
    }

    @Override
    public VacationDto edit(Long id, VacationDto vacationDto) {

        log.info("edit With File started Service with {}", kv("vacation", vacationDto));

        VacationEntity entity = vacationUtil.findVacationById(id);
        checkIsDeleted(entity);

        updateFile(vacationDto, entity);

        vacationDto.setUserId(entity.getUserId());
        log.info("edit With File completed Service with {}", kv("vacation", vacationDto));

        return vacationMapper.toVacationDto(
                vacationRepo.save(vacationMapper.toVacationEntity(vacationDto)));
    }

    private void updateFile(VacationDto vacationDto, VacationEntity entity) {
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

    @Override
    public String delete(Long id) {
        VacationEntity entity = vacationUtil.findVacationById(id);
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

    private void deleteFileWithCheck(VacationEntity entity) {
        if (entity.getFileId() != null) {
            minioClient.deleteToMinio(entity.getFileId());
        }
    }

    @Override
    public VacationDto getById(Long id) {

        VacationEntity vacationEntity = vacationUtil.findVacationById(id);

        return vacationMapper.toVacationDto(vacationEntity);

    }

    @Override
    public String getFileUrlById(Long id) {
        VacationEntity vacationEntity = vacationUtil.findVacationById(id);
        if (vacationEntity.getFileName() != null) {
            return minioClient.getFromMinio(vacationEntity.getFileName());
        }
        return null;
    }


    @Override
    public List<VacationDto> getList() {

        return vacationMapper.toVacationDtoList(vacationRepo.findAll());
    }

    @Override
    public List<VacationDto> getByUserId(Long userid) {
        vacationUtil.findVacationByUserId(userid);
        return vacationMapper
                .toVacationDtoList(vacationUtil.findVacationListByUserId(userid));
    }
}
