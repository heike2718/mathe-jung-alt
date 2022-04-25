import { InjectionToken } from '@angular/core';


export interface Configuration {
    readonly baseUrl: string;
    readonly storagePrefix: string;
    readonly production: boolean;
    readonly profileUrl: string;
    readonly withFakeLogin: boolean;
    readonly admin: boolean;
}

export const SharedConfigService = new InjectionToken<Configuration>('Configuration');
