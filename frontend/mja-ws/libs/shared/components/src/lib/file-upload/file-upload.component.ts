import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UploadComponentModel } from './file-upload.model';
import { Message } from '@mja-ws/shared/messaging/api';
import { FileUploadService } from './file-upload.service';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { tap } from 'rxjs';

@Component({
  selector: 'mja-file-upload',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule
  ],
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

  selectedFiles: FileList | undefined;
  currentFile: File | undefined;

  maxFileSizeInfo!: string;
  fileSize = '';

  uploading = false;
  uploadSuccessful = false;
  canSubmit = false;
  showMaxSizeExceeded = false;
  errmMaxFileSize = 'die gewählte Datei ist zu groß. Bitte wählen Sie eine andere Datei.'

  #fileUploadService = inject(FileUploadService);

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

    this.#fileUploadService.uploadFile(this.currentFile, this.uploadModel).pipe(
      tap((m: Message) => {
        this.uploading = false;
        this.currentFile = undefined;
        this.canSubmit = false;
        this.responsePayload.emit(m)
      })
    ).subscribe();
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
