S3 WebCache Maven Plugin
========================

S3 WebCache Maven Plugin uploads the static files from the src/main/webapp directory of a Java
Web Application to S3, which can then be placed behind CloudFront to use as a CDN.

The purpose of this plugin is to make the use of a CDN for you static assets automatic when
deploying a maven webapp.

## Installation
Download the source code and run `mvn install` to add the plugin to your local repository.

## Setup
Add the following lines to your pom.xml (with the correct values of course).

    <properties>
    	<aws.accessKey>IASDHASDHASIDHWERBW2</aws.accessKey>
    	<aws.secretKey>eptWdHgsN4XU0N4SFlpZvzikGkTFnGF/nl/mEAhm</aws.secretKey>
    	<aws.bucketName>data.fs.mycompany</aws.bucketName>
    </properties>

Then in the build section of your pom.xml add the plugin:

	<plugin>
	 	<groupId>com.cleanenergyexperts</groupId>
		<artifactId>s3-webcache-maven-plugin</artifactId>
		<version>0.0.5-SNAPSHOT</version>
		<configuration>
			<accessKey>${aws.accessKey}</accessKey>
			<secretKey>${aws.secretKey}</secretKey>
			<bucketName>${aws.bucketName}</bucketName>
			<digestType>none</digestType> <!-- none,md5,sha1,sha256,sha384,sha512 -->
			<contentEncoding>plain</contentEncoding> <!-- plain,gzip -->
			<versionKey>${project.timestamp}</versionKey>
			<includes>
				<include>**/*.gif</include>
				<include>**/*.jpg</include>
				<include>**/*.tif</include>
				<include>**/*.png</include>
				<include>**/*.pdf</include>
				<include>**/*.swf</include>
				<include>**/*.eps</include>
				<include>**/*.js</include>
				<include>**/*.css</include>
			</includes>
			<excludes>
				<exclude>WEB-INF/**</exclude>
			</excludes>
		</configuration>
		<executions>
			<execution>
				<goals>
					<goal>upload</goal>
				</goals>
				<phase>prepare-package</phase>
			</execution>
		</executions>
	</plugin>

## Usage
To upload your files to AWS S3 after completing setup there are two options:

1. Manual: Run `mvn s3-webcache:upload`.

2. Automated: The setup above is already configured to run during the build. Typically,
this is done in a profile for only certain environments.
