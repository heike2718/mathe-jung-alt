import { InjectionToken } from '@angular/core';


export interface AuthConfiguration {
    readonly baseUrl: string;
    readonly storagePrefix: string;
    readonly production: boolean;
    readonly loginSuccessUrl: string;
    readonly profileUrl: string;
    readonly withFakeLogin: boolean;
}

export const AuthConfigService = new InjectionToken<AuthConfiguration>('AuthConfiguration');
