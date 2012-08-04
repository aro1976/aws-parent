package br.com.dynamicflow.aws.s3.webcache.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName="WebCacheFilter", description="S3 WebCache Filter")
public class WebCacheFilter implements Filter {
	
	private static final String HTTP_PREFIX = "http://";
	private static final Logger log = Logger.getLogger("WebCacheFilter.class");
	private String hostName;
	private Map<String,String> urlMapping;
	private boolean loaded = false;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("init WebCacheFilter");

		InputStream is = filterConfig.getServletContext().getResourceAsStream(WebCacheManager.DEFAULT_MANIFEST);
		if (is == null) {
			log.warning("WebCacheFilter will be disabled, absent configuration");
		} else {
			urlMapping = new HashMap<String,String>();
			WebCacheManager webCacheManager = new WebCacheManager();
			WebCacheConfig webCacheConfig = webCacheManager.loadConfig(is);
			hostName = webCacheConfig.getHostName();
			for (CachedFile cachedFile: webCacheConfig.getCachedFiles()) {
				urlMapping.put(cachedFile.getOriginalPath(), hostName+"/"+cachedFile.getDigest());
			}
			loaded = true;
		}
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		if (!loaded) {
			chain.doFilter(req, resp);
			return;
		}
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		String mappedUrl = urlMapping.get(request.getServletPath());
		if (mappedUrl != null) {
			String redirectedUrl = HTTP_PREFIX+mappedUrl;
			log.info("send redirect from "+request.getServletPath()+" to "+redirectedUrl);
			response.sendRedirect(redirectedUrl);
		} else {
			chain.doFilter(req, resp);
		}
	}
	
	@Override
	public void destroy() {
		log.info("destroy");
	}
}
