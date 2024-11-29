/*
 * Copyright 2023-2024 Steinar Bang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package no.priv.bang.ratatoskr.web.as;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ServerProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.log.LogService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;

import no.priv.bang.ratatoskr.asvocabulary.Actor;
import no.priv.bang.ratatoskr.asvocabulary.Person;
import no.priv.bang.ratatoskr.backend.RatatoskrServiceProvider;
import no.priv.bang.ratatoskr.db.liquibase.test.RatatoskrTestDbLiquibaseRunner;
import no.priv.bang.ratatoskr.services.RatatoskrService;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;
import no.priv.bang.osgiservice.users.UserManagementService;

class RatatoskrActivityStreamsResourceServletTest extends ShiroTestBase {

    public static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .findAndRegisterModules();

    private static DataSource datasource;

    @BeforeAll
    static void beforeAll() throws Exception {
        var derbyDataSourceFactory = new DerbyDataSourceFactory();
        var properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:ratatoskr;create=true");
        datasource = derbyDataSourceFactory.createDataSource(properties);
        var runner = new RatatoskrTestDbLiquibaseRunner();
        runner.activate();
        runner.prepare(datasource);

        // Pre-populate the database with some data
        var ratatoskr = new RatatoskrServiceProvider();
        ratatoskr.setDatasource(datasource);
        ratatoskr.setUseradmin(mock(UserManagementService.class));
        ratatoskr.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var kenzoishii = Person.with()
            .id("https://kenzoishii.example.com")
            .preferredUsername("kenzoishii")
            .name("石井健蔵")
            .summary("この方はただの例です")
            .inbox("https://kenzoishii.example.com/inbox.json")
            .following("https://kenzoishii.example.com/following.json")
            .followers("https://kenzoishii.example.com/followers.json")
            .liked("https://kenzoishii.example.com/liked.json")
            .icon("https://kenzoishii.example.com/image/165987aklre4")
            .build();
        ratatoskr.addActor(kenzoishii);
        var johnd = Person.with()
            .id("http://localhost:8181/ratatoskr/as/actor/johnd")
            .preferredUsername("johnd")
            .name("John Doe")
            .summary("The common man")
            .icon("http://localhost:8181/ratatoskr/image/165987aklre4")
            .build();
        ratatoskr.addActor(johnd);
    }

    @Test
    void testGetActor() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var ratatoskr = new RatatoskrServiceProvider();
        ratatoskr.setLogservice(logservice);
        ratatoskr.setDatasource(datasource);
        ratatoskr.setUseradmin(useradmin);
        ratatoskr.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(ratatoskr , useradmin, logservice);
        var request = buildGetUrl("/actor/johnd");
        var response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        var actor = mapper.readValue(response.getOutputStreamBinaryContent(), Actor.class);
        var johnd = Person.with()
            .id("http://localhost:8181/ratatoskr/as/actor/johnd")
            .preferredUsername("johnd")
            .name("John Doe")
            .summary("The common man")
            .inbox("http://localhost:8181/ratatoskr/as/commoninbox")
            .following("http://localhost:8181/ratatoskr/as/following/johnd")
            .followers("http://localhost:8181/ratatoskr/as/followers/johnd")
            .liked("http://localhost:8181/ratatoskr/as/liked/johnd")
            .icon("http://localhost:8181/ratatoskr/image/165987aklre4")
            .build();
        assertThat(actor).isEqualTo(johnd);
    }

    @Test
    void testGetActorWhenNotFound() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var ratatoskr = new RatatoskrServiceProvider();
        ratatoskr.setLogservice(logservice);
        ratatoskr.setDatasource(datasource);
        ratatoskr.setUseradmin(useradmin);
        ratatoskr.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(ratatoskr , useradmin, logservice);
        var request = buildGetUrl("/actor/jonhd");
        var response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(404, response.getStatus());
    }

    private MockHttpServletRequest buildGetUrl(String resource) {
        var request = buildRequest(resource);
        request.setMethod("GET");
        return request;
    }

    private MockHttpServletRequest buildPostUrl(String resource) {
        var contenttype = MediaType.APPLICATION_JSON;
        var request = buildRequest(resource);
        request.setMethod("POST");
        request.setContentType(contenttype);
        request.addHeader("Content-Type", contenttype);
        return request;
    }

    private MockHttpServletRequest buildRequest(String resource) {
        var session = new MockHttpSession();
        var request = new MockHttpServletRequest();
        request.setProtocol("HTTP/1.1");
        request.setRequestURL("http://localhost:8181/ratatoskr/as" + resource);
        request.setRequestURI("/ratatoskr/as" + resource);
        request.setContextPath("/ratatoskr");
        request.setServletPath("/as");
        request.setSession(session);
        return request;
    }

    private RatatoskrActivityStreamsResourceServlet simulateDSComponentActivationAndWebWhiteboardConfiguration(RatatoskrService ratatoskr, UserManagementService useradmin, LogService logservice) throws Exception {
        var servlet = new RatatoskrActivityStreamsResourceServlet();
        servlet.setLogService(logservice);
        servlet.setRatatoskrService(ratatoskr);
        servlet.setUseradmin(useradmin);
        servlet.activate();
        var config = createServletConfigWithApplicationAndPackagenameForJerseyResources();
        servlet.init(config);
        return servlet;
    }

    private ServletConfig createServletConfigWithApplicationAndPackagenameForJerseyResources() {
        var config = mock(ServletConfig.class);
        when(config.getInitParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(ServerProperties.PROVIDER_PACKAGES)));
        when(config.getInitParameter(ServerProperties.PROVIDER_PACKAGES)).thenReturn("no.priv.bang.ratatoskr.web.as.resources");
        var servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("/ratatoskr");
        when(config.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttributeNames()).thenReturn(Collections.emptyEnumeration());
        return config;
    }
}
