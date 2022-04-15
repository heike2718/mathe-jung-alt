import { Component } from "@angular/core";
import { LoadingService } from "./loading.service";


@Component({
    selector: 'mja-loader',
    template: `<mat-progress-spinner
      *ngIf="(loadingService.loading$ | async) === true"
      mode="indeterminate"
    ></mat-progress-spinner>`,
})
export class LodingIndicatorComponent {

    constructor(public loadingService: LoadingService) { }
}