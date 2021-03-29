package io.ioxcorp.ioxbox.data.json;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;

public final class FrickYouJacksonTwoElectricBoogaloo {
    private Map<String, Integer> data;

    @JsonCreator
    public FrickYouJacksonTwoElectricBoogaloo() {

    }

    public FrickYouJacksonTwoElectricBoogaloo(final Map<String, Integer> inputData) {
        this.data = inputData;
    }

    public Map<String, Integer> getData() {
        return this.data;
    }
}
