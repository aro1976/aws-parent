package br.com.dynamicflow.aws.s3.webcache.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CachedFile")
public class CachedFile {
	String originalPath;
	String digest;
	
	public CachedFile() {
	}
	
	public CachedFile(String originalPath, String digest) {
		super();
		this.originalPath = originalPath;
		this.digest = digest;
	}

	public String getOriginalPath() {
		return originalPath;
	}
	
	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}
	
	public String getDigest() {
		return digest;
	}
	
	public void setDigest(String digest) {
		this.digest = digest;
	}
	
	@Override
	public String toString() {
		return "CachedFile [originalPath=" + originalPath + ", digest="
				+ digest + "]";
	}
}
