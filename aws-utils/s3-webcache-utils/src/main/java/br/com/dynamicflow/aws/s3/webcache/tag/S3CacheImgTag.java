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
public class S3CacheImgTag extends S3CacheTagSupport {
    
    private String src;
    private String border;
    private String width;
    private String height;
    private String className;
    private String style;
    private String alt;
    
    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        
        try {
            out.print("<img");
            
            if (src!=null && src.length()>0) {
            	out.print(" src=\""+translateSrcPath(src)+"\"");
            }
            
            if (border!=null && border.length()>0) {
                out.print(" border=\""+border+"\"");
            }
            
            if (width!=null && width.length()>0) {
                out.print(" width=\""+width+"\"");
            }
            
            if (height!=null && height.length()>0) {
                out.print(" height=\""+height+"\"");
            }
            
            if (className!=null && className.length()>0) {
                out.print(" className=\""+className+"\"");
            }
            
            if (style!=null && style.length()>0) {
                out.print(" style=\""+style+"\"");
            }
            
            if (alt!=null && alt.length()>0) {
                out.print(" alt=\""+alt+"\"");
            }

            out.println("/>");

        } catch (java.io.IOException ex) {
            throw new JspException("Error in S3CacheImgTag tag", ex);
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

    public void setBorder(String border) {
        this.border = border;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
