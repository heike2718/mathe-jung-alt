import { Component, inject } from '@angular/core';

import { LoadingService } from '@rbk-ws/shared/messaging/api';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'

@Component({
    selector: 'rbk-loader',
    imports: [MatProgressSpinnerModule],
    templateUrl: './loading-indicator.component.html',
    styleUrls: ['./loading-indicator.component.scss']
})
export class LoadingIndicatorComponent {

  loadingService = inject(LoadingService);
}
