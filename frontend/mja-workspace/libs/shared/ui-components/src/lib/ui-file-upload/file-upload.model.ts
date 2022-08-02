
export type FILE_UPLOAD_TYPE = 'GRAFIK';

export interface UploadComponentModel {
    readonly typ: FILE_UPLOAD_TYPE;
	readonly pfad: string;
	readonly titel: string;
	readonly maxSizeBytes: number;
	readonly errorMessageSize: string;
	readonly accept: string;
	readonly acceptMessage: string;
};

export const initialUploadComponentModel: UploadComponentModel = {
    typ: 'GRAFIK',
	pfad: '',
	titel: 'Grafik hochladen',
	maxSizeBytes: 2097152,
	errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
	accept: '.eps',
	acceptMessage: 'erlaubte Dateitypen: eps'	
};