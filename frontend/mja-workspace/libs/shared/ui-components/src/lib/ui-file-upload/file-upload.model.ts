
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
