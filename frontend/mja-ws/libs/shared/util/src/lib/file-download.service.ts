import { Injectable } from "@angular/core";
import { generateUUID } from "./uuid-generator";

@Injectable({
    providedIn: 'root'
})
export class FileDownloadService {

    downloadZip(fileData: Blob, fileName: string) {

        const dataUrl = URL.createObjectURL(fileData);
        const a = document.createElement('a');
        document.body.appendChild(a);
        a.style.display = 'none';
        a.href = dataUrl;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(dataUrl);
    }

    downloadPdf(fileData: Blob, fileName: string) {
        const source = `data:application/pdf;base64,${fileData}`;
        const link = document.createElement("a");
        link.href = source;
        link.download = `${fileName}`
        link.click();
    }

    downloadText(fileData: Blob, fileName: string) {
        const source = `data:text/plain;base64,${fileData}`;
        const link = document.createElement("a");
        link.href = source;
        link.download = `${fileName}`
        link.click();
    }
};


