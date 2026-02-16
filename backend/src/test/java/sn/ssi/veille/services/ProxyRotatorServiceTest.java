package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import sn.ssi.veille.services.implementation.ProxyRotatorService;
import sn.ssi.veille.services.implementation.ProxyRotatorService.ProxyInfo;

@ExtendWith(MockitoExtension.class)
class ProxyRotatorServiceTest {

    @InjectMocks
    private ProxyRotatorService proxyRotatorService;

    @BeforeEach
    void setUp() {
        // Clear internal state if necessary via Reflection
        // (Not strictly needed if new instance per test, which JUnit does)
    }

    @Test
    @DisplayName("Get Next Proxy - Empty List -> Returns Null")
    void getNext_EmptyList_ReturnsNull() {
        ProxyInfo proxy = proxyRotatorService.getNext();
        assertThat(proxy).isNull();
    }

    @Test
    @DisplayName("Get Next Proxy - Populated List -> Returns Proxy")
    void getNext_PopulatedList_ReturnsProxy() {
        // Manually inject proxies for testing logic without calling external API
        List<ProxyInfo> testProxies = List.of(new ProxyInfo("1.2.3.4", 8080));
        ReflectionTestUtils.setField(proxyRotatorService, "proxies",
                new java.util.concurrent.CopyOnWriteArrayList<>(testProxies));

        ProxyInfo proxy = proxyRotatorService.getNext();
        assertThat(proxy).isNotNull();
        assertThat(proxy.host()).isEqualTo("1.2.3.4");
        assertThat(proxy.port()).isEqualTo(8080);
    }

    @Test
    @DisplayName("Mark Bad Proxy - Should Blacklist")
    void markBad_ShouldBlacklist() {
        // Given
        ProxyInfo p = new ProxyInfo("1.2.3.4", 8080);
        List<ProxyInfo> testProxies = List.of(p);
        ReflectionTestUtils.setField(proxyRotatorService, "proxies",
                new java.util.concurrent.CopyOnWriteArrayList<>(testProxies));

        // When
        proxyRotatorService.markBad(p);

        // Then
        // getNext filters out blacklisted
        ProxyInfo next = proxyRotatorService.getNext();
        assertThat(next).isNull();
    }

    @Test
    @DisplayName("Get Random User Agent - Returns Non Empty String")
    void getRandomUserAgent_ReturnsString() {
        String ua = proxyRotatorService.getRandomUserAgent();
        assertThat(ua).isNotBlank();
        assertThat(ua).contains("Mozilla");
    }

    @Test
    @DisplayName("Active Count - Reflects Blacklist")
    void activeCount_ReflectsBlacklist() {
        // Given
        ProxyInfo p1 = new ProxyInfo("1.1.1.1", 80);
        ProxyInfo p2 = new ProxyInfo("2.2.2.2", 80);
        List<ProxyInfo> testProxies = List.of(p1, p2);
        ReflectionTestUtils.setField(proxyRotatorService, "proxies",
                new java.util.concurrent.CopyOnWriteArrayList<>(testProxies));

        // When
        proxyRotatorService.markBad(p1);

        // Then
        assertThat(proxyRotatorService.activeCount()).isEqualTo(1);
    }
}
