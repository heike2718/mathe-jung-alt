// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.deskriptoren.impl;

import java.text.Collator;
import java.util.Comparator;

import de.egladil.raetselbaukasten.infrastructure.persistence.entities.Deskriptor;

/**
 * DeskriptorenNameComparator
 */
public class DeskriptorenNameComparator implements Comparator<Deskriptor> {

    @Override
    public int compare(final Deskriptor o1, final Deskriptor o2) {

        return Collator.getInstance().compare(o1.name, o2.name);
    }

}
