import { Component, Input, HostListener, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneratedImage, GeneratedImages } from '@rbk-ws/core/model';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { MatExpansionModule } from '@angular/material/expansion';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Configuration } from '@rbk-ws/shared/config';

@Component({
    selector: 'rbk-frage-loesung-images',
    imports: [
        CommonModule,
        CdkAccordionModule,
        MatExpansionModule
    ],
    templateUrl: './frage-loesung-images.component.html',
    styleUrls: ['./frage-loesung-images.component.scss']
})
export class FrageLoesungImagesComponent implements OnInit {

  @Input()
  images!: GeneratedImages;

  imageFrage: { data: string, width: string } | undefined;
  imageLoesung: { data: string, width: string } | undefined;

  maxWidth = 500; // Maximum width to scale down to
  minWidth = 300; // Minimum width to prevent smaller images from being too tiny

  #breakpointObserver = inject(BreakpointObserver);

  #config = inject(Configuration);
  devMode = !this.#config.production;

  constructor(private cdr: ChangeDetectorRef) {}


  get isHandset(): boolean {
    return this.#breakpointObserver.isMatched(Breakpoints.Handset);
  }

  ngOnInit(): void {

    if (this.images.imageFrage) {
      this.imageFrage = { data: this.images.imageFrage.data, width: this.images.imageFrage.width + 'px' };
    }

    if (this.images.imageLoesung) {
      this.imageLoesung = { data: this.images.imageLoesung.data, width: this.images.imageLoesung.width + 'px' };
    }
  }

  calculateScale(width: number): string {
    if (this.isHandset) {
      return `${80}%`
    } else {
      const maxWidth = 500;
      // if (width <= maxWidth) {
      //   return `${30}%`
      // }
      const scaleFactor = Math.min(1, maxWidth / width);
      return `${scaleFactor * 100}%`;
    }
  }

  adjustImageFrageSize(event: Event) {

    const imgElement = event.target as HTMLImageElement;

    console.log(`Natural Width Frage: ${imgElement.naturalWidth}`);  // Debug: log natural width
    if (this.imageFrage) {
      if (imgElement.naturalWidth > this.maxWidth) {
        this.imageFrage.width = `${this.maxWidth}px`;
      } else if (imgElement.naturalWidth < this.minWidth) {
        this.imageFrage.width = `${this.minWidth}px`;
      } else {
        this.imageFrage.width = `${imgElement.naturalWidth}px`;
      }
      this.cdr.detectChanges();
    }
  }

  adjustImageLoesungSize(imgElement: any) {
    if (this.imageLoesung) {
      if (imgElement.naturalWidth > this.maxWidth) {
        this.imageLoesung.width = `${this.maxWidth}px`;
      } else if (imgElement.naturalWidth < this.minWidth) {
        this.imageLoesung.width = `${this.minWidth}px`;
      } else {
        this.imageLoesung.width = `${imgElement.naturalWidth}px`;
      }
      this.cdr.detectChanges();
    }
  }
}
