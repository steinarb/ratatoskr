package no.priv.bang.ratatoskr.asvocabulary;
/*
 * Copyright 2026 Steinar Bang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlaceTest {

    @Test
    void testCreate() {
        var accuracy = 0.75f;
        var altitude = 0.22f;
        var latitude = 60.0f;
        var longitude = 10.0f;
        var radius = 1;
        var units = "km";

        var place = Place.with()
            .accuracy(accuracy)
            .altitude(altitude)
            .latitude(latitude)
            .longitude(longitude)
            .radius(radius)
            .units(units)
            .build();

        assertThat(place.type()).isEqualTo(ActivityStreamObjectType.Place);
        assertThat(place.accuracy()).isEqualTo(accuracy);
        assertThat(place.altitude()).isEqualTo(altitude);
        assertThat(place.latitude()).isEqualTo(latitude);
        assertThat(place.longitude()).isEqualTo(longitude);
        assertThat(place.radius()).isEqualTo(radius);
        assertThat(place.units()).isEqualTo(units);
    }

    @Test
    void testCopy() {
        var accuracy = 0.75f;
        var altitude = 0.22f;
        var latitude = 60.0f;
        var longitude = 10.0f;
        var radius = 1;
        var units = "km";

        var originalPlace = Place.with()
            .accuracy(accuracy)
            .altitude(altitude)
            .latitude(latitude)
            .longitude(longitude)
            .radius(radius)
            .units(units)
            .build();

        var copiedPlace = Place.with(originalPlace).build();

        assertThat(copiedPlace).isEqualTo(originalPlace);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Place.with(null).build();
        assertThat(copyOfNull).hasAllNullFieldsOrPropertiesExcept("type", "accuracy", "altitude", "latitude", "longitude", "radius");
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Place);
    }

}
