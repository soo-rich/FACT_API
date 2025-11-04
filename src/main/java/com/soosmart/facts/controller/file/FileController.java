package com.soosmart.facts.controller.file;

import com.soosmart.facts.service.file.FileMetadataService;
import com.soosmart.facts.service.file.FileStorageService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("file")
@AllArgsConstructor
public class FileController {
    private FileMetadataService fileMetadataService;
    private FileStorageService fileStorageService;


    @PostMapping(value = "uploader-file", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(this.fileMetadataService.save(file, "bc"));
    }

    @GetMapping(path = "presigned")
    public ResponseEntity<String> pResponseEntity(@RequestParam("url") String url){
        return ResponseEntity.status(HttpStatus.OK).body(this.fileStorageService.generateSignedUrl(url, 10));
    }
}
