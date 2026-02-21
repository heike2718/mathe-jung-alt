// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import de.egladil.raetselbaukasten.domain.AbstractDomainEntity;
import de.egladil.raetselbaukasten.domain.quellen.dto.QuelleDto;

/**
 * Quelle
 */
public class Quelle extends AbstractDomainEntity {

    private QuelleDto datenQuelle;

    private String userId;

    private String owner;

    /**
     *
     */
    protected Quelle() {

        super();

    }

    /**
     * @param uuid
     */
    public Quelle(final String uuid) {

        super(uuid);

    }

    @Override
    public int hashCode() {

        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {

        if (!(obj instanceof Quelle)) {

            return false;
        }

        return super.equals(obj);
    }

    public QuelleDto getDatenQuelle() {

        return datenQuelle;
    }

    public Quelle withDatenQuelle(final QuelleDto datenQuelle) {

        this.datenQuelle = datenQuelle;
        return this;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(final String userId) {

        this.userId = userId;
    }

    public String getOwner() {

        return owner;
    }

    public Quelle withOwner(final String owner) {

        this.owner = owner;
        return this;
    }

}
