package com.newrelic.contributors.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContributorsError {

    String message;

}
