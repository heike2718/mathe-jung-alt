# Rätsel

Ist eine domain library, bei der es um die Rätsel-Domain geht.

## Das Suchfeature

Dieses Feature stellt eine komplexe Komponente bestehend aus einer Suchfilterkomponente und einer DataTable zu Vefügung, mit der man aus dem Backend mit Hilfe von Suchkriterien eine Teilmenge von Rätseln laden und blättern kann.

Die Suchfilterkomponente dient dazu, Suchkriterien bestehend aus einer Teilmenge von Deskriptoren oder einer Zeichenkette für eine Volltextsuche zusammenzustellen. Die Suche wird nach einer sinnvollen Verzögerung (einige 100 Millisekunden) getriggert, wenn es nichtleere Suchkiterien gibt.

[Suchfilter](../shared/suchfilter/README.md)

## Dynamic form fields

Quellen:

[Stackblitz](https://stackblitz.com/edit/angular-10-dynamic-reactive-forms-example?file=src%2Fapp%2Fapp.component.html)

[Website](https://jasonwatmore.com/post/2020/09/18/angular-10-dynamic-reactive-forms-example)

app.component.ts

```
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';

@Component({ selector: 'app', templateUrl: 'app.component.html' })
export class AppComponent implements OnInit {
    dynamicForm: FormGroup;
    submitted = false;

    constructor(private formBuilder: FormBuilder) { }

    ngOnInit() {
        this.dynamicForm = this.formBuilder.group({
            numberOfTickets: ['', Validators.required],
            tickets: new FormArray([])
        });
    }

    // convenience getters for easy access to form fields
    get f() { return this.dynamicForm.controls; }
    get t() { return this.f.tickets as FormArray; }
    get ticketFormGroups() { return this.t.controls as FormGroup[]; }

    onChangeTickets(e) {
        const numberOfTickets = e.target.value || 0;
        if (this.t.length < numberOfTickets) {
            for (let i = this.t.length; i < numberOfTickets; i++) {
                this.t.push(this.formBuilder.group({
                    name: ['', Validators.required],
                    email: ['', [Validators.required, Validators.email]]
                }));
            }
        } else {
            for (let i = this.t.length; i >= numberOfTickets; i--) {
                this.t.removeAt(i);
            }
        }
    }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.dynamicForm.invalid) {
            return;
        }

        // display form values on success
        alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.dynamicForm.value, null, 4));
    }

    onReset() {
        // reset whole form back to initial state
        this.submitted = false;
        this.dynamicForm.reset();
        this.t.clear();
    }

    onClear() {
        // clear errors and reset ticket fields
        this.submitted = false;
        this.t.reset();
    }
}
```

app.component.html

```
<form [formGroup]="dynamicForm" (ngSubmit)="onSubmit()">
    <div class="card m-3">
        <h5 class="card-header">Angular 10 Dynamic Reactive Forms Example</h5>
        <div class="card-body border-bottom">
            <div class="form-row">
                <div class="form-group">
                    <label>Number of Tickets</label>
                    <select formControlName="numberOfTickets" class="form-control" (change)="onChangeTickets($event)" [ngClass]="{ 'is-invalid': submitted && f.numberOfTickets.errors }">
                        <option value=""></option>
                        <option *ngFor="let i of [1,2,3,4,5,6,7,8,9,10]">{{i}}</option>
                    </select>
                    <div *ngIf="submitted && f.numberOfTickets.errors" class="invalid-feedback">
                        <div *ngIf="f.numberOfTickets.errors.required">Number of tickets is required</div>
                    </div>
                </div>
            </div>
        </div>
        <div *ngFor="let ticket of ticketFormGroups; let i = index" class="list-group list-group-flush">
            <div class="list-group-item">
                <h5 class="card-title">Ticket {{i + 1}}</h5>
                <div [formGroup]="ticket" class="form-row">
                    <div class="form-group col-6">
                        <label>Name</label>
                        <input type="text" formControlName="name" class="form-control" [ngClass]="{ 'is-invalid': submitted && ticket.controls.name.errors }" />
                        <div *ngIf="submitted && ticket.controls.name.errors" class="invalid-feedback">
                            <div *ngIf="ticket.controls.name.errors.required">Name is required</div>
                        </div>
                    </div>
                    <div class="form-group col-6">
                        <label>Email</label>
                        <input type="text" formControlName="email" class="form-control" [ngClass]="{ 'is-invalid': submitted && ticket.controls.email.errors }" />
                        <div *ngIf="submitted && ticket.controls.email.errors" class="invalid-feedback">
                            <div *ngIf="ticket.controls.email.errors.required">Email is required</div>
                            <div *ngIf="ticket.controls.email.errors.email">Email must be a valid email address</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="card-footer text-center border-top-0">
            <button class="btn btn-primary mr-1">Buy Tickets</button>
            <button class="btn btn-secondary mr-1" type="reset" (click)="onReset()">Reset</button>
            <button class="btn btn-secondary" type="button" (click)="onClear()">Clear</button>
        </div>
    </div>
</form>

<!-- credits -->
<div class="text-center">
    <p>
        <a href="https://jasonwatmore.com/post/2020/09/18/angular-10-dynamic-reactive-forms-example" target="_top">Angular 10 - Dynamic Reactive Forms Example</a>
    </p>
    <p>
        <a href="https://jasonwatmore.com" target="_top">JasonWatmore.com</a>
    </p>
</div>
```

