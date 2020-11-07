package liquibase.ext.mongodb.lockservice;

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

import org.junit.jupiter.api.Test;

import static liquibase.ext.mongodb.lockservice.ReplaceChangeLogLockStatement.COMMAND_NAME;
import static org.assertj.core.api.Assertions.assertThat;

class ReplaceChangeLogLockStatementTest {

    @Test
    void testToJs() {
        final String collectionName = "testCollection";
        final ReplaceChangeLogLockStatement replaceChangeLogLockStatement = new ReplaceChangeLogLockStatement(collectionName, false);
        assertThat(replaceChangeLogLockStatement.toJs()).isEqualTo("db.".concat(collectionName).concat(".").concat(COMMAND_NAME).concat("(false);"));
    }

}
