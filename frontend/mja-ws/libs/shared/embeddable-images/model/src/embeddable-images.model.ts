import { UploadedFile, initialUploadedFile } from "@mja-ws/core/model";
export type Textart = 'FRAGE' | 'LOESUNG';

export interface EmbeddableImageInfo {
  readonly pfad: string;
  readonly existiert: boolean;
  readonly textart: Textart;
};

export interface EmbeddableImageContext {
  readonly raetselId: string;
  readonly textart: Textart | undefined;
};

export const initialEmbeddableImageContext: EmbeddableImageContext = {
  raetselId: '',
  textart: undefined
};

/** Dieses Objekt aus der select-file.component an die parent-component geben. Dann kann dort eine file-info.component angezeigt werdeb */
export interface FileInfo {
  readonly file: UploadedFile;
  readonly filesize: string;
};

export interface CreateEmbeddableImageRequestDto {

  readonly context: EmbeddableImageContext;
  readonly file: UploadedFile;
};

export const initialCreateEmbeddableImageRequestDto: CreateEmbeddableImageRequestDto = {
  context: initialEmbeddableImageContext,
  file: initialUploadedFile
};

/** 
 * includegraphicsCommand: an den Text der context.textart anhängen
 * pfad: pfad der (umbenannten) hochgeladenen Datei relativ zum latex.base.dir
 */
export interface EmbeddableImageResponseDto {
  readonly context: EmbeddableImageContext;
  readonly includegraphicsCommand: string;
  readonly pfad: string;
  readonly replaced: boolean;
};

export const initialEmbeddableImageResponseDto: EmbeddableImageResponseDto = {
  context: initialEmbeddableImageContext,
  includegraphicsCommand: '',
  pfad: '',
  replaced: false
};

export interface ReplaceEmbeddableImageRequestDto {
  readonly context: EmbeddableImageContext;
  readonly relativerPfad: string;
  readonly file: UploadedFile;
};

export const initialReplaceEmbeddableImageRequestDto: ReplaceEmbeddableImageRequestDto = {
  context: initialEmbeddableImageContext,
  relativerPfad: '',
  file: initialUploadedFile
};

export interface EmbeddableImageVorschau {
  readonly pfad: string;
  readonly exists: boolean;
  readonly image: string | undefined
};

