package pl.lodz.p.library.adapters.soap.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration @EnableWs
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> servletRegistrationBean(ApplicationContext ac) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(ac);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean
    public XsdSchema bookSetsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("book_sets.xsd"));
    }

    @Bean(name = "bookSets")
    public DefaultWsdl11Definition bookSetsWsdl(XsdSchema bookSetsSchema) {
        DefaultWsdl11Definition def = new DefaultWsdl11Definition();
        def.setPortTypeName("BookSetsPort");
        def.setLocationUri("/ws");
        def.setTargetNamespace("http://pl.lodz.p.library.adapters.soap.dto.bookset/");
        def.setSchema(bookSetsSchema);
        return def;
    }

    @Bean
    public XsdSchema loanSchema() {
        return new SimpleXsdSchema(new ClassPathResource("loans.xsd"));
    }

    @Bean(name = "loans")
    public DefaultWsdl11Definition loansWsdl(XsdSchema loanSchema) {
        DefaultWsdl11Definition def = new DefaultWsdl11Definition();
        def.setPortTypeName("LoansPort");
        def.setLocationUri("/ws");
        def.setTargetNamespace("http://pl.lodz.p.library.adapters.soap.dto.loan/");
        def.setSchema(loanSchema);
        return def;
    }

    @Bean
    public XsdSchema userSchema() {
        return new SimpleXsdSchema(new ClassPathResource("users.xsd"));
    }

    @Bean(name = "users")
    public DefaultWsdl11Definition usersWsdl(XsdSchema userSchema) {
        DefaultWsdl11Definition def = new DefaultWsdl11Definition();
        def.setPortTypeName("UsersPort");
        def.setLocationUri("/ws");
        def.setTargetNamespace("http://pl.lodz.p.library.adapters.soap.dto.user/");
        def.setSchema(userSchema);
        return def;
    }

    @Bean
    public XsdSchema authSchema() {return new SimpleXsdSchema(new ClassPathResource("auth.xsd"));}

    @Bean(name = "auth")
    public DefaultWsdl11Definition authWsdl(XsdSchema authSchema) {
        DefaultWsdl11Definition def = new DefaultWsdl11Definition();
        def.setPortTypeName("AuthPort");
        def.setLocationUri("/ws");
        def.setTargetNamespace("http://pl.lodz.p.library.adapters.soap.dto.auth/");
        def.setSchema(authSchema);
        return def;
    }
}
