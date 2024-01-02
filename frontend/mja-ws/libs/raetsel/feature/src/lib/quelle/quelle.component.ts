import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GuiQuellenartenMap, MediumQuelleDto } from '@mja-ws/raetsel/model';
import { Subject, Subscription, debounceTime, distinctUntilChanged, tap } from 'rxjs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { TextFieldModule } from '@angular/cdk/text-field';
import { Quellenart, QuelleDto } from '@mja-ws/core/model';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { RaetselFacade } from '@mja-ws/raetsel/api';

@Component({
  selector: 'mja-quelle',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    TextFieldModule
  ],
  templateUrl: './quelle.component.html',
  styleUrl: './quelle.component.scss',
})
export class QuelleComponent implements OnInit, OnDestroy {

  @ViewChild('klasseInput') inputFieldKlasse!: ElementRef;
  @ViewChild('stufeInput') inputFieldStufe!: ElementRef;
  @ViewChild('ausgabeInput') inputFieldAusgabe!: ElementRef;
  @ViewChild('jahrInput') inputFieldJahr!: ElementRef;
  @ViewChild('seiteInput') inputFieldSeite!: ElementRef;
  @ViewChild('personInput') inputFieldPerson!: ElementRef;
  @ViewChild('pfadInput') inputFieldPfad!: ElementRef;

  raetselFacade = inject(RaetselFacade);

  @Input()
  quelle!: QuelleDto;

  @Output()
  private quelleChanged: EventEmitter<QuelleDto> = new EventEmitter<QuelleDto>();

  searchTerm = '';

  selectQuellenartInput: string[] = new GuiQuellenartenMap().getLabelsSorted();
  selectedMedium: MediumQuelleDto | undefined;

  selectedQuellenart: string = '';
  klasse = '';
  stufe = '';
  ausgabe = '';
  jahr = '';
  seite = '';
  person = '';
  pfad = '';

  showMediensuche = false;
  showKlasse = false;
  showStufe = false;
  showAusgabe = false;
  showJahr = false;
  showSeite = false;
  showPerson = false;
  showPfad = false;

  private inputSubjects: { [key: string]: Subject<string> } = {};
  private subscriptions: { [key: string]: Subscription } = {};

  #titelSearchSubject = new Subject<string>();

  ngOnInit(): void {

    this.#initializeInputField('klasseInput');
    this.#initializeInputField('stufeInput');
    this.#initializeInputField('ausgabeInput');
    this.#initializeInputField('jahrInput');
    this.#initializeInputField('seiteInput');
    this.#initializeInputField('personInput');
    this.#initializeInputField('pfadInput');

    this.selectedQuellenart = new GuiQuellenartenMap().getLabelOfQuellenart(this.quelle.quellenart);
    this.#handleQuellenartChanged();
  }

  ngOnDestroy(): void {
    for (const key in this.subscriptions) {
      const sub = this.subscriptions[key];
      if (sub) {
        sub.unsubscribe();
      }
    }
  }

  onSelectQuellenart($event: string): void {
    this.selectedQuellenart = $event;
    this.#handleQuellenartChanged();
  }

  onSelectMedium($event: MediumQuelleDto): void {
    this.selectedMedium = $event;
    this.#fireQuelleChanged();
  }

  onMediumTitelInputChange(): void {
    this.#titelSearchSubject.next(this.searchTerm);
  }

  onInputChange(event: Event, fieldName: string): void {
    const target = event.target as HTMLInputElement;
    const inputValue = target.value;
    this.inputSubjects[fieldName].next(inputValue);
  }

  #initializeInputField(fieldName: string): void {
    const subject = new Subject<string>();
    this.inputSubjects[fieldName] = subject;

