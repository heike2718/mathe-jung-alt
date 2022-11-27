import { HttpErrorResponse } from "@angular/common/http";


export function getHttpErrorResponse(error: any): HttpErrorResponse | undefined {

    if (error instanceof HttpErrorResponse) {
        return <HttpErrorResponse>error;
    }

    return undefined;

}