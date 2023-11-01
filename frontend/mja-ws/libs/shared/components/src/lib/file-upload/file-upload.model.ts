// TODO: ist überflüssig => deprecated
export interface UploadComponentModel {
	readonly schluesselRaetsel: string;
    readonly pfad: string;
	readonly titel: string;
	readonly maxSizeBytes: number;
	readonly errorMessageSize: string;
	readonly accept: string;
	readonly acceptMessage: string;
};
