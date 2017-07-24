package com.newrelic.contributors.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContributorsError {

    String message;

}
