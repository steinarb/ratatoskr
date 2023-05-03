/*
 * Copyright 2023 Steinar Bang
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
package no.priv.bang.ratatoskr.web.api.resources;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.util.WebUtils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mockrunner.mock.web.MockHttpServletRequest;

import no.priv.bang.ratatoskr.services.RatatoskrService;
import no.priv.bang.ratatoskr.services.beans.Credentials;
import no.priv.bang.ratatoskr.services.beans.Loginresult;
import no.priv.bang.ratatoskr.web.api.ShiroTestBase;
import no.priv.bang.authservice.definitions.AuthserviceException;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;
import no.priv.bang.osgiservice.users.User;
import no.priv.bang.osgiservice.users.UserManagementService;

class LoginResourceTest extends ShiroTestBase {

    @Test
    void testLogin() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        UserManagementService useradmin = mock(UserManagementService.class);
        LoginResource resource = new LoginResource();
        resource.request = request;
        resource.ratatoskr = ratatoskr;
        resource.useradmin = useradmin;
        String username = "jd";
        String password = "johnnyBoi";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult result = resource.login(locale, credentials);
        assertTrue(result.getSuccess());
        assertTrue(result.isAuthorized());
        assertNull(result.getOriginalRequestUrl());
    }

    @Test
    void testLoginByUserWithoutRole() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        UserManagementService useradmin = mock(UserManagementService.class);
        LoginResource resource = new LoginResource();
        resource.request = request;
        resource.ratatoskr = ratatoskr;
        resource.useradmin = useradmin;
        String username = "jad";
        String password = "1ad";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult result = resource.login(locale, credentials);
        assertTrue(result.getSuccess());
        assertFalse(result.isAuthorized());
    }

    @Test
    void testLoginWithOriginalRequestUrl() {
        MockHttpServletRequest request = new MockHttpServletRequest()
            .setContextPath("/ratatoskr");
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        UserManagementService useradmin = mock(UserManagementService.class);
        LoginResource resource = new LoginResource();
        resource.request = request;
        resource.ratatoskr = ratatoskr;
        resource.useradmin = useradmin;
        String username = "jd";
        String password = "johnnyBoi";
        MockHttpServletRequest originalRequest = new MockHttpServletRequest();
        originalRequest.setRequestURI("/ratatoskr/");
        createSubjectFromOriginalRequestAndBindItToThread(originalRequest);
        WebUtils.saveRequest(originalRequest);
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult result = resource.login(locale, credentials);
        assertTrue(result.getSuccess());
        assertTrue(result.isAuthorized());
        assertEquals("/", result.getOriginalRequestUrl());
    }

    @Test
    void testLoginFeilPassord() {
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        when(ratatoskr.displayText(anyString(), anyString())).thenReturn("Feil passord");
        MockLogService logservice = new MockLogService();
        LoginResource resource = new LoginResource();
        resource.ratatoskr = ratatoskr;
        resource.setLogservice(logservice);
        String username = "jd";
        String password = "feil";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult result = resource.login(locale, credentials);
        assertFalse(result.getSuccess());
        assertThat(result.getErrormessage()).startsWith("Feil passord");
    }

    @Test
    void testLoginUkjentBrukernavn() {
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        when(ratatoskr.displayText(anyString(), anyString())).thenReturn("Ukjent konto");
        MockLogService logservice = new MockLogService();
        LoginResource resource = new LoginResource();
        resource.ratatoskr = ratatoskr;
        resource.setLogservice(logservice);
        String username = "jdd";
        String password = "feil";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult result = resource.login(locale, credentials);
        assertThat(result.getErrormessage()).startsWith("Ukjent konto");
    }

    @Test
    void testFindUserSafelyWithUnknownUsername() {
        UserManagementService useradmin = mock(UserManagementService.class);
        when(useradmin.getUser(anyString())).thenThrow(AuthserviceException.class);
        LoginResource resource = new LoginResource();
        resource.useradmin = useradmin;
        User user = resource.findUserSafely("null");
        assertNull(user.getUsername());
    }

    @Test
    void testLogout() {
        String locale = "nb_NO";
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        when(ratatoskr.displayText(anyString(), anyString())).thenReturn("Logget ut");
        LoginResource resource = new LoginResource();
        resource.ratatoskr = ratatoskr;
        String username = "jd";
        String password = "johnnyBoi";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);
        assertTrue(subject.isAuthenticated()); // Verify precondition user logged in

        Loginresult loginresult = resource.logout(locale);
        assertFalse(loginresult.getSuccess());
        assertEquals("Logget ut", loginresult.getErrormessage());
        assertFalse(loginresult.isAuthorized());
        assertFalse(subject.isAuthenticated()); // Verify user has been logged out
    }

    @Test
    void testGetLoginstateWhenLoggedIn() {
        String locale = "nb_NO";
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        when(ratatoskr.displayText(anyString(), anyString())).thenReturn("Bruker er logget inn og har tilgang");
        UserManagementService useradmin = mock(UserManagementService.class);
        LoginResource resource = new LoginResource();
        resource.ratatoskr = ratatoskr;
        resource.useradmin = useradmin;
        String username = "jd";
        String password = "johnnyBoi";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);

        Loginresult loginresult = resource.loginstate(locale);
        assertTrue(loginresult.getSuccess());
        assertEquals("Bruker er logget inn og har tilgang", loginresult.getErrormessage());
        assertTrue(loginresult.isAuthorized());
    }

    @Test
    void testGetLoginstateWhenLoggedInButUserDoesntHaveRoleRatatoskruser() {
        String locale = "nb_NO";
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        when(ratatoskr.displayText(anyString(), anyString())).thenReturn("Bruker er logget inn men mangler tilgang");
        UserManagementService useradmin = mock(UserManagementService.class);
        LoginResource resource = new LoginResource();
        resource.ratatoskr = ratatoskr;
        resource.useradmin = useradmin;
        String username = "jad";
        String password = "1ad";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);

        Loginresult loginresult = resource.loginstate(locale);
        assertTrue(loginresult.getSuccess());
        assertEquals("Bruker er logget inn men mangler tilgang", loginresult.getErrormessage());
        assertFalse(loginresult.isAuthorized());
    }

    @Test
    void testGetLoginstateWhenNotLoggedIn() {
        String locale = "nb_NO";
        RatatoskrService ratatoskr = mock(RatatoskrService.class);
        when(ratatoskr.displayText(anyString(), anyString())).thenReturn("Bruker er ikke logget inn");
        UserManagementService useradmin = mock(UserManagementService.class);
        LoginResource resource = new LoginResource();
        resource.ratatoskr = ratatoskr;
        resource.useradmin = useradmin;
        createSubjectAndBindItToThread();

        Loginresult loginresult = resource.loginstate(locale);
        assertFalse(loginresult.getSuccess());
        assertEquals("Bruker er ikke logget inn", loginresult.getErrormessage());
        assertFalse(loginresult.isAuthorized());
    }

}
