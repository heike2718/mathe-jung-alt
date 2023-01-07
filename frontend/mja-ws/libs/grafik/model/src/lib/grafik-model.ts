import { Message } from "@mja-ws/shared/messaging/api";


export interface GrafikSearchResult {
    readonly pfad: string;
    readonly messagePayload: Message;
    readonly image?: string;
};

export const nullGraphicSearchResult: GrafikSearchResult = {pfad: '', messagePayload: {level: 'INFO', message: ''}};