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

    #extractFileName(contentDisposition: string): string {



        const tokens: string[] = contentDisposition.split(';');

        if (tokens.length >= 2) {
            const theFilenameToken = tokens[1];

            const startIdex = 'filename='.length;
            return theFilenameToken.substring(startIdex, theFilenameToken.length - 1);
        }

        return generateUUID().substring(0, 8) + '.zip';
    }


};


