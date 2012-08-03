package br.com.dynamicflow.aws.s3.webcache.util;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class WebCacheManager {
	
	private static final Logger log = Logger.getLogger("WebCacheManager.class");
	
	private File manifestFile;
	
	public WebCacheManager(File manifestFile) {
		this.manifestFile = manifestFile;
	}

	public void persistConfig(WebCacheConfig webCacheConfig) {
		log.info("generating s3-webcache configuration manifest into "+ manifestFile.getPath());
		try {
			JAXBContext context = JAXBContext.newInstance(WebCacheConfig.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			if (!manifestFile.getParentFile().exists()) {
				boolean success = manifestFile.getParentFile().mkdirs();
				if (!success) {
					throw new RuntimeException("cannot create directory");
				}
			}
			marshaller.marshal(webCacheConfig, manifestFile);
		} catch (JAXBException e) {
			throw new RuntimeException("could not generate config manifest",e);
		}
	}
	
	public WebCacheConfig loadConfig() {
		log.info("loading s3-webcache from manifest "+ manifestFile.getPath());
		try {
			JAXBContext context = JAXBContext.newInstance(WebCacheConfig.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			WebCacheConfig webCacheConfig = (WebCacheConfig) unmarshaller.unmarshal(manifestFile);
			return webCacheConfig;
		} catch (JAXBException e) {
			throw new RuntimeException("could not generate config manifest",e);
		}
	}
}
