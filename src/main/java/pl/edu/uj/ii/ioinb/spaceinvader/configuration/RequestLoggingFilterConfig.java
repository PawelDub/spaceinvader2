package pl.edu.uj.ii.ioinb.spaceinvader.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        filter.setBeforeMessagePrefix("======================= Request begin =======================================\n" +
                "REQUEST BEFORE : ");
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        filter.setAfterMessageSuffix("\n ===================== Request end ===========================================");
        return filter;
    }

    @Bean
    public FilterRegistrationBean loggingFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(logFilter());
        registration.addUrlPatterns("/");
        registration.addUrlPatterns("/user/*");
        registration.addUrlPatterns("/login/**");
        registration.addUrlPatterns("/registration/*");
        registration.addUrlPatterns("/home/*");
        return registration;
    }
}
