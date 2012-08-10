/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dynamicflow.aws.s3.webcache.tag;

import br.com.dynamicflow.aws.s3.webcache.util.CachedFile;
import br.com.dynamicflow.aws.s3.webcache.util.WebCacheConfig;
import br.com.dynamicflow.aws.s3.webcache.util.WebCacheManager;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 * @author aoliveir
 */
public class S3CacheGetTag extends S3CacheTagSupport {
    
    private String src;
    
    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        
        try {
            out.print(translateSrcPath(src));
        } catch (java.io.IOException ex) {
            throw new JspException("Error in S3CacheGetTag tag", ex);
        }
        return SKIP_BODY;
    }

    // called at end of tag
    public int doEndTag() {
        return EVAL_PAGE;
    }
    
    public void setSrc(String src) {
        this.src = src;
    }
}
