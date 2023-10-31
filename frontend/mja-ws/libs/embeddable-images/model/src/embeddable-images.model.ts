export type TEXTART = 'FRAGE' | 'LOESUNG';

export interface EmbeddableImageContext {
  readonly raetselId: string;
  readonly textart: TEXTART | undefined;
};

export const initialEmbeddableImageContext: EmbeddableImageContext = {
  raetselId: '',
  textart: undefined
};

export interface UploadedFile {
    readonly name: string;
    readonly dataBase64: string
};

export const initialUploadedFile: UploadedFile = {
    name: '',
    dataBase64: ''
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
};

export interface ReplaceEmbeddableImageRequestDto {
    readonly raetselId: string;
    readonly relativerPfad: string;
    readonly file: UploadedFile;
};

export const initialReplaceEmbeddableImageRequestDto: ReplaceEmbeddableImageRequestDto = {
    raetselId: 'neu',
    relativerPfad: '',
    file: initialUploadedFile
};
