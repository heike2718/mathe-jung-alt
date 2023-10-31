export type TEXTART = 'FRAGE' | 'LOESUNG';

export interface EmbeddableImageContext {
  readonly raetselId: string;
  readonly textart: TEXTART | undefined;
};

export const initialEmbeddableImageContext: EmbeddableImageContext = {
  raetselId: '',
  textart: undefined
};

/** 
 * includegraphicsCommand: an den Text der context.textart anhängen
 * pfad: pfad der (umbenannten) hochgeladenen Datei relativ zum LaTeX-Verzeichnis
 */
export interface CreateEmbeddableImageResponse {
  readonly context: EmbeddableImageContext;
  readonly includegraphicsCommand: string;
  readonly pfad: string;
};

export interface UploadUIModel {
  readonly context: EmbeddableImageContext;
  readonly pfad: string;
  readonly titel: string;
  readonly maxSizeBytes: number;
  readonly maxFileSizeInfo: string;
  readonly errorMessageSize: string;
  readonly acceptFileType: string;
  readonly acceptMessage: string;
};

export const initialUploadUIModel: UploadUIModel = {
  context: initialEmbeddableImageContext,
  pfad: '',
  titel: '',
  maxSizeBytes: 2097152,
  maxFileSizeInfo: 'Die maximale erlaubte Größe ist 2 MB.',
  errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
  acceptFileType: '',
  acceptMessage: ''
};

