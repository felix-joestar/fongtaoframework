package com.fongtaoframework.starter.logging.support;

import cn.hutool.core.util.StrUtil;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class SensitiveDataSanitizer {

    private static final String MASK = "******";

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "authorization",
            "proxy-authorization",
            "cookie",
            "set-cookie",
            "x-api-key",
            "x-auth-token");

    private static final Set<String> SENSITIVE_FIELDS = Arrays.stream(new String[]{
                    "password",
                    "oldPassword",
                    "newPassword",
                    "client_secret",
                    "clientSecret",
                    "access_token",
                    "refresh_token",
                    "id_token",
                    "token",
                    "secret",
                    "sign",
                    "signature",
                    "mobile",
                    "phone",
                    "email"})
            .map(item -> item.toLowerCase(Locale.ROOT))
            .collect(Collectors.toUnmodifiableSet());

    private static final Pattern HEADER_PATTERN = Pattern.compile("(?i)^([\\w-]+\\s*:\\s*)[^\\r\\n]*$");
    private static final Pattern URL_WITH_QUERY_PATTERN = Pattern.compile("(?i)(https?://[^\\s\"'<>]+|/[^\\s\"'<>]*\\?[^\\s\"'<>]+)");
    private static final Pattern JSON_STRING_FIELD_PATTERN = Pattern.compile(
            "(?i)(\"([\\w-]+)\"\\s*:\\s*\")([^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)(\")");
    private static final Pattern JSON_VALUE_FIELD_PATTERN = Pattern.compile(
            "(?i)(\"([\\w-]+)\"\\s*:\\s*)(?!\")([^,}\\s]+)");
    private static final Pattern FORM_FIELD_PATTERN = Pattern.compile("(?i)(^|[&\\s])([\\w-]+)=([^&\\s]*)");

    private SensitiveDataSanitizer() {
    }

    public static String maskLine(String line) {
        if (StrUtil.isBlank(line)) {
            return line;
        }
        String sanitized = maskHeader(line);
        sanitized = maskUrls(sanitized);
        sanitized = maskJsonFields(sanitized);
        return maskFormFields(sanitized);
    }

    public static String maskUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return url;
        }
        int queryStart = url.indexOf('?');
        if (queryStart < 0) {
            return url;
        }
        int fragmentStart = url.indexOf('#', queryStart);
        String prefix = url.substring(0, queryStart + 1);
        String query = fragmentStart < 0 ? url.substring(queryStart + 1) : url.substring(queryStart + 1, fragmentStart);
        String suffix = fragmentStart < 0 ? "" : url.substring(fragmentStart);
        return prefix + maskQuery(query) + suffix;
    }

    private static String maskHeader(String line) {
        Matcher matcher = HEADER_PATTERN.matcher(line);
        if (matcher.matches()) {
            String prefix = matcher.group(1);
            String headerName = prefix.substring(0, prefix.indexOf(':')).trim().toLowerCase(Locale.ROOT);
            if (SENSITIVE_HEADERS.contains(headerName)) {
                return prefix + MASK;
            }
        }
        return line;
    }

    private static String maskUrls(String line) {
        Matcher matcher = URL_WITH_QUERY_PATTERN.matcher(line);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(builder, Matcher.quoteReplacement(maskUrl(matcher.group())));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    private static String maskJsonFields(String line) {
        String maskedStringFields = maskJsonStringFields(line);
        return maskJsonValueFields(maskedStringFields);
    }

    private static String maskJsonStringFields(String line) {
        Matcher matcher = JSON_STRING_FIELD_PATTERN.matcher(line);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            if (isSensitiveField(matcher.group(2))) {
                matcher.appendReplacement(builder, Matcher.quoteReplacement(matcher.group(1) + MASK + matcher.group(4)));
            }
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    private static String maskJsonValueFields(String line) {
        Matcher matcher = JSON_VALUE_FIELD_PATTERN.matcher(line);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            if (isSensitiveField(matcher.group(2))) {
                matcher.appendReplacement(builder, Matcher.quoteReplacement(matcher.group(1) + MASK));
            }
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    private static String maskFormFields(String line) {
        Matcher matcher = FORM_FIELD_PATTERN.matcher(line);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(2);
            if (isSensitiveField(key)) {
                matcher.appendReplacement(builder, Matcher.quoteReplacement(matcher.group(1) + key + "=" + MASK));
            }
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    private static boolean isSensitiveField(String key) {
        return key != null && SENSITIVE_FIELDS.contains(key.toLowerCase(Locale.ROOT));
    }

    private static String maskQuery(String query) {
        if (StrUtil.isBlank(query)) {
            return query;
        }
        return Arrays.stream(query.split("&", -1))
                .map(SensitiveDataSanitizer::maskQueryPart)
                .collect(Collectors.joining("&"));
    }

    private static String maskQueryPart(String part) {
        int equalsIndex = part.indexOf('=');
        if (equalsIndex < 0) {
            return part;
        }
        return part.substring(0, equalsIndex + 1) + MASK;
    }
}
