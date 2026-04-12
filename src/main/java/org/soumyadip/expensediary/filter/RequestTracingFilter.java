package org.soumyadip.expensediary.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.soumyadip.expensediary.service.UlidGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Every request is given an ID during Slf4j logging
 */

@RequiredArgsConstructor
@Component
public class RequestTracingFilter extends OncePerRequestFilter {

    private final UlidGenerator ulid;
    private static final String REQUEST_TRACE_ID = "request-trace-id";

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestId = ulid.generate();

        try {
            MDC.put(REQUEST_TRACE_ID, requestId);
            MDC.put("method", request.getMethod());
            MDC.put("uri", request.getRequestURI());

            filterChain.doFilter(request, response);
        }  finally {
            MDC.remove(REQUEST_TRACE_ID);
            MDC.remove("method");
            MDC.remove("uri");
        }

    }
}
