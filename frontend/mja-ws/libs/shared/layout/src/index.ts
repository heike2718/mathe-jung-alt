/* You can add global styles to this file, and also import other style files */
// @import '~@angular/material/prebuilt-themes/deeppurple-amber.css';
// @import url('https://fonts.googleapis.com/icon?family=Material+Icons');

@use 'sass:map';
@use '@angular/material' as mat;
@include mat.core();

$theme-primary: mat.define-palette(mat.$indigo-palette);
$theme-accent: mat.define-palette(mat.$pink-palette, A200, A100, A400);

// The warn palette is optional (defaults to red).
$theme-warn: mat.define-palette(mat.$red-palette);

$theme: mat.define-light-theme(
  (
    color: (
      primary: $theme-primary,
      accent: $theme-accent,
      warn: $theme-warn,
    ),
    typography: mat.define-typography-config(),
  )
);

//@import '../node_modules/material-design-icons/';

$custom-typography: mat.define-typography-config(
  $font-family: 'Verdana',
  $body-1: mat.define-typography-level(1.2rem, 1.2rem, 400),
);
@include mat.all-component-typographies($custom-typography);
@include mat.all-component-themes($theme);
// // Include theme styles for core and each component used in your app.
// // Alternatively, you can import and @include the theme mixins for each component
// // that you are using.
// @include angular-material-theme($custom-typography);
// Include the common styles for Angular Material. We include this here so that you only
// have to load a single css file for Angular Material in your app.
// Be sure that you only ever include this mixin once!

$hessen-dark-grey: #333333;

$font-color: $hessen-dark-grey;
$invalid-color: #a94442;
$hint-color: #319943;

$hzd-blue: #004a96;
$hzd-blue-hover: #003d7d;
$hzd-red: #a94442;
$mdc-icon-button-icon-color: transparent;

@mixin border-accessability--green {
  border-bottom: none !important;
  outline-color: $hzd-blue !important;
  outline-width: 2px !important;
  outline-style: solid !important;
  outline-offset: 0;
  z-index: 10 !important;
}

.b3-select {
  .mat-mdc-form-field-icon-suffix {
    top: 5px;
  }
}

.date_small {
  font-size: 12px;
}

//material input fields
.mdc-text-field__input {
  border-radius: 4px !important;
}

.mat-mdc-form-field-flex {
  background-color: white !important;
}

.mat-mdc-form-field-flex:hover {
  background-color: white !important;
}

mat-form-field:hover {
  background-color: white !important;
}

.mat-mdc-form-field-focus-overlay {
  background-color: white !important;
}

.mat-mdc-input-element {
  border: 1px solid #ccc !important;
  font-size: 19px !important;
  border-radius: 4px;
  // border-color: #ccc !important;
  padding: 6px 12px !important;
  width: 100%;
  -webkit-box-sizing: border-box; /* Safari/Chrome, other WebKit */
  -moz-box-sizing: border-box; /* Firefox, other Gecko */
  box-sizing: border-box; /* Opera/IE 8+ */
  &:focus {
    border: none;
    padding: 0;
    outline: none;
    text-align: left;
    color: #495057;
    //border: 2px solid black;
    transition: box-shadow 0.3s, border-color 0.3s;
    //border-radius: 4px;
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
  }
  &:required {
    background-color: #ffffe0;
  }
}

.mat-mdc-form-field-wrapper {
  padding-bottom: 0;
}

.formFieldWidth300 .mat-mdc-form-field-infix {
  // width: 100%;
}

.mat-mdc-form-field-infix {
  width: 100% !important;
  padding-top: 25px;
  margin: 2px;
}

.mat-mdc-form-field-ripple {
  background-color: transparent !important;
}

//material label
mat-mdc-label {
  padding-bottom: 3px;
}

//material select box
.mat-mdc-select {
  border: 1px solid #ccc;
  border-radius: 4px;
  // border-color: #ccc;
  &:focus {
    padding: 0;
    outline: none;
    text-align: left;
    color: #495057;

    transition: box-shadow 0.3s, border-color 0.3s;
    border-radius: 4px;
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
  }
}

