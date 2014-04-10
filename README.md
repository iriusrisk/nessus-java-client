nessus-java-client
==================

A Java client to the Nessus scanner's XML RPC interface</h2>
The API is divided into a ScanClient and a ReportClient which both extend BaseClient.

Install
=======
```mvn install -DskipTests```
  
Usage
=====

```java
ScanClient scan = new ScanClient("https://nessusurl");
scan.login("username","password");
String scanID = scan.newScan("myScanName","myExistingPolicyName","127.0.0.1,someotherhost");
while (scanClient.isScanRunning("myScanName")) {
     try {
        Thread.sleep(2000);
     } catch (InterruptedException e) {
        e.printStackTrace();
     }
}

ReportClient report = new ReportClient("https://nessusurl");
report.login("username","password");
Map<Integer,Issue> issues = report.getAllIssuesSortedByPluginId(scanID);
```
