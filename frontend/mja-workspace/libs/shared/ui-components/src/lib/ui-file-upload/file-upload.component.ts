import { HttpEventType } from "@angular/common/http";
import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { Message } from "@mja-workspace/shared/util-mja";
import { Subscription } from "rxjs";
import { finalize } from 'rxjs/operators';
import { UploadComponentModel } from "./file-upload.model";
import { FileUploadService } from "./file-upload.service";


@Component({
    selector: 'mja-file-upload',
    templateUrl: './file-upload.component.html',
    styleUrls: ['./file-upload.component.scss'],
})
export class FileUploadComponent implements OnInit {

    // adapted from https://blog.angular-university.io/angular-file-upload/

    @Input()
    uploadModel!: UploadComponentModel;

    fileName = '';

    maxFileSizeInfo!: string;
    fileSize = '';

    uploading = false;
    uploadSuccessful = false;
    canSubmit = false;
    showMaxSizeExceeded = false;
    errmMaxFileSize = 'die gewählte Datei ist zu groß. Bitte wählen Sie eine andere Datei.'

    uploadProgress!: number;
    uploadSub!: Subscription;

    constructor(private fileUploadService: FileUploadService) { }

    ngOnInit(): void {
        const maxFileSizeInKB = this.uploadModel.maxSizeBytes / 1024;
        const maxFileSizeInMB = maxFileSizeInKB / 1024;

        this.maxFileSizeInfo = 'Maximale erlaubte Größe: ' + maxFileSizeInKB + ' kB bzw. ' + maxFileSizeInMB + ' MB';
    }

    onFileSelected(event: any) {

        const file: File = event.target.files[0];
        this.showMaxSizeExceeded = false;

        if (file) {
            const size = file.size;
            this.#calculateFileSize(size);

            if (size <= this.uploadModel.maxSizeBytes) {
                this.#submitUpload(file);
            } else {
                this.showMaxSizeExceeded = true;
            }
        }
    }

    cancelUpload() {
        if (this.uploadSub) {
            this.uploadSub.unsubscribe();
        }
        this.#reset();
    }

    #submitUpload(file: File): void {
        this.showMaxSizeExceeded = false;

        if (this.uploadSuccessful) {
            return;
        }

        if (file) {


            this.uploading = true;
            

            const upload$ = this.fileUploadService.uploadFile(file, this.uploadModel).pipe(
                finalize(() => this.#reset())
            );

            this.uploadSub = upload$.subscribe(event => {
                if (event.type == HttpEventType.UploadProgress) {
                    this.uploadProgress = Math.round(100 * (event.loaded / event.total));
                }
            })
        }
    }

    #calculateFileSize(size: number): void {

        // let kb = size / 1024;

        // console.log('kb: ' + kb);

        if (Math.round(size / 1024) < 2048) {
            this.fileSize = Math.round(size / 1024) + ' kB';
        } else {
            this.fileSize = Math.round(size / 1024 / 1024) + ' MB';
        }
    }



    #reset(): void {
        if (this.uploadSub) {
            this.uploadSub.unsubscribe();
        }
    }
}
