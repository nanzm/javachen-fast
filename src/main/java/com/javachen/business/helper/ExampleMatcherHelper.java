package com.javachen.business.helper;

import org.springframework.data.domain.ExampleMatcher;

public class ExampleMatcherHelper {
    public static ExampleMatcher and() {
        return ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();
    }

    public static ExampleMatcher or() {
        return ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();
    }
}
