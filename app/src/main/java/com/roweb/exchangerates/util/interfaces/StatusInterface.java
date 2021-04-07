package com.roweb.exchangerates.util.interfaces;

import com.google.gson.JsonObject;

public interface StatusInterface {
    void success(JsonObject jsonObject);

    void fail(String message);
}
