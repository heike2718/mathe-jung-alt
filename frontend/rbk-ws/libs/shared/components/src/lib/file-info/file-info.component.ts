import { Component, Input, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Configuration } from '@rbk-ws/shared/config';
import { calculateFileSize } from '@rbk-ws/shared/util';

@Component({
  selector: 'rbk-file-info',
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './file-info.component.html',
  styleUrls: ['./file-info.component.scss'],
})
export class FileInfoComponent implements OnInit {
  @Input()
  titel = '';

  @Input()
  beschreibung: string | undefined;

  @Input()
  fileName = '';

  @Input()
  maxSizeBytes!: number;

  @Input()
  actualFileSize!: number;

  #config = inject(Configuration);
  devMode = !this.#config.production;

  maxFileSizeInfo!: string;
  fileSize = '';

  ngOnInit(): void {
    const maxFileSizeInKB = this.maxSizeBytes / 1024;
    const maxFileSizeInMB = maxFileSizeInKB / 1024;

    this.maxFileSizeInfo = 'Maximale erlaubte Größe: ' + maxFileSizeInKB + ' kB bzw. ' + maxFileSizeInMB + ' MB';

    if (this.actualFileSize) {
      this.fileSize = calculateFileSize(this.actualFileSize);
    }
  }
}
