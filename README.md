# wildfly-sfsb

Demonstrate when a transaction is canceled by the Wildfly transaction reaper, the transaction status of the involved stateful session backed by Infinispan is not correctly cleared. 

Need to change the settings of transactions subsystem, to make the issue appear. 

Adding the following: 
```
<coordinator-environment default-timeout="25"/>
```
to
```
<subsystem xmlns="urn:jboss:domain:transactions:2.0">
</subsystem>
```

Usage: 
```
mvn clean install
```
```
jboss-cli.sh -c 'deploy target/wildfly.sfsb.war'
```
```
jboss-cli.sh -c 'undeploy wildfly.sfsb.war'
```

The bug report is here: 
```
https://issues.jboss.org/browse/WFLY-4896
```