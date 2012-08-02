package br.com.dynamicflow.aws.s3.webcache.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="WebCacheConfig")
public class WebCacheConfig {

	private String hostName;
	private Map<String,String> cachedFiles;
	
	public WebCacheConfig() {
		super();
	}
	
	public WebCacheConfig(String hostName) {
		super();
		this.hostName = hostName;
		cachedFiles = new HashMap<String,String>();
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Map<String, String> getCachedFiles() {
		return cachedFiles;
	}

	public void setCachedFiles(Map<String, String> cachedFiles) {
		this.cachedFiles = cachedFiles;
	}

	@Override
	public String toString() {
		return "WebCacheConfig [hostName=" + hostName + "]";
	}

	public void addToCachedFiles(String file, String hash) {
		cachedFiles.put(file, hash);
	}
}
