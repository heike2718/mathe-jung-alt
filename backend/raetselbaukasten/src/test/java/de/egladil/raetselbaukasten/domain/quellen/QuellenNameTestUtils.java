// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * QuellenNameTestUtils
 */
public class QuellenNameTestUtils {

    static PersistenteQuelleReadonly createQuelleAlleAttributeOhneQuellenart() {

        PersistenteQuelleReadonly quelle = PersistenteQuelleReadonly.builder()
                .ausgabe("11")
                .autor("Johannes Lehmann")
                .jahr("1987")
                .klasse("Klasse 4")
                .mediumTitel("Grundschulolympiade 2x2")
                .mediumUuid("m-uuid-1")
                .person("Hans Manz")
                .seite("42")
                .sortNumber(1)
                .stufe("Stufe 2")
                .userId("p-uuid-1")
                .uuid("q-uuid-1")
                .build();

        return quelle;

    }

}
