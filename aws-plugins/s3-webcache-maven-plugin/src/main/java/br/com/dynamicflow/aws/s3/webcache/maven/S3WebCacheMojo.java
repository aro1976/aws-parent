package br.com.dynamicflow.aws.s3.webcache.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal upload
 * 
 */
public class S3WebCacheMojo extends AbstractMojo {
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info( "establishing connection to S3" );	
		
		getLog().info( "determining files that should be uploaded" );	
		
		getLog().info( "uploading files" );	
		
		getLog().info( "closing S3 connection" );	
	}
}
