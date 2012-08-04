package br.com.dynamicflow.aws.s3.webcache.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="WebCacheConfig")
public class WebCacheConfig {

	private String hostName;
	private List<CachedFile> cachedFiles;
	
	public WebCacheConfig() {
		super();
	}
	
	public WebCacheConfig(String hostName) {
		super();
		this.hostName = hostName;
		cachedFiles = new ArrayList<CachedFile>();
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public List<CachedFile> getCachedFiles() {
		return cachedFiles;
	}

	public void setCachedFiles(List<CachedFile> cachedFiles) {
		this.cachedFiles = cachedFiles;
	}

	public void addToCachedFiles(String originalPath, String hash) {
		cachedFiles.add(new CachedFile(originalPath,hash));
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "WebCacheConfig [hostName="
				+ hostName
				+ ", cachedFiles="
				+ (cachedFiles != null ? cachedFiles.subList(0,
						Math.min(cachedFiles.size(), maxLen)) : null) + "]";
	}
}
