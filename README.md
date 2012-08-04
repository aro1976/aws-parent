aws-parent
==========

AWS Maven Plugins and Examples

create maven settings.xml file before running examples

<?xml version="1.0" encoding="UTF-8"?>
<settings xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd" xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

        <profiles>
                <profile>
                        <id>aws</id>
                        <properties>
                                <aws.accessKey>IASDHASDHASIDHWERBW2</aws.accessKey>
                                <aws.secretKey>eptWdHgsN4XU0N4SFlpZvzikGkTFnGF/nl/mEAhm</aws.secretKey>
                                <aws.bucketName>data.fs.mycompany</aws.bucketName>
                        </properties>
                </profile>
        </profiles>

        <activeProfiles>
                <activeProfile>aws</activeProfile>
        </activeProfiles>
</settings>
