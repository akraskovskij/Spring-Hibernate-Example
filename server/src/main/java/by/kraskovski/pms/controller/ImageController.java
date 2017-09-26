package by.kraskovski.pms.controller;

import by.kraskovski.pms.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for {@link ImageService}.
 */
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Uploading image to the system.
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity uploadImage(@RequestParam("file") final MultipartFile uploadedFile) {
        log.info("uploading image: \"" + uploadedFile.getOriginalFilename() + "\"");
        final String image = imageService.upload(uploadedFile);
        return new ResponseEntity<>(image, HttpStatus.CREATED);
    }

    /**
     * Get image from the system.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity loadImageAsResource(@PathVariable final String id) {
        log.info("loading image with id: \"{}\"", id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageService.load(id), headers, HttpStatus.OK);
    }

    /**
     * Delete image from system.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable final String id) {
        log.info("deleting image with id: \"" + id + "\"");
        imageService.delete(id);
    }
}
