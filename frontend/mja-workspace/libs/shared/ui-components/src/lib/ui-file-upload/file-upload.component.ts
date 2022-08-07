import { HttpEventType } from "@angular/common/http";
import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { Message, SafeNgrxService } from "@mja-workspace/shared/util-mja";
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

    @Output()
    responsePayload: EventEmitter<Message> = new EventEmitter<Message>();

    @Output()
    dateiAusgewaehlt: EventEmitter<string> = new EventEmitter<string>();

    fileName = '';
    selectedFiles?: FileList;
  	currentFile?: File;

    maxFileSizeInfo!: string;
    fileSize = '';

    uploading = false;
    uploadSuccessful = false;
    canSubmit = false;
    showMaxSizeExceeded = false;
    errmMaxFileSize = 'die gewählte Datei ist zu groß. Bitte wählen Sie eine andere Datei.'

    constructor(private fileUploadService: FileUploadService, private errorMapper: SafeNgrxService) { }

    ngOnInit(): void {
        const maxFileSizeInKB = this.uploadModel.maxSizeBytes / 1024;
        const maxFileSizeInMB = maxFileSizeInKB / 1024;

        this.maxFileSizeInfo = 'Maximale erlaubte Größe: ' + maxFileSizeInKB + ' kB bzw. ' + maxFileSizeInMB + ' MB';
    }

    onFileAdded($event: any) {

        this.selectedFiles = $event.target.files;
		this.showMaxSizeExceeded = false;

		if (this.selectedFiles && this.selectedFiles.length === 1) {
			
			const size = this.selectedFiles[0].size;
			this.#calculateFileSize(size);

			if (size <= this.uploadModel.maxSizeBytes) {				
				this.currentFile = this.selectedFiles[0];
				this.showMaxSizeExceeded = false;
				this.canSubmit = true;	
				this.dateiAusgewaehlt.emit(this.currentFile.name);	
				this.uploading = false;	
			} else {
				this.showMaxSizeExceeded = true;
			}									
		}
    }

    submitUpload(): void {

		this.showMaxSizeExceeded = false;

		if (this.uploadSuccessful) {
			return;
		}

		if (!this.currentFile) {
			return;
		}

		this.uploading = true;	

		this.fileUploadService.uploadFile(this.currentFile, this.uploadModel).subscribe(
			(rp: Message) => {

				this.uploading = false;
				this.currentFile = undefined;
				this.canSubmit = false;
				this.responsePayload.emit(rp);

			},
			(error => {
				this.uploading = false;
				this.currentFile = undefined;
				this.canSubmit = true;

                this.errorMapper.handleError(error, 'Leider konnte die Datei nicht hochgeladen werden.')
			})
		);		
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
}
