package com.javachen.web.i18n;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * Expose the current locale and all messages as properties.
 */
public class FrontendMessageSource extends ReloadableResourceBundleMessageSource {

    private static final String PROPERTIES_SUFFIX = ".properties";

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        // ignore setting of application context specific resource loader to retain compatibility with
        // ResourceBundleMessageSource
    }

    /**
     * Return current locale as IETF BCP 47 code.
     */
    public String getLocale() {
        return LocaleContextHolder.getLocale().toLanguageTag();
    }

    public Properties getMessages() {
        if (getCacheMillis() >= 0) {
            clearCacheIncludingAncestors();
        }
        return getMergedProperties(LocaleContextHolder.getLocale()).getProperties();
    }

    @Override
    protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
        if (filename.startsWith(PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
            return refreshClassPathProperties(filename, propHolder);
        } else {
            return super.refreshProperties(filename, propHolder);
        }
    }

    private PropertiesHolder refreshClassPathProperties(String filename, PropertiesHolder propHolder) {
        Properties properties = new Properties();
        long lastModified = -1;
        try {
            Resource[] resources = resolver.getResources(filename + PROPERTIES_SUFFIX);
            for (Resource resource : resources) {
                String sourcePath = resource.getURI().toString();
                sourcePath = sourcePath.substring(0, sourcePath.length() - PROPERTIES_SUFFIX.length());
                PropertiesHolder holder = super.refreshProperties(sourcePath, propHolder);
                properties.putAll(holder.getProperties());
                if (lastModified < resource.lastModified()) {
                    lastModified = resource.lastModified();
                }
            }
        } catch (IOException ignored) {
            // ignore exception
        }
        return new PropertiesHolder(properties, lastModified);
    }

}
