// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * FullDatabaseAutorTestProfile
 */
public class FullDatabaseAutorTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {

        // Präfix = _FULL-DB-AUTOR-TEST_ (siehe .env und templates/env-template)
        return "full-db-autor-test";
    }

}
