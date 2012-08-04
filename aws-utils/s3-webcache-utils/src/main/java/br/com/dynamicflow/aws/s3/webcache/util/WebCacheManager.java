package br.com.dynamicflow.aws.s3.webcache.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class WebCacheManager {
	
	public static final String DEFAULT_MANIFEST = "/WEB-INF/s3-webcache.xml";
	
	private static final Logger log = Logger.getLogger("WebCacheManager.class");

	public void persistConfig(WebCacheConfig webCacheConfig, File manifestFile) {
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
	
	public WebCacheConfig loadConfig(File manifestFile) {
		log.info("loading s3-webcache from manifest "+ manifestFile.getPath());
		try {
			return loadConfig(new FileInputStream(manifestFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("could not read config manifest",e);
		}
	}
	
	public WebCacheConfig loadConfig(InputStream is) {
		try {
			JAXBContext context = JAXBContext.newInstance(WebCacheConfig.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			WebCacheConfig webCacheConfig = (WebCacheConfig) unmarshaller.unmarshal(is);
			return webCacheConfig;
		} catch (JAXBException e) {
			throw new RuntimeException("could not read config manifest",e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("could not read config manifest",e);
			}
		}
	}
}
