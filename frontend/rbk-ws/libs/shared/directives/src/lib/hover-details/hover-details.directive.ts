import { Directive, ElementRef, HostListener, Input, Renderer2 } from '@angular/core';

@Directive({
  selector: '[mjaHoverDetails]',
  standalone: true,
})
export class HoverDetailsDirective {

  @Input()
  mjaHoverDetails!: string;

  @HostListener('mouseover', ['$event']) onMouseOver(event: MouseEvent) {
    this.#showDetails(event);
  }

  @HostListener('mouseout') onMouseOut() {
    this.#hideDetails();
  }

  constructor(private el: ElementRef, private renderer: Renderer2) { }

  #showDetails(event: MouseEvent) {


    if (this.mjaHoverDetails && this.mjaHoverDetails.trim().length > 0) {
      // Use Renderer2 to manipulate the DOM
      const detailsElement = this.renderer.createElement('div');
      this.renderer.addClass(detailsElement, 'hover-details');
      const text = this.renderer.createText(this.mjaHoverDetails);
      this.renderer.appendChild(detailsElement, text);
      this.renderer.appendChild(this.el.nativeElement, detailsElement);

      // Position the tooltip near the cursor
      const offsetX = 10; // Adjust this value to control the distance from the cursor
      const offsetY = 10; // Adjust this value to control the distance from the cursor

      this.renderer.setStyle(detailsElement, 'position', 'fixed');
      this.renderer.setStyle(detailsElement, 'top', `${event.clientY + offsetY}px`);
      this.renderer.setStyle(detailsElement, 'left', `${event.clientX + offsetX}px`);
    }
  }

  #hideDetails() {
    // Use Renderer2 to remove the details element
    const detailsElement = this.el.nativeElement.querySelector('.hover-details');
    if (detailsElement) {
      this.renderer.removeChild(this.el.nativeElement, detailsElement);
    }
  }
}
