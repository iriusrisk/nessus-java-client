package net.continuumsecurity;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import net.continuumsecurity.v6.ScanClientV6;
import net.continuumsecurity.v6.model.ExportFormat;
import net.continuumsecurity.v6.model.ExportV6;
import net.continuumsecurity.v6.model.ScanV6;
import net.continuumsecurity.v6.model.ScansV6;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import javax.security.auth.login.LoginException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Created by stephen on 07/02/15.
 */
public class ScanClientV6Test implements BaseTest {
    static ScanClientV6 client;
    String policyName = "basic";
    String scanName = "testScan";
    String hostname = "127.0.0.1";
    int port = 22;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @BeforeClass
    public static void setup() throws LoginException {
        client = new ScanClientV6(NESSUS_URL,true);
        client.login(USER, PASSWORD);
    }

    @AfterClass
    public static void after() {
        client.logout();
    }

    @Before
    public void checkPrerequisites() throws InterruptedException {
        try {
            client.getScanIDFromName(scanName);
        } catch (ScanNotFoundException e) {
            String id = client.newScan(scanName,policyName,"127.0.0.1");
            assertThat(client.getScanStatus(id), Matchers.equalTo("running"));
            assertThat(client.isScanRunning(id), Matchers.equalTo(true));
            while (client.isScanRunning(id)) {
                Thread.sleep(3*1000);
            }
            assertThat(client.getScanStatus(id), Matchers.equalTo("completed"));
        }
    }

    @Test
    public void testGetPolicyIdWithAPIKey() throws LoginException {
        // Set the API Keys
        client.setApiKeys(ACCESS_KEY, SECRET_KEY);
        testGetPolicyIdFromName();

        // use use/password again
        client.login(USER, PASSWORD);
    }

    @Test(expected=ScanNotFoundException.class)
    public void testGetScanStatusForInValidName() throws LoginException {
        client.getScanStatus("12312412");
    }

    @Test
    public void testGetPolicyIdFromName() {
        int id = client.getPolicyIDFromName(policyName);
        assertThat(id,greaterThan(0));
    }

    @Test(expected=PolicyNotFoundException.class)
    public void testGetPolicyIdFromInvalidName() {
        client.getPolicyIDFromName("oompaloompa");
    }

    @Test
    public void launchScan() throws InterruptedException {
        int id = client.getScanIDFromName(scanName);
        client.launchScan(id);
        assertThat(client.getScanStatus(Integer.toString(id)), Matchers.equalTo("running"));
        assertThat(client.isScanRunning(Integer.toString(id)), Matchers.equalTo(true));
        while (client.isScanRunning(Integer.toString(id))) {
            Thread.sleep(3*1000);
        }
        assertThat(client.getScanStatus(Integer.toString(id)), Matchers.equalTo("completed"));
    }

    @Test
    public void testExportScanReport() throws LoginException, IOException {

        // Retrieve last scan produced
        ScansV6 scansV6 = client.listScans();
        ScanV6 scanV6 = scansV6.getScans().get(0);
        int scanId = scanV6.getId();

        // Export file
        ExportV6 export = client.export(scanId, ExportFormat.HTML);
        assertThat(export.getFile(), Matchers.not(Matchers.isEmptyOrNullString()));
    }

    @Test
    public void testDownloadCsvScanReport() throws LoginException, IOException {
        // Retrieve last scan produced
        ScansV6 scansV6 = client.listScans();
        ScanV6 scanV6 = scansV6.getScans().get(0);
        int scanId = scanV6.getId();
        testFolder.create();

        // Download exported file
        File downloaded = client.download(scanId, ExportFormat.CSV, testFolder.newFolder().toPath());
        String result = fileToString(downloaded);
        assertThat(result, Matchers.containsString("The remote host is up"));
    }

    @Test
    public void testDownloadNessusScanReport() throws LoginException, IOException {
        // Retrieve last scan produced
        ScansV6 scansV6 = client.listScans();
        ScanV6 scanV6 = scansV6.getScans().get(0);
        int scanId = scanV6.getId();
        testFolder.create();

        // Download exported file
        File downloaded = client.download(scanId, ExportFormat.NESSUS, testFolder.newFolder().toPath());
        String result = fileToString(downloaded);
        assertThat(result, Matchers.containsString("<NessusClientData_v2>"));
    }

    private String fileToString(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, Charset.defaultCharset());
    }
}
