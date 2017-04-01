<img src="https://www.continuumsecurity.net/wp-content/uploads/2016/10/continuum-logo.png" width="400"/>

Nessus Java API Client
======================

A Java client to the Nessus scanner's REST interface, supports both v5 and v6</h2>
The API is divided into a ScanClient and a ReportClient.

  
Usage
=====

```java
ScanClient scan = ClientFactory.createScanClient("https://nessusurl",6,true); // true == accept all hostnames from SSL cert
scan.login("username","password");
String scanID = scan.newScan("myScanName","myExistingPolicyName","127.0.0.1,someotherhost");
while (scan.isScanRunning(scanID)) {
     try {
        Thread.sleep(2000);
     } catch (InterruptedException e) {
        e.printStackTrace();
     }
}

ReportClient report = ClientFactory.createReportClient("https://nessusurl",6,true);
report.login("username","password");
Map<Integer,Issue> issues = report.getAllIssuesSortedByPluginId(scanID);
```

Differences between Nessus v5 and v6 APIs
=========
Nessus V5 used a unique long string as a UUID to identify scans.  Nessus V6 uses a numeric scanId value. Since this library uses a single interface for both V5 and V6, the scanId parameter in the V6 client is a String which is converted to an int.


Reference
=========
This library supports a subset of the entire API interface to Nessus, if you'd like to add other features the full specification for v5 can be found [here](http://static.tenable.com/documentation/nessus_5.0_XMLRPC_protocol_guide.pdf)
Nessus v6 API documentation can be read from the API itself by visiting: https://mynessuslocation:8834/nessus6-api.html

Install
=======
```mvn install -DskipTests```
