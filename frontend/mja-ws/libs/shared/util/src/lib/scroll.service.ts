import { DOCUMENT } from "@angular/common";
import { Inject, Injectable } from "@angular/core";


@Injectable({
    providedIn: 'root'
})
export class ScrollService {

    constructor(@Inject(DOCUMENT) private document: Document) { }

    public scrollToTop() {
        const document = this.document;
        (function smoothscroll() {
            const currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
            // console.log('currentScroll=' + currentScroll);
            if (currentScroll > 0) {
                window.requestAnimationFrame(smoothscroll);
                window.scrollTo(0, currentScroll - (currentScroll / 8));
            }
        })();
    }
}