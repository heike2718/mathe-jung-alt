import { inject, Injectable } from "@angular/core";
import { MessageService } from "@mja-ws/shared/messaging/api";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { concatMap, map, tap } from "rxjs";
import { GrafikHttpService } from "./grafik-http.service";
import { grafikActions } from "./grafik.actions";

@Injectable({ providedIn: 'root' })
export class GrafikEffects {

    #actions = inject(Actions);
    #grafikHttpService = inject(GrafikHttpService);
    #messageService = inject(MessageService);

    pruefeGrafik$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(grafikActions.lADE_VORSCHAU),
            concatMap((action) => this.#grafikHttpService.loadGrafik(action.pfad)),
            map((embeddableImageVorschau) => grafikActions.vORSCHAU_GELADEN({embeddableImageVorschau }))
        );
    });

    grafikHochgeladen$ = createEffect(() =>
        this.#actions.pipe(
            ofType(grafikActions.gRAFIK_HOCHGELADEN),
            tap((action) => {
                this.#messageService.message(action.message);
            }),
        ), { dispatch: false });

}