.mat-mdc-select-trigger {
  padding-left: 12px;
}

.mat-mdc-select-panel {
  @include mat.elevation(8);
  // background-color: #ffffff;
  // box-shadow: 0px 2px 4px -1px rgba(0, 0, 0, 0.2),
  //            0px 4px 5px 0px rgba(0, 0, 0, 0.14),
  //            0px 1px 10px 0px rgba(0, 0, 0, 0.12);
}

.mat-mdc-select-panel .mat-mdc-optgroup-label {
  height: 100% !important;
}

.mat-mdc-optgroup-label {
  .mat-mdc-icon {
    margin-right: 0 !important;
    vertical-align: unset !important;
  }
  .mdc-icon-button {
    margin-left: 5px;
  }
}

.mat-mdc-form-field-underline {
  background-color: transparent !important;
}

.mat-mdc-select-value {
  padding: 7px 0 !important;
  border-radius: 4px !important;
}

.mat-mdc-form-field-icon-suffix .mat-icon {
  color: $hzd-blue !important;
  font-size: 130% !important;
}

.b3-suffix {
  margin-left: 5px;
  color: $hzd-blue;
  cursor: pointer;
}

.b3-suffix--state-warning {
  color: $hzd-red;
}

.mat-mdc-form-field-subscript-wrapper {
  position: relative !important;
  height: 0;
}

.mat-mdc-form-field-error {
  display: inline-block !important;
  margin-bottom: 5px;
  background-color: #f2dede;
  border-color: #f2dede;
  border-radius: 4px;
  padding: 0.5em 0.5em;
  font-size: 1rem;

  span {
    color: $invalid-color;
  }
}

.mat-mdc-form-field-can-float.mat-mdc-form-field-should-float
  .mat-mdc-form-field-label {
  transform: translateY(-1.34375em) scale(0.85);
}

.mat-mdc-form-field {
  width: 100%;
  .mat-mdc-form-field-label {
    color: $font-color;
    overflow: initial !important;
    font-size: 19px !important;
  }
  &.mat-mdc-focused .mat-mdc-form-field-label,
  &.mat-mdc-focused .mat-mdc-floating-label {
    color: $font-color;
  }
  &.mat-mdc-form-field-invalid {
    .mat-mdc-form-field-label,
    .mat-mdc-floating-label {
      color: $invalid-color;
      .mat-mdc-form-field-required-marker {
        color: $invalid-color;
      }
    }

    .mat-mdc-input-element {
      border: 1px solid $invalid-color !important;
    }
  }
  .mat-mdc-select {
    &.mat-mdc-select-invalid {
      border-color: $invalid-color;
      .mat-mdc-select-arrow {
        color: $invalid-color;
      }
    }
    &.mat-mdc-select-required {
      background-color: #ffffe0;
    }
  }
}

.mat-mdc-form-field-appearance-legacy .mat-mdc-form-field-wrapper {
  padding-bottom: 0;
}

.mat-mdc-focused {
  .mat-mdc-form-field-required-marker {
    color: $invalid-color;
  }
}

.mat-mdc-form-field-underline {
  display: none !important;
}

.mat-mdc-input-element:disabled,
.mat-mdc-input-element:read-only {
  background-color: #eff0f1;
  //border-color: #777777 !important;
  border: none !important;
}
.mat-mdc-form-field-disabled {
  background-color: #ffffff;
}

.mat-mdc-form-field-prefix,
.mat-mdc-form-field-icon-suffix {
  display: flex !important;
  position: relative;
  padding: 0 !important;
}

.mat-mdc-icon-button {
  width: 1.5em !important;
  height: 1.5em !important;
  font-size: 1.2rem !important;
  background-color: #ffffff !important;
  line-height: 1 !important;
  padding: 0 !important;
}

.mdc-icon {
  color: $hzd-blue;
}

