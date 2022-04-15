import { HttpContextToken } from '@angular/common/http';

export const OPEN_API_HTTP_TOKEN = new HttpContextToken<boolean>(() => false);

export const DEFAULT_ERROR_MESSAGE_CONTEXT = new HttpContextToken(() => 'Ups, da ist leider ein Fehler aufgetreten.');