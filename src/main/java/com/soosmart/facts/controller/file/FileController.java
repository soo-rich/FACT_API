package com.soosmart.facts.controller.file;

// import com.soosmart.facts.service.file.FileMetadataService;
import com.soosmart.facts.service.file.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
// import java.util.UUID;

// import static com.soosmart.facts.utils.FileUtlis.generateUniqueFileName;
// import static com.soosmart.facts.utils.FileUtlis.getFileExtension;

@RestController()
@RequestMapping("file")
@AllArgsConstructor
public class FileController {
    // private FileMetadataService fileMetadataService;
    private FileStorageService fileStorageService;


//     @PostMapping(value = "uploader-file", consumes = "multipart/form-data")
//     public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file) {
// //        return ResponseEntity.status(HttpStatus.OK).body(this.fileMetadataService.save(file,"test", "test"));
//         return ResponseEntity.status(HttpStatus.OK).body(this.fileStorageService.uploadFileToSubFolder(file,
//                 String.format("/%s/%s", "test", generateUniqueFileName("test--"+ UUID.randomUUID().toString(),getFileExtension(file.getOriginalFilename())))));
//     }

    @GetMapping(path = "presigned")
    public ResponseEntity<Map<String, String>> pResponseEntity(@RequestParam("url") String url) {
        Map<String, String> map = new HashMap<>();
        map.put("presigned", this.fileStorageService.generateSignedUrl(url, 10));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
