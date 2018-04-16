package com.eurodyn.qlack.fuse.fileupload;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileChunkDTO;
import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;
import com.eurodyn.qlack.fuse.fileupload.exception.FileNotCompletedException;
import com.eurodyn.qlack.fuse.fileupload.exception.FileNotFoundException;
import com.eurodyn.qlack.fuse.fileupload.exception.FileUploadException;
import com.eurodyn.qlack.fuse.fileupload.exception.VirusScanException;
import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.model.DBFilePK;
import com.eurodyn.qlack.fuse.fileupload.model.QDBFile;
import com.eurodyn.qlack.fuse.fileupload.request.CheckChunkRequest;
import com.eurodyn.qlack.fuse.fileupload.request.FileUploadRequest;
import com.eurodyn.qlack.fuse.fileupload.request.VirusScanRequest;
import com.eurodyn.qlack.fuse.fileupload.response.CheckChunkResponse;
import com.eurodyn.qlack.fuse.fileupload.response.ChunkGetResponse;
import com.eurodyn.qlack.fuse.fileupload.response.FileDeleteResponse;
import com.eurodyn.qlack.fuse.fileupload.response.FileGetResponse;
import com.eurodyn.qlack.fuse.fileupload.response.FileListResponse;
import com.eurodyn.qlack.fuse.fileupload.response.FileUploadResponse;
import com.eurodyn.qlack.fuse.fileupload.response.VirusScanResponse;
import com.eurodyn.qlack.fuse.fileupload.util.Properties;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.sensesecure.clamav4j.ClamAV;
import io.sensesecure.clamav4j.ClamAVException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Validated
@Transactional
public class FileUploadService {

  // Logger
  private static final Logger LOGGER = Logger.getLogger(FileUploadService.class
      .getName());

  @PersistenceContext
  private EntityManager em;

  // How long to wait for ClamAV to reply before timeout.
  private static final int CLAMAV_SOCKET_TIMEOUT = 10000;

  // Service references.
  private Properties properties;

  @Autowired
  public FileUploadService(Properties properties) {
    this.properties = properties;
  }

  /**
   * Given a file ID it reconstructs the complete file that was uploaded together with it metadata.
   *
   * @param fileID The File ID to reconstruct.
   * @param includeBinary Whether to include the binary content of the file or not.
   */
  private DBFileDTO getByID(String fileID, boolean includeBinary) {
    // Find all chunks of the requested file.
    Query q = em
        .createQuery(
            "select f from DBFile f where f.id.id = :id order by f.id.chunkOrder")
        .setParameter("id", fileID);
    @SuppressWarnings("unchecked")
    List<DBFile> results = q.getResultList();

    // Check if any chunk for the requested file has been found.
    if (results != null && results.size() == 0) {
      throw new FileNotFoundException();
    }

    // Get a random chunk to obtain information for the underlying file
    // (i.e. all chunks contain replicated information about the file from
    // which they were decomposed).
    DBFile randomChunk = results.get(0);

    // Prepare the return value.
    DBFileDTO dto = new DBFileDTO();

    // Check if all expected chunks of this file are available. This check
    // is performed only when the caller has requested the binary
    // representation of the file in order not to return a corrupted file.
    if (includeBinary) {
      long startTime = System.currentTimeMillis();
      if (randomChunk.getExpectedChunks() != results.size()) {
        throw new FileNotCompletedException();
      }
      // Assemble the original file out of its chunks.
      try {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(
            (int) randomChunk.getFileSize());
        for (DBFile f : results) {
          bOut.write(f.getChunkData());
        }
        dto.setFileData(bOut.toByteArray());

      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Could not reassemble file " + fileID,
            e);
        throw new FileUploadException("Could not reassemble file "
            + fileID);
      }
      dto.setReassemblyTime(System.currentTimeMillis() - startTime);
    } else {
      dto.setReassemblyTime(-1);
    }

    // Further compose the return value.
    dto.setFilename(randomChunk.getFileName());
    dto.setId(fileID);
    dto.setReceivedChunks(results.size());
    dto.setTotalChunks(randomChunk.getExpectedChunks());
    dto.setUploadedAt(randomChunk.getUploadedAt());
    dto.setUploadedBy(randomChunk.getUploadedBy());
    dto.setTotalSize(randomChunk.getFileSize());

