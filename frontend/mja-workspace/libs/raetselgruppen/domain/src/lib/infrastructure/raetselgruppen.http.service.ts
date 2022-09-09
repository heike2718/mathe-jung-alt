import { HttpClient } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService } from "@mja-workspace/shared/util-configuration";
import { LoadingIndicatorService } from "@mja-workspace/shared/util-mja";


@Injectable(
    {
        providedIn: 'root'
    }
)
export class RaetselgruppenHttpService {

    constructor(http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration, private loadingService: LoadingIndicatorService) { }

}