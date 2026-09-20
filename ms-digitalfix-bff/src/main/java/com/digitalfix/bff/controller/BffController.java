package com.digitalfix.bff.controller;

import com.digitalfix.bff.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BffController {
    private final ProxyService proxyService;
    private final String workordersUrl;
    private final String catalogUrl;
    private final String reportUrl;

    public BffController(
            ProxyService proxyService,
            @Value("${services.workorders-url}") String workordersUrl,
            @Value("${services.catalog-url}") String catalogUrl,
            @Value("${services.report-url}") String reportUrl) {
        this.proxyService = proxyService;
        this.workordersUrl = workordersUrl;
        this.catalogUrl = catalogUrl;
        this.reportUrl = reportUrl;
    }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @RequestMapping("/api/workorders/**")
    public ResponseEntity<byte[]> workorders(HttpServletRequest request) throws Exception {
        return forward(request, workordersUrl, "/api/workorders");
    }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @RequestMapping("/api/catalog/**")
    public ResponseEntity<byte[]> catalog(HttpServletRequest request) throws Exception {
        return forward(request, catalogUrl, "/api/catalog");
    }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @RequestMapping("/api/report/**")
    public ResponseEntity<byte[]> report(HttpServletRequest request) throws Exception {
        return forward(request, reportUrl, "/api/report");
    }

    private ResponseEntity<byte[]> forward(HttpServletRequest request, String baseUrl, String prefix) throws Exception {
        String requestUri = request.getRequestURI();
        String suffix = requestUri.substring(prefix.length());
        String targetUrl = baseUrl + prefix + suffix;
        if (request.getQueryString() != null && !request.getQueryString().isBlank()) {
            targetUrl += "?" + request.getQueryString();
        }

        byte[] body = request.getInputStream().readAllBytes();
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String contentType = request.getContentType();
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        return proxyService.forward(targetUrl, method, authorization, contentType, body);
    }
}
