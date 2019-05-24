package com.eurodyn.qlack.util.clamav.service;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.mockito.Spy;

import com.eurodyn.qlack.util.clamav.InitTestValues;
import com.eurodyn.qlack.util.clamav.dto.VirusScanDTO;
import com.eurodyn.qlack.util.clamav.util.ClamAvProperties;

/**
 * @author European Dynamics
 */
//@RunWith(MockitoJUnitRunner.class)
public class ClamAvServiceTest {

  @Spy
  ClamAvProperties clamAvProperties;

  private ClamAvService clamAvService = mock(ClamAvService.class);
  private InitTestValues initTestValues;
  private VirusScanDTO vsDTO;

  /**
   * This byte array stands for actual
   * file data for testing purposes
   */
  private byte[] data = {80, 65, 78, 75, 65, 74};

  @Before
  public void init() {
    clamAvService = new ClamAvService(clamAvProperties);
    initTestValues = new InitTestValues();
    vsDTO = initTestValues.createVirusScanDTO();
  }

  /**
   * This test is purposely commented out as it requires
   * a running instance of ClamAV server. Please see the README.md
   * for more details on how to setup a ClamAV server.
   */
  //  @Test
  //  public void testVirusScan() {
  //    ReflectionTestUtils.setField(clamAvProperties, "clamAvHost", "127.0.0.1");
  //    ReflectionTestUtils.setField(clamAvProperties, "clamAvPort", 3310);
  //    ReflectionTestUtils.setField(clamAvProperties, "clamAvSocketTimeout", 100000);
  //    VirusScanDTO virusScanDTO = clamAvService.virusScan(data);
  //    assertTrue(virusScanDTO.isVirusFree());
  //    assertNotNull(virusScanDTO.getVirusScanDescription());
  //  }
}
