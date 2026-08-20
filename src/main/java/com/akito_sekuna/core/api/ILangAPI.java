package com.akito_sekuna.core.api;

import java.util.Map;

public interface ILangAPI {
    String get(String key);
    String get(String key, Map<String, String> replacements);
    String get(String key, String placeholder, String value);
}
