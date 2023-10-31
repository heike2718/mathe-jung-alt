import { EmbeddableImageContext, initialEmbeddableImageContext } from "@mja-ws/embeddable-images/model";

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