//button
.mdc-button {
  background-color: $hzd-blue !important;
  color: #ffffff !important;
  padding: 0 15px !important;
  letter-spacing: normal !important;
  &:hover {
    background-color: $hzd-blue-hover !important;
    border-color: $hzd-blue-hover !important;
    transition: background-color 0.3s;
  }
  &:focus {
    border: none;
    outline: none;
    //color: #495057;
    //border: 2px solid black;
    transition: box-shadow 0.3s, border-color 0.3s;
    //border-radius: 4px;
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
  }
  &:disabled {
    opacity: 0.65;
    border: none;
    color: $font-color !important;
    background-color: #eff0f1 !important;
    &:hover {
      border: none;
      opacity: 0.65;
      // background-color: $hzd-blue;
    }
  }
}

.form-field__elements-wrapper {
  .mat-mdc-tooltip-trigger {
    margin-top: 32px;
    width: 29px;
  }
}

.mat-mdc-form-field-error-wrapper {
  position: relative !important;
}

.mat-mdc-form-field-error-wrapper {
  padding: 0 !important;
}

.mat-form-field-suffix__clear {
  background-color: #ffffff !important;
  &:hover {
    background-color: #f2dede !important;
    transition: background-color 0.3s;
  }
}

.mat-button {
  background-color: $hzd-blue !important;
  border: 1px solid currentColor;
  //padding: 0 15px;
  line-height: 34px;
  margin-bottom: 5px;
  min-width: 122px !important;
}
.sportstaette-sportarten-tree .mat-mdc-button-base {
  // background-color: #004a96 !important;
  color: #020202 !important;
}

.sportstaette-sportarten-tree {
  max-height: 60vh;
  overflow-y: auto;
}
.sportstaette-sportarten-table {
  //max-height: 60vh;
  overflow-y: auto;
}
.sportstaette-sportanlagentyp-list {
  max-height: 55vh;
  overflow-y: auto;
}

.sportstaette-sportanlagentyp-table {
  max-height: 55vh;
  overflow-y: auto;
}

.icon__expand-arrow {
  display: flex !important;
  justify-content: center;
}

.mat-mdc-icon-rtl-mirror {
  vertical-align: center !important;
}

.button-update {
  align-self: center;
}

.button-wrapper {
  padding: 10px 0;
  margin: 10px 0;
}

//mat-mdc-tabs

.mdc-tab {
  letter-spacing: normal;
  background-color: #f1f1f1 !important;
  font-size: 1.2rem !important;
  flex-grow: 0 !important;
}

.mdc-tab:focus {
  background-color: white !important;
}

.mat-mdc-tab-label-content {
  color: $font-color !important;
}
.mdc-tab__content {
  color: #858585 !important;
}

.mdc-tab--active {
  background-color: #ffffff !important;
  border: 1px solid #f1f1f1 !important;
}

.mdc-tab-indicator__content {
  transition: 300ms cubic-bezier(0.35, 0, 0.25, 1) !important;
  background-color: $hzd-blue !important;
  height: 2px;
}

// Tabelle
.mat-mdc-header-cell {
  font-size: 1.2rem !important;
  border: none !important;
}
.mat-mdc-cell {
  font-size: 1.1rem;
  padding-right: 20px !important;
  word-break: break-word;
  word-wrap: break-word; /* IE11, Firefox */
}
.mat-mdc-column-Bezeichnung {
  padding-right: 10px !important;
}
.mat-mdc-column-actions {
  padding-top: 5px !important;
  padding-bottom: 5px !important;
}

.form-invalid--message-container {
  display: inline-flex;
  color: #a94442;
  background-color: #f2dede;
  border-color: #f2dede;
  padding: 19px 9px;
  margin: 6px;
  border-radius: 4px;
}

.icon--warning__red {
  color: #a94442;
}

.icon--blue {
  color: $hzd-blue;
}

.button-column {
  .mat-mdc-button-base {
    margin-bottom: 10px;
  }
}

.mat-mdc-form-field-wrapper {
  padding: 0 0 0 0;
}

lib-b3-input .mat-mdc-form-field-flex {
  padding: 0 !important;
}

app-admin-wartungstext .mat-mdc-form-field-flex {
  align-items: center;
}

