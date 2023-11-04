import { UploadedFile } from "@mja-ws/core/model";

export interface SelectFileModel {
    readonly titel: string;
	readonly beschreibung: string | undefined;
	readonly hinweis: string | undefined;
	readonly maxSizeBytes: number;
	readonly errorMessageSize: string;
	readonly accept: string;
	readonly acceptMessage: string;
};

export interface FileInfoModel {
	readonly fileSize: number;
	readonly file: UploadedFile;
};
