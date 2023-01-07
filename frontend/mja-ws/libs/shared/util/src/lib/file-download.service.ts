import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class FileDownloadService {

    downloadPdf(fileData: Blob, fileName: string) {
        const source = `data:application/pdf;base64,${fileData}`;
        const link = document.createElement("a");
        link.href = source;
        link.download = `${fileName}`
        link.click();
    }
}