    const subscription = subject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(value => {
      if (fieldName === 'ausgabeInput') {
        this.ausgabe = value;
      }
      if (fieldName === 'jahrInput') {
        this.jahr = value;
      }
      if (fieldName === 'klasseInput') {
        this.klasse = value;
      }
      if (fieldName === 'personInput') {
        this.person = value;
      }
      if (fieldName === 'pfadInput') {
        this.pfad = value;
      }
      if (fieldName === 'seiteInput') {
        this.seite = value;
      }
      if (fieldName === 'stufeInput') {
        this.stufe = value;
      }
      this.#fireQuelleChanged();
    });

    this.subscriptions[fieldName] = subscription;
  }

  #handleQuellenartChanged(): void {

    const quellenart: Quellenart = new GuiQuellenartenMap().getQuellenartOfLabel(this.selectedQuellenart);
    this.searchTerm = '';

    switch (quellenart) {
      case 'BUCH': {
        this.showAusgabe = false;
        this.showJahr = false;
        this.showKlasse = false;
        this.showPerson = false;
        this.showPfad = true;
        this.showSeite = true;
        this.showStufe = false;
        this.showMediensuche = true;

        this.ausgabe = '';
        this.jahr = '';
        this.klasse = '';
        this.person = '';
        this.pfad = this.quelle.pfad ? this.quelle.pfad : '';
        this.seite = this.quelle.seite ? this.quelle.seite : '';
        this.stufe = '';
        break;
      }
      case 'INTERNET': {
        this.showAusgabe = false;
        this.showJahr = true;
        this.showKlasse = true;
        this.showPerson = false;
        this.showPfad = true;
        this.showSeite = false;
        this.showStufe = true;
        this.showMediensuche = true;

        this.ausgabe = '';
        this.jahr = this.quelle.jahr ? this.quelle.jahr : '';
        this.klasse = this.quelle.klasse ? this.quelle.klasse : '';
        this.person = '';
        this.pfad = this.quelle.pfad ? this.quelle.pfad : '';
        this.seite = '';
        this.stufe = this.quelle.stufe ? this.quelle.stufe : '';
        break;
      }
      case 'PERSON': {
        this.showAusgabe = false;
        this.showJahr = false;
        this.showKlasse = false;
        this.showPerson = true;
        this.showPfad = false;
        this.showSeite = false;
        this.showStufe = false;
        this.showMediensuche = false;

        this.ausgabe = '';
        this.jahr = '';
        this.klasse = '';
        this.person = this.quelle.person ? this.quelle.person : '';
        this.pfad = '';
        this.seite = '';
        this.stufe = '';
        break;
      }
      case 'ZEITSCHRIFT': {
        this.showAusgabe = true;
        this.showJahr = true;
        this.showKlasse = false;
        this.showPerson = false;
        this.showPfad = true;
        this.showSeite = true;
        this.showStufe = false;
        this.showMediensuche = true;

        this.ausgabe = this.quelle.ausgabe ? this.quelle.ausgabe : '';
        this.jahr = this.quelle.jahr ? this.quelle.jahr : '';
        this.klasse = '';
        this.person = '';
        this.pfad = this.quelle.pfad ? this.quelle.pfad : '';
        this.seite = this.quelle.seite ? this.quelle.seite : '';
        this.stufe = '';
        break;
      }
    }

    this.#fireQuelleChanged();

    if (quellenart !== 'PERSON') {
      this.raetselFacade.findMedienForQuelle(quellenart);
    }
  }

  #fireQuelleChanged(): void {

    const quellenart: Quellenart = new GuiQuellenartenMap().getQuellenartOfLabel(this.selectedQuellenart);
    const mediumUuid: string | undefined = this.selectedMedium ? this.selectedMedium.id : undefined;

    this.quelle = {
      ...this.quelle,
      ausgabe: this.ausgabe,
      jahr: this.jahr,
      klasse: this.klasse,
      person: this.person,
      pfad: this.pfad,
      seite: this.seite,
      stufe: this.stufe,
      quellenart: quellenart,
      mediumUuid: mediumUuid
    };

    this.quelleChanged.emit(this.quelle);
  }
}
