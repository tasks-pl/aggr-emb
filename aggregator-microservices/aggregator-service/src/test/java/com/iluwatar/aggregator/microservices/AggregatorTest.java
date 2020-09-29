/*
 * The MIT License
 * Copyright © 2014-2019 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.iluwatar.aggregator.microservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test Aggregation of domain objects
 */

public class AggregatorTest {

  @InjectMocks
  private Aggregator aggregator;

  @Mock
  private Micro0Client micro0Client;

  @Mock
  private Micro1Client micro1Client;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Tests getting the data for a desktop client
   */

  @Test
  public void weatherTest() {

    var json0 = "{\"coord\":{\"lon\":37.62,\"lat\":55.75},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"base\":\"stations\",\"main\":{\"temp\":10.18,\"feels_like\":7.89,\"temp_min\":8,\"temp_max\":12.22,\"pressure\":1025,\"humidity\":76},\"visibility\":10000,\"wind\":{\"speed\":2,\"deg\":130},\"rain\":{\"1h\":0.15},\"clouds\":{\"all\":100},\"dt\":1601350621,\"sys\":{\"type\":1,\"id\":9029,\"country\":\"RU\",\"sunrise\":1601350126,\"sunset\":1601392238},\"timezone\":10800,\"id\":524901,\"name\":\"Moscow\",\"cod\":200}";

    var json1 = "{\"data\":[{\"rh\":78,\"pod\":\"n\",\"lon\":37.61556,\"pres\":996,\"timezone\":\"Europe\\/Moscow\",\"ob_time\":\"2020-09-29 02:35\",\"country_code\":\"RU\",\"clouds\":76,\"ts\":1601346900,\"solar_rad\":0,\"state_code\":\"48\",\"city_name\":\"Moscow\",\"wind_spd\":1,\"wind_cdir_full\":\"north\",\"wind_cdir\":\"N\",\"slp\":1010.9,\"vis\":5,\"h_angle\":-90,\"sunset\":\"15:09\",\"dni\":0,\"dewpt\":7.4,\"snow\":0,\"uv\":0,\"precip\":0,\"wind_dir\":0,\"sunrise\":\"03:28\",\"ghi\":0,\"dhi\":0,\"aqi\":123,\"lat\":55.75222,\"weather\":{\"icon\":\"c04n\",\"code\":804,\"description\":\"Overcast clouds\"},\"datetime\":\"2020-09-29:03\",\"temp\":11.1,\"station\":\"E8051\",\"elev_angle\":-4.88,\"app_temp\":11.2}],\"count\":1}";

    when(micro0Client.getWeather("moscow")).thenReturn(json0);
    when(micro1Client.getWeather("moscow")).thenReturn(json1);

    var testweather = aggregator.weather("moscow");

    assertEquals(json0, testweather.getservice0());
    assertEquals(json1, testweather.getservice1());
  }

//-----------------------------------------------------------------------------
//  public class StringParameterResolver implements ParameterResolver {
//
//    @Override
//    public boolean supportsParameter(ParameterContext parameterContext,
//                                     ExtensionContext extensionContext) throws ParameterResolutionException {
//      return parameterContext.getParameter().getType() == String.class;
//    }
//
//    @Override
//    public Object resolveParameter(ParameterContext parameterContext,
//                                   ExtensionContext extensionContext) throws ParameterResolutionException {
//      return "moscow";
//
//    }
//
//  }
//-----------------------------------------------------------------------------
//  @ExtendWith(StringParameterResolver.class)
//  public class StringTest {
//
//  .....
//
//  }
//-----------------------------------------------------------------------------

}
