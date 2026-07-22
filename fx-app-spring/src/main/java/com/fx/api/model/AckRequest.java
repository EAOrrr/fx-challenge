package com.fx.api.model;

public record AckRequest(long batchId, String status) {}