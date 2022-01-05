package az.xazar.msvacation.client;

import az.xazar.msvacation.exception.FileNotFoundException;
import az.xazar.msvacation.model.FileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
public class MinioClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public MinioClient(RestTemplate restTemplate
            , @Value("${client.minio.int.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public ResponseEntity<FileDto> postToMinio(FileDto fileDto) {
        log.info("postToMinio started with {}", kv("fileDto", fileDto));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, String> pdfHeaderMap = new LinkedMultiValueMap<>();
        pdfHeaderMap.add("Content-disposition", "form-data; name=file; filename=" + fileDto.getFile().getOriginalFilename());
        pdfHeaderMap.add("Content-type", "application/pdf");
        pdfHeaderMap.add("Content-disposition", "form-data; name=userId; filename=" + fileDto.getUserId());
        pdfHeaderMap.add("Content-type", "Long");
        pdfHeaderMap.add("Content-disposition", "form-data; name=type; filename=" + fileDto.getType());
        pdfHeaderMap.add("Content-type", "String");

        HttpEntity<byte[]> file = null;
        HttpEntity<Long> userId = null;
        HttpEntity<String> type = null;

        try {
            file = new HttpEntity<byte[]>(fileDto.getFile().getBytes(), pdfHeaderMap);
            userId = new HttpEntity<Long>(fileDto.getUserId());
            type = new HttpEntity<String>(fileDto.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinkedMultiValueMap<String, Object> multipartReqMap = new LinkedMultiValueMap<>();
        multipartReqMap.add("file", file);
        multipartReqMap.add("userId", userId);
        multipartReqMap.add("type", type);

        HttpEntity<LinkedMultiValueMap<String, Object>> reqEntity = new HttpEntity<>(multipartReqMap, headers);
        ResponseEntity<FileDto> rest = restTemplate.exchange(apiUrl, HttpMethod.POST, reqEntity, FileDto.class);
        log.info("postToMinio completed with {}", kv("rest ", rest));
        return rest;
    }

    public ResponseEntity<FileDto> putToMinio(FileDto fileDto) {
        log.info("putToMinio started with {}", kv("fileDto", fileDto));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, String> pdfHeaderMap = new LinkedMultiValueMap<>();
        pdfHeaderMap.add("Content-disposition", "form-data; name=file; filename=" + fileDto.getFile().getOriginalFilename());
        pdfHeaderMap.add("Content-type", "application/pdf");
        pdfHeaderMap.add("Content-disposition", "form-data; name=userId; filename=" + fileDto.getUserId());
        pdfHeaderMap.add("Content-type", "Long");
        pdfHeaderMap.add("Content-disposition", "form-data; name=type; filename=" + fileDto.getType());
        pdfHeaderMap.add("Content-type", "String");
        pdfHeaderMap.add("Content-disposition", "form-data; name=fileId; filename=" + fileDto.getFileId());
        pdfHeaderMap.add("Content-type", "Long");

        HttpEntity<byte[]> file = null;
        HttpEntity<Long> userId = null;
        HttpEntity<String> type = null;
        HttpEntity<Long> fileId = null;

        try {
            file = new HttpEntity<byte[]>(fileDto.getFile().getBytes(), pdfHeaderMap);
            userId = new HttpEntity<Long>(fileDto.getUserId());
            type = new HttpEntity<String>(fileDto.getType());
            fileId = new HttpEntity<Long>(fileDto.getFileId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinkedMultiValueMap<String, Object> multipartReqMap = new LinkedMultiValueMap<>();
        multipartReqMap.add("file", file);
        multipartReqMap.add("userId", userId);
        multipartReqMap.add("type", type);
        multipartReqMap.add("fileId", fileId);

        HttpEntity<LinkedMultiValueMap<String, Object>> reqEntity = new HttpEntity<>(multipartReqMap, headers);

        ResponseEntity<FileDto> rest = restTemplate.exchange(apiUrl, HttpMethod.PUT, reqEntity, FileDto.class);
        log.info("putToMinio completed with {}", kv("rest ", rest));
        return rest;
    }

    public void deleteToMinio(Long fileId) {
        log.info("putToMinio started with {}", kv("fileId", fileId));
        restTemplate.delete(apiUrl + "/" + fileId);
        log.info("putToMinio completed with {}", kv("fileId", fileId));

    }

    public String getFromMinio(String fileName) {
        log.info("getFromMinio started with {}", kv("fileName", fileName));

        try {
            log.info("getFromMinio completed with {}", kv("fileName", fileName));

            return restTemplate.getForObject(apiUrl + "/url/" + fileName, String.class);
        } catch (FileNotFoundException e) {
            log.info("getFromMinio exception with {}", kv("fileName", fileName));
            throw new FileNotFoundException(e.getMessage());
        }



    }

}

