/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.longs;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.ShouldBeEqualWithinPercentage.shouldBeEqualWithinPercentage;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

@RunWith(DataProviderRunner.class)
public class Longs_assertIsCloseToPercentage_Test extends LongsBaseTest {

    private static final Long ZERO = 0L;
    private static final Long ONE = 1L;
    private static final Long TEN = 10L;

    @Test
    public void should_fail_if_actual_is_null() {
        thrown.expectAssertionError(actualIsNull());
        longs.assertIsCloseToPercentage(someInfo(), null, ONE, withPercentage(ONE));
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_if_expected_value_is_null() {
        longs.assertIsCloseToPercentage(someInfo(), ONE, null, withPercentage(ONE));
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_if_percentage_is_null() {
        longs.assertIsCloseToPercentage(someInfo(), ONE, ZERO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_if_percentage_is_negative() {
        longs.assertIsCloseToPercentage(someInfo(), ONE, ZERO, withPercentage(-1L));
    }

    // @format:off
    @Test
    @DataProvider({
      "1, 1, 1",
      "1, 2, 100",
      "-1, -1, 1",
      "-1, -2, 100",
      "-1, 1, 200"
    })
    // @format:on
    public void should_pass_if_difference_is_less_than_given_percentage(Long actual, Long other, Long percentage) {
      longs.assertIsCloseToPercentage(someInfo(), actual, other, withPercentage(percentage));
    }

    // @format:off
    @Test
    @DataProvider({
      "1, 1, 0",
      "2, 1, 100",
      "1, 2, 50",
      "-1, -1, 0",
      "-2, -1, 100",
      "-1, -2, 50"
    })
    // @format:on
    public void should_pass_if_difference_is_equal_to_given_percentage(Long actual, Long other, Long percentage) {
      longs.assertIsCloseToPercentage(someInfo(), actual, other, withPercentage(percentage));
    }

    @Test
    public void should_fail_if_actual_is_not_close_enough_to_expected_value() {
        AssertionInfo info = someInfo();
        try {
            longs.assertIsCloseToPercentage(someInfo(), ONE, TEN, withPercentage(TEN));
        } catch (AssertionError e) {
            verify(failures).failure(info, shouldBeEqualWithinPercentage(ONE, TEN, withinPercentage(TEN),
                                                                         (TEN - ONE)));
            return;
        }
        failBecauseExpectedAssertionErrorWasNotThrown();
    }
}
