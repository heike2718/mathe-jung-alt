import { InjectionToken } from '@angular/core';


export interface HttpConfiguration {
    readonly baseUrl: string;
    readonly admin: boolean;
}

export const HttpConfigurationService = new InjectionToken<HttpConfiguration>('HttpConfiguration');
