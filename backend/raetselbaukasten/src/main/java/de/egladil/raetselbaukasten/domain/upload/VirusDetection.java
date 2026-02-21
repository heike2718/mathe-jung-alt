// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VirusDetection
 */
public class VirusDetection {

    @JsonProperty
    private boolean virusDetected;

    @JsonProperty
    private String scannerMessage;

    @Override
    public String toString() {

        return "VirusDetection [virusDetected=" + virusDetected + ", scannerMessage=" + scannerMessage + "]";
    }

    public boolean isVirusDetected() {

        return virusDetected;
    }

    public VirusDetection withVirusDetected(final boolean virusDetected) {

        this.virusDetected = virusDetected;
        return this;
    }

    public String getScannerMessage() {

        return scannerMessage;
    }

    public VirusDetection withScannerMessage(final String scannerMessage) {

        this.scannerMessage = scannerMessage;
        return this;
    }
}
