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

import com.mongodb.DBRefCodecProvider;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.*;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static liquibase.util.StringUtil.trimToNull;
import static lombok.AccessLevel.PRIVATE;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@NoArgsConstructor(access = PRIVATE)
public final class BsonUtils {

    public static final DocumentCodec DOCUMENT_CODEC =
        new DocumentCodec(fromProviders(
            new UuidCodecProvider(UuidRepresentation.STANDARD),
            new ValueCodecProvider(),
            new BsonValueCodecProvider(),
            new DocumentCodecProvider(),
            new DBRefCodecProvider()));

    public static CodecRegistry uuidCodecRegistry() {
        return CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(new UuidCodec(UuidRepresentation.STANDARD)),
                MongoClientSettings.getDefaultCodecRegistry()
        );
    }

    public static Document orEmptyDocument(final String json) {
        return (
            ofNullable(trimToNull(json))
                .map(s -> Document.parse(s, DOCUMENT_CODEC))
                .orElseGet(Document::new)
        );
    }

    public static List<Document> orEmptyList(final String json) {
        return (
            ofNullable(trimToNull(json))
                .map(jn -> "{ items: " + jn + "}")
                .map(s -> Document.parse(s, DOCUMENT_CODEC))
                .map(d -> d.getList("items", Document.class, new ArrayList<>()))
                .orElseGet(ArrayList::new)
        );
    }

    public static CreateCollectionOptions orEmptyCreateCollectionOptions(final Document options) {
        final CreateCollectionOptions createCollectionOptions =
                new CreateCollectionOptions();

        if (nonNull(options)) {
            final ValidationAction
                    validationAction =
                    ofNullable(options.getString("validationAction"))
                            .map(ValidationAction::fromString)
                            .orElse(null);

            final ValidationLevel
                    validationLevel =
                    ofNullable(options.getString("validationLevel"))
                            .map(ValidationLevel::fromString)
                            .orElse(null);

            createCollectionOptions.validationOptions(
                    new ValidationOptions()
                            .validationAction(validationAction)
                            .validationLevel(validationLevel)
                            .validator(options.get("validator", Document.class)));
        }
        return createCollectionOptions;
    }

    public static IndexOptions orEmptyIndexOptions(final Document options) {
        //TODO: add POJO codec
        final IndexOptions indexOptions = new IndexOptions();
        if (options.containsKey("unique") && options.getBoolean("unique")) {
            indexOptions.unique(true);
        }
        if (options.containsKey("name")) {
            indexOptions.name(options.getString("name"));
        }
        if (options.containsKey("expireAfterSeconds")) {
            indexOptions.expireAfter(options.getLong("expireAfterSeconds"), TimeUnit.SECONDS);
        }
        return indexOptions;
    }

}