a,
a:visited,
a:active {
  text-decoration: none;
  color: $hzd-blue;
}

.wrapper__headline-paginator {
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  @media screen and (min-width: 700px) {
    flex-direction: row;
  }
}

a:focus,
//button:focus,
input:focus,
mat-mdc-select:focus,
mat-mdc-checkbox:focus,
mat-mdc-icon:focus {
  @include border-accessability--green;
}

.div--sportstaettenid__margin {
  margin: -16px 16px 16px 16px;
}

.header-links {
  color: #ffffff;
  &:active,
  &:visited,
  &:hover {
    color: #ffffff;
    text-decoration: none;
  }
}

.link--action {
  color: $hzd-blue;
  &:hover {
    cursor: pointer;
    color: $font-color;
  }
}

.mat-mdc-checkbox
  .mdc-checkbox
  .mdc-checkbox__native-control:enabled:checked
  ~ .mdc-checkbox__background {
  &:hover {
    background-color: $hzd-blue-hover;
    border-color: $hzd-blue-hover;
    transition: background-color 0.3s;
  }
  background-color: $hzd-blue !important;
  border-color: $hzd-blue !important;
}

// fieldset
fieldset {
  border: 1px solid #ccc !important;
  border-radius: 4px;
  margin-bottom: 1em;
}

.legend__text {
  display: inline-block;
  padding-bottom: 6px;
}

// mat-mdc-card
.mat-mdc-card {
  border: 1px solid #ccc !important;
  //border-top: none !important;
  border-top-left-radius: 0 !important;
  border-top-right-radius: 0 !important;
}

.mat-mdc-card.mat-elevation-z8 {
  @include mat.elevation(8);
}

.mat-mdc-snack-bar-container {
  background-color: $hint-color;
  max-width: initial !important;
  padding: 25px 20px !important;
}

.mat-mdc-pseudo-checkbox-checked {
  background-color: $hzd-blue !important;
}

.mat-mdc-paginator-page-size {
  justify-content: flex-end;
  padding-right: 20px;
}
.mat-mdc-paginator-page-size-select {
  min-width: 70px !important;
}

.mat-mdc-paginator-icon {
  fill: $hzd-blue !important;
}

.mat-mdc-paginator-container {
  box-shadow: none !important;
  justify-content: center !important;
}

.mat-mdc-paginator-icon {
  width: auto !important;
}

.mat-mdc-paginator-navigation-previous,
.mat-mdc-paginator-navigation-next {
  font-size: 1.2rem !important;
}

.mat-mdc-tooltip {
  font-size: 0.9rem !important;
}

.mat-mdc-toolbar {
  background-color: transparent;
}

.menu {
  font-size: 1.2rem;
  line-height: 26px;
  padding-right: 32px;
  nav {
    height: 100%;
  }
  a,
  button {
    font-family: Verdana;
    display: flex;
    height: 100%;
    background-color: whitesmoke;
    text-decoration: none;
    font-size: 1.1rem;
    color: $font-color;
    align-items: center;
    padding: 0 15px;
    span {
      font-weight: 500;
    }
    &:hover {
      background-color: #e8e8e8;
    }
    &:active {
      color: $font-color;
    }
    &.nav__active-link {
      background-color: #e8e8e8;
    }
  }
}

.mat-mdc-cell__buttons-aktionen-wrapper {
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  .mat-mdc-cell__buttons__edit {
    margin-bottom: 5px;
    @media screen and (min-width: 700px) {
      margin-right: 5px;
      margin-bottom: 0;
    }
  }
}

.mat-mdc-cell__buttons-aktionen-wrapper {
  display: flex;
  @media screen and (min-width: 700px) {
    display: block;
  }
}

.mat-mdc-list-base .mat-mdc-list-option {
  font-size: 1.2rem;
  .mat-mdc-list-text {
    padding-left: 8px !important;
  }
}

[mat-mdc-sort-header].cdk-keyboard-focused .mat-mdc-sort-header-container {
  @include border-accessability--green;
}

