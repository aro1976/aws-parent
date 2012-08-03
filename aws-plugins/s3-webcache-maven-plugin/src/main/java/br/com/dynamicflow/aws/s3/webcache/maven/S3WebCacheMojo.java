package br.com.dynamicflow.aws.s3.webcache.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.plugin.AbstractMojo;

import br.com.dynamicflow.aws.s3.webcache.plugin.WebCacheConfig;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal upload
 * 
 */
public class S3WebCacheMojo extends AbstractMojo {
	
	static final String S3_URL = "s3.amazonaws.com";
	
	private static final SimpleDateFormat httpDateFormat;
	 
	static{
		httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	/**
	 * @parameter property="accessKey"
	 */
	String accessKey;
	
	/**
	 * @parameter property="secretKey"
	 */
	String secretKey;
	
	/**
	 * @parameter property="bucketName" 
	 */
	String bucketName;
	
	/**
	 * @parameter property="hostName"
	 */
	String hostName;
	
	/**
	 * @parameter expression="${project.build.directory}/${project.build.finalName}"
	 */
	private File buildDir;
	
	public void execute() {
		if (hostName==null || hostName.length()==0) {
			hostName=bucketName+"."+S3_URL;
		}
		getLog().info("using hostName " + hostName);
		
		WebCacheConfig webCacheConfig = new WebCacheConfig(hostName);
		
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
		AmazonS3Client client = new AmazonS3Client(awsCredentials);
		
		getLog().info( "determining files that should be uploaded" );
		
		String fileName = "grails_logo.jpg";
		
		// para cada arquivo
		ObjectMetadata objectMetadata = retrieveObjectMetadata(client, fileName);	
		
		if (objectMetadata != null && objectMetadata.getETag().equals(calculateETag(fileName))) {
			getLog().info("the object "+fileName+" stored at "+bucketName+" does not require update");
		} else {
			uploadFile(client, fileName);
		}
		
		String digest = calculateDigest(fileName);
		webCacheConfig.addToCachedFiles(fileName,digest);
		
		// finaliza para cada arquivo
		generateConfigManifest(webCacheConfig);

		getLog().info( "closing S3 connection" );	
	}

	private void uploadFile(AmazonS3Client client, String fileName) {
		getLog().info("uploading file "+fileName+" to "+bucketName);	
		try {
			File file = new File(fileName);
			
			MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.length());
			objectMetadata.setHeader("Content-Disposition", "filename="+file.getName());
			objectMetadata.setHeader("Cache-Control", "public, s-maxage=315360000, max-age=315360000");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, 10);
			objectMetadata.setHeader("Expires", httpDateFormat.format(calendar.getTime()));
			objectMetadata.setLastModified(new Date(file.lastModified()));
			objectMetadata.setContentType(mimetypesFileTypeMap.getContentType(file));
			client.putObject(bucketName, fileName, new FileInputStream(file), objectMetadata);
			
		} catch (FileNotFoundException e) {
			getLog().error(e);
		} 
	}
	
	private ObjectMetadata retrieveObjectMetadata(AmazonS3Client client, String fileName) {
		getLog().info("retrieving metadata for "+fileName);
		ObjectMetadata objectMetadata = null;

		objectMetadata = client.getObjectMetadata(bucketName, fileName);
		getLog().info( "object metadata ETag: " + objectMetadata.getETag());
		getLog().info( "object metadata ContentMD5: " + objectMetadata.getContentMD5());
		getLog().info( "object metadata ContentType: " + objectMetadata.getContentType());
		getLog().info( "object metadata CacheControl: " + objectMetadata.getCacheControl());
		getLog().info( "object metadata ContentEncoding: " + objectMetadata.getContentEncoding());
		getLog().info( "object metadata ContentDisposition: " + objectMetadata.getContentDisposition());
		getLog().info( "object metadata ContentLength: " + objectMetadata.getContentLength());
		getLog().info( "object metadata LastModified: " + objectMetadata.getLastModified());

		return objectMetadata;
	}

	private String calculateETag(String fileName) {
		String eTag = null;
		try {
			eTag = Hex.encodeHexString(DigestUtils.md5(new FileInputStream(new File(fileName))));
		} catch (Exception e) {
			getLog().error(e);
		} 
		getLog().info("eTag for "+fileName+" is "+eTag);
		return eTag;
	}
	
	private String calculateDigest(String fileName) {
		String digest = null;
		try {
			digest = Hex.encodeHexString(DigestUtils.sha256(new FileInputStream(new File(fileName))));
		} catch (Exception e) {
			getLog().error(e);
		} 
		getLog().info("digest for "+fileName+" is "+digest);
		return digest;
	}
	
	private void generateConfigManifest(WebCacheConfig webCacheConfig) {
		File manifestFile = new File(buildDir,"WEB-INF"+"/"+"s3-webcache.xml");
		getLog().info("generating s3-webcache configuration manifest into "+ manifestFile.getPath());
		try {
			JAXBContext context = JAXBContext.newInstance(WebCacheConfig.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(webCacheConfig, manifestFile);
		} catch (JAXBException e) {
			getLog().error(e);
		}
	}
}
