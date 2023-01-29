export interface Message {
  message: string;
  level: 'ERROR' | 'WARN' | 'INFO';
};

export interface ResponsePayload {
  readonly message: Message;
  readonly data: any | undefined;
};