mat-mdc-row,
mat-mdc-header-row,
mat-mdc-footer-row {
  align-items: inherit !important;
}

mat-row.mat-mdc-row,
mat-header-row.mat-mdc-header-row,
mat-footer-row.mat-mdc-footer-row {
  border-bottom: 1px solid !important;
  border-bottom-color: rgba(0, 0, 0, 0.12) !important;
}

.mdc-data-table__cell,
.mdc-data-table__header-cell {
  border: none !important;
}

.break-word {
  word-break: break-word;
}

.mat-icon {
  background-color: transparent !important;
}

.mat-mdc-option.mdc-list-item {
  background-color: white !important;
}

.mat-form-field-appearance-fill .mat-mdc-select-arrow-wrapper {
  transform: translate(-10px, 0px) !important;
}

.mdc-text-field {
  padding: 0px !important;
  box-sizing: content-box !important;
}
.mdc-floating-label {
  position: absolute !important;
}
.mdc-line-ripple {
  display: none !important;
}

.mdc-notched-outline {
  display: none !important;
}

.mdc-dialog__container {
  display: block;
  border-radius: 4px;
  box-sizing: border-box;
  overflow: auto;
  outline: 0;
  width: 100%;
}
/*
.mat-mdc-dialog-container {
  padding: 24px !important;
  background-color: #fff;
}
*/
.mdc-dialog__title {
  padding: 0 !important;
}
.mdc-dialog__content {
  padding: 0 !important;
}

.mdc-menu-surface.mat-mdc-select-panel {
  padding-bottom: 0 !important;
}

.mat-mdc-option:hover,
.mat-mdc-option:focus {
  background: rgba(0, 0, 0, 0.04) !important;
}

.mat-mdc-select-panel {
  background-color: #ffffff;
}

.mdc-checkbox
  .mdc-checkbox__native-control:enabled:checked
  ~ .mdc-checkbox__background,
.mdc-checkbox
  .mdc-checkbox__native-control:enabled:indeterminate
  ~ .mdc-checkbox__background,
.mdc-checkbox
  .mdc-checkbox__native-control[data-indeterminate='true']:enabled
  ~ .mdc-checkbox__background {
  border-color: $hzd-blue !important;
  background-color: $hzd-blue !important;
}

.mat-mdc-radio-button.mat-accent {
  --mdc-radio-selected-focus-icon-color: white !important;
  --mdc-radio-selected-hover-icon-color: #004a96 !important;
  --mdc-radio-selected-icon-color: #004a96 !important;
  --mdc-radio-selected-pressed-icon-color: #004a96 !important;
  --mat-radio-checked-ripple-color: white !important;
}

.mat-mdc-checkbox.mat-accent {
  --mdc-checkbox-disabled-selected-checkmark-color: #fff;
  --mdc-checkbox-selected-focus-state-layer-opacity: 0;
  --mdc-checkbox-selected-pressed-state-layer-opacity: 0;
  --mdc-checkbox-unselected-focus-state-layer-opacity: 0;
  --mdc-checkbox-unselected-pressed-state-layer-opacity: 0;
  --mdc-checkbox-selected-focus-state-layer-color: #fff;
  --mdc-checkbox-selected-pressed-state-layer-color: #fff;
  --mdc-checkbox-disabled-selected-icon-color: #fff;
  --mdc-checkbox-selected-checkmark-color: #fff;
  --mdc-checkbox-selected-focus-icon-color: #fff
  --mdc-checkbox-selected-icon-color: #fff;
  --mdc-checkbox-selected-pressed-icon-color: #fff;
  --mdc-checkbox-selected-focus-state-layer-color: #fff;
  --mdc-checkbox-unselected-focus-state-layer-color: #fff;
  --mdc-checkbox-unselected-pressed-state-layer-color: #fff;
}

.mat-accent {
  --mat-option-selected-state-label-text-color: yellow !important;
}

.mdc-checkbox
  .mdc-checkbox__native-control:focus:checked
  ~ .mdc-checkbox__ripple {
  background-color: #004a96 !important;
}

