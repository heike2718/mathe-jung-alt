// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * FullDatabaseAutorTestProfile
 */
public class FullDatabaseQuelleAutorTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {

        // Präfix = _FULL-DB-QUELLE-TEST_ (siehe .env und templates/env-template)
        return "full-db-quelle-test";
    }

}
