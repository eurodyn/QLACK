package com.eurodyn.qlack.fuse.fileupload;

import com.eurodyn.qlack.fuse.fileupload.dto.QFFIDBFileChunkDTO;
import com.eurodyn.qlack.fuse.fileupload.dto.QFFIDBFileDTO;
import com.eurodyn.qlack.fuse.fileupload.exception.QFFIFileNotCompletedException;
import com.eurodyn.qlack.fuse.fileupload.exception.QFFIFileNotFoundException;
import com.eurodyn.qlack.fuse.fileupload.exception.QFFIFileUploadException;
import com.eurodyn.qlack.fuse.fileupload.exception.QFFIVirusScanException;
import com.eurodyn.qlack.fuse.fileupload.model.QFFIDBFile;
import com.eurodyn.qlack.fuse.fileupload.model.QFFIDBFilePK;
import com.eurodyn.qlack.fuse.fileupload.model.QQFFIDBFile;
import com.eurodyn.qlack.fuse.fileupload.request.QFFICheckChunkRequest;
import com.eurodyn.qlack.fuse.fileupload.request.QFFIFileUploadRequest;
import com.eurodyn.qlack.fuse.fileupload.request.QFFIVirusScanRequest;
import com.eurodyn.qlack.fuse.fileupload.response.QFFICheckChunkResponse;
import com.eurodyn.qlack.fuse.fileupload.response.QFFIChunkGetResponse;
import com.eurodyn.qlack.fuse.fileupload.response.QFFIFileDeleteResponse;
import com.eurodyn.qlack.fuse.fileupload.response.QFFIFileGetResponse;
import com.eurodyn.qlack.fuse.fileupload.response.QFFIFileListResponse;
import com.eurodyn.qlack.fuse.fileupload.response.QFFIFileUploadResponse;
import com.eurodyn.qlack.fuse.fileupload.response.QFFIVirusScanResponse;
import com.eurodyn.qlack.fuse.fileupload.util.QFFIProperties;
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
public class QFFIFileUploadService {

  // Logger
  private static final Logger LOGGER = Logger.getLogger(QFFIFileUploadService.class
      .getName());

  @PersistenceContext
  private EntityManager em;

  // How long to wait for ClamAV to reply before timeout.
  private static final int CLAMAV_SOCKET_TIMEOUT = 10000;

  // Service references.
  private QFFIProperties qffiProperties;

  @Autowired
  public QFFIFileUploadService(QFFIProperties qffiProperties) {
    this.qffiProperties = qffiProperties;
  }

