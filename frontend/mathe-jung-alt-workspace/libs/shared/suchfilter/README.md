# Suchfilter

Ist eine domain library, die zum Zusammenstellen von Suchkriterien dient.

Suchfilter:

```
interface Suchfilter {
    readonly kontext: Suchkontext;
    readonly suchstring: string;
    readonly deskriptoren: Deskriptor[];
};
```

* Suchkontext entspricht der gewählten Domäne, also RAETSEL, BILDER, MEDIEN, .... 
* suchstring ist ein Text, der zur Volltextsuche über die entsprechenden Domänenobjekte verwendet wird. Er steht nur ADMINs zu Verfügung
* deskriptoren ist eine Teilmenge der Schlagworte, mit denen die Domänenobjekte behaftet sind. Gewöhnliche Benutzer können nur nach Schlagworten suchen.

## Suchfilterkomponente

Es gibt 2 Arten nvon Suchfilterkomponenten:

* für ADMINs: die [AdminSuchfilterComponent](./suchfilter-component/src/lib/admin/admin-suchfilter.component.ts)
* für alle anderen: die [DeskriptorenFilterComponent](./suchfilter-component/src/lib/deskriptoren-filter-component/deskriptoren-filter.component.ts).

Die DeskriptorenFilterComponent erzeugt als Output ein Event mit der gewählten Deskiptorenmenge für die Suche.

Die AdminSuchfilterComponent enthält die DeskriptorenFilterComponent, lauscht auf das Event mit der Deskriptorenmenge und reicht es einfach weiter. Außerdem entält sie ein Eingabefeld, das im Sinne einer "type ahead- Suche" Suchstring- Events nach oben reicht.

Neben dem Senden dieser Events wird über die [SuchfilterFacade](../suchfilter/domain/src/lib/application/suchfilter.facade.ts) der SuchfilterPartialState im ngrx-Store gefüllt. 



## Host-Komponente

Die Hostkomponente hängt sich an das durch die SuchfilterFacade zur Verfügung gestellte Obervable __suchfilterWithStatus$__ welches einen Stream von 

```
interface SuchfilterWithStatus {
    readonly suchfilter: Suchfilter;
    readonly nichtLeer: boolean;
}
```

Objekten emittiert. 

Sinnvoll zum Triggern der Suche sind 2 Subscriptions an 2 Teilstreams:

* Teilstream zu Suche: filtert alle Observables heraus, die nichtLeer sind und zum Suchkontext passen
* Teilstream zum Leeren der Trefferliste: filtert alle Observables heraus, die leer (also nicht nichtLeer) sind und zum Suchkontext passen und subscribed sich an diesen gefilterten Stream

Im ersten Fall wird der Suchfilter (nach einer Tastendrucklangen Wartezeit) an die zum Domänenobjekt passende Facade gesendet. Das triggert die entsprechende "Suchen-"Action, die von dem passenden Effect in ein http-Request umgewandelt wird. Die Treffermenge wird im ngrx-Store verwaltet und in der DataTable der Domänen-Suchkomponente dargestellt.

Im zweiten Fall wird über die zum Domänenobjekt passende Facade eine Action getriggert, um die Trefferliste zu leeren.

## Offene Punkte

Was passiert, wenn sich die Suchkontexte zweier Domänen behakeln? 

Wenn aus der Rätselsuchkomponente in die Dashboardkomponente und wieder zurück gewechselt wird, beleiben sowohl Suchfilter als auch Trefferliste noch erhalten. Muss mal schauen, ob das so bleiben kann.
