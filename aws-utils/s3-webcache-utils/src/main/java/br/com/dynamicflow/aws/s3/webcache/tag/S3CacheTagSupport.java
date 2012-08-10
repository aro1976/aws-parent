/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dynamicflow.aws.s3.webcache.tag;

import br.com.dynamicflow.aws.s3.webcache.util.CachedFile;
import br.com.dynamicflow.aws.s3.webcache.util.WebCacheConfig;
import br.com.dynamicflow.aws.s3.webcache.util.WebCacheManager;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 * @author aoliveir
 */
public class S3CacheTagSupport extends TagSupport {
	private static final Logger log = Logger.getLogger("br.com.dynamicflow.aws.s3.webcache.tag.S3CacheTagSupport");
	
    private static Map<String,String> urlMapping;

    public synchronized Map<String,String> getUrlMapping() {
        if (urlMapping==null) {
            urlMapping = new HashMap<String,String>();
            InputStream is = pageContext.getServletContext().getResourceAsStream(WebCacheManager.DEFAULT_MANIFEST);
            if (is == null) {
            	log.log(Level.INFO,"S3Cache tags will be set to passthrough, sice no configuration file was found at "+WebCacheManager.DEFAULT_MANIFEST);
            } else {
            	log.log(Level.INFO,"loading S3Cache configuration");
                urlMapping = new HashMap<String,String>();
                WebCacheManager webCacheManager = new WebCacheManager();
                WebCacheConfig webCacheConfig = webCacheManager.loadConfig(is);
                for (CachedFile cachedFile: webCacheConfig.getCachedFiles()) {
                    urlMapping.put(cachedFile.getOriginalPath(), "http://"+webCacheConfig.getHostName()+"/"+cachedFile.getDigest());
                }
            }
        }
        return urlMapping;
    }
    
    protected String translateSrcPath(String src) {
    	 String result = src;
    	 String rootPath = pageContext.getServletContext().getRealPath("/");
    	
         try {
        	 String canonicalPath = new File(pageContext.getServletContext().getRealPath(src)).getCanonicalPath();
        	 String srcPath = canonicalPath.substring(rootPath.length());
        	 if (!srcPath.startsWith("/")) {
        		 srcPath = "/"+srcPath;
        	 }
             log.log(Level.FINE,"lookup for resource "+srcPath);
             if (getUrlMapping().containsKey(srcPath)) {
                 result = getUrlMapping().get(srcPath);
             } 
         } catch (Exception e) {
        	 throw new RuntimeException("could not resolve the canonical path of resource "+src,e);
         } 
        
         return result;
    }
}