  /**
   * Given a file ID it reconstructs the complete file that was uploaded together with it metadata.
   *
   * @param fileID The File ID to reconstruct.
   * @param includeBinary Whether to include the binary content of the file or not.
   */
  private QFFIDBFileDTO getByID(String fileID, boolean includeBinary) {
    // Find all chunks of the requested file.
    Query q = em
        .createQuery(
            "select f from QFFIDBFile f where f.id.id = :id order by f.id.chunkOrder")
        .setParameter("id", fileID);
    @SuppressWarnings("unchecked")
    List<QFFIDBFile> results = q.getResultList();

    // Check if any chunk for the requested file has been found.
    if (results != null && results.size() == 0) {
      throw new QFFIFileNotFoundException();
    }

    // Get a random chunk to obtain information for the underlying file
    // (i.e. all chunks contain replicated information about the file from
    // which they were decomposed).
    QFFIDBFile randomChunk = results.get(0);

    // Prepare the return value.
    QFFIDBFileDTO dto = new QFFIDBFileDTO();

    // Check if all expected chunks of this file are available. This check
    // is performed only when the caller has requested the binary
    // representation of the file in order not to return a corrupted file.
    if (includeBinary) {
      long startTime = System.currentTimeMillis();
      if (randomChunk.getExpectedChunks() != results.size()) {
        throw new QFFIFileNotCompletedException();
      }
      // Assemble the original file out of its chunks.
      try {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(
            (int) randomChunk.getFileSize());
        for (QFFIDBFile f : results) {
          bOut.write(f.getChunkData());
        }
        dto.setFileData(bOut.toByteArray());

      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Could not reassemble file " + fileID,
            e);
        throw new QFFIFileUploadException("Could not reassemble file "
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

  public QFFIFileGetResponse getByIDForConsole(String fileID) {
    return new QFFIFileGetResponse(getByID(fileID, true));
  }

  public QFFIChunkGetResponse getByIDAndChunk(String fileID, long chunkIndex) {
    Query q = em
        .createQuery(
            "select f from QFFIDBFile f "
                + "where "
                + "f.id.id = :id and f.id.chunkOrder in :chunkIndexes "
                + "order by f.id.chunkOrder")
        .setParameter("id", fileID)
        .setParameter("chunkIndexes", Arrays.asList(new Long[]{chunkIndex, chunkIndex + 1}));

    @SuppressWarnings("unchecked")
    List<QFFIDBFile> results = q.getResultList();
    // Check if any chunk for the requested file has been found.
    if (results != null && results.size() == 0) {
      throw new QFFIFileNotFoundException();
    }

    // Get the respective to the chunkIndex chunk  to obtain information for the
    // underlying file, from which the chunk has been decomposed
    QFFIDBFile currentChunk = results.get(0);

    // Prepare the return value.
    QFFIDBFileChunkDTO dto = new QFFIDBFileChunkDTO();

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

    return new QFFIChunkGetResponse(dto);
  }

  public QFFICheckChunkResponse checkChunk(QFFICheckChunkRequest req) {
    QFFICheckChunkResponse res = new QFFICheckChunkResponse();
    res.setChunkExists(QFFIDBFile.getChunk(req.getFileAlias(),
        req.getChunkNumber(), em) != null);

    return res;
  }

  public QFFIFileUploadResponse upload(QFFIFileUploadRequest req) {
    QFFIFileUploadResponse res = new QFFIFileUploadResponse();

    // Check if this chunk has already been uploaded, so that we can support
    // updating existing chunks.
    QFFIDBFile file = QFFIDBFile.getChunk(req.getAlias(), req.getChunkNumber(), em);
    if (file == null) {
      file = new QFFIDBFile(
          new QFFIDBFilePK(req.getAlias(), req.getChunkNumber()));
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

  public QFFIFileDeleteResponse deleteByID(String fileID) {
    return new QFFIFileDeleteResponse(QFFIDBFile.delete(fileID, em));
  }

  public QFFIFileDeleteResponse deleteByIDForConsole(String fileID) {
    return new QFFIFileDeleteResponse(QFFIDBFile.delete(fileID, em));
  }

  public QFFIFileGetResponse getByID(String fileID) {
    return new QFFIFileGetResponse(getByID(fileID, true));
  }

  public QFFIFileListResponse listFiles(boolean includeBinaryContent) {
    List<QFFIDBFileDTO> retVal = new ArrayList<>();

    // First find all unique IDs for file chunks.
    Query q = em
        .createQuery("select distinct f.id.id from QFFIDBFile f order by f.uploadedAt");
    @SuppressWarnings("unchecked")
    List<String> chunks = q.getResultList();
    for (String id : chunks) {
      retVal.add(getByID(id, includeBinaryContent));
    }

    return new QFFIFileListResponse(retVal);
  }

  public QFFIVirusScanResponse virusScan(QFFIVirusScanRequest req) {
    // Check if a custom address for ClamAV has been provided or use the
    // default.
    InetSocketAddress clamAVAddress;
    if (StringUtils.isBlank(req.getClamAVHost())) {
      clamAVAddress = new InetSocketAddress(qffiProperties.getClamAV().substring(0,
          qffiProperties.getClamAV().indexOf(":")),
          Integer.parseInt(qffiProperties.getClamAV().substring(qffiProperties.getClamAV()
              .indexOf(":") + 1)));
    } else {
      clamAVAddress = new InetSocketAddress(req.getClamAVHost(), req.getClamAVPort());
    }
    LOGGER.log(Level.FINE, "Contacting ClamAV at: {0}.", clamAVAddress);
    ClamAV clamAV = new ClamAV(clamAVAddress, CLAMAV_SOCKET_TIMEOUT);
    QFFIDBFileDTO fileDTO = getByID(req.getId(), true);
    String scanResult = null;
    try {
      scanResult = clamAV.scan(new ByteArrayInputStream(fileDTO.getFileData()));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check file for virus, file ID=" + req.getId(), e);
      throw new QFFIVirusScanException("Could not check file for virus, file ID=" + req.getId());
    } catch (ClamAVException e) {
      LOGGER.log(Level.SEVERE, "Could not check file for virus, file ID=" + req.getId(), e);
      throw new QFFIVirusScanException("Could not check file for virus, file ID=" + req.getId());
    }

    QFFIVirusScanResponse res = new QFFIVirusScanResponse();
    res.setId(req.getId());
    res.setVirusFree(scanResult.equals("OK") ? true : false);
    res.setVirusScanDescription(scanResult);

    return res;
  }

  public void cleanupExpired(long deleteBefore) {
    QQFFIDBFile qFile = QQFFIDBFile.qFFIDBFile;
    new JPAQueryFactory(em).delete(qFile)
        .where(qFile.uploadedAt.lt(deleteBefore)).execute();
  }

  public QFFIFileListResponse listFilesForConsole(boolean includeBinary) {
    List<QFFIDBFileDTO> retVal = new ArrayList<>();

    // First find all unique IDs for file chunks.
    Query q = em
        .createQuery("select distinct f.id.id from QFFIDBFile f order by f.uploadedAt");
    @SuppressWarnings("unchecked")
    List<String> chunks = q.getResultList();
    for (String id : chunks) {
      retVal.add(getByID(id, includeBinary));
    }

    return new QFFIFileListResponse(retVal);
  }

}
