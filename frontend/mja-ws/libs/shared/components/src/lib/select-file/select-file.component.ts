import { CommonModule } from "@angular/common";
import { Component, Input, EventEmitter, Output, OnInit, inject } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { FileInfoModel, SelectFileModel } from "./select-file.model";
import { UploadedFile } from "@mja-ws/core/model";
import { Configuration } from "@mja-ws/shared/config";
import { isValidFileName } from "@mja-ws/shared/util";



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

    #config = inject(Configuration);
    devMode = !this.#config.production;

    currentFile: File | undefined;

    maxFileSizeInfo!: string;
    fileSize = '';

    erlaubteDateinamenInfo = 'Der Dateiname darf nur Ziffern, Buchstaben des deutschen Alphabets sowie die Zeichen Minus, Punkt und Unterstrich enthalten. Auch Leerzeichen sind nicht erlaubt.';

    showMaxSizeExceeded = false;
    errmMaxFileSize = 'Die gewählte Datei ist zu groß. Bitte andere Datei wählen.';

    showInvalidFilename = false;
    errmFilename = 'Der Name der gewählten Datei enthält ungültige Zeichen. Datei bitte umbenennen oder andere Datei auswählen.';

    ngOnInit(): void {
        const maxFileSizeInKB = this.selectFileModel.maxSizeBytes / 1024;
        const maxFileSizeInMB = maxFileSizeInKB / 1024;

        this.maxFileSizeInfo = 'Maximale erlaubte Größe: ' + maxFileSizeInKB + ' kB bzw. ' + maxFileSizeInMB + ' MB';
    }

    onFileAdded($event: Event): void {

        const inputElement = $event.target as HTMLInputElement;

        if (inputElement.files) {

            const selectedFiles: FileList = inputElement.files;

            if (selectedFiles && selectedFiles.length === 1) {
                const size = selectedFiles[0].size;
                this.#calculateFileSize(size);

                if (size <= this.selectFileModel.maxSizeBytes) {
                    this.currentFile = selectedFiles[0];
                    this.showMaxSizeExceeded = false;
                    this.showInvalidFilename = false;

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

                        if (isValidFileName(filename)) {

                            this.fileSelected.emit({ file: uploadedFile, fileSize: size });
                        } else {
                            this.currentFile = undefined;
                            this.showInvalidFilename = true;
                        }
                    }

                } else {
                    this.currentFile = undefined;
                    this.showMaxSizeExceeded = true;
                }
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