    return dto;
  }

  public FileGetResponse getByIDForConsole(String fileID) {
    return new FileGetResponse(getByID(fileID, true));
  }

  public ChunkGetResponse getByIDAndChunk(String fileID, long chunkIndex) {
    Query q = em
        .createQuery(
            "select f from DBFile f "
                + "where "
                + "f.id.id = :id and f.id.chunkOrder in :chunkIndexes "
                + "order by f.id.chunkOrder")
        .setParameter("id", fileID)
        .setParameter("chunkIndexes", Arrays.asList(new Long[]{chunkIndex, chunkIndex + 1}));

    @SuppressWarnings("unchecked")
    List<DBFile> results = q.getResultList();
    // Check if any chunk for the requested file has been found.
    if (results != null && results.size() == 0) {
      throw new FileNotFoundException();
    }

    // Get the respective to the chunkIndex chunk  to obtain information for the
    // underlying file, from which the chunk has been decomposed
    DBFile currentChunk = results.get(0);

    // Prepare the return value.
    DBFileChunkDTO dto = new DBFileChunkDTO();

    // Retrieve the file info from the specific
    dto.setFilename(currentChunk.getFileName());
    dto.setId(fileID);
    dto.setTotalChunks(currentChunk.getExpectedChunks());
    dto.setUploadedAt(currentChunk.getUploadedAt());
    dto.setUploadedBy(currentChunk.getUploadedBy());
    dto.setTotalSize(currentChunk.getFileSize());
    dto.setBinContent(currentChunk.getChunkData());

    if (results.size() > 0) {
      dto.setChunkIndex(results.get(0).getId().getChunkOrder());
      dto.setHasMoreChunks(results.size() == 2);
    }

    return new ChunkGetResponse(dto);
  }

  public CheckChunkResponse checkChunk(CheckChunkRequest req) {
    CheckChunkResponse res = new CheckChunkResponse();
    res.setChunkExists(DBFile.getChunk(req.getFileAlias(),
        req.getChunkNumber(), em) != null);

    return res;
  }

  public FileUploadResponse upload(FileUploadRequest req) {
    FileUploadResponse res = new FileUploadResponse();

    // Check if this chunk has already been uploaded, so that we can support
    // updating existing chunks.
    DBFile file = DBFile.getChunk(req.getAlias(), req.getChunkNumber(), em);
    if (file == null) {
      file = new DBFile(
          new DBFilePK(req.getAlias(), req.getChunkNumber()));
      res.setChunkExists(false);
    } else {
      res.setChunkExists(true);
    }
    file.setExpectedChunks(req.getTotalChunks());
    file.setFileName(req.getFilename());
    if (req.getTotalSize() == 0) {
      file.setFileSize(req.getData().length);
    } else {
      file.setFileSize(req.getTotalSize());
    }
    file.setUploadedAt(System.currentTimeMillis());
    file.setUploadedBy(req.getUploadedBy());
    file.setChunkData(req.getData());
    file.setChunkSize(req.getData().length);

    em.persist(file);

    return res;
  }

  public FileDeleteResponse deleteByID(String fileID) {
    return new FileDeleteResponse(DBFile.delete(fileID, em));
  }

  public FileDeleteResponse deleteByIDForConsole(String fileID) {
    return new FileDeleteResponse(DBFile.delete(fileID, em));
  }

  public FileGetResponse getByID(String fileID) {
    return new FileGetResponse(getByID(fileID, true));
  }

  public FileListResponse listFiles(boolean includeBinaryContent) {
    List<DBFileDTO> retVal = new ArrayList<>();

    // First find all unique IDs for file chunks.
    Query q = em
        .createQuery("select distinct f.id.id from DBFile f order by f.uploadedAt");
    @SuppressWarnings("unchecked")
    List<String> chunks = q.getResultList();
    for (String id : chunks) {
      retVal.add(getByID(id, includeBinaryContent));
    }

    return new FileListResponse(retVal);
  }

  public VirusScanResponse virusScan(VirusScanRequest req) {
    // Check if a custom address for ClamAV has been provided or use the
    // default.
    InetSocketAddress clamAVAddress;
    if (StringUtils.isBlank(req.getClamAVHost())) {
      clamAVAddress = new InetSocketAddress(properties.getClamAV().substring(0,
          properties.getClamAV().indexOf(":")),
          Integer.parseInt(properties.getClamAV().substring(properties.getClamAV()
              .indexOf(":") + 1)));
    } else {
      clamAVAddress = new InetSocketAddress(req.getClamAVHost(), req.getClamAVPort());
    }
    LOGGER.log(Level.FINE, "Contacting ClamAV at: {0}.", clamAVAddress);
    ClamAV clamAV = new ClamAV(clamAVAddress, CLAMAV_SOCKET_TIMEOUT);
    DBFileDTO fileDTO = getByID(req.getId(), true);
    String scanResult = null;
    try {
      scanResult = clamAV.scan(new ByteArrayInputStream(fileDTO.getFileData()));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check file for virus, file ID=" + req.getId(), e);
      throw new VirusScanException("Could not check file for virus, file ID=" + req.getId());
    } catch (ClamAVException e) {
      LOGGER.log(Level.SEVERE, "Could not check file for virus, file ID=" + req.getId(), e);
      throw new VirusScanException("Could not check file for virus, file ID=" + req.getId());
    }

    VirusScanResponse res = new VirusScanResponse();
    res.setId(req.getId());
    res.setVirusFree(scanResult.equals("OK") ? true : false);
    res.setVirusScanDescription(scanResult);

    return res;
  }

  public void cleanupExpired(long deleteBefore) {
    QDBFile qFile = QDBFile.dBFile;
    new JPAQueryFactory(em).delete(qFile)
        .where(qFile.uploadedAt.lt(deleteBefore)).execute();
  }

  public FileListResponse listFilesForConsole(boolean includeBinary) {
    List<DBFileDTO> retVal = new ArrayList<>();

    // First find all unique IDs for file chunks.
    Query q = em
        .createQuery("select distinct f.id.id from DBFile f order by f.uploadedAt");
    @SuppressWarnings("unchecked")
    List<String> chunks = q.getResultList();
    for (String id : chunks) {
      retVal.add(getByID(id, includeBinary));
    }

    return new FileListResponse(retVal);
  }

}
