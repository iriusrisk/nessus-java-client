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
while (scan.isScanRunning("myScanName")) {
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

Reference
=========
This library supports a subset of the entire XML RPC interface to Nessus, if you'd like to add other features the full specification can be found [here](http://static.tenable.com/documentation/nessus_5.0_XMLRPC_protocol_guide.pdf)