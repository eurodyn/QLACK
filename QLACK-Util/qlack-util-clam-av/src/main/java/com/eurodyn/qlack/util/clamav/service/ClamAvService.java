package com.eurodyn.qlack.util.clamav.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurodyn.qlack.util.clamav.dto.VirusScanDTO;
import com.eurodyn.qlack.util.clamav.exception.VirusScanException;
import com.eurodyn.qlack.util.clamav.util.ClamAvProperties;

import io.sensesecure.clamav4j.ClamAV;
import io.sensesecure.clamav4j.ClamAVException;
import lombok.extern.java.Log;

/**
 * @author European Dynamics
 */
@Log
@Service
public class ClamAvService {

  private ClamAvProperties properties;

  @Autowired
  public ClamAvService(ClamAvProperties properties) {
    this.properties = properties;
  }

  /**
   * Sends file data for scanning to an open socket of
   * the ClamAV antivirus server instance as a
   * {@link ByteArrayInputStream}
   *
   * @param data a {@link java.lang.Byte} array containing file data to be scanned
   *
   * @return {@link com.eurodyn.qlack.util.clamav.dto.VirusScanDTO} the scanning result
   */
  public VirusScanDTO virusScan(byte[] data) {
    VirusScanDTO vsDTO = new VirusScanDTO();
    InetSocketAddress clamAVAddress = new InetSocketAddress(properties.getClamAvHost(), properties.getClamAvPort());

    log.log(Level.FINE, "Contacting ClamAV at: {0}.", clamAVAddress);
    ClamAV clamAV = new ClamAV(clamAVAddress, properties.getClamAvSocketTimeout());
    String scanResult;
    try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
      scanResult = clamAV.scan(bis);
    } catch (IOException | ClamAVException e) {
      log.log(Level.SEVERE, "Could not check file for virus", e);
      throw new VirusScanException("Could not check file for virus");
    }

    vsDTO.setVirusFree(scanResult.equals("OK"));
    vsDTO.setVirusScanDescription(scanResult);

    return vsDTO;
  }
}
