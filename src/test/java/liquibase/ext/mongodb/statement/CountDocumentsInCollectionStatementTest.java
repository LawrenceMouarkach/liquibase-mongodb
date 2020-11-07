package liquibase.ext.mongodb.statement;

/*-
 * #%L
 * Liquibase MongoDB Extension
 * %%
 * Copyright (C) 2019 Mastercard
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import liquibase.ext.mongodb.TestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountDocumentsInCollectionStatementTest {
    private static final String COLLECTION_NAME = TestUtils.COLLECTION_NAME_1;
    private static final String COLLECTION_CMD = String.format("db.%s.countDocuments(null);", COLLECTION_NAME);
    private static final CountDocumentsInCollectionStatement COUNT_COLLECTION = new CountDocumentsInCollectionStatement(COLLECTION_NAME);

    @Test
    void testToJs() {
        assertThat(COUNT_COLLECTION.toJs())
            .isEqualTo(COLLECTION_CMD);
    }

    @Test
    void testToString() {
        assertThat(COUNT_COLLECTION.toJs())
            .isEqualTo(COUNT_COLLECTION.toString())
            .isEqualTo(COLLECTION_CMD, COLLECTION_NAME);
    }
}
