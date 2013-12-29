package jetbrick.template.springsecurity.tags;

import java.io.IOException;

import jetbrick.template.JetAnnoations;
import jetbrick.template.runtime.JetTagContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.taglibs.velocity.Authz;
import org.springframework.security.taglibs.velocity.AuthzImpl;

/**
 * 为spring-security提供的jetbrick-template标签。<br>
 * <strong>注意：</strong> 依赖commons-beanutils-1.8.3.jar <br>
 * 使用时，只需要配置
 * <pre>
 * <code>
 * import.autoscan          = true
 * import.autoscan.packages = jetbrick.template.shiro.tags,your.other.pkgs
 * </code>
 * </pre>
 * 
 * 并需要在spring中指定下面的bean，用于属性注入
 * <pre>
 * &lt;bean class="jetbrick.template.springsecurity.tags.SpringSecurityTags" /&gt;
 * </pre>
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
@JetAnnoations.Tags
public final class SpringSecurityTags implements ApplicationContextAware {

	private static final Authz AUTHZ = new AuthzImpl();
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AUTHZ.setAppCtx(applicationContext);
	}

	// -----------------------------------------------------------------------
	
	public static void allGranted(JetTagContext ctx, String roles) throws IOException {
		boolean show = AUTHZ.allGranted(roles);
		if (show) {
			ctx.writeBodyContent();
		}
	}
	
	public static void anyGranted(JetTagContext ctx, String roles) throws IOException {
		boolean show = AUTHZ.anyGranted(roles);
		if (show) {
			ctx.writeBodyContent();
		}
	}
	
	public static void noneGranted(JetTagContext ctx, String roles) throws IOException {
		boolean show = AUTHZ.noneGranted(roles);
		if (show) {
			ctx.writeBodyContent();
		}
	}
	
	public static void principal(JetTagContext ctx, String property) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal != null) {
				try {
					String val = PropertyUtils.getProperty(principal, property).toString();
					ctx.getWriter().print(val);
				} catch (Exception e) {
					// 忽略异常
				}
			}
		}
	}

}
