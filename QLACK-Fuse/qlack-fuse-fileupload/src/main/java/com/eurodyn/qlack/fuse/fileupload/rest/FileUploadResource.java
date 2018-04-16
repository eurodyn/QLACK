package com.eurodyn.qlack.fuse.fileupload.rest;

import com.eurodyn.qlack.fuse.fileupload.FileUploadService;
import com.eurodyn.qlack.fuse.fileupload.request.CheckChunkRequest;
import com.eurodyn.qlack.fuse.fileupload.response.CheckChunkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Logger;

@RestController
@RequestMapping("/file-upload")
@Validated
public class FileUploadResource {

  // Logger.
  private static final Logger LOGGER = Logger.getLogger(FileUploadResource.class.getName());

  // Service references.
  private FileUploadService fileUploadService;

  @Autowired
  public FileUploadResource(
      FileUploadService fileUploadService) {
    this.fileUploadService = fileUploadService;
  }


//  private byte[] getBin(String fieldName, MultipartBody body)
//      throws IOException {
//    Attachment attachment = body.getAttachment(fieldName);
//    if (attachment != null) {
//      return IOUtils.readBytesFromStream(attachment.getDataHandler().getInputStream());
//    } else {
//      return null;
//    }
//  }
//
//  private String getString(String fieldName, MultipartBody body)
//      throws IOException {
//    Attachment attachment = body.getAttachment(fieldName);
//    if (attachment != null) {
//      return IOUtils.toString(attachment.getDataHandler()
//          .getInputStream());
//    } else {
//      return null;
//    }
//  }
//
//  private Long getLong(String fieldName, MultipartBody body)
//      throws IOException {
//    Attachment attachment = body.getAttachment(fieldName);
//    if (attachment != null) {
//      return Long.valueOf(IOUtils.toString(attachment.getDataHandler().getInputStream()));
//    } else {
//      return null;
//    }
//  }

  @RequestMapping(value = "/upload", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity checkChunk(
      @RequestParam("flowChunkNumber") long chunkNumber,
      @RequestParam("flowCurrentChunkSize") long chunkSize,
      @RequestParam("flowTotalSize") long totalSize,
      @RequestParam("flowIdentifier") String alias,
      @RequestParam("flowFilename") String filename,
      @RequestParam("flowTotalChunks") long totalChunks) {
    CheckChunkRequest req = new CheckChunkRequest();
    req.setChunkNumber(chunkNumber);
    req.setFileAlias(alias);
    CheckChunkResponse res = fileUploadService.checkChunk(req);

    if (!res.isChunkExists()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.ok().build();
    }
  }

  @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE, consumes = {
      "multipart/form-data"})
//  @ResponseBody
  public ResponseEntity upload(@RequestParam("file") MultipartFile file, @RequestParam("flowIdentifier") String flowIdentifier,
      @RequestParam("flowChunkNumber") String flowChunkNumber) {
    System.out.println("file got");
    System.out.println(file.getName());
    System.out.println(flowIdentifier);

    return ResponseEntity.ok().build();
  }

//  @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE, consumes = {
//      "multipart/form-data"})
////  @ResponseBody
//  public String upload(MultipartBody body) {
//    String retVal = "";
//
//    try {
//      FileUploadRequest fur = new FileUploadRequest();
//      fur.setAlias(getString("flowIdentifier", body));
//      fur.setAutoDelete(true);
//      if (body.getAttachment("flowChunkNumber") != null) {
//        fur.setChunkNumber(getLong("flowChunkNumber", body).longValue());
//      } else {
//        fur.setChunkNumber(1); // Support for older browsers, where there is always one chunk.
//      }
//      if (body.getAttachment("flowChunkSize") != null) {
//        fur.setChunkSize(getLong("flowChunkSize", body).longValue());
//      }
//      fur.setFilename(getString("flowFilename", body));
//      if (body.getAttachment("flowTotalChunks") != null) {
//        fur.setTotalChunks(getLong("flowTotalChunks", body).longValue());
//      } else {
//        fur.setTotalChunks(1); // Support for older browsers, where there is always one chunk.
//      }
//      if (body.getAttachment("flowTotalSize") != null) {
//        fur.setTotalSize(getLong("flowTotalSize", body).longValue());
//      }
//
//      fur.setData(getBin("file", body));
//
//      qffiFileUploadService.upload(fur);
//    } catch (IOException e) {
//      LOGGER.log(Level.SEVERE, "Could not process file upload.", e);
//      retVal = "ERROR";
//    }
//
//    return retVal;
//  }

}
