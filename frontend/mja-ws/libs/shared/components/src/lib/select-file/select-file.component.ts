import { CommonModule } from "@angular/common";
import { Component, Input, EventEmitter, Output, OnInit } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { FileInfoModel, SelectFileModel } from "./select-file.model";
import { UploadedFile } from "@mja-ws/embeddable-images/model";



@Component({
    selector: 'mja-select-file',
    standalone: true,
    imports: [
        CommonModule,
        MatButtonModule,
        MatIconModule
    ],
    templateUrl: './select-file.component.html',
    styleUrls: ['./select-file.component.scss'],
})
export class SelectFileComponent implements OnInit {

    @Input()
    selectFileModel!: SelectFileModel;

    @Output()
    fileSelected: EventEmitter<FileInfoModel> = new EventEmitter<FileInfoModel>();

    currentFile: File | undefined;

    maxFileSizeInfo!: string;
    fileSize = '';

    showMaxSizeExceeded = false;
    errmMaxFileSize = 'die gewählte Datei ist zu groß. Bitte wählen Sie eine andere Datei.'

    ngOnInit(): void {
        const maxFileSizeInKB = this.selectFileModel.maxSizeBytes / 1024;
        const maxFileSizeInMB = maxFileSizeInKB / 1024;

        this.maxFileSizeInfo = 'Maximale erlaubte Größe: ' + maxFileSizeInKB + ' kB bzw. ' + maxFileSizeInMB + ' MB';
    }

    onFileAdded($event: any): void {

        const selectedFiles: FileList = $event.target.files;

        if (selectedFiles && selectedFiles.length === 1) {
            const size = selectedFiles[0].size;
            this.#calculateFileSize(size);

            if (size <= this.selectFileModel.maxSizeBytes) {
                this.currentFile = selectedFiles[0];
                this.showMaxSizeExceeded = false;

                const filename = selectedFiles[0].name;
                const fileReader = new FileReader();

                fileReader.readAsDataURL(this.currentFile);
                fileReader.onload = () => {
                    let base64String = fileReader.result as string;
                    base64String = base64String.split(',')[1];

                    const uploadedFile: UploadedFile = {
                        name: filename,
                        dataBase64: base64String
                    }

                    this.fileSelected.emit({file: uploadedFile, fileSize: this.currentFile!.size});
                }

            } else {
                this.showMaxSizeExceeded = true;
            }
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